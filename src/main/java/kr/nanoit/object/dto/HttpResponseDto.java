package kr.nanoit.object.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponseDto {
    private String timestamp;
    private int code;
    private String error;
    private String message;
}
