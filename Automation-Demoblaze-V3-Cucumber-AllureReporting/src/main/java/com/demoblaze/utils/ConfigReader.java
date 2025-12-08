package com.demoblaze.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from config.properties file
 * Implements Singleton pattern
 */
public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;

    private ConfigReader() {
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigReader
     */
    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        properties = new Properties();
        try {
            // Try loading from classpath
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("config.properties");
            
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration loaded successfully from classpath");
            } else {
                // Try loading from file system
                FileInputStream fileInputStream = new FileInputStream(
                        "src/test/resources/config.properties");
                properties.load(fileInputStream);
                logger.info("Configuration loaded successfully from file system");
            }
        } catch (IOException e) {
            logger.error("Error loading configuration file: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    /**
     * Get property value by key
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '" + key + "' not found in configuration");
        }
        return value;
    }

    /**
     * Get property value by key with default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get integer property value
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Property '" + key + "' is not a valid integer");
            return 0;
        }
    }

    /**
     * Get boolean property value
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }

    // Convenience methods for commonly used properties
    public String getAppUrl() {
        return getProperty("app.url");
    }

    public String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        return getBooleanProperty("headless");
    }

    public int getImplicitWait() {
        return getIntProperty("implicit.wait");
    }

    public int getExplicitWait() {
        return getIntProperty("explicit.wait");
    }

    public int getPageLoadTimeout() {
        return getIntProperty("page.load.timeout");
    }

    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.onFailure");
    }

    public String getScreenshotDirectory() {
        return getProperty("screenshot.directory", "target/screenshots");
    }

    public String getDefaultPassword() {
        return getProperty("default.password", "Test@123");
    }

    public String getTestUserPrefix() {
        return getProperty("test.user.prefix", "testuser_");
    }

    // Video recording configuration methods
    public boolean isVideoRecordingEnabled() {
        return getBooleanProperty("video.recording.enabled");
    }

    public String getVideoDirectory() {
        return getProperty("video.directory", "target/videos");
    }

    public boolean isVideoOnFailureOnly() {
        return getBooleanProperty("video.onFailureOnly");
    }

    public int getVideoFrameRate() {
        return getIntProperty("video.framerate");
    }

    public String getVideoFormat() {
        return getProperty("video.format", "avi");
    }

    public int getVideoCleanupDays() {
        return getIntProperty("video.cleanup.days");
    }

    // Allure reporting configuration
    public boolean isAllureReportingEnabled() {
        return getBooleanProperty("allure.reporting.enabled");
    }

    public String getAllureResultsDirectory() {
        return getProperty("allure.results.directory", "target/allure-results");
    }

    public String getAllureReportDirectory() {
        return getProperty("allure.report.directory", "target/allure-report");
    }
}
