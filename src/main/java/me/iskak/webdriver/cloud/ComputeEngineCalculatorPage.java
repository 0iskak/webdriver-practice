package me.iskak.webdriver.cloud;

import me.iskak.webdriver.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ComputeEngineCalculatorPage extends Page {
    @FindBy(css = "[title='Compute Engine']")
    private WebElement computeEngineButton;
    @FindBy(css = "input[name='quantity']")
    private WebElement quantityInput;

    @FindBy(css = "md-select[name='series']")
    private WebElement seriesSelect;

    @FindBy(xpath = "//label[text()='Machine type']/following-sibling::md-select")
    private WebElement typeSelect;

    @FindBy(xpath = "//md-checkbox/*[contains(text(), 'Add GPUs.')]//..")
    private WebElement gpuCheckbox;

    private int estimatesCount = 0;

    public ComputeEngineCalculatorPage(WebDriver driver) {
        super(driver, () -> {
            var iframeSelector = By.cssSelector("iframe");
            driver.switchTo().frame(driver.findElement(iframeSelector))
                    .switchTo().frame(driver.findElement(iframeSelector));
        });
    }

    public ComputeEngineCalculatorPage setQuantity(int quantity) {
        quantityInput.sendKeys(Integer.toString(quantity));
        return this;
    }

    public ComputeEngineCalculatorPage setSeries(String series) {
        seriesSelect.click();
        waitAnimation();
        driver.findElement(mdOption(series))
                .click();
        return this;
    }

    public ComputeEngineCalculatorPage setType(String type) {
        typeSelect.click();
        waitAnimation();
        driver.findElement(mdOptionContains(type)).click();
        return this;
    }

    public ComputeEngineCalculatorPage setGPUs(int count, String name) {
        if (gpuCheckbox.getAttribute("aria-checked").equals("false"))
            gpuCheckbox.click();

        driver.findElement(mdInput("GPU type")).click();
        waitAnimation();
        driver.findElement(mdOption(name)).click();

        driver.findElement(mdInput("Number of GPUs")).click();
        waitAnimation();
        driver.findElement(mdOption(Integer.toString(count))).click();

        return this;
    }

    public ComputeEngineCalculatorPage setSSD(String ssd) {
        driver.findElement(mdInput("Local SSD")).click();
        waitAnimation();
        driver.findElement(mdOptionContains(ssd)).click();
        return this;
    }

    public ComputeEngineCalculatorPage setCenterLocation(String location) {
        driver.findElement(mdInput("Datacenter location")).click();
        waitAnimation();
        findDisplayed(mdOption(location)).click();
        return this;
    }

    public ComputeEngineCalculatorPage setCommittedUsage(String year) {
        driver.findElement(mdInput("Committed usage")).click();
        waitAnimation();
        findDisplayed(mdOption(year)).click();
        return this;
    }

    public ComputeEngineCalculatorPage setSoftware(String software) {
        driver.findElement(mdInput("Operating System / Software")).click();
        waitAnimation();
        driver.findElement(mdOptionContains(software)).click();
        return this;
    }

    public ComputeEngineCalculatorPage setClass(String vmClass) {
        driver.findElement(mdInput("Provisioning model")).click();
        waitAnimation();
        driver.findElement(mdOptionContains(vmClass)).click();
        return this;
    }

    public EstimatePage getEstimate(int nth) {
        var el = driver.findElements(
                By.cssSelector("#compute > md-list")
        ).get(nth - 1);
        return new EstimatePage(driver, el);
    }

    public ComputeEngineCalculatorPage addEstimate() {
        var xpath = By.xpath("//button[contains(text(), 'Add to Estimate')]");
        driver.findElement(xpath).click();
        estimatesCount++;
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driver -> {
                    try {
                        var elementsCount = driver.findElements(
                                By.cssSelector("#compute > md-list")
                        ).size();
                        return elementsCount == estimatesCount;
                    } catch (NoSuchElementException ignored) {
                        return false;
                    }
                });
        return this;
    }

    private WebElement findDisplayed(By by) {
        return driver.findElements(by)
                .stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No displayed elements"));
    }

    private static By mdInput(String text) {
        return By.xpath("//md-input-container/*[text()='%s']/../md-select".formatted(text));
    }

    private static By mdOption(String name) {
        return By.xpath("//md-option/*[normalize-space()='%s']/..".formatted(name));
    }

    private static By mdOptionContains(String name) {
        return By.xpath("//md-option/*[contains(text(), '%s')]/..".formatted(name));
    }

    // IDK how to wait for animation end

    private static void waitAnimation() {
        try {
            TimeUnit.MILLISECONDS.sleep(250);
        } catch (InterruptedException ignored) {
        }
    }
}
