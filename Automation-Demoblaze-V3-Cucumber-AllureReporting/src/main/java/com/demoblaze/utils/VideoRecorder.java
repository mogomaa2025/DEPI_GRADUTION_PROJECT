package com.demoblaze.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * VideoRecorder - Records screen during test execution for enhanced reporting
 * Uses Java Robot for basic screen capture functionality
 */
public class VideoRecorder {
    private static final Logger logger = LogManager.getLogger(VideoRecorder.class);
    private static VideoRecorder instance;
    private static final ConfigReader config = ConfigReader.getInstance();
    
    private Robot robot;
    private ScheduledExecutorService executor;
    private List<BufferedImage> frames;
    private Rectangle captureArea;
    private String testName;
    private File outputDirectory;
    private boolean isRecording = false;
    
    private VideoRecorder(String testName) throws AWTException {
        this.robot = new Robot();
        this.frames = new ArrayList<>();
        this.captureArea = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        this.testName = testName;
        this.outputDirectory = new File(config.getVideoDirectory());
        
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
    }

    /**
     * Start recording for a test
     */
    public static void startRecording(String testName) {
        try {
            if (config.isVideoRecordingEnabled()) {
                stopRecording(); // Stop any existing recording
                
                String videoDir = config.getVideoDirectory();
                Files.createDirectories(Paths.get(videoDir));
                
                instance = new VideoRecorder(testName);
                instance.startCapture();
                
                logger.info("Video recording started for test: " + testName);
            }
        } catch (Exception e) {
            logger.error("Failed to start video recording: " + e.getMessage(), e);
        }
    }
    
    /**
     * Start the actual capture process
     */
    private void startCapture() {
        isRecording = true;
        frames.clear();
        
        int frameRate = config.getVideoFrameRate();
        if (frameRate <= 0) frameRate = 2; // Default to 2 FPS for performance
        
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            if (isRecording) {
                try {
                    BufferedImage screenshot = robot.createScreenCapture(captureArea);
                    synchronized (frames) {
                        frames.add(screenshot);
                        // Limit frames to prevent memory issues (max 30 seconds at 2 FPS = 60 frames)
                        if (frames.size() > 60) {
                            frames.remove(0);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error capturing frame: " + e.getMessage());
                }
            }
        }, 0, 1000 / frameRate, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop recording and attach to Allure report
     */
    public static void stopRecording() {
        try {
            if (instance != null) {
                instance.stopCapture();
                
                // Save frames as images and create a summary
                File outputFile = instance.saveFrames(false);
                
                if (outputFile != null && outputFile.exists()) {
                    // Attach to Allure report as images
                    instance.attachFramesToAllure("Test Recording: " + instance.testName);
                    
                    logger.info("Video recording stopped and frames attached to Allure report");
                } else {
                    logger.warn("No frames captured during recording");
                }
                
                instance = null;
            }
        } catch (Exception e) {
            logger.error("Failed to stop video recording: " + e.getMessage(), e);
        }
    }
    
    /**
     * Stop the capture process
     */
    private void stopCapture() {
        isRecording = false;
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stop recording for failed tests
     */
    public static void stopRecordingOnFailure(String testName) {
        try {
            if (instance != null) {
                instance.stopCapture();
                
                // Save frames with failure indication
                File outputFile = instance.saveFrames(true);
                
                if (outputFile != null && outputFile.exists()) {
                    // Attach failed test frames to Allure
                    instance.attachFramesToAllure("FAILED Test Recording: " + testName);
                    
                    logger.info("Failed test recording saved and attached to Allure");
                }
                
                instance = null;
            }
        } catch (Exception e) {
            logger.error("Failed to handle video recording for failed test: " + e.getMessage(), e);
        }
    }

    /**
     * Save captured frames to disk
     */
    private File saveFrames(boolean isFailure) {
        try {
            if (frames.isEmpty()) {
                logger.warn("No frames captured to save");
                return null;
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String prefix = isFailure ? "FAILED_" : "";
            String baseName = prefix + testName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp;
            
            // Create a summary image with key frames
            File summaryFile = new File(outputDirectory, baseName + "_summary.png");
            createFrameSummary(summaryFile);
            
            return summaryFile;
        } catch (Exception e) {
            logger.error("Error saving frames: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Create a summary image with key frames
     */
    private void createFrameSummary(File outputFile) throws IOException {
        synchronized (frames) {
            if (frames.isEmpty()) return;
            
            // Select up to 6 key frames for summary
            List<BufferedImage> keyFrames = new ArrayList<>();
            int frameCount = frames.size();
            
            if (frameCount <= 6) {
                keyFrames.addAll(frames);
            } else {
                // Select frames at equal intervals
                for (int i = 0; i < 6; i++) {
                    int index = (i * frameCount) / 6;
                    keyFrames.add(frames.get(index));
                }
            }
            
            // Create a grid layout (2x3 or 3x2)
            int cols = Math.min(3, keyFrames.size());
            int rows = (keyFrames.size() + cols - 1) / cols;
            
            BufferedImage firstFrame = keyFrames.get(0);
            int frameWidth = firstFrame.getWidth() / 4; // Scale down
            int frameHeight = firstFrame.getHeight() / 4;
            
            BufferedImage summary = new BufferedImage(
                frameWidth * cols, 
                frameHeight * rows, 
                BufferedImage.TYPE_INT_RGB
            );
            
            Graphics2D g2d = summary.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            for (int i = 0; i < keyFrames.size(); i++) {
                int x = (i % cols) * frameWidth;
                int y = (i / cols) * frameHeight;
                g2d.drawImage(keyFrames.get(i), x, y, frameWidth, frameHeight, null);
                
                // Add frame number
                g2d.setColor(Color.YELLOW);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString("Frame " + (i + 1), x + 5, y + 15);
            }
            
            g2d.dispose();
            
            ImageIO.write(summary, "png", outputFile);
        }
    }
    
    /**
     * Attach frames to Allure report
     */
    private void attachFramesToAllure(String attachmentName) {
        try {
            synchronized (frames) {
                if (frames.isEmpty()) return;
                
                // Attach first frame (start)
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(frames.get(0), "png", baos);
                Allure.addAttachment(
                    attachmentName + " - Start",
                    "image/png",
                    new ByteArrayInputStream(baos.toByteArray()),
                    ".png"
                );
                
                // Attach last frame (end) if different
                if (frames.size() > 1) {
                    baos = new ByteArrayOutputStream();
                    ImageIO.write(frames.get(frames.size() - 1), "png", baos);
                    Allure.addAttachment(
                        attachmentName + " - End",
                        "image/png",
                        new ByteArrayInputStream(baos.toByteArray()),
                        ".png"
                    );
                }
                
                // Attach middle frame if available
                if (frames.size() > 2) {
                    baos = new ByteArrayOutputStream();
                    ImageIO.write(frames.get(frames.size() / 2), "png", baos);
                    Allure.addAttachment(
                        attachmentName + " - Middle",
                        "image/png",
                        new ByteArrayInputStream(baos.toByteArray()),
                        ".png"
                    );
                }
            }
        } catch (Exception e) {
            logger.error("Error attaching frames to Allure: " + e.getMessage(), e);
        }
    }

    /**
     * Clean up old video files
     */
    public static void cleanupOldVideos(int daysOld) {
        try {
            String videoDir = config.getVideoDirectory();
            File directory = new File(videoDir);
            
            if (!directory.exists()) {
                return;
            }
            
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
            
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
                
                logger.info("Deleted " + deletedCount + " old video file(s)");
            }
        } catch (Exception e) {
            logger.error("Error cleaning up old videos: " + e.getMessage(), e);
        }
    }

    /**
     * Check if video recording is currently active
     */
    public static boolean isRecording() {
        return instance != null && instance.isRecording;
    }
}