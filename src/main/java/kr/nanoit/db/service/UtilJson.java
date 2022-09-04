package kr.nanoit.db.service;

import org.json.JSONException;
import org.json.JSONObject;

public final class UtilJson {

  private UtilJson() {
  }

  public static JSONObject createJsonData(String id, Integer pw, String email)
      throws JSONException {
    JSONObject userInfo = new JSONObject();

    userInfo.put("id", id);
    userInfo.put("password", pw);
    userInfo.put("email", email);

    return userInfo;
  }
}
