package kr.nanoit.handler.todo;

import kr.nanoit.SandBoxHttpServer;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.db.impl.todoservice.TodoServiceTestImpl;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.db.impl.userservice.UserServiceTestImpl;
import kr.nanoit.object.dto.TodoDto;
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
import java.sql.SQLException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("POST / todo 테스트")
 class PostTodoTest {

    private SandBoxHttpServer httpServer;
    private UserService userService;
    private TodoService todoService;
    private int port;

    @BeforeEach
    void setup() throws IOException {
        port = getRandomPort();
        userService = new UserServiceTestImpl();
        todoService = new TodoServiceTestImpl();
        httpServer = new SandBoxHttpServer("localhost", port , userService, todoService);
        httpServer.start();
    }

    @Test
    @DisplayName("POST / todo ->  header = content type 을 요청했을때 올바르지 않으면 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_uncorrected_content_type_header() throws IOException {
        // given
        String contentType = "xontent-type";
        String headerValue = "application/json";
        String url = "http://localhost:" + port + "/todo";

        // when
        Response actual = post(url, contentType, headerValue);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.header).isNotEqualTo(contentType);
        assertThat(actual.body).contains("not found: Content-Type Header");
    }

//    @Test
//    @DisplayName("POST / todo -> header = content type 을 요청했을때 비어있으면 badRequest 가 떨어져야됨")
//    void should_return_bad_request_when_empty_content_type_header() throws IOException {
//        // given
//        String url = "http://localhost:" + port + "/todo";
//
//        // when
//        Response actual = post(url, null, null);
//
//        // then
//        assertThat(actual.code).isEqualTo(400);
//        assertThat(actual.header).contains("application/json");
//        assertThat(actual.body).contains("invalid: Content-Type Header");
//    }

    @Test
    @DisplayName("POST / todo -> header = content type 을 요청했을때  application/json 이 아닌경우 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_not_application_json() throws IOException {
        // given
        String contentType = "content-type";
        String headerValue = "application/xml";
        String url = "http://localhost:" + port + "/todo";

        // when
        Response actual = post(url, contentType, headerValue);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("accept Content-Type application/json");
    }

    @Test
    @DisplayName("POST / todo -> TodoDto 가 null 일 경우  badRequest 가 떨어져야됨")
    void should_return_bad_request_when_tododto_is_null() throws IOException {
        // given
        String json = "";
        String url = "http://localhost:" + port + "/todo";
        StringEntity stringEntity = new StringEntity(json);


        // when
        Response actual = postJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("parse failed");
    }

    @Test
    @DisplayName("POST / todo -> createdAt 이 null 일 경우  badRequest 가 떨어져야됨")
    void should_return_bad_request_when_createAt_is_null() throws IOException {
        // given
        String json = "{\"modifiedAt\": \"2022-03-03 12:12:12\" , \"content\" : \"hello\", \"writer\" : \"lee\"}";
        String url = "http://localhost:" + port + "/todo";
        StringEntity stringEntity = new StringEntity(json);


        // when
        Response actual = postJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not Found : Created Time");
    }

    @Test
    @DisplayName("POST / todo -> content 가 null 일 경우  badRequest 가 떨어져야됨")
    void should_return_bad_request_when_content_is_null() throws IOException {
        // given
        String json = "{\"createdAt\": \"2022-02-02 05:55:22\",\"modifiedAt\" : \"2022-12-12 12:12:12\" , \"writer\" : \"lee\"}";
        String url = "http://localhost:" + port + "/todo";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = postJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not Found : Content");
    }




    @Test
    @DisplayName("POST / todo -> Todo를 등록했을 때  정상이면 요청한 Todo 정보가 내려와야 됨")
    void should_return_ok_when_user() throws IOException, SQLException {
        // given
        TodoDto expected = new TodoDto(0, "2022-09-02 05:05:22", null,"hi nice to meet you", "lee");
        String url = "http://localhost:" + port + "/todo";

        // when
        Response actual = postJson(url, new StringEntity(Mapper.write(expected)));
        todoService.save(expected.toEntity());

        // then
        assertThat(actual.code).isEqualTo(200);
        assertThat(Mapper.read(actual.body, TodoDto.class))
                .usingRecursiveComparison()
                .ignoringFields("todoId")
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
