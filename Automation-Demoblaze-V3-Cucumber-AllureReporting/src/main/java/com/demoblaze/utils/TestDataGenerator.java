package com.demoblaze.utils;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * TestDataGenerator - Generates random test data for automation
 */
public class TestDataGenerator {
    private static final Logger logger = LogManager.getLogger(TestDataGenerator.class);
    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private static final ConfigReader config = ConfigReader.getInstance();

    /**
     * Generate unique username with timestamp
     */
    public static String generateUsername() {
        String username = config.getTestUserPrefix() + System.currentTimeMillis();
        logger.debug("Generated username: " + username);
        return username;
    }

    /**
     * Generate unique username with prefix
     */
    public static String generateUsername(String prefix) {
        String username = prefix + System.currentTimeMillis();
        logger.debug("Generated username with prefix: " + username);
        return username;
    }

    /**
     * Generate random email
     */
    public static String generateEmail() {
        String email = faker.internet().emailAddress();
        logger.debug("Generated email: " + email);
        return email;
    }

    /**
     * Generate email with specific domain
     */
    public static String generateEmail(String domain) {
        String localPart = faker.name().firstName().toLowerCase() + System.currentTimeMillis();
        String email = localPart + "@" + domain;
        logger.debug("Generated email with domain: " + email);
        return email;
    }

    /**
     * Generate random password
     */
    public static String generatePassword() {
        String password = faker.internet().password(8, 16, true, true, true);
        logger.debug("Generated password");
        return password;
    }

    /**
     * Generate strong password with specific requirements
     */
    public static String generateStrongPassword() {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*";
        
        StringBuilder password = new StringBuilder();
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        
        String allChars = uppercase + lowercase + digits + special;
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        logger.debug("Generated strong password");
        return shuffleString(password.toString());
    }

    /**
     * Get default password from config
     */
    public static String getDefaultPassword() {
        return config.getDefaultPassword();
    }

    /**
     * Generate random first name
     */
    public static String generateFirstName() {
        return faker.name().firstName();
    }

    /**
     * Generate random last name
     */
    public static String generateLastName() {
        return faker.name().lastName();
    }

    /**
     * Generate random full name
     */
    public static String generateFullName() {
        return faker.name().fullName();
    }

    /**
     * Generate random phone number
     */
    public static String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    /**
     * Generate random address
     */
    public static String generateAddress() {
        return faker.address().streetAddress();
    }

    /**
     * Generate random city
     */
    public static String generateCity() {
        return faker.address().city();
    }

    /**
     * Generate random country
     */
    public static String generateCountry() {
        return faker.address().country();
    }

    /**
     * Generate random zip code
     */
    public static String generateZipCode() {
        return faker.address().zipCode();
    }

    /**
     * Generate random credit card number
     */
    public static String generateCreditCardNumber() {
        return faker.business().creditCardNumber();
    }

    /**
     * Generate random CVV
     */
    public static String generateCVV() {
        return String.format("%03d", random.nextInt(1000));
    }

    /**
     * Generate future expiry month
     */
    public static String generateExpiryMonth() {
        return String.format("%02d", random.nextInt(12) + 1);
    }

    /**
     * Generate future expiry year
     */
    public static String generateExpiryYear() {
        int currentYear = LocalDateTime.now().getYear();
        int futureYear = currentYear + random.nextInt(5) + 1;
        return String.valueOf(futureYear);
    }

    /**
     * Generate random message/text
     */
    public static String generateMessage() {
        return faker.lorem().paragraph();
    }

    /**
     * Generate random message with specific word count
     */
    public static String generateMessage(int wordCount) {
        return faker.lorem().sentence(wordCount);
    }

    /**
     * Generate long text (for testing text areas)
     */
    public static String generateLongText(int characterCount) {
        StringBuilder text = new StringBuilder();
        while (text.length() < characterCount) {
            text.append(faker.lorem().paragraph()).append(" ");
        }
        return text.substring(0, Math.min(text.length(), characterCount));
    }

    /**
     * Generate timestamp string
     */
    public static String generateTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * Generate timestamp with custom format
     */
    public static String generateTimestamp(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Generate random number in range
     */
    public static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Generate random alphanumeric string
     */
    public static String generateAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }

    /**
     * Generate SQL injection test strings
     */
    public static String[] getSqlInjectionStrings() {
        return new String[]{
            "' OR '1'='1",
            "admin' --",
            "1' OR '1' = '1",
            "'; DROP TABLE users--",
            "1' UNION SELECT NULL--"
        };
    }

    /**
     * Generate XSS test strings
     */
    public static String[] getXssStrings() {
        return new String[]{
            "<script>alert('XSS')</script>",
            "<img src=x onerror=alert('XSS')>",
            "<svg onload=alert('XSS')>",
            "javascript:alert('XSS')",
            "<iframe src='javascript:alert(\"XSS\")'>"
        };
    }

    /**
     * Helper method to shuffle string
     */
    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }
}
