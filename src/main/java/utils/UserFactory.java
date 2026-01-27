package utils;

import dto.User;
import net.datafaker.Faker;

public class UserFactory {
    static Faker faker = new Faker();
//    public static void main(String[] args) {
//        Faker faker = new Faker();
//        String name = faker.name().fullName();
//        String firstName = faker.name().firstName();
//        String lastName = faker.name().lastName();
//        String email = faker.internet().emailAddress();
//
//        System.out.println(name);
//        System.out.println(firstName);
//        System.out.println(lastName);
//        System.out.println(email);
//
//    }

    public static User positiveUser(){
        User user = new User(faker.internet().emailAddress(),
                "Qwerty145!");
        return user;
    }
}