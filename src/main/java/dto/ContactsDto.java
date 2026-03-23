package dto;

import lombok.*;

import java.util.List;
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ContactsDto {
    private List<Contact> contacts;
}
