package kr.nanoit;

import kr.nanoit.db.impl.userservice.UserService;
import kr.nanoit.object.config.DataBaseConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 7000;

    public static void main(String[] args) throws IOException {
        UserService userService = UserService.createTest();
        DataBaseConfig dataBaseConfig = new DataBaseConfig();

        log.info("[API SERVER START]");

        SandBoxHttpServer sandBoxHttpServer = new SandBoxHttpServer(HOSTNAME, PORT, userService);
        sandBoxHttpServer.start();
    }

}
