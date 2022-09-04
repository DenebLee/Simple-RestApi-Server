import kr.nanoit.SandBoxHttpServer;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectHttpServerTest {

    private BufferedReader bufferedReader;
    private String serverResponseData;
    private String queryString;


    @Test
    public void HttpServerTest() throws IOException {

        //given
        String host = "localhost";
        int port = 8080;

        //when
        SandBoxHttpServer sandBoxHttpServer = new SandBoxHttpServer(host, port);


        //then

        sandBoxHttpServer.start();

    }

@Test
    public void ConnectHttpServer() throws IOException {
    HttpURLConnection conn;
        //given
    final String connect = "http://localhost:8090/user";
    URL url = new URL(connect);


        //when
    conn = (HttpURLConnection) url.openConnection();
    conn.getResponseCode();
    conn.setRequestMethod("POST");
    conn.setDoOutput(true);
    conn.connect();


        //then
    // OutputStream outputStream = conn.getOutputStream();
    PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
    String test = "안녕 서버야";

    printWriter.println(test);
    printWriter.flush();
    System.out.println("서버에 전달완료 ");
    }

}
