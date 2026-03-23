package api_tests;

import dto.User;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static utils.UserFactory.positiveUser;

public class RegistrationApiTests implements BaseApi {
    @Test
    public void registrationPositiveApiTest() {
        User user = positiveUser();
        System.out.println(user);
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(response.code());
        Assert.assertEquals(response.code(), 200);
    }

    @Test
    public void registrationNegative_Wrong_Password_ApiTest() {
        User user = positiveUser();
        user.setPassword("wrong password");
        System.out.println(user);
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(user), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
                .post(requestBody)
                .build();
        Response response;
        try {
            response = OK_HTTP_CLIENT.newCall(request)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(response.code());
        Assert.assertEquals(response.code(), 400);
    }

    @Test
    public void registrationNegative_Wrong_Format_Text_ApiTest() {
        User user = positiveUser();
        user.setPassword("wrong password");
        System.out.println(user);
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(user), TEXT);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
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

    @Test
    public void registrationNegative_Wrong_Key_User_ApiTest() {
        User user = positiveUser();
        Map<String, String> invalidJson = new HashMap<>();
        invalidJson.put("name", user.getUsername());
        invalidJson.put("password", user.getPassword());
        RequestBody requestBody = RequestBody
                .create(GSON.toJson(invalidJson), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTRATION_URL)
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
