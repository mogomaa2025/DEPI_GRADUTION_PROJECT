package com.demoblaze.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * CriticalTestRunner - Runs critical tests with @critical tag
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.demoblaze.stepdefinitions", "com.demoblaze.hooks"},
        tags = "@critical",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-critical.html",
                "json:target/cucumber-reports/cucumber-critical.json",
                "junit:target/cucumber-reports/cucumber-critical.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        dryRun = false
)
public class CriticalTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
