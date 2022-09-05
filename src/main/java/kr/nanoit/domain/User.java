package kr.nanoit.domain;


import lombok.Builder;
import lombok.Value;

@Value
@Builder


public class User {

    public String id;
    public String password;
    public String email;
}
