package kr.nanoit.extension;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;

public class RelatedBody {



    public static String parseBody(BufferedReader bufferedReader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            builder.append(inputLine);
        }
        return builder.toString();
    }

}
