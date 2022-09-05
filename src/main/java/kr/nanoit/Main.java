package kr.nanoit;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class Main {

  private static final String HOSTNAME = "localhost";
  private static final int PORT = 7000;

  public static void main(String[] args) throws IOException {
    log.info("[API SERVER START]");
    SandBoxHttpServer sandBoxHttpServer = new SandBoxHttpServer(HOSTNAME, PORT);
    sandBoxHttpServer.start();
  }
}
