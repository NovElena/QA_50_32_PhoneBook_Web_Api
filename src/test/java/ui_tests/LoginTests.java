package ui_tests;

import dto.User;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
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
    @Owner("NovElena")
    @Description("Login with positive data from properties file")
    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void loginPositiveTest(Method method){
        // System.out.println("first test");
        //logger.info("start test " + method.getName());
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

    @Owner("NovElena")
    @Description("Login with positive User object data")
    @Test(groups = {"smoke", "user"})
    public void loginPositiveTestWithUser(){
        User user = new User(getProperty("base.properties", "login")
                ,getProperty("base.properties", "password"));
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnLoginForm();
        Assert.assertTrue(new ContactPage(getDriver()).isTextInBtnSignOutPresent("Sign Out"));
    }

    @Owner("NovElena")
    @Description("Login negative test with email without at symbol")
    @Test(groups = "negative")
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

    @Owner("NovElena")
    @Description("Login negative test with empty email")
    @Test(groups = "negative")
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

    @Owner("NovElena")
    @Description("Login negative test with short password")
    @Test(groups = "negative")
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

    @Owner("NovElena")
    @Description("Login negative test without uppercase letter in password")
    @Test(groups = "negative")
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
