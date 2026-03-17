package pages;

import dto.Contact;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.Keys;

import java.util.List;

public class ContactPage extends BasePage {
    public ContactPage(WebDriver driver) {
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
    List<WebElement> contactsList;
    @FindBy(xpath = "//div[@class='contact-item_card__2SOIM'][last()]")
    WebElement lastContact;
    @FindBy(xpath = "//div[@class='contact-item-detailed_card__50dTS']//h2")
    WebElement contactNameDetailed;
    @FindBy(xpath = "//div[@class='contact-item-detailed_card__50dTS']")
    WebElement itemDetailCard;
    @FindBy(xpath = "//button[text()='Remove']")
    WebElement btnRemove;
    @FindBy(xpath = "//div[@class='contact-page_leftdiv__yhyke']/div")
    WebElement divListContacts;
    @FindBy(xpath = "//button[text()='Edit']")
    WebElement btnEdit;
    @FindBy(xpath = "//div[@class='form_form__FOqHs']/input[1]")
    WebElement inputName;
    @FindBy(xpath = "//div[@class='form_form__FOqHs']/input[2]")
    WebElement inputLastName;
    @FindBy(xpath = "//div[@class='form_form__FOqHs']/input[3]")
    WebElement inputPhone;
    @FindBy(xpath = "//div[@class='form_form__FOqHs']/input[4]")
    WebElement inputEmail;
    @FindBy(xpath = "//div[@class='form_form__FOqHs']/input[5]")
    WebElement inputAddress;
    @FindBy(xpath = "//div[@class='form_form__FOqHs']/input[6]")
    WebElement inputDescription;
    @FindBy(xpath = "//button[text()='Save']")
    WebElement btnSave;

    public String getTextInContact(){
       return itemDetailCard.getText();
    }

    public boolean isContactPresent(Contact contact) {
        for (WebElement element : contactsList) {
            if (element.getText().contains(contact.getName())
                    && element.getText().contains(contact.getPhone())) {
                System.out.println(element.getText());
                return true;
            }
        }

        return false;

    }

//    @FindBy(xpath = "//div[@class='contact-page_leftdiv__yhyke']/div")
//    WebElement divListContacts;

    public void scrollToLastContact() {
        Actions actions = new Actions(driver);
        //actions.scrollToElement(lastContact).perform();
//        int deltaY = driver.findElement(By
//                        .xpath("//div[@class='contact-page_leftdiv__yhyke']/div"))
//                .getSize().getHeight();
        int deltaY = divListContacts.getSize().getHeight();
        System.out.println("Height -->" + deltaY);
        WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin
                .fromElement(contactsList.get(0));
        pause(3);
        actions.scrollFromOrigin(scrollOrigin, 0, 10000).perform();
    }

    public void clickLastContact() {
        lastContact.click();
    }

    public int getCountOfContacts() {
        return contactsList.size();
    }

    public boolean isTextInContactPageMessagePresent(String text) {
        return isTextInElementPresent(contactPageMessage, text);
    }

    public boolean isTextInBtnSignOutPresent(String text) {
        return isTextInElementPresent(btnSignOut, text);
    }

    public boolean isTextInBtnAddPresent(String text) {
        return isTextInElementPresent(btnAdd, text);
    }

    public String getContactNameFromDetailedCard(){
        return contactNameDetailed.getText();
    }

    public void deleteFirstContact() {
        contactsList.get(0).click();
        btnRemove.click();
    }

    public void typeEditForm(Contact contact) {
        contactsList.get(0).click();
        btnEdit.click();
        inputName.clear();
        inputName.sendKeys(contact.getName());
        inputLastName.clear();
        inputLastName.sendKeys(contact.getLastName());
        inputPhone.clear();
        inputPhone.sendKeys(contact.getPhone());
        inputEmail.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        inputEmail.sendKeys(contact.getEmail());
        inputAddress.clear();
        inputAddress.sendKeys(contact.getAddress());
        pause(3);
        btnSave.click();
    }
}