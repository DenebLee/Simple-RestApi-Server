package kr.nanoit.extension;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Variable {

  private Variable() {
  }

  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final Charset CHARSET = StandardCharsets.UTF_8;
  public static final int STATUS_OK = 200;
  public static final String METHOD_GET = "GET";
  public static final String METHOD_PATCH = "PATCH";
  public static final String METHOD_DELETE = "DELETE";
  public static final String METHOD_POST = "POST";
  public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=UTF-8";
}
