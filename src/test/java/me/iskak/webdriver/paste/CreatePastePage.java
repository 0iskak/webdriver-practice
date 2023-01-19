package me.iskak.webdriver.paste;

import me.iskak.webdriver.Page;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class CreatePastePage extends Page {
    @FindBy(css = "textarea")
    private WebElement codeTextarea;

    @FindBy(css = "select[name='expire']")
    private WebElement expirationSelect;

    @FindBy(css = "input[name='title']")
    private WebElement titleInput;

    @FindBy(css = "button[type='submit']")
    private WebElement createButton;

    @FindBy(css = "select[name='syntax']")
    private WebElement syntaxSelect;

    public CreatePastePage(WebDriver driver) {
        super(driver, null);
    }

    public CreatePastePage setCode(String code) {
        codeTextarea.clear();
        codeTextarea.sendKeys(code);
        return this;
    }

    public CreatePastePage setExpiration(String expiration) {
        new Select(expirationSelect).selectByVisibleText(expiration);
        return this;
    }

    public CreatePastePage setTitle(String title) {
        titleInput.clear();
        titleInput.sendKeys(title);
        return this;
    }

    public CreatePastePage setSyntax(String syntax) {
        new Select(syntaxSelect).selectByVisibleText(syntax);
        return this;
    }

    public PastePage createPastePage() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", createButton);
        createButton.click();
        return new PastePage(driver);
    }
}
