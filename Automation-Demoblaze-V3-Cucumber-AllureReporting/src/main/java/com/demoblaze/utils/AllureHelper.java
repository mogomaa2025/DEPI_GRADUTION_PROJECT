package com.demoblaze.utils;

import io.qameta.allure.*;
import io.qameta.allure.model.Status;
import io.qameta.allure.util.ResultsUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * AllureHelper - Enhanced Allure reporting utilities
 */
public class AllureHelper {
    private static final Logger logger = LogManager.getLogger(AllureHelper.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    /**
     * Attach screenshot to Allure report
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] attachScreenshot(WebDriver driver) {
        return ScreenshotHelper.captureScreenshotAsBytes(driver);
    }

    /**
     * Attach screenshot with custom name
     */
    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachScreenshot(WebDriver driver, String name) {
        logger.info("Attaching screenshot: " + name);
        return ScreenshotHelper.captureScreenshotAsBytes(driver);
    }

    /**
     * Attach page source to Allure report
     */
    @Attachment(value = "Page Source", type = "text/html")
    public static String attachPageSource(WebDriver driver) {
        return driver.getPageSource();
    }

    /**
     * Attach text content to Allure report
     */
    @Attachment(value = "{name}", type = "text/plain")
    public static String attachText(String name, String content) {
        logger.info("Attaching text content: " + name);
        return content;
    }

    /**
     * Attach JSON content to Allure report
     */
    @Attachment(value = "{name}", type = "application/json")
    public static String attachJson(String name, String jsonContent) {
        logger.info("Attaching JSON content: " + name);
        return jsonContent;
    }

    /**
     * Attach video to Allure report
     */
    @Attachment(value = "{name}", type = "video/avi")
    public static byte[] attachVideo(String name, File videoFile) {
        try {
            if (videoFile.exists()) {
                byte[] videoBytes = new byte[(int) videoFile.length()];
                try (FileInputStream fis = new FileInputStream(videoFile)) {
                    fis.read(videoBytes);
                }
                logger.info("Video attached to Allure report: " + name);
                return videoBytes;
            }
        } catch (IOException e) {
            logger.error("Failed to attach video: " + e.getMessage(), e);
        }
        return new byte[0];
    }

    /**
     * Add step with description
     */
    @Step("{stepDescription}")
    public static void addStep(String stepDescription) {
        logger.info("Allure Step: " + stepDescription);
    }

    /**
     * Add step with description and attachment
     */
    @Step("{stepDescription}")
    public static void addStepWithScreenshot(WebDriver driver, String stepDescription) {
        logger.info("Allure Step with Screenshot: " + stepDescription);
        attachScreenshot(driver, stepDescription);
    }

    /**
     * Add environment information to Allure report
     */
    public static void addEnvironmentInfo() {
        try {
            Properties props = System.getProperties();
            
            // Add browser information
            Allure.addAttachment("Environment Info", "text/plain", 
                "Browser: " + config.getBrowser() + "\n" +
                "App URL: " + config.getAppUrl() + "\n" +
                "Headless Mode: " + config.isHeadless() + "\n" +
                "Java Version: " + props.getProperty("java.version") + "\n" +
                "OS Name: " + props.getProperty("os.name") + "\n" +
                "OS Version: " + props.getProperty("os.version") + "\n" +
                "User Name: " + props.getProperty("user.name") + "\n" +
                "Test Environment: " + config.getProperty("environment", "QA") + "\n" +
                "Execution Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            
            logger.info("Environment information added to Allure report");
        } catch (Exception e) {
            logger.error("Failed to add environment info: " + e.getMessage(), e);
        }
    }

    /**
     * Add test description
     */
    public static void addDescription(String description) {
        Allure.description(description);
    }

    /**
     * Add test link
     */
    public static void addLink(String name, String url) {
        Allure.link(name, url);
    }

    /**
     * Add issue link
     */
    public static void addIssue(String issueKey) {
        Allure.issue("Issue", issueKey);
    }

    /**
     * Add test management system link
     */
    public static void addTestCase(String testCaseId) {
        Allure.tms("Test Case", testCaseId);
    }

    /**
     * Set test severity
     */
    public static void setSeverity(SeverityLevel severity) {
        Allure.label("severity", severity.value());
    }

    /**
     * Add test owner
     */
    public static void setOwner(String owner) {
        Allure.label("owner", owner);
    }

    /**
     * Add feature
     */
    public static void setFeature(String feature) {
        Allure.label("feature", feature);
    }

    /**
     * Add story
     */
    public static void setStory(String story) {
        Allure.label("story", story);
    }

    /**
     * Add epic
     */
    public static void setEpic(String epic) {
        Allure.label("epic", epic);
    }

    /**
     * Add custom label
     */
    public static void addLabel(String name, String value) {
        Allure.label(name, value);
    }

    /**
     * Start test case with metadata
     */
    public static void startTestCase(String testName, String description, 
                                   SeverityLevel severity, String feature, 
                                   String story, String owner) {
        Allure.description(description);
        setSeverity(severity);
        setFeature(feature);
        setStory(story);
        setOwner(owner);
        
        logger.info("Starting Allure test case: " + testName);
    }

    /**
     * Log test failure with details
     */
    public static void logTestFailure(WebDriver driver, String testName, 
                                    String errorMessage, Throwable throwable) {
        try {
            // Attach failure screenshot
            attachScreenshot(driver, "Failure Screenshot - " + testName);
            
            // Attach page source
            attachPageSource(driver);
            
            // Attach error details
            attachText("Error Details", 
                "Test: " + testName + "\n" +
                "Error: " + errorMessage + "\n" +
                "Exception: " + (throwable != null ? throwable.toString() : "N/A") + "\n" +
                "Timestamp: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );
            
            logger.error("Test failure logged for Allure report: " + testName);
        } catch (Exception e) {
            logger.error("Failed to log test failure: " + e.getMessage(), e);
        }
    }

    /**
     * Add browser console logs
     */
    public static void attachConsoleLogs(WebDriver driver) {
        try {
            // Get console logs if available
            org.openqa.selenium.logging.LogEntries logs = driver.manage().logs().get("browser");
            
            StringBuilder logContent = new StringBuilder();
            for (org.openqa.selenium.logging.LogEntry entry : logs) {
                logContent.append(entry.getLevel()).append(": ")
                          .append(entry.getMessage()).append("\n");
            }
            
            if (logContent.length() > 0) {
                attachText("Browser Console Logs", logContent.toString());
                logger.info("Browser console logs attached to Allure report");
            }
        } catch (Exception e) {
            logger.debug("Console logs not available or failed to capture: " + e.getMessage());
        }
    }

    /**
     * Add network logs (if available)
     */
    public static void attachNetworkLogs(WebDriver driver) {
        try {
            org.openqa.selenium.logging.LogEntries logs = driver.manage().logs().get("performance");
            
            StringBuilder logContent = new StringBuilder();
            for (org.openqa.selenium.logging.LogEntry entry : logs) {
                logContent.append(entry.getLevel()).append(": ")
                          .append(entry.getMessage()).append("\n");
            }
            
            if (logContent.length() > 0) {
                attachText("Network Performance Logs", logContent.toString());
                logger.info("Network performance logs attached to Allure report");
            }
        } catch (Exception e) {
            logger.debug("Network logs not available or failed to capture: " + e.getMessage());
        }
    }

    /**
     * Create test suite summary
     */
    public static void createTestSuiteSummary(String suiteName, int totalTests, 
                                            int passed, int failed, int skipped, 
                                            long executionTime) {
        String summary = String.format(
            "Test Suite: %s\n" +
            "Total Tests: %d\n" +
            "Passed: %d\n" +
            "Failed: %d\n" +
            "Skipped: %d\n" +
            "Execution Time: %d ms\n" +
            "Success Rate: %.2f%%",
            suiteName, totalTests, passed, failed, skipped, executionTime,
            totalTests > 0 ? (passed * 100.0 / totalTests) : 0.0
        );
        
        attachText("Test Suite Summary", summary);
        logger.info("Test suite summary created for Allure report");
    }
}