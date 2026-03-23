package utils;


import dto.TokenDto;
import dto.User;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import static utils.PropertiesReader.getProperty;

public interface ILogin extends BaseApi{
    default TokenDto login_get_token(){
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
        TokenDto token;
        try {
            token = GSON.fromJson(response.body()
                    .string(), TokenDto.class);
            return token;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
