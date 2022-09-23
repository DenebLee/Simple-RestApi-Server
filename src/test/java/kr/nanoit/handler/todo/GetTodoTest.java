package kr.nanoit.handler.todo;

import kr.nanoit.SandBoxHttpServer;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.db.impl.todoservice.TodoServiceTestImpl;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.db.impl.userservice.UserServiceTestImpl;
import kr.nanoit.object.entity.TodoEntity;
import lombok.Getter;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;

class GetTodoTest {
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
    @DisplayName("GET / todo -> (쿼리 스트링이 NULL) 로 요청했을때 BAD REQUEST 내려와야 함")
    void should_return_bad_request_when_null_queryString() throws IOException {
        // given
        String url = "http://localhost:" + port + "/todo";

        // when
        Response actual = get(url);

        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("null: query id");
    }

    @Test
    @DisplayName("GET / todo -> (쿼리 스트링이 1개가 아닐때) 로 요청 했을때 BAD REQUEST 가 내려와야 됨")
    void should_return_bad_request_when_many_query_string() throws IOException {
        // given
        String url = "http://localhost:" + port + "/todo?id=1&id=4";

        // when
        Response actual = get(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("invalid: query.id");
    }

    @Test
    @DisplayName("GET / todo -> (쿼리 스트링 ID가 -1일때) 로 요청 했을때 BAD REQUEST 내려와야 됨")
    void should_return_bad_request_when_query_string_is_minus() throws IOException {
        // given
        String url = "http://localhost:" + port + "/todo?id=-1";

        // when
        Response actual = get(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("zero value: query.id");
    }

    @Test
    @DisplayName("GET / todo -> 요청했을때 OK, TODO 가 내려와야 함")
    void should_return_ok_when_todo() throws IOException {
        // given
        TodoEntity todo = todoService.save(new TodoEntity(0, "2022-02-04 05:05:05", "2022-02-04 05:05:05","today i forgot my umbrella","lee"));
        String url = "http://localhost:" + port + "/todo?id=1";

        // when
        Response actual = get(url);

        // then
        assertThat(actual.code).isEqualTo(200);
        assertThat(actual.body).contains("2022-02-04 05:05:05", "2022-02-04 05:05:05","today i forgot my umbrella","lee");
    }



    private Response get(String uri) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(uri);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                return new Response(response.getCode(), EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int getRandomPort() {
        return new SecureRandom().nextInt(65411) + 1024;
    }

    @Getter
    static class Response {
        private int code;
        private String body;

        public Response(int code, String body) {
            this.code = code;
            this.body = body;
        }
    }
}
