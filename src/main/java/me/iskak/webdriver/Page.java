package me.iskak.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class Page {
    protected final WebDriver driver;

    public Page(WebDriver driver, Runnable init) {
        if (init != null) init.run();
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
