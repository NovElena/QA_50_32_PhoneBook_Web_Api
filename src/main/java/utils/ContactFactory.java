package utils;

import dto.Contact;
import net.datafaker.Faker;

public class ContactFactory {
    static Faker faker = new Faker();

    public static Contact positiveContact(){
        return Contact.builder()
                .name(faker.name().firstName())
                .lastName(faker.name().lastName())
                .phone(faker.number().digits(13))
                .email(faker.internet().emailAddress())
                .address(faker.address().fullAddress())
                .description("My family")
                .build();
    }
}
