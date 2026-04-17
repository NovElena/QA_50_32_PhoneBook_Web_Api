package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.util.Objects;

public class HomePage extends BasePage{
    public HomePage(WebDriver driver){
        WebDriver currentDriver = Objects.requireNonNull(driver, "WebDriver is null. Check browser setup before opening HomePage.");
        setDriver(currentDriver);
        PageFactory.initElements(new AjaxElementLocatorFactory(currentDriver, 10), this);
    }

    @FindBy(xpath = "//a[text()='LOGIN']")
    WebElement btnLogin;

    public void clickBtnLogin(){

        btnLogin.click();
    }


}
