package com.demoblaze.hooks;

import com.demoblaze.utils.*;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import io.qameta.allure.SeverityLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Hooks - Cucumber hooks for setup and teardown
 */
public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    /**
     * Before hook - Runs before each scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        logger.info("========================================");
        logger.info("Starting Scenario: " + scenario.getName());
        logger.info("Tags: " + scenario.getSourceTagNames());
        logger.info("========================================");
        
        // Initialize WebDriver
        DriverManager.initializeDriver();
        logger.info("WebDriver initialized successfully");
        
        // Log browser and environment info
        logger.info("Browser: " + config.getBrowser());
        logger.info("Application URL: " + config.getAppUrl());
        logger.info("Headless mode: " + config.isHeadless());
        
        // Setup Allure reporting for scenario
        setupAllureReporting(scenario);
        
        // Start video recording if enabled
        if (config.isVideoRecordingEnabled() && !config.isVideoOnFailureOnly()) {
            VideoRecorder.startRecording(scenario.getName());
            logger.info("Video recording started for scenario: " + scenario.getName());
        }
        
        // Add environment info to Allure
        AllureHelper.addEnvironmentInfo();
    }

    /**
     * After hook - Runs after each scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = null;
        boolean driverCleanupSuccessful = false;
        
        try {
            driver = DriverManager.getDriver();
            logger.info("Active drivers before cleanup: " + DriverManager.getActiveDriverCount());
            
            if (driver != null) {
                if (scenario.isFailed()) {
                    logger.error("Scenario FAILED: " + scenario.getName());
                    
                    // Handle video recording for failed tests
                    if (config.isVideoRecordingEnabled()) {
                        if (config.isVideoOnFailureOnly()) {
                            // Start recording for failure if not already recording
                            VideoRecorder.startRecording(scenario.getName());
                            try {
                                Thread.sleep(2000); // Give time to capture failure state
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        VideoRecorder.stopRecordingOnFailure(scenario.getName());
                    }
                    
                    // Enhanced failure reporting
                    captureComprehensiveFailureData(scenario, driver);
                    
                } else {
                    logger.info("Scenario PASSED: " + scenario.getName());
                    
                    // Stop video recording for passed tests
                    if (config.isVideoRecordingEnabled() && !config.isVideoOnFailureOnly()) {
                        VideoRecorder.stopRecording();
                    }
                    
                    // Capture final screenshot for passed tests
                    capturePassedScenarioData(scenario, driver);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error during scenario cleanup: " + e.getMessage(), e);
        } finally {
            // Always attempt to clean up the driver
            try {
                if (DriverManager.isDriverInitialized()) {
                    DriverManager.quitDriver();
                    driverCleanupSuccessful = true;
                    logger.info("WebDriver closed successfully");
                }
            } catch (Exception e) {
                logger.error("Error closing WebDriver: " + e.getMessage(), e);
                
                // Force cleanup if normal quit failed
                try {
                    DriverManager.forceCleanupAllDrivers();
                    logger.warn("Force cleanup executed due to quit failure");
                } catch (Exception forceError) {
                    logger.error("Even force cleanup failed: " + forceError.getMessage(), forceError);
                }
            }
            
            // Check for orphaned drivers
            int remainingDrivers = DriverManager.getActiveDriverCount();
            if (remainingDrivers > 0) {
                logger.warn("Warning: " + remainingDrivers + " drivers still active after cleanup");
                
                if (DriverManager.hasOrphanedDrivers()) {
                    logger.warn("Orphaned drivers detected - attempting cleanup");
                    DriverManager.cleanupAllDrivers();
                }
            }
            
            logger.info("========================================");
            logger.info("Finished Scenario: " + scenario.getName());
            logger.info("Status: " + scenario.getStatus());
            logger.info("Driver Cleanup: " + (driverCleanupSuccessful ? "SUCCESS" : "FAILED"));
            logger.info("Remaining Active Drivers: " + DriverManager.getActiveDriverCount());
            logger.info("========================================\n");
        }
    }

    /**
     * After step hook - Runs after each step (optional, for debugging)
     */
    @AfterStep
    public void afterStep(Scenario scenario) {
        // Uncomment to capture screenshot after every step
        // WebDriver driver = DriverManager.getDriver();
        // if (driver != null) {
        //     captureScreenshot(scenario, driver);
        // }
    }

    /**
     * Setup Allure reporting metadata for scenario
     */
    private void setupAllureReporting(Scenario scenario) {
        try {
            String scenarioName = scenario.getName();
            AllureHelper.addDescription("Automated test scenario: " + scenarioName);
            
            // Set severity based on tags
            if (scenario.getSourceTagNames().contains("@critical")) {
                AllureHelper.setSeverity(SeverityLevel.CRITICAL);
            } else if (scenario.getSourceTagNames().contains("@high")) {
                AllureHelper.setSeverity(SeverityLevel.CRITICAL);
            } else if (scenario.getSourceTagNames().contains("@medium")) {
                AllureHelper.setSeverity(SeverityLevel.NORMAL);
            } else if (scenario.getSourceTagNames().contains("@low")) {
                AllureHelper.setSeverity(SeverityLevel.TRIVIAL);
            } else {
                AllureHelper.setSeverity(SeverityLevel.NORMAL);
            }
            
            // Set feature and story based on tags
            scenario.getSourceTagNames().forEach(tag -> {
                if (tag.startsWith("@feature_")) {
                    AllureHelper.setFeature(tag.replace("@feature_", ""));
                } else if (tag.startsWith("@story_")) {
                    AllureHelper.setStory(tag.replace("@story_", ""));
                } else if (tag.startsWith("@epic_")) {
                    AllureHelper.setEpic(tag.replace("@epic_", ""));
                }
            });
            
            // Add custom labels
            AllureHelper.addLabel("testType", getTestType(scenario));
            AllureHelper.addLabel("executionDate", 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            
        } catch (Exception e) {
            logger.error("Failed to setup Allure reporting: " + e.getMessage(), e);
        }
    }
    
    /**
     * Capture comprehensive failure data
     */
    private void captureComprehensiveFailureData(Scenario scenario, WebDriver driver) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String failureName = scenario.getName() + "_FAILED_" + timestamp;
            
            // Capture screenshot
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            
            // Attach to Cucumber report
            scenario.attach(screenshot, "image/png", failureName);
            
            // Enhanced Allure attachments
            AllureHelper.attachScreenshot(driver, "Failure Screenshot - " + scenario.getName());
            AllureHelper.attachPageSource(driver);
            AllureHelper.attachConsoleLogs(driver);
            AllureHelper.attachNetworkLogs(driver);
            
            // Save screenshot to file system
            ScreenshotHelper.captureScreenshot(driver, failureName);
            
            // Log failure details
            String failureDetails = String.format(
                "Test: %s\nStatus: FAILED\nTimestamp: %s\nBrowser: %s\nURL: %s",
                scenario.getName(),
                timestamp,
                config.getBrowser(),
                driver.getCurrentUrl()
            );
            
            AllureHelper.attachText("Failure Details", failureDetails);
            
            logger.info("Comprehensive failure data captured for scenario: " + scenario.getName());
            
        } catch (Exception e) {
            logger.error("Failed to capture comprehensive failure data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Capture data for passed scenarios
     */
    private void capturePassedScenarioData(Scenario scenario, WebDriver driver) {
        try {
            // Optional: Capture final screenshot for passed tests
            if (config.getBooleanProperty("screenshot.onPassed")) {
                AllureHelper.attachScreenshot(driver, "Final State - " + scenario.getName());
            }
            
            // Add success details
            String successDetails = String.format(
                "Test: %s\nStatus: PASSED\nTimestamp: %s\nBrowser: %s",
                scenario.getName(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                config.getBrowser()
            );
            
            AllureHelper.attachText("Success Details", successDetails);
            
        } catch (Exception e) {
            logger.error("Failed to capture passed scenario data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get test type based on scenario tags
     */
    private String getTestType(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@smoke")) return "Smoke";
        if (scenario.getSourceTagNames().contains("@regression")) return "Regression";
        if (scenario.getSourceTagNames().contains("@e2e")) return "End-to-End";
        if (scenario.getSourceTagNames().contains("@api")) return "API";
        if (scenario.getSourceTagNames().contains("@ui")) return "UI";
        return "Functional";
    }

    /**
     * Capture screenshot (general purpose)
     */
    private void captureScreenshot(Scenario scenario, WebDriver driver) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Screenshot");
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
