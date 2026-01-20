package pages;

import dto.User;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class Login_page extends BasePage{
    public Login_page (WebDriver driver){
        setDriver(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10),this);

    }
    @FindBy(xpath = ("//input[@name='email']"))
    WebElement inputEmail;
    @FindBy(xpath = "//input[@placeholder='Password']")
    WebElement inputPassword;
    @FindBy(xpath = "//button[text()='Login']")
    WebElement btnLoginForm;
    @FindBy(css = "button[name='registration']")
    WebElement btnRegistrationForm;


    public void typeLoginRegistrationForm(String email, String password){
        inputEmail.sendKeys(email);
        inputPassword.sendKeys(password);

    }

    public void typeLoginRegistrationFormWithUser(User user){
        inputEmail.sendKeys(user.getUsername());
        inputPassword.sendKeys(user.getPassword());
    }
    public void clickBtnLoginForm() {
        btnLoginForm.click();

    }
    public void clickBtnRegistrationForm(){
        btnRegistrationForm.click();

    }
}
