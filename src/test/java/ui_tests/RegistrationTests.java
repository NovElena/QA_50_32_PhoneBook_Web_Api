package ui_tests;

import dto.User;
import manager.AppManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ContactPage;
import pages.HomePage;
import pages.LoginPage;

import java.util.Random;
import static utils.UserFactory.*;

public class RegistrationTests extends AppManager {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationTests.class);
    LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void goToRegistrationPage(){
        new HomePage(getDriver()).clickBtnLogin();
        loginPage = new LoginPage(getDriver());
    }

    @Test
    public void registrationPositiveTest(){
        int i = new Random().nextInt(1000);
        User user = new User("muyitr"+i+"@gmail.com"
                , "Password124!");
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnRegistrationForm();
        Assert.assertTrue(new ContactPage(getDriver())
                .isTextInContactPageMessagePresent("No Contacts here!"));
    }

    @Test(groups = {"smoke", "user"})
    public void registrationPositiveTest_WithFaker(){
        User user = positiveUser();
        System.out.println(user);
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnRegistrationForm();
        Assert.assertTrue(new ContactPage(getDriver())
                .isTextInContactPageMessagePresent("No Contacts here!"));
    }

    @Test(groups = "negative")
    public void registrationNegativeTest_WithFaker_EmptyPassword() {
        User user = positiveUser();
        user.setPassword("");
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnRegistrationForm();
        Assert.assertTrue(loginPage.closeAlertReturnText().contains("Wrong email or password format"));
    }

    @Test(groups = "negative")
    public void registrationNegativeTest_WrongEmail_WOAtSymbol(){
        User user = new User("Emailgmail.com", "Password124!");
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnRegistrationForm();
        Assert.assertEquals(loginPage.closeAlertReturnText(),"Wrong email or password format");

    }

    @Test
    public void registrationNegativeTest_WrongEmail_Empty(){
        User user = new User("", "Password124!");
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnRegistrationForm();
        Assert.assertEquals(loginPage.closeAlertReturnText(),"Wrong email or password format");

    }

    @Test
    public void registrationNegativeTest_WrongPassword_WOUpperCase(){
        User user = new User("Email1@gmail.com", "password124!");
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnRegistrationForm();
        Assert.assertEquals(loginPage.closeAlertReturnText(),"Wrong email or password format");

    }

    @Test
    public void registrationNegativeTest_WrongPassword_WOSpecSymbol(){
        User user = new User("Email1@gmail.com", "Password124");
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnRegistrationForm();
        Assert.assertEquals(loginPage.closeAlertReturnText(),"Wrong email or password format");

    }

}