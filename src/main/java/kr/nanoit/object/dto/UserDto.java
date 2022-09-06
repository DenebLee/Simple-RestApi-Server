package kr.nanoit.object.dto;

// 외부 입력, 출력
// HTTP INPUT 값으로 받을때 또는 RESPONSE로 줄때 DTO
public class UserDto {

    private long id;
    private String username;
    private String password;
    private String email;

    public UserDto() {
    }

    public UserDto(long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
