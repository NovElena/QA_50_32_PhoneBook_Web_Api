package ui_tests;

import dto.Contact;
import manager.AppManager;
import org.checkerframework.checker.units.qual.A;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import utils.ContactFactory;

import static utils.HeaderMenuItem.LOGIN;
import static utils.PropertiesReader.getProperty;

public class EditContactTests extends AppManager {
    SoftAssert softAssert = new SoftAssert();
    HomePage homePage;
    LoginPage loginPage;
    ContactPage contactPage;

    @BeforeMethod(alwaysRun = true)
    public void login() {
        homePage = new HomePage(getDriver());
        loginPage = BasePage.clickButtonHeader(LOGIN);
        loginPage.typeLoginRegistrationForm
                (getProperty("base.properties", "login"),
                        getProperty("base.properties", "password"));
        loginPage.clickBtnLoginForm();
        contactPage = new ContactPage(getDriver());
    }

    @Test(groups = {"smoke", "contact"})
    public void editFirstContactPositiveTest() {
        Contact contact = ContactFactory
                .positiveContact();
        contactPage.typeEditForm(contact);
        contactPage.pause(3);
        Assert.assertTrue(contactPage
                .isContactPresent(contact));
    }

    @Test(groups = "negative")
    public void editFirstContactPositiveTest_WithCardOfContact() {
        Contact contact = ContactFactory
                .positiveContact();
        contactPage.typeEditForm(contact);
        contactPage.pause(3);
        String text = contactPage.getTextInContact();
        softAssert.assertTrue(text.contains(contact.getName()),
                "validate Name in DetailCard");
        softAssert.assertTrue(text.contains(contact.getEmail()),
                "validate Email in DetailCard");
        softAssert.assertTrue(text.contains(contact.getPhone()),
                "validate Phone in DetailCard");
        softAssert.assertAll();
    }
}
