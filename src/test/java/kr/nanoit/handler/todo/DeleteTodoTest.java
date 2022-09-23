package kr.nanoit.handler.todo;

import kr.nanoit.SandBoxHttpServer;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.db.impl.todoservice.TodoServiceTestImpl;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.object.entity.TodoEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
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


@DisplayName("DELETE /todo 테스트")
@Slf4j
public class DeleteTodoTest {
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
    @DisplayName("DELETE / todo-> (쿼리스트링이 NULL) 로 요청 했을때 BAD REQUEST가 내려와야 함")
    void should_return_bad_request_when_null_query_string() throws IOException {
        // given
        String url = "http://localhost:" + port + "/todo";

        // when
        Response actual = delete(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: query.id");
    }

    @Test
    @DisplayName("DELETE / todo-> (쿼리 스트링이 1개가 아닐때) 로 요청했을때 BAD REQUEST 가 내려와야 됨")
    void should_return_bad_request_when_many_query_string() throws IOException {
        // given
        String url = "http://localhost:" + port + "/todo?id=1&id=4";

        // when
        Response actual = delete(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("invalid: query.id");
    }

    @Test
    @DisplayName("DELETE / todo-> (쿼리 스트링 ID가 -1일때) 로 요청했을때 BAD REQUEST가 내려와야 함")
    void should_return_bad_request_when_query_string_is_minus() throws IOException{
        // given
        String url = "http://localhost:" + port + "/todo?id=-1";

        // when
        Response actual = delete(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("zero value: query.id");
    }

    @Test
    @DisplayName("DELETE / todo-> (유저가 NOT FOUND) 로 요청했을때 BAD REQUEST 가 내려와야 됨")
    void should_return_bad_request_when_todo_not_found() throws IOException {
        // given
        String url = "http://localhost:" + port + "/todo?id=12";

        // when
        Response actual = delete(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: todo.id");
    }

    @Test
    @DisplayName("DELETE / todo-> 요청했을때 OK, 요청한 todo 가 삭제되어야 함")
    void should_return_ok_when_todo_delete() throws IOException {
        // given
        TodoEntity todoData = new TodoEntity(0, "2022-12-12 12:12:12", "2022-12-12 12:12:12", "안녕하세요","lee");
        TodoEntity expected = todoService.save(todoData);
        String url = "http://localhost:" + port + "/todo?id=1";

        // when
        Response responseActual = delete(url);
        boolean actual = todoService.deleteById(expected.getTodoId());
        // then

        assertThat(actual).isTrue();
        assertThat(todoService.findById(expected.getTodoId())).isNull();
        assertThat(responseActual.code).isEqualTo(200);
        assertThat(responseActual.body).contains("OK");
    }


    private Response delete(String uri) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpDelete httpDelete = new HttpDelete(uri);
            try (CloseableHttpResponse response = httpclient.execute(httpDelete)) {
                return new Response(response.getCode(), EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
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
        private String body;

        public Response(int code, String body) {
            this.code = code;
            this.body = body;
        }
    }
}
