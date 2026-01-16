package ui_tests;

import manager.AppManager;
import org.testng.annotations.Test;
import pages.HomePage;

public class LoginTests extends AppManager {
    @Test
    public void loginPositiveTest(){
        System.out.println("first test");
        HomePage homePage = new HomePage(getDriver());
        homePage.clickBtnLogin();
    }
}
