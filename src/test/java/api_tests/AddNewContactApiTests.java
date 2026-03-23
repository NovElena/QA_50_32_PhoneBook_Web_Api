package api_tests;

import dto.Contact;
import dto.ResponseMessageDto;
import dto.TokenDto;
import dto.User;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;

import java.io.IOException;

import static utils.UserFactory.positiveUser;
import static utils.PropertiesReader.*;
import static utils.ContactFactory.*;

public class AddNewContactApiTests implements BaseApi {
    TokenDto token;
    SoftAssert softAssert = new SoftAssert();

    @BeforeClass
    public void login() {
        User user = new User(getProperty("base.properties",
                "login"), getProperty("base.properties",
                "password"));
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + LOGIN_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (response.code() == 200) {
            try {
                token = GSON.fromJson(response.body()
                        .string(), TokenDto.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //System.out.println(token.toString());
    }

    @Test
    public void addNewContactPositiveApiTest() {
        Contact contact = positiveContact();
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + ADD_NEW_CONTACT_URL)
                .addHeader(AUTH, token.getToken())
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 200);
    }

    @Test
    public void addNewContactPositiveApiTest2() {
        Contact contact = positiveContact();
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + ADD_NEW_CONTACT_URL)
                .addHeader(AUTH, token.getToken())
                .post(requestBody)
                .build();
        try (Response response = OK_HTTP_CLIENT.newCall(request)
                .execute()) {
            softAssert.assertEquals(response.code(), 200,
                    "validate status code");
            ResponseMessageDto responseMessageDto = GSON.fromJson
                    (response.body().string(),
                            ResponseMessageDto.class);
            System.out.println(responseMessageDto);
            softAssert.assertTrue(responseMessageDto
                            .getMessage().contains("Contact was added!")
                    ,"validate message");
            softAssert.assertAll();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("created exception");
        }
    }

    @Test
    public void addNewContactNegative_WO_Token_ApiTest() {
        Contact contact = positiveContact();
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + ADD_NEW_CONTACT_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 403);
    }

    @Test
    public void addNewContactNegative_Wrong_Token_ApiTest() {
        Contact contact = positiveContact();
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + ADD_NEW_CONTACT_URL)
                .addHeader(AUTH, "token.getToken()")
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 401);
    }

    @Test
    public void addNewContactNegative_Wrong_MediaType_ApiTest() {
        Contact contact = positiveContact();
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(contact), TEXT);
        Request request = new Request.Builder()
                .url(BASE_URL + ADD_NEW_CONTACT_URL)
                .addHeader(AUTH, token.getToken())
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.code(), 500);
    }
}
