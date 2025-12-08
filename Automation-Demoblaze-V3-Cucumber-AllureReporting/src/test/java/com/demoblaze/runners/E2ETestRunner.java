package com.demoblaze.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * E2ETestRunner - Runs end-to-end tests with @e2e tag
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.demoblaze.stepdefinitions", "com.demoblaze.hooks"},
        tags = "@e2e",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-e2e.html",
                "json:target/cucumber-reports/cucumber-e2e.json",
                "junit:target/cucumber-reports/cucumber-e2e.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        dryRun = false
)
public class E2ETestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
