package com.demoblaze.runners;

import com.demoblaze.utils.AllureHelper;
import com.demoblaze.utils.ConfigReader;
import com.demoblaze.utils.VideoRecorder;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * SmokeTestRunner - Runs smoke tests with @smoke tag
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.demoblaze.stepdefinitions", "com.demoblaze.hooks"},
        tags = "@smoke",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-smoke.html",
                "json:target/cucumber-reports/cucumber-smoke.json",
                "junit:target/cucumber-reports/cucumber-smoke.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        dryRun = false
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
    private static final Logger logger = LogManager.getLogger(SmokeTestRunner.class);
    private static final ConfigReader config = ConfigReader.getInstance();
    private static LocalDateTime suiteStartTime;
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    
    @BeforeSuite
    public void beforeSuite() {
        logger.info("========================================");
        logger.info("Starting SMOKE TEST SUITE");
        logger.info("========================================");
        
        suiteStartTime = LocalDateTime.now();
        
        // Setup Allure environment
        AllureHelper.setEpic("DemoBlaze E-Commerce");
        AllureHelper.setFeature("Smoke Tests");
        AllureHelper.addDescription("Critical smoke test suite for DemoBlaze application");
        
        // Clean up old videos if enabled
        if (config.isVideoRecordingEnabled() && config.getVideoCleanupDays() > 0) {
            VideoRecorder.cleanupOldVideos(config.getVideoCleanupDays());
        }
        
        logger.info("Suite setup completed successfully");
    }
    
    @AfterSuite
    public void afterSuite() {
        logger.info("========================================");
        logger.info("SMOKE TEST SUITE COMPLETED");
        logger.info("========================================");
        
        LocalDateTime suiteEndTime = LocalDateTime.now();
        long executionTime = java.time.Duration.between(suiteStartTime, suiteEndTime).toMillis();
        
        // Final driver cleanup check
        int remainingDrivers = com.demoblaze.utils.DriverManager.getActiveDriverCount();
        if (remainingDrivers > 0) {
            logger.warn("Suite cleanup: " + remainingDrivers + " drivers still active, cleaning up...");
            com.demoblaze.utils.DriverManager.cleanupAllDrivers();
            
            // Final check after cleanup
            int finalCount = com.demoblaze.utils.DriverManager.getActiveDriverCount();
            if (finalCount > 0) {
                logger.error("Failed to cleanup all drivers! Remaining: " + finalCount);
                logger.error("This may indicate orphaned browser processes");
                
                // Last resort - kill browser processes
                if (config.getBooleanProperty("driver.forceKillOnExit")) {
                    logger.warn("Force killing browser processes as configured");
                    com.demoblaze.utils.DriverManager.killAllBrowserProcesses();
                }
            } else {
                logger.info("All drivers cleaned up successfully");
            }
        } else {
            logger.info("No remaining drivers to cleanup");
        }
        
        // Create test suite summary
        AllureHelper.createTestSuiteSummary(
            "Smoke Test Suite",
            totalTests,
            passedTests,
            failedTests,
            0, // skipped tests
            executionTime
        );
        
        // Log summary
        logger.info("Test execution summary:");
        logger.info("Total tests: " + totalTests);
        logger.info("Passed: " + passedTests);
        logger.info("Failed: " + failedTests);
        logger.info("Execution time: " + executionTime + " ms");
        logger.info("Final driver count: " + com.demoblaze.utils.DriverManager.getActiveDriverCount());
        
        if (totalTests > 0) {
            double successRate = (passedTests * 100.0) / totalTests;
            logger.info("Success rate: " + String.format("%.2f%%", successRate));
        }
        
        // Clean up old videos if enabled
        if (config.isVideoRecordingEnabled() && config.getVideoCleanupDays() > 0) {
            VideoRecorder.cleanupOldVideos(config.getVideoCleanupDays());
        }
        
        logger.info("Suite teardown completed successfully");
    }
    
    @BeforeMethod
    public void beforeMethod() {
        totalTests++;
    }
    
    @AfterMethod
    public void afterMethod(org.testng.ITestResult result) {
        if (result.getStatus() == org.testng.ITestResult.SUCCESS) {
            passedTests++;
        } else if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            failedTests++;
        }
    }
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
