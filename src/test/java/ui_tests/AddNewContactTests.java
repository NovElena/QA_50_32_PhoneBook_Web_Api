package ui_tests;

import dto.Contact;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.*;
import utils.HeaderMenuItem;
import static pages.BasePage.clickButtonHeader;
import static utils.ContactFactory.*;

public class AddNewContactTests extends AppManager {
    HomePage homePage;
    LoginPage loginPage;
    ContactPage contactPage;
    AddPage addPage;
    int countOfContacts;

    @BeforeMethod
    public void login(){
        homePage = new HomePage(getDriver());
        loginPage = BasePage.clickButtonHeader(HeaderMenuItem.LOGIN);
        loginPage.typeLoginRegistrationForm("123qwe@gmail.com","123Qwerty!");
        loginPage.clickBtnLoginForm();
        contactPage = new ContactPage(getDriver());
        countOfContacts = contactPage.getCountOfContacts();
        addPage = clickButtonHeader(HeaderMenuItem.ADD);
    }

    @Test
    public void addNewContactPositiveTest(){
        addPage.typeContactForm(positiveContact());
        int countOfContactsAfterAdd = contactPage.getCountOfContacts();
        Assert.assertEquals(countOfContactsAfterAdd, countOfContacts+1);

    }
    @Test
    public void addNewContactPositiveTest_ClickLastContact(){
        Contact contact = positiveContact();
        addPage.typeContactForm(contact);
        //contactPage.clickLastContact();
        Assert.assertTrue(contactPage.isContactPresent(contact));
    }
}
