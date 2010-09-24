package org.jbehave.web.examples.trader.webdriver;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterScenario.Outcome;
import org.jbehave.web.webdriver.PerScenarioWebDriverSteps;
import org.jbehave.web.webdriver.WebDriverFactory;

public class FailingScenarioScreenshotCapture extends PerScenarioWebDriverSteps {

    public FailingScenarioScreenshotCapture(WebDriverFactory driverFactory) {
        super(driverFactory);
    }

    @AfterScenario(uponOutcome = Outcome.FAILURE)
    public void afterFailingScenario() throws Exception {
        String home = System.getenv("HOME");
        // getDriver().captureScreenshot(home+"/failedScenario.png");
    }

}
