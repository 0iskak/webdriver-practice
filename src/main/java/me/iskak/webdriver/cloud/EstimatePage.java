package me.iskak.webdriver.cloud;

import me.iskak.webdriver.Page;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EstimatePage extends Page {
    private final WebElement estimateElement;
    public EstimatePage(WebDriver driver, WebElement estimateElement) {
        super(driver, null);
        this.estimateElement = estimateElement;
    }

    public String getValue(String key) {
        var xpath = By.xpath("//*[contains(text(), '%s:')]".formatted(key));
        var value = estimateElement.findElement(xpath).getText();
        return value.substring(value.indexOf(':') + 1).strip();
    }

    private EstimatePage emailEstimate(String email) {
        var xpath = By.xpath("//button[@title='Email Estimate']");
        estimateElement.findElement(xpath).click();

        var selector = By.cssSelector("form[name='emailForm']");
        var form = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(selector));

        form.findElement(By.cssSelector("input[type='email']")).sendKeys(email);
        form.findElement(By.xpath("//button[normalize-space()='Send Email']")).click();
        return this;
    }

    public String getPriceFromEmail() {
        var cloudHandle = driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to("https://yopmail.com/en/email-generator");

        var emailHandle = driver.getWindowHandle();

        var emailPage = new EmailPage(driver);
        var address = emailPage.getAddress();

        driver.switchTo().window(cloudHandle);
        var iframeSelector = By.cssSelector("iframe");
        driver.switchTo().frame(driver.findElement(iframeSelector))
                .switchTo().frame(driver.findElement(iframeSelector));
        emailEstimate(address);


        driver.switchTo().window(emailHandle);
        return emailPage.getPriceEstimate();
    }
}
