package ui_tests;

import dto.User;
import manager.AppManager;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContactPage;
import pages.HomePage;
import pages.LoginPage;
import utils.RetryAnalyzer;
import utils.TestNGListener;

import java.lang.reflect.Method;

import static utils.PropertiesReader.*;
@Listeners(TestNGListener.class)

public class LoginTests extends AppManager {
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void loginPositiveTest(Method method){
        // System.out.println("first test");
        logger.info("start test " + method.getName());
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        LoginPage loginPage = new LoginPage(getDriver());
        //loginPage.typeLoginRegistrationForm("123qwe@gmail.com","123Qwerty!");
        loginPage.typeLoginRegistrationForm(getProperty("base.properties", "login")
                ,getProperty("base.properties", "password"));
        loginPage.clickBtnLoginForm();
        Assert.assertTrue(new ContactPage(getDriver())
                .isTextInBtnAddPresent("ADD"));
    }

    @Test
    public void loginPositiveTestWithUser(){
        User user = new User(getProperty("base.properties", "password")
                ,getProperty("base.properties", "login"));
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnLoginForm();
        Assert.assertTrue(new ContactPage(getDriver()).isTextInBtnSignOutPresent("Sign Out"));
    }

    @Test
    public void loginNegativeTest_WrongEmail_WOSpecSymbol(){
        User user = new User("123qwegmail.com", "123Qwerty!");
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnLoginForm();
        Assert.assertEquals(loginPage.closeAlertReturnText(),
                "Wrong email or password");
    }

    @Test
    public void loginNegativeTest_WrongEmail_Empty(){
        User user = new User("", "123Qwerty!");
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnLoginForm();
        Assert.assertEquals(loginPage.closeAlertReturnText(),
                "Wrong email or password");
    }

    @Test
    public void loginNegativeTest_WrongPassword_Short(){
        User user = new User("123qwe@gmail.com", "Wp!123");
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnLoginForm();
        Assert.assertEquals(loginPage.closeAlertReturnText(),
                "Wrong email or password");
    }

    @Test
    public void loginNegativeTest_WrongPassword_WOUpperCase(){
        User user = new User("123qwe@gmail.com", "123qwerty!");
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnLoginForm();
        Assert.assertEquals(loginPage.closeAlertReturnText(),
                "Wrong email or password");
    }

}