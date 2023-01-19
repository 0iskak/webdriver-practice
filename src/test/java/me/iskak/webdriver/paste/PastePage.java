package me.iskak.webdriver.paste;

import me.iskak.webdriver.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PastePage extends Page {
    @FindBy(id = "pre")
    private WebElement locadingCode;

    @FindBy(css = "#printarea > *:nth-child(1) > *:nth-child(1)")
    private WebElement syntax;

    public PastePage(WebDriver driver) {
        super(driver, null);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getSyntax() {
        return syntax.getText();
    }

    public String getCode() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driver -> !locadingCode.isDisplayed());

        var js = (JavascriptExecutor) driver;

        return (String) js.executeScript("return content");
    }
}
