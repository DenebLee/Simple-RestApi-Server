package kr.nanoit.handler.todo;

import kr.nanoit.SandBoxHttpServer;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.db.impl.todoservice.TodoServiceTestImpl;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.object.dto.TodoDto;
import kr.nanoit.object.entity.TodoEntity;
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


@DisplayName("PATCH / todo 테스트")
class PatchTodoTest {
    private SandBoxHttpServer httpServer;
    private UserService userService;
    private TodoService todoService;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        port = getRandomPort();
        todoService = new TodoServiceTestImpl();
        httpServer = new SandBoxHttpServer("localhost", port, userService, todoService);
        httpServer.start();
    }


    @Test
    @DisplayName("PATCH / todo-> content Type 을 요청했을 때 올바르지 않으면 badRequest 가 떨어져야 함")
    void should_return_bad_request_when_uncorrected_content_type_header() throws IOException {
        // given
        String contentType = "xontent-type";
        String headerValue = "application/json";
        String url = "http://localhost:" + port + "/todo";

        // when
        Response actual = patch(url, contentType, headerValue);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.header).isNotEqualTo(contentType);
        assertThat(actual.body).contains("not found: Content-Type Header");
    }

//    @Test
//    @DisplayName("PATCH / todo-> content type 을 요청했을때 비어있으면 badRequest 가 떨어져야 함")
//    void should_return_bad_request_when_empty_content_type_header() throws IOException {
//        // given
//        String url = "http://localhost:" + port + "/todo";
//
//        // when
//        Response actual = patch(url, null, null);
//
//        // then
//        assertThat(actual.code).isEqualTo(400);
//        assertThat(actual.body).contains("invalid: Content-Type Header");
//    }

    @Test
    @DisplayName("PATCH / todo-> header = content type 을 요청했을때  application/json 이 아닌경우 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_not_application_json() throws IOException {
        // given
        String contentType = "content-type";
        String headerValue = "application/xml";
        String url = "http://localhost:" + port + "/todo";

        // when
        Response actual = patch(url, contentType, headerValue);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("accept Content-Type: application/json");
    }

    @Test
    @DisplayName("PATCH / todo-> TodoDto 가 null 일 경우 badRequest 떨어져야됨")
    void should_return_bad_request_when_tododto_is_null() throws IOException {
        // given
        String json = "";
        String url = "http://localhost:" + port + "/todo";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("parse failed");
    }

    @Test
    @DisplayName("PATCH / todo-> createdAt 이  null 일 경우 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_createdAt_is_null() throws IOException {
        // given
        String json = "{\"modifiedAt\": \"2022-02-02 12:12:12\" ,\"content\": \"안녕하세요 수정 전입니다\" , \"writer\" : \"Leejeongseob\"}";
        String url = "http://localhost:" + port + "/todo";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: createdAt");
    }

    @Test
    @DisplayName("PATCH / todo-> content 이  null 일 경우 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_content_is_null() throws IOException {
        // given
        String json = "{\"createdAt\": \"2022-02-02 12:12:12\" ,\"modifiedAt\": \"2022-02-02 12:13:13\" , \"writer\" : \"Leejeongseob\"}";
        String url = "http://localhost:" + port + "/todo";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: content");
    }

    @Test
    @DisplayName("PATCH / todo-> modifiedAt 이  null 일 경우 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_modifiedAt_is_null() throws IOException {
        // given
        String json = "{\"createdAt\": \"2022-02-02 12:12:12\" ,\"content\": \"안녕하세요 수정 전입니다\" , \"writer\" : \"Leejeongseob\"}";
        String url = "http://localhost:" + port + "/todo";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: modified");
    }

    @Test
    @DisplayName("PATCH / todo-> writer 이  null 일 경우 badRequest 가 떨어져야됨")
    void should_return_bad_request_when_writer_is_null() throws IOException {
        // given
        String json = "{\"createdAt\": \"2022-02-02 12:12:12\" ,\"modifiedAt\": \"2022-03-03 12:12:13\" , \"content\" : \"안녕하세요 수정 전입니다\"}";
        String url = "http://localhost:" + port + "/todo";
        StringEntity stringEntity = new StringEntity(json);

        // when
        Response actual = patchJson(url, stringEntity);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: writer");
    }

    @Test
    @DisplayName("PATCH / todo-> todo 정보 수정 요청을 했을때 정상이면 요청했던 TODO 정보가 내려와야 함")
    void should_return_ok_when_todo_patch() throws IOException{
        // given
        TodoEntity originalTodoData = todoService.save(new TodoEntity(0,"2022-03-03 12:12:12", "2022-04-04 12:12:12", "before modified", "lee"));
        TodoDto expected = new TodoDto(1, "2022-03-03 12:12:12", "2022-04-04 12:12:12", "after modified ", "lee");
        String url = "http://localhost:" + port + "/todo";

        // when
        Response actual = patchJson(url, new StringEntity(Mapper.write(expected)));
        todoService.update(expected.toEntity());

        // then
        assertThat(actual.code).isEqualTo(200);
        assertThat(Mapper.read(actual.body, TodoDto.class))
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
            this.header = header;
            this.body = body;
        }
    }
}
