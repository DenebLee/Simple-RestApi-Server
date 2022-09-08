package kr.nanoit;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

import kr.nanoit.db.UserService;
import kr.nanoit.handler.HealthHandler;
import kr.nanoit.handler.user.UserHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SandBoxHttpServer {

  private final HttpServer httpServer;

  public SandBoxHttpServer(String host, int port, UserService userService) throws IOException {
    this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);
    this.httpServer.createContext("/user", new UserHandler(userService));
    this.httpServer.createContext("/health", new HealthHandler());
  }

  public void start() {
    httpServer.start();
  }

  public void stop(int delay) {
    httpServer.stop(delay);
  }
}
