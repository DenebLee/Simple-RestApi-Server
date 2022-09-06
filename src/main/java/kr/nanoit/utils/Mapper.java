package kr.nanoit.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Mapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private Mapper() {
    }

    public static <T> String write(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> String writePretty(T object) throws JsonProcessingException {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static <T> T read(String raw, Class<T> classObject) throws JsonProcessingException {
        return objectMapper.readValue(raw, classObject);
    }
}
