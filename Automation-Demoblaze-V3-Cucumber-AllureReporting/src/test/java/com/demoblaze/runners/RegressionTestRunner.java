package com.demoblaze.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * RegressionTestRunner - Runs all regression tests with @regression tag
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.demoblaze.stepdefinitions", "com.demoblaze.hooks"},
        tags = "@regression",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-regression.html",
                "json:target/cucumber-reports/cucumber-regression.json",
                "junit:target/cucumber-reports/cucumber-regression.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        dryRun = false
)
public class RegressionTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
