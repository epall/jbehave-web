package org.jbehave.web.selenium;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class SauceWebDriverProvider extends DelegatingWebDriverProvider {

    DesiredCapabilities desiredCapabilities;

    public SauceWebDriverProvider(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    public SauceWebDriverProvider() {
        desiredCapabilities = DesiredCapabilities.firefox();
        desiredCapabilities.setVersion("3.6.");
        desiredCapabilities.setPlatform(Platform.WINDOWS);
        desiredCapabilities.setCapability("name", "JBehave");
    }

    public void initialize() {
        try {
            String username = System.getProperty("SAUCE_USERNAME");
            String access_key = System.getProperty("SAUCE_ACCESS_KEY");
            if (username == null) {
                throw new UnsupportedOperationException("SAUCE_USERNAME property name variable not specified");
            }
            if (access_key == null) {
                throw new UnsupportedOperationException("SAUCE_ACCESS_KEY property name variable not specified");
            }

            delegate.set(new RemoteWebDriver(new URL("http://" + username + ":" + access_key
                    + "@ondemand.saucelabs.com/wd/hub"), desiredCapabilities));
        } catch (MalformedURLException e) {
            banner();
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        } catch (UnsupportedOperationException e) {
            banner();
            e.printStackTrace();
            throw e;
        } catch (Throwable e) {
            banner();
            e.printStackTrace();
            throw new UnsupportedOperationException(e);
        }
    }

    private void banner() {
        System.out.println("************** Error Initializing WebDriverProvider *************");
    }

}