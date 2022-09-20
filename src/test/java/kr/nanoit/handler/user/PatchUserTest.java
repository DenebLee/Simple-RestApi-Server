package kr.nanoit.handler.user;

import kr.nanoit.SandBoxHttpServer;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.db.impl.userservice.UserServiceTestImpl;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.object.entity.UserEntity;
import kr.nanoit.utils.Mapper;
import lombok.Getter;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("PATCH /user 테스트")
class PatchUserTest {
    private SandBoxHttpServer httpServer;
    private UserService userService;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        port = getRandomPort();
        userService = new UserServiceTestImpl();
        httpServer = new SandBoxHttpServer("localhost", port, userService);
        httpServer.start();
    }

    @Test
    @DisplayName("PATCH / header = content type 을 요청했을때 올바르지 않으면 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_uncorrected_content_type_header() throws IOException {
        // given
        String contentType = "xontent-type";
        String headerValue = "application/json";
        String url = "http://localhost:" + port + "/user";

        // when
        Response actual = patch(url, contentType, headerValue);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.header).isNotEqualTo(contentType);
        assertThat(actual.body).contains("not found: Content-Type Header");
    }

    @Test
    @DisplayName("PATCH / header = content type 을 요청했을때 비어있으면 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_empty_content_type_header() throws IOException {
        // given
        String contentType = "xontent-type";
        String headerValue = "application/json";
        String url = "http://localhost:" + port + "/user";

        // when
        Response actual = patch(url, null, null);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.header).contains("application/json");
        assertThat(actual.body).contains("not found: Content-Type Header");
    }

    @Test
    @DisplayName("PATCH / header = content type 을 요청했을때  application/json 이 아닌경우 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_not_application_json() throws IOException {
        // given
        String contentType = "content-type";
        String headerValue = "application/xml";
        String url = "http://localhost:" + port + "/user";

        // when
        Response actual = patch(url, contentType, headerValue);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("accept Content-Type: application/json");
    }

    @Test
    @DisplayName("PATCH / UserDto 가 null 일 경우 badRequest 떨어져야됨")
    void should_return_bad_request_when_userdto_is_null() throws IOException {
        // given
        String json = "";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("parse failed");
    }

    @Test
    @DisplayName("PATCH / id 값이 null 일 경우 badRequest 가 떨어져야됨 ")
    void should_return_bad_request_when_id_is_null() throws IOException {
        // given
        String json = "{\"username\": \"lee\" ,\"password\": \"123123\" , \"email\" : \"test@test.com\"}";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: user.id");
    }

    @Test
    @DisplayName("PATCH / username 값이 null 일 경우 badRequest 가 떨어져야됨 ")
    void should_return_bad_request_when_username_is_null() throws IOException {
        // given
        String json = "{\"id\": 2, \"password\": \"123123\" , \"email\" : \"test@test.com\"}";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: user.username");
    }

    @Test
    @DisplayName("PATCH / password 값이 null 일 경우 badRequest 가 떨어져야됨 ")
    void should_return_bad_request_when_password_is_null() throws IOException {
        // given
        String json = "{\"id\": 2, \"username\": \"lee\" ,\"email\" : \"test@test.com\"}";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: user.password");
    }

    @Test
    @DisplayName("PATCH / email 값이 null 일 경우 badRequest 가 떨어져야됨 ")
    void should_return_bad_request_when_email_is_null() throws IOException {
        // given
        String json = "{\"id\": 2, \"username\": \"lee\" ,\"password\": \"123123\"}";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: user.email");
    }

    @Test
    @DisplayName("[PATCH /user] 유저 정보 수정 요청을 했을때 정상이면 요청했던 USER 정보가 내려와야 됨")
    void should_return_ok_when_user_patch() throws IOException {
        // given
        UserEntity originalUserData = userService.save(new UserEntity(0, "test01", "123123", "test01@test.com"));
        UserDto expected = new UserDto(1, "leejeongseob", "123123", "test@test.com");
        String url = "http://localhost:" + port + "/user";

        // when
        Response actual = patchJson(url, new StringEntity((Mapper.write(expected))));
        userService.update(expected.toEntity());

        // then
        assertThat(actual.code).isEqualTo(200);
        assertThat(Mapper.read(actual.body, UserDto.class))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }


    private Response patch(String uri, String contentType, String value) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPatch httpPatch = new HttpPatch(uri);
            if (contentType != null) {
                httpPatch.setHeader(contentType, value);
            }
            try (CloseableHttpResponse response = httpclient.execute(httpPatch)) {
                return new Response(response.getCode(), Arrays.toString(response.getHeaders()), EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Response patchJson(String uri, StringEntity stringEntity) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPatch httpPatch = new HttpPatch(uri);
            httpPatch.setEntity(stringEntity);
            httpPatch.setHeader("Content-type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
                return new Response(response.getCode(), Arrays.toString(response.getHeaders()), EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int getRandomPort() {
        return new SecureRandom().nextInt(64511) + 1024;
    }

    @Getter
    static class Response {
        private int code;
        private String header;
        private String body;

        public Response(int code, String header, String body) {
            this.code = code;
            this.body = body;
            this.header = header;
        }
    }
}