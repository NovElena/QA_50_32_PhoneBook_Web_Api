package api_tests;

import dto.ContactsDto;
import dto.ErrorMessageDto;
import dto.TokenDto;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;
import utils.ILogin;

import java.io.IOException;

public class GetAllContactsApiTests implements
        BaseApi, ILogin {
    TokenDto token;
    SoftAssert softAssert = new SoftAssert();

    @BeforeClass
    public void login(){
        token = login_get_token();
    }

    @Test
    public void getAllUserContactsPositiveApiTest(){
        System.out.println(token);
        Request request = new Request.Builder()
                .url(BASE_URL+GET_ALL_CONTACTS_URL)
                .addHeader(AUTH, token.getToken())
                .get()
                .build();
        try(Response response = OK_HTTP_CLIENT
                .newCall(request).execute()) {
            ContactsDto contactsDto = GSON.fromJson(response.body().string(), ContactsDto.class);
            System.out.println(contactsDto.toString());
            Assert.assertEquals(response.code(), 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllUserContactsNegativeWrongTokenApiTest(){
        Request request = new Request.Builder()
                .url(BASE_URL+GET_ALL_CONTACTS_URL)
                .addHeader(AUTH, "token.getToken()")
                .get()
                .build();
        try(Response response = OK_HTTP_CLIENT
                .newCall(request).execute()) {
            softAssert.assertEquals(response.code(), 401,
                    "validate status code");
            ErrorMessageDto errorMessageDto = GSON.fromJson(response.body()
                    .string(), ErrorMessageDto.class);
            System.out.println(errorMessageDto);
            softAssert.assertEquals(errorMessageDto.getError(),
                    "Unauthorized", "validate error name");
            softAssert.assertTrue(errorMessageDto.getMessage().contains
                            ("strings must contain exactly 2 period characters."),
                    "validate message");
            softAssert.assertAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
