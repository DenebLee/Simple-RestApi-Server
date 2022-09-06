package kr.nanoit.extension;

import kr.nanoit.db.service.UtilJson;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;

public class RelatedBody {


    public static String makeBody() throws JSONException {
        return UtilJson.createJsonData("test", "123123", "test@test.com").toString();
    }

    public static String parseBody(BufferedReader bufferedReader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            builder.append(inputLine);
        }
        return builder.toString();
    }

}
