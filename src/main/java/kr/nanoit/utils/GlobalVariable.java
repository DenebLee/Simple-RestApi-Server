package kr.nanoit.utils;

public final class GlobalVariable {

    private GlobalVariable() {
    }

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final int HTTP_OK = 200;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_POST = "POST";
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";
}

