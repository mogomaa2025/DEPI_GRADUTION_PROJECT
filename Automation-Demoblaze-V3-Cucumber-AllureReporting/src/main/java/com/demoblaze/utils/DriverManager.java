package com.demoblaze.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DriverManager - Manages WebDriver instances
 * Implements ThreadLocal for parallel execution
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ConfigReader config = ConfigReader.getInstance();
    
    // Track all driver instances for proper cleanup
    private static final Set<WebDriver> allDriverInstances = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    // JVM shutdown hook to cleanup any remaining drivers
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("JVM shutdown detected, cleaning up remaining drivers...");
            cleanupAllDrivers();
        }));
    }

    /**
     * Initialize WebDriver based on browser configuration
     */
    public static void initializeDriver() {
        initializeDriver(config.getBrowser());
    }

    /**
     * Initialize WebDriver with specific browser
     */
    public static void initializeDriver(String browser) {
        // Clean up any existing driver for this thread first
        if (driver.get() != null) {
            logger.warn("Driver already initialized for this thread, cleaning up first");
            quitDriver();
        }

        logger.info("Initializing " + browser + " driver...");
        WebDriver webDriver = null;

        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    webDriver = new ChromeDriver(getChromeOptions());
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    webDriver = new FirefoxDriver(getFirefoxOptions());
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    webDriver = new EdgeDriver(getEdgeOptions());
                    break;

                case "safari":
                    webDriver = new SafariDriver();
                    break;

                default:
                    logger.error("Unsupported browser: " + browser);
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }

            // Configure timeouts
            webDriver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
            webDriver.manage().timeouts()
                    .pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));

            // Maximize window
            webDriver.manage().window().maximize();

            // Track this driver instance
            driver.set(webDriver);
            allDriverInstances.add(webDriver);
            
            logger.info(browser + " driver initialized successfully");
            logger.info("Total active drivers: " + allDriverInstances.size());

        } catch (Exception e) {
            logger.error("Failed to initialize " + browser + " driver: " + e.getMessage(), e);
            
            // Cleanup if driver was partially created
            if (webDriver != null) {
                try {
                    webDriver.quit();
                } catch (Exception cleanup) {
                    logger.error("Error during cleanup of failed driver: " + cleanup.getMessage());
                }
            }
            throw new RuntimeException("Driver initialization failed", e);
        }
    }

    /**
     * Get Chrome options
     */
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        
        // Disable automation flags
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        return options;
    }

    /**
     * Get Firefox options
     */
    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--disable-notifications");
        options.addPreference("dom.webnotifications.enabled", false);
        
        return options;
    }

    /**
     * Get Edge options
     */
    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        
        return options;
    }

    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            logger.warn("Driver not initialized, initializing with default browser");
            initializeDriver();
        }
        return driver.get();
    }

    /**
     * Quit and remove WebDriver instance
     */
    public static void quitDriver() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                logger.info("Quitting driver for current thread...");
                
                // Remove from tracking first
                allDriverInstances.remove(currentDriver);
                
                // Close all browser windows
                try {
                    currentDriver.quit();
                } catch (Exception e) {
                    logger.warn("Error during driver.quit(): " + e.getMessage());
                    // Force close if quit fails
                    forceCloseDriver(currentDriver);
                }
                
                // Remove from ThreadLocal
                driver.remove();
                
                logger.info("Driver quit successfully. Remaining drivers: " + allDriverInstances.size());
                
            } catch (Exception e) {
                logger.error("Error quitting driver: " + e.getMessage(), e);
                
                // Force remove from tracking even if cleanup failed
                allDriverInstances.remove(currentDriver);
                driver.remove();
            }
        } else {
            logger.debug("No driver to quit for current thread");
        }
    }

    /**
     * Force close a driver instance (for cleanup failures)
     */
    private static void forceCloseDriver(WebDriver driverInstance) {
        try {
            if (driverInstance instanceof RemoteWebDriver) {
                RemoteWebDriver remoteDriver = (RemoteWebDriver) driverInstance;
                if (remoteDriver.getSessionId() != null) {
                    logger.info("Force closing driver session: " + remoteDriver.getSessionId());
                    remoteDriver.quit();
                }
            }
        } catch (Exception e) {
            logger.warn("Force close also failed: " + e.getMessage());
        }
    }

    /**
     * Clean up all tracked driver instances
     */
    public static void cleanupAllDrivers() {
        logger.info("Cleaning up all tracked drivers. Total count: " + allDriverInstances.size());
        
        for (WebDriver driverInstance : allDriverInstances.toArray(new WebDriver[0])) {
            try {
                if (driverInstance != null) {
                    logger.info("Cleaning up driver instance");
                    driverInstance.quit();
                    allDriverInstances.remove(driverInstance);
                }
            } catch (Exception e) {
                logger.warn("Error cleaning up driver instance: " + e.getMessage());
                allDriverInstances.remove(driverInstance);
            }
        }
        
        // Clear ThreadLocal for current thread
        driver.remove();
        
        logger.info("Driver cleanup completed. Remaining drivers: " + allDriverInstances.size());
    }

    /**
     * Force cleanup all drivers (for emergency situations)
     */
    public static void forceCleanupAllDrivers() {
        logger.warn("Force cleanup initiated for all drivers");
        
        // Clear the tracking set
        Set<WebDriver> driversToClose = Set.copyOf(allDriverInstances);
        allDriverInstances.clear();
        
        // Force quit all drivers
        for (WebDriver driverInstance : driversToClose) {
            try {
                if (driverInstance != null) {
                    forceCloseDriver(driverInstance);
                }
            } catch (Exception e) {
                logger.error("Force cleanup failed for driver: " + e.getMessage());
            }
        }
        
        // Clear ThreadLocal
        driver.remove();
        
        logger.warn("Force cleanup completed");
    }

    /**
     * Check if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }

    /**
     * Get count of active drivers
     */
    public static int getActiveDriverCount() {
        return allDriverInstances.size();
    }

    /**
     * Check if there are any orphaned drivers
     */
    public static boolean hasOrphanedDrivers() {
        return !allDriverInstances.isEmpty() && driver.get() == null;
    }

    /**
     * Kill all browser processes (OS-level cleanup - use with caution)
     */
    public static void killAllBrowserProcesses() {
        logger.warn("Attempting to kill all browser processes - THIS IS A LAST RESORT!");
        
        try {
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // Windows
                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM firefox.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM msedge.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe /T");
                logger.info("Windows browser kill commands executed");
                
            } else if (os.contains("mac")) {
                // macOS
                Runtime.getRuntime().exec("pkill -f chrome");
                Runtime.getRuntime().exec("pkill -f chromedriver");
                Runtime.getRuntime().exec("pkill -f firefox");
                Runtime.getRuntime().exec("pkill -f geckodriver");
                logger.info("macOS browser kill commands executed");
                
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux
                Runtime.getRuntime().exec("pkill -f chrome");
                Runtime.getRuntime().exec("pkill -f chromedriver");
                Runtime.getRuntime().exec("pkill -f firefox");
                Runtime.getRuntime().exec("pkill -f geckodriver");
                logger.info("Linux browser kill commands executed");
            }
            
        } catch (Exception e) {
            logger.error("Failed to kill browser processes: " + e.getMessage());
        }
    }
}
