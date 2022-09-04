package kr.nanoit;

import java.io.IOException;
import kr.nanoit.db.service.UtilJson;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j

public class Main {
  private static final String HOSTNAME = "localhost";
  private static final int PORT = 7000;

  public static void main(String[] args) throws IOException {
    SandBoxHttpServer sandBoxHttpServer = new SandBoxHttpServer(HOSTNAME, PORT);
    sandBoxHttpServer.start();

//    UtilJson.createJsonData("asdf", 1234, "asdf");
//    UtilJson utilJson = new UtilJson();
  }
}
