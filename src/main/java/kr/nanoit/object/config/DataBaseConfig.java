package kr.nanoit.object.config;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DataBaseConfig {
    private String ip;
    private int port;
    private String dataBaseName;
    private String username;
    private String password;
}
