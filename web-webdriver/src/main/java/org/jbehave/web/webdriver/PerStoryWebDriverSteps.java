package org.jbehave.web.webdriver;

import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeStory;

/**
 * Steps implementation that can be used in WebDriver-based stories. It
 * provides annotated methods to start and stop WebDriver before and after
 * stories. 
 *
 * @author Mauro Talevi
 */
public class PerStoryWebDriverSteps extends WebDriverSteps {
        
    public PerStoryWebDriverSteps(WebDriverFactory driverFactory) {
        super(driverFactory);
    }

    @BeforeStory
    public void beforeStory() throws Exception {
        driverFactory.initialize();
    }

    @AfterStory
    public void afterStory() throws Exception {
        getDriverFactory().get().quit();
    }

}