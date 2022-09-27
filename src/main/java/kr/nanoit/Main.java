package kr.nanoit;

import kr.nanoit.db.impl.PostgreSqlDbcp;
import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.object.config.DataBaseConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 7000;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DataBaseConfig config = new DataBaseConfig()
                .setIp("192.168.0.64")
                .setPort(5432)
                .setDataBaseName("distributor")
                .setUsername("distributor")
                .setPassword("distributor");
        PostgreSqlDbcp dbcp = new PostgreSqlDbcp(config);
//        UserService userService = UserService.createTest();
        UserService userService = UserService.createPostgreSQL(dbcp);
//        TodoService todoService = TodoService.createTest();
        TodoService todoService = TodoService.createPostgreSQL(dbcp);

        log.info("[API SERVER START]");

        SandBoxHttpServer userHttpServer = new SandBoxHttpServer(HOSTNAME, PORT, userService, todoService);

        userHttpServer.start();
    }

}
