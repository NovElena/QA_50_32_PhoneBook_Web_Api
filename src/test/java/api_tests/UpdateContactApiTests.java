package api_tests;

import dto.Contact;
import dto.ResponseMessageDto;
import dto.TokenDto;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseApi;
import utils.ILogin;

import java.io.IOException;

import static utils.ContactFactory.*;

public class UpdateContactApiTests implements BaseApi, ILogin {
    TokenDto token;
    String idContact;
    SoftAssert softAssert = new SoftAssert();

    @BeforeClass
    public void login() {
        token = login_get_token();
    }

    @BeforeMethod
    public void createContact() {
        Contact contact = positiveContact();
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + ADD_NEW_CONTACT_URL)
                .addHeader(AUTH, token.getToken())
                .post(requestBody)
                .build();
        try (Response response = OK_HTTP_CLIENT
                .newCall(request).execute()) {
            if (response.code() == 200) {
                ResponseMessageDto responseMessageDto =
                        GSON.fromJson(response.body().string(),
                                ResponseMessageDto.class);
                //System.out.println(responseMessageDto.getMessage());
                idContact = responseMessageDto
                        .getMessage().split("ID: ")[1];
                // System.out.println(idContact);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Created exception");
        }
    }

    @Test
    public void updateContactPositiveApiTest() {
        Contact contact = positiveContact();
        contact.setId(idContact);
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(contact), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + EDIT_CONTACT_URL)
                .addHeader(AUTH, token.getToken())
                .put(requestBody)
                .build();
        try (Response response = OK_HTTP_CLIENT
                .newCall(request).execute()) {
            softAssert.assertEquals(response.code(),
                    200, "validate status code");
            ResponseMessageDto responseMessageDto =
                    GSON.fromJson(response.body().string(),
                            ResponseMessageDto.class);
            //System.out.println(responseMessageDto);
            softAssert.assertTrue(responseMessageDto.getMessage()
                    .contains("Contact was updated"), "validate message");
            softAssert.assertAll();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Created exception");
        }
    }

}
