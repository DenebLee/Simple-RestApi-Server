package kr.nanoit;

import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.db.impl.userservice.UserService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 7000;

    public static void main(String[] args) throws IOException {
        UserService userService = UserService.createTest();
        TodoService todoService = TodoService.createTest();

        log.info("[API SERVER START]");

        SandBoxHttpServer userHttpServer = new SandBoxHttpServer(HOSTNAME, PORT, userService, todoService);

        userHttpServer.start();
    }

}
