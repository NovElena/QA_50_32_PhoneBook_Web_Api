package ui_tests;

import dto.User;
import manager.AppManager;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.Login_page;

public class LoginTests extends AppManager {
    @Test
    public void loginPositiveTest(){
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        Login_page loginPage = new Login_page(getDriver());
        loginPage.typeLoginRegistrationForm("family@mail.ru", "Family123!" );
        loginPage.clickBtnLoginForm();

    }
    @Test
    public void loginPositiveTestWithUser(){
        User user = new User("family@mail.ru","Family123!" );
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
        Login_page loginPage = new Login_page(getDriver());
        loginPage.typeLoginRegistrationFormWithUser(user);
        loginPage.clickBtnLoginForm();
    }


}
