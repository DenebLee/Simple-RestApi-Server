package kr.nanoit.extension;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class CustomObjectMapper {

    private final ObjectMapper objectMapper;

    private CustomObjectMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static CustomObjectMapper getInstance() {
        return Internal.SINGLETON;
    }

    private static class Internal {
        private static final CustomObjectMapper SINGLETON = new CustomObjectMapper();
    }
}
