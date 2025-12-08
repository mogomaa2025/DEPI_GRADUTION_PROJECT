package com.demoblaze.utils;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotHelper - Captures and manages screenshots
 */
public class ScreenshotHelper {
    private static final Logger logger = LogManager.getLogger(ScreenshotHelper.class);
    private static final ConfigReader config = ConfigReader.getInstance();

    /**
     * Capture screenshot with generated name
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            String directory = config.getScreenshotDirectory();
            File targetDirectory = new File(directory);

            if (!targetDirectory.exists()) {
                targetDirectory.mkdirs();
            }

            File targetFile = new File(directory + File.separator + fileName);
            FileUtils.copyFile(sourceFile, targetFile);

            logger.info("Screenshot captured: " + targetFile.getAbsolutePath());
            return targetFile.getAbsolutePath();

        } catch (IOException e) {
            logger.error("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
//
//    /**
//     * Capture screenshot with specific filename
//     */
//    public static String captureScreenshot(WebDriver driver, String fileName) {
//        try {
//            TakesScreenshot screenshot = (TakesScreenshot) driver;
//            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);
//
//            String directory = config.getScreenshotDirectory();
//            File targetDirectory = new File(directory);
//
//            if (!targetDirectory.exists()) {
//                targetDirectory.mkdirs();
//            }
//
//            File targetFile = new File(directory + File.separator + fileName);
//            FileUtils.copyFile(sourceFile, targetFile);
//
//            logger.info("Screenshot captured: " + targetFile.getAbsolutePath());
//            return targetFile.getAbsolutePath();
//
//        } catch (IOException e) {
//            logger.error("Failed to capture screenshot: " + e.getMessage());
//            return null;
//        }
//    }

    /**
     * Capture screenshot as byte array
     */
    public static byte[] captureScreenshotAsBytes(WebDriver driver) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            return screenshot.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as bytes: " + e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Capture screenshot and attach to Allure report
     */
    public static void captureScreenshotForAllure(WebDriver driver, String name) {
        try {
            byte[] screenshot = captureScreenshotAsBytes(driver);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
            logger.info("Screenshot attached to Allure report: " + name);
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Allure: " + e.getMessage());
        }
    }

    /**
     * Capture screenshot on test failure
     */
    public static void captureScreenshotOnFailure(WebDriver driver, String testName) {
        if (config.isScreenshotOnFailure()) {
            logger.info("Test failed, capturing screenshot...");
            String screenshotPath = captureScreenshot(driver, testName + "_FAILED");
            captureScreenshotForAllure(driver, "Failed: " + testName);
            logger.info("Failure screenshot saved: " + screenshotPath);
        }
    }

    /**
     * Delete old screenshots (cleanup)
     */
    public static void deleteOldScreenshots(int daysOld) {
        try {
            File screenshotDir = new File(config.getScreenshotDirectory());
            if (screenshotDir.exists()) {
                File[] files = screenshotDir.listFiles();
                if (files != null) {
                    long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60 * 60 * 1000);
                    int deletedCount = 0;
                    
                    for (File file : files) {
                        if (file.lastModified() < cutoffTime) {
                            if (file.delete()) {
                                deletedCount++;
                            }
                        }
                    }
                    
                    logger.info("Deleted " + deletedCount + " old screenshot(s)");
                }
            }
        } catch (Exception e) {
            logger.error("Error deleting old screenshots: " + e.getMessage());
        }
    }
}
