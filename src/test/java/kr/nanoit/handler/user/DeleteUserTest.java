package kr.nanoit.handler.user;

import kr.nanoit.SandBoxHttpServer;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.db.impl.userservice.UserServiceTestImpl;
import kr.nanoit.object.entity.UserEntity;
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

@DisplayName("DELETE /user 테스트")
@Slf4j
class DeleteUserTest {

    private SandBoxHttpServer httpServer;
    private UserService userService;
    private int port;

    // 테스트 메소드 한개당 새롭게 실행됨
    @BeforeEach
    void setUp() throws IOException {
        port = getRandomPort();
        userService = new UserServiceTestImpl();
        httpServer = new SandBoxHttpServer("localhost", port, userService);
        httpServer.start();
    }


    @Test
    @DisplayName("Delete /user (쿼리 스트링이 NULL) 로 요청했을때 BAD REQUEST 가 내려와야 됨")
    void should_return_bad_request_when_null_query_string() throws IOException {
        // given
        String url = "http://localhost:" + port + "/user";

        // when
        Response actual = delete(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: query.id");
    }

    @Test
    @DisplayName("Delete /user (쿼리 스트링이 1개가 아닐때) 로 요청했을때 BAD REQUEST 가 내려와야 됨")
    void should_return_bad_request_when_many_query_string() throws IOException {
        // given
        String url = "http://localhost:" + port + "/user?id=1&id=4";

        // when
        Response actual = delete(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("invalid: query.id");
    }

    @Test
    @DisplayName("Delete /user (쿼리 스트링 ID가 -1일때) 로 요청했을때 BAD REQUEST 가 내려와야 됨")
    void should_return_bad_request_when_query_string_is_minus() throws IOException {
        // given
        String url = "http://localhost:" + port + "/user?id=-1";


        // when
        Response actual = delete(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("zero value: query.id");
    }

    @Test
    @DisplayName("Delete /user?id=1 (유저가 NOT FOUND) 로 요청했을때 BAD REQUEST 가 내려와야 됨")
    void should_return_bad_request_when_user_not_found() throws IOException {
        // given
        String url = "http://localhost:" + port + "/user?id=1";

        // when
        Response actual = delete(url);

        // then
        assertThat(actual.code).isEqualTo(400);
        assertThat(actual.body).contains("not found: user.id");
    }

    @Test
    @DisplayName("Delete /user?id=1 로 요청했을때 OK, 요청한 아이디의 유저가 삭제되어야 함")
    void should_return_ok_when_user_delete() throws IOException {
        // given
        UserEntity userData = new UserEntity(0, "test01", "123123", "test01@test.com");
        UserEntity expected = userService.save(userData);
        String url = "http://localhost:" + port + "/user?id=1";

        // when
        Response responseActual = delete(url);
        boolean actual = userService.deleteById(expected.getId());
        // then

        assertThat(actual).isTrue(); 
        assertThat(userService.findById(expected.getId())).isNull();
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