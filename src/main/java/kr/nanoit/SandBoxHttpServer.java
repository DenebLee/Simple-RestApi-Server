package kr.nanoit;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import kr.nanoit.handler.user.UserHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SandBoxHttpServer {

  private final HttpServer httpServer;

  public SandBoxHttpServer(String host, int port) throws IOException {
    this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);
    this.httpServer.createContext("/user", new UserHandler());


    // PATH HANDLE
//     http.handler(POST, "/user", new GetUserHandler());
//     http.handler(GET, "/user", new GetUserHandler());
//     http.handler(PATCH, "/user", new GetUserHandler());
//     http.handler(DELETE, "/user", new GetUserHandler());
  }

  public void start() {
    httpServer.start();
  }

  public void stop(int delay) {
    httpServer.stop(delay);
  }
}
