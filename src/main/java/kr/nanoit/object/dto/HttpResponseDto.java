package kr.nanoit.object.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HttpResponseDto {
    private String timestamp;
    private int code;
    private String error;
    private String message;
}
