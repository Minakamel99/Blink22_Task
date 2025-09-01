package Task;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class task {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://blink22.com/");     // Go to Blink22 website
    }

    @BeforeMethod
    public void BlogPage() {
        driver.findElement(By.linkText("Blog")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("blog"),
                "Blog page should be opened");
        driver.get("https://blink22.com/blog");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='rcc-confirm-button']"))).click();
            System.out.println("Accepted cookies");
        } catch (Exception e) {
            System.out.println("The cookies have already been accepted.");
        }
    }



    @Test(priority = 1)
    public void testExtractPlaceholders() {
        WebElement nameField = driver.findElement(By.xpath("//*[@id='fullname']"));
        WebElement emailField = driver.findElement(By.xpath("//*[@id='email']"));
        String namePlaceholder = nameField.getAttribute("placeholder");
        String emailPlaceholder = emailField.getAttribute("placeholder");
        System.out.println("Name Placeholder: " + namePlaceholder);
        System.out.println("Email Placeholder: " + emailPlaceholder);
        Assert.assertNotNull(namePlaceholder, "Name placeholder not found");
        Assert.assertNotNull(emailPlaceholder, "Email placeholder not found");
    }

    @Test(priority = 2)
    public void testMissingFieldValidation() {
        driver.findElement(By.xpath("//*[@id='_form_5_submit']")).click();

        WebElement nameError = driver.findElement(By.xpath("//*[contains(text(), 'This field is required.')]"));
        WebElement emailError = driver.findElement(By.xpath("//*[contains(text(), 'This field is required.')]"));

        System.out.println("Name Error: " + nameError.getText());
        System.out.println("Email Error: " + emailError.getText());

        Assert.assertTrue(nameError.isDisplayed(), "Name error message not displayed");
        Assert.assertTrue(emailError.isDisplayed(), "Email error message not displayed");
    }


    @Test(priority = 3)
    public void requiredFieldsValidation() {
        WebElement submitButton = driver.findElement(By.cssSelector("form button[type='submit']"));
        submitButton.click();

        // Validate error messages appear
        List<WebElement> errors = driver.findElements(By.cssSelector("form .error"));
        Assert.assertTrue(errors.size() > 0, "Error message should appear for required fields");
    }


    @Test(priority = 4)
    public void testInvalidEmailFormat() {
        WebElement nameField = driver.findElement(By.xpath("//*[@id='fullname']"));
        WebElement emailField = driver.findElement(By.xpath("//*[@id='email']"));

        nameField.clear();
        emailField.clear();
        nameField.sendKeys("Mina");
        emailField.sendKeys("123Mina123");
        driver.findElement(By.xpath("//*[@id='_form_5_submit']")).click();
        WebElement invalidEmailError = driver.findElement(By.xpath("//*[contains(text(), 'Enter a valid email address.')]"));
        System.out.println("Invalid Email Error: " + invalidEmailError.getText());
        Assert.assertTrue(invalidEmailError.isDisplayed(), "Invalid email error message not displayed");
    }

    @Test(priority = 5)
    public void testvalidEmailFormat() {
        WebElement nameField = driver.findElement(By.xpath("//*[@id='fullname']"));
        WebElement emailField = driver.findElement(By.xpath("//*[@id='email']"));

        nameField.clear();
        emailField.clear();
        nameField.sendKeys("Mina");
        emailField.sendKeys("minakamel@gmail.com");
        driver.findElement(By.xpath("//*[@id='_form_5_submit']")).click();
        WebElement thanksMessage = driver.findElement(By.xpath("//*[contains(text(),'Thank')]"));

        Assert.assertTrue(thanksMessage.isDisplayed(), "Thanks message should appear after valid submission");
        System.out.println("Thanks Message: " + thanksMessage.getText());
    }



    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
