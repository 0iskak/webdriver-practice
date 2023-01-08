package me.iskak.webdriver.cloud;

import me.iskak.webdriver.Page;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

public class CloudSearchPage extends Page {
    @FindBy(css = "input.devsite-search-field.devsite-search-query")
    private WebElement searchInput;

    public CloudSearchPage(WebDriver driver) {
        super(driver, null);
    }

    public CloudSearchPage setSearch(String search) {
        searchInput.click();
        searchInput.sendKeys(search);
        return this;
    }

    public CloudSearchPage search() {
        searchInput.click();
        searchInput.sendKeys(Keys.ENTER);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> {
                    try {
                        var by = By.cssSelector(".gsc-expansionArea > .gsc-webResult.gsc-result");
                        var elements = driver.findElements(by);
                        return elements.size() > 0;
                    } catch (NoSuchElementException ignored) {
                        return false;
                    }
                });

        return this;
    }

    public <T> T getFirst(Function<WebDriver, T> callback) {
        driver.findElement(By.cssSelector(".gsc-expansionArea > .gsc-webResult.gsc-result a"))
                .click();

        return callback.apply(driver);
    }
}
