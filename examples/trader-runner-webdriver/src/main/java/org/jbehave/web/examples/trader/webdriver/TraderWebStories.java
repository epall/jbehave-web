package org.jbehave.web.examples.trader.webdriver;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.HTML;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.IDE_CONSOLE;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.TXT;
import static org.jbehave.core.reporters.StoryReporterBuilder.Format.XML;

import java.util.List;

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.ConsoleOutput;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.SilentStepMonitor;
import org.jbehave.web.examples.trader.webdriver.pages.Pages;
import org.jbehave.web.selenium.WebDriverScreenshotOnFailure;
import org.jbehave.web.examples.trader.webdriver.steps.TraderWebSteps;
import org.jbehave.web.selenium.ContextView;
import org.jbehave.web.selenium.PerStoriesWebDriverSteps;
import org.jbehave.web.selenium.LocalFrameContextView;
import org.jbehave.web.selenium.PropertyWebDriverProvider;
import org.jbehave.web.selenium.SeleniumConfiguration;
import org.jbehave.web.selenium.SeleniumContext;
import org.jbehave.web.selenium.SeleniumStepMonitor;
import org.jbehave.web.selenium.WebDriverProvider;

public class TraderWebStories extends JUnitStories {

    private WebDriverProvider driverProvider = new PropertyWebDriverProvider();
    private Pages pages = new Pages(driverProvider);
    private SeleniumContext context = new SeleniumContext();
    private ContextView contextView = new LocalFrameContextView().sized(500, 100);

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();
        return new SeleniumConfiguration()
                .useSeleniumContext(context)
                .useWebDriverProvider(driverProvider)
                .useStepMonitor(new SeleniumStepMonitor(contextView, context, new SilentStepMonitor()))
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryReporterBuilder(new StoryReporterBuilder() {
                            @Override
                            public StoryReporter reporterFor(String storyPath, Format format) {
                                if (format == IDE_CONSOLE) {
                                    return new ConsoleOutput() {
                                        @Override
                                        public void beforeScenario(String scenarioTitle) {
                                            context.setCurrentScenario(scenarioTitle);
                                            super.beforeScenario(scenarioTitle);
                                        }

                                        @Override
                                        public void afterStory(boolean givenStory) {
                                            contextView.close();
                                            super.afterStory(givenStory);
                                        }
                                    };
                                } else {
                                    return super.reporterFor(storyPath, format);
                                }
                            }

                        }
                    .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                    .withDefaultFormats()
                    .withFormats(IDE_CONSOLE, TXT, HTML, XML));
    }

    @Override
    public List<CandidateSteps> candidateSteps() {
        Configuration configuration = configuration();
        return new InstanceStepsFactory(configuration, 
                new TraderWebSteps(pages),
                new PerStoriesWebDriverSteps(driverProvider), // or PerStoryWebDriverSteps or PerScenarioWebDriverSteps
                new WebDriverScreenshotOnFailure(driverProvider, configuration.storyReporterBuilder())).createCandidateSteps();
    }


    @Override
    protected List<String> storyPaths() {
        return new StoryFinder()
                .findPaths(codeLocationFromClass(this.getClass()).getFile(), asList("**/*.story"), null);
    }

}
