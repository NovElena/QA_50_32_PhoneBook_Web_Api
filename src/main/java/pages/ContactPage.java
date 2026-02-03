package pages;

import dto.Contact;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.util.List;

public class ContactPage extends BasePage{
    public ContactPage(WebDriver driver){
        setDriver(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,
                10), this);
    }

    @FindBy(xpath = "//button[text()='Sign Out']")
    WebElement btnSignOut;
    @FindBy(xpath = "//*[text()='ADD']")
    WebElement btnAdd;
    @FindBy(xpath = "//h1[text()=' No Contacts here!']")
    WebElement contactPageMessage;
    @FindBy(className = "contact-item_card__2SOIM")
    List<WebElement>contactsList;
    @FindBy(xpath = "//div[@class='contact-item_card__2SOIM'][last()]")
    WebElement lastContact;

    public boolean isContactPresent(Contact contact){
        for(WebElement element: contactsList) {
            if (element.getText().contains(contact.getName())
                    && element.getText().contains(contact.getPhone())){
                System.out.println(element.getText());
                return true;
            }
        }
        return false;
    }

    public void clickLastContact(){
        lastContact.click();
    }

    public int getCountOfContacts(){
        return contactsList.size();
    }

    public boolean isTextInContactPageMessagePresent(String text){
        return isTextInElementPresent(contactPageMessage, text);
    }

    public boolean isTextInBtnSignOutPresent(String text){
        return isTextInElementPresent(btnSignOut, text);
    }

    public boolean isTextInBtnAddPresent(String text){
        return isTextInElementPresent(btnAdd, text);
    }
}
