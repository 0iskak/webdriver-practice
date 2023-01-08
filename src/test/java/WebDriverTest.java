import me.iskak.webdriver.cloud.CloudSearchPage;
import me.iskak.webdriver.cloud.ComputeEngineCalculatorPage;
import me.iskak.webdriver.paste.CreatePastePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WebDriverTest {
    private WebDriver driver;
    private CreatePastePage createPastePage;
    private CloudSearchPage cloudSearchPage;

    @BeforeClass
    private void initProperty() {
        String driverPropertyName = "webdriver.edge.driver";
        if (System.getProperty(driverPropertyName) == null)
            System.setProperty(driverPropertyName, "A:\\msedgedriver.exe");
    }

    @BeforeMethod
    private void initDriver() {
        driver = new EdgeDriver();
    }

    @BeforeMethod(dependsOnMethods = "initDriver", onlyForGroups = "paste")
    private void initPasteDriver() {
        driver.get("https://textbin.net");
        createPastePage = new CreatePastePage(driver);
    }

    @BeforeMethod(dependsOnMethods = "initDriver", onlyForGroups = "cloud")
    private void initGloudDriver() {
        driver.get("https://cloud.google.com/");
        cloudSearchPage = new CloudSearchPage(driver);
    }

    @AfterMethod
    private void quitDriver() {
        driver.quit();
    }

    @Test(groups = "paste", priority = 1)
    public void iCanWin() {
        createPastePage.setCode("Hello from WebDriver")
                .setExpiration("10 Minutes")
                .setTitle("heeloweb")
                .createPastePage();
    }

    @Test(groups = "paste", priority = 2)
    public void bringItOn() {
        var code = """
                git config --global user.name  "New Sheriff in Town"
                git reset $(git commit-tree HEAD^{tree} -m "Legacy code")
                git push origin master --force""";
        var title = "how to gain dominance among developers";
        var syntax = "Bash";
        var expiration = "10 Minutes";


        var pastePage = createPastePage.setCode(code)
                .setSyntax(syntax)
                .setExpiration(expiration)
                .setTitle(title)
                .createPastePage();

        Assert.assertTrue(pastePage.getTitle().startsWith(title), "Paste title");
        Assert.assertEquals(pastePage.getSyntax(), syntax, "Paste syntax");
        Assert.assertEquals(pastePage.getCode(), code, "Paste code");
    }

    @Test(groups = "cloud", priority = 3)
    public void hurtMePlenty() {
        var vmClass = "Regular";
        var type = "n1-standard-8";
        var ssd = "2x375";
        var location = "Frankfurt (europe-west3)";
        var comittedYear = "1 Year";

        var cost = "USD 1,081.20 per 1 month";

        var estimatePage = cloudSearchPage.setSearch("Google Cloud Platform Pricing Calculator")
                .search()
                .getFirst(ComputeEngineCalculatorPage::new)
                .setQuantity(4)
                .setSoftware("Free: Debian, CentOS, CoreOS, Ubuntu")
                .setClass(vmClass)
                .setSeries("N1")
                .setType(type)
                .setGPUs(1, "NVIDIA Tesla V100")
                .setSSD(ssd)
                .setCenterLocation(location)
                .setCommittedUsage(comittedYear)
                .addEstimate()
                .getEstimate(1);


        Assert.assertEquals(
                estimatePage.getValue("Provisioning model"),
                vmClass
        );
        Assert.assertTrue(
                estimatePage.getValue("Instance type")
                        .contains(type)
        );
        Assert.assertTrue(
                estimatePage.getValue("Local SSD")
                        .contains(ssd)
        );
        Assert.assertTrue(
                location.contains(
                        estimatePage.getValue("Region")
                )
        );
        Assert.assertEquals(
                estimatePage.getValue("Commitment term"),
                comittedYear
        );
        Assert.assertEquals(
                estimatePage.getValue("Estimated Component Cost"),
                cost
        );
    }

    @Test(groups = "cloud", priority = 4)
    public void hardcore() {
        var vmClass = "Regular";
        var type = "n1-standard-8";
        var ssd = "2x375";
        var location = "Frankfurt (europe-west3)";
        var comittedYear = "1 Year";

        var estimatePage = cloudSearchPage.setSearch("Google Cloud Platform Pricing Calculator")
                .search()
                .getFirst(ComputeEngineCalculatorPage::new)
                .setQuantity(4)
                .setSoftware("Free: Debian, CentOS, CoreOS, Ubuntu")
                .setClass(vmClass)
                .setSeries("N1")
                .setType(type)
                .setGPUs(1, "NVIDIA Tesla V100")
                .setSSD(ssd)
                .setCenterLocation(location)
                .setCommittedUsage(comittedYear)
                .addEstimate()
                .getEstimate(1);

        Assert.assertEquals(estimatePage.getPriceFromEmail(), "USD 1,081.20");
    }
}
