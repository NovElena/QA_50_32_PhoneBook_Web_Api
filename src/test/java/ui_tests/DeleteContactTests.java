package ui_tests;

import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.ContactPage;
import pages.HomePage;
import pages.LoginPage;

import static utils.PropertiesReader.*;

import static utils.HeaderMenuItem.*;

public class DeleteContactTests extends AppManager {
    HomePage homePage;
    LoginPage loginPage;
    ContactPage contactPage;

    int countOfContacts;

    @BeforeMethod(alwaysRun = true)
    public void login() {
        homePage = new HomePage(getDriver());
        loginPage = BasePage.clickButtonHeader(LOGIN);
        loginPage.typeLoginRegistrationForm
                (getProperty("base.properties", "login"),
                        getProperty("base.properties", "password"));
        loginPage.clickBtnLoginForm();
        contactPage = new ContactPage(getDriver());
        countOfContacts = contactPage.getCountOfContacts();
    }

    @Test(groups = {"smoke", "contact"})
    public void deleteFirstContactPositiveTest() {
        contactPage.deleteFirstContact();
        contactPage.pause(3);
        int countOfContactsAfterDelete =
                contactPage.getCountOfContacts();
        Assert.assertEquals(countOfContactsAfterDelete,
                countOfContacts -1);
    }
}
