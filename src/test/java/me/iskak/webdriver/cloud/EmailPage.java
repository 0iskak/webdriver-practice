package me.iskak.webdriver.cloud;

import me.iskak.webdriver.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

public class EmailPage extends Page {
    @FindBy(id = "geny")
    private WebElement emailElement;

    @FindBy(xpath = "//*[text()='Check Inbox']//..")
    private WebElement checkButton;

    public EmailPage(WebDriver driver) {
        super(driver, null);
    }

    public String getAddress() {
        return emailElement.getText().strip();
    }

    public String getPriceEstimate() {
        checkButton.click();

        switchToMailFrame();

        var price = new AtomicReference<String>();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> {
                    try {
                        var xpath = By.xpath("//h2[contains(text(), 'Estimated Monthly Cost:')]");
                        price.set(driver.findElement(xpath).getText());
                        return true;
                    } catch (NoSuchElementException ignored) {
                        driver.navigate().refresh();
                        switchToMailFrame();
                        return false;
                    }
                });

        return price.get().substring(price.get().indexOf(':') + 1).strip();
    }

    private void switchToMailFrame() {
        driver.switchTo().frame(
                driver.findElement(By.cssSelector("#wmmailmain iframe"))
        );
    }
}
