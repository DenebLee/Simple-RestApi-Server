package kr.nanoit.handler.user;

import kr.nanoit.SandBoxHttpServer;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.db.impl.todoservice.TodoServiceTestImpl;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.db.impl.userservice.UserServiceTestImpl;
import kr.nanoit.exception.CreateFailedException;
import kr.nanoit.object.dto.UserDto;
import kr.nanoit.utils.Mapper;
import lombok.Getter;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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

@DisplayName("POST /user 테스트")
class PostUserTest {

    private SandBoxHttpServer httpServer;
    private UserService userService;
    private TodoService todoService;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        port = getRandomPort();
        userService = new UserServiceTestImpl();
        todoService = new TodoServiceTestImpl();
        httpServer = new SandBoxHttpServer("localhost", port, userService, todoService);
        httpServer.start();
    }

    @Test
        @DisplayName("POST / header = content type 을 요청했을때 올바르지 않으면 badRequest 가 떨어져야됨")
        void should_return_bad_request_when_uncorrected_content_type_header() throws IOException {
            // given
            String contentType = "xontent-type";
            String headerValue = "application/json";
            String url = "http://localhost:" + port + "/user";

            // when
            Response actual = post(url, contentType, headerValue);

            // then
            assertThat(actual.code).isEqualTo(400);
            assertThat(actual.header).isNotEqualTo(contentType);
            assertThat(actual.body).contains("not found: Content-Type Header");
        }

//        @Test
//        @DisplayName("POST / user-> header = content type 을 요청했을때 비어있으면 badRequest 가 떨어져야됨")
//        void should_return_bad_request_when_empty_content_type_header() throws IOException {
//            // given
//            String url = "http://localhost:" + port + "/user";
//
//            // when
//            Response actual = post(url, null, null);
//
//            // then
//            assertThat(actual.code).isEqualTo(400);
//            assertThat(actual.body).contains("invalid: Content-Type Header");
//        }

        @Test
        @DisplayName("POST / user-> header = content type 을 요청했을때  application/json 이 아닌경우 badRequest 가 떨어져야됨")
        void should_return_bad_request_when_not_application_json() throws IOException {
            // given
            String contentType = "content-type";
            String headerValue = "application/xml";
            String url = "http://localhost:" + port + "/user";

            // when
            Response actual = post(url, contentType, headerValue);

            // then
            assertThat(actual.code).isEqualTo(400);
            assertThat(actual.body).contains("accept Content-Type: application/json");
    }

    @Test
    @DisplayName("POST /  user-> UserDto 가 null 일 경우  badRequest 가 떨어져야됨")
    void should_return_bad_request_when_userdto_is_null() throws IOException {
        // given
        String json = "";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);


        // when
        Response actual = postJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("parse failed");
    }

    @Test
    @DisplayName("POST / user-> userName 이 null 일 경우  badRequest 가 떨어져야됨")
    void should_return_bad_request_when_username_is_null() throws IOException {
        // given
        String json = "{\"password\": \"123123\" , \"email\" : \"test@test.com\"}";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);


        // when
        Response actual = postJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: user.username");
    }

    @Test
    @DisplayName("POST / user-> userPassword 가 null 일 경우  badRequest 가 떨어져야됨")
    void should_return_bad_request_when_userpassword_is_null() throws IOException {
        // given
        String json = "{\"username\": \"test\", \"email\" : \"test@test.com\"}";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = postJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: user.password");
    }

    @Test
    @DisplayName("POST / user-> userEmail null 일 경우  badRequest 가 떨어져야됨")
    void should_return_bad_request_when_user_email_is_null() throws IOException {
        // given
        String json = "{\"username\": \"test\",\"password\": \"123123\" }";
        String url = "http://localhost:" + port + "/user";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = postJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: user.email");
    }


    @Test
    @DisplayName("POST / user-> 회원가입 요청했을때 정상이면 요청한 USER 정보가 내려와야 됨")
    void should_return_ok_when_user() throws IOException, CreateFailedException {
        // given
        UserDto expected = new UserDto(0, "lee", "123123", "test@test.com");
        String url = "http://localhost:" + port + "/user";

        // when
        Response actual = postJson(url, new StringEntity(Mapper.write(expected)));
        userService.save(expected.toEntity());

        // then
        assertThat(actual.code).isEqualTo(200);
        assertThat(Mapper.read(actual.body, UserDto.class))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    private Response post(String uri, String contentType, String value) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(uri);
            if (contentType != null) {
                httpPost.setHeader(contentType, value);
            }
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                return new Response(response.getCode(), Arrays.toString(response.getHeaders()), EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Response postJson(String uri, StringEntity stringEntity) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-type", "application/json");

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
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
            this.header = header;
            this.body = body;
        }
    }
}