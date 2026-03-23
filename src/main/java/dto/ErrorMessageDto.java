package dto;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class ErrorMessageDto {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
