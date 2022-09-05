package kr.nanoit.handler.user;

import kr.nanoit.SandBoxHttpServer;
import lombok.Getter;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.impl.bootstrap.HttpServer;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.*;

@DisplayName("GET HANDLER 테스트")
class PostUserTest {

    private HttpServer httpServer;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        port = getRandomPort();
        SandBoxHttpServer expected = new SandBoxHttpServer("localhost", port);
        expected.start();
    }

    @Test
    @DisplayName("User id = 1일때 테스트")
    void should_get_when_id_1() throws IOException {
        // given
        int expected_id = 1;

        // when
        Response actual = get("http://localhost:" + port + "/get?idd=" + expected_id);

        // then
        assertThat(actual.code).isEqualTo(200);
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