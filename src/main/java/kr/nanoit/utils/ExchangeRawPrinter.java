package kr.nanoit.utils;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.sun.net.httpserver.HttpExchange;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Slf4j
public final class ExchangeRawPrinter {
    private ExchangeRawPrinter() {
    }

    public static String print(HttpExchange exchange) throws IOException {
        StringBuilder raw = new StringBuilder();
        raw.append("\r\n");
        raw.append("=============================\r\n");
        raw.append(exchange.getRequestMethod());
        raw.append(" ");
        raw.append(exchange.getRequestURI());
        raw.append("\r\n");

        for (Map.Entry<String, List<String>> entry : exchange.getRequestHeaders().entrySet()) {
            raw.append(entry.getKey());
            raw.append(": ");
            raw.append(String.join(", ", entry.getValue()));
            raw.append("\r\n");
        }
        String body = CharStreams.toString(new InputStreamReader(exchange.getRequestBody(), Charsets.UTF_8));
        raw.append("\r\n");
        raw.append(body);
        raw.append("\r\n");
        raw.append("=============================");
        log.debug("{}", raw);

        return body;
    }
}
