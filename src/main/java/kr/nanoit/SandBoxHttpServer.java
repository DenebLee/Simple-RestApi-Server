package kr.nanoit;

import com.sun.net.httpserver.HttpServer;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.handler.HealthHandler;
import kr.nanoit.handler.todo.TodoHandler;
import kr.nanoit.handler.user.UserHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class SandBoxHttpServer {

  private final HttpServer httpServer;

  public SandBoxHttpServer(String host, int port, UserService userService) throws IOException {
    this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);
    this.httpServer.createContext("/user", new UserHandler(userService));
    this.httpServer.createContext("/health", new HealthHandler());
    this.httpServer.createContext("/todo", new TodoHandler());
  }

  public void start() {
    httpServer.start();
  }

  public void stop(int delay) {
    httpServer.stop(delay);
  }
}
