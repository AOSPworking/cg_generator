package aosp.working.cggenerator.util;

import com.google.gson.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONFormatUtils {

    public static String formatJson(String jsonStr) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String result = "";
        JsonElement jElement = new JsonParser().parse(jsonStr);
        if (jElement instanceof JsonObject) {
            JsonObject jsonObject = jElement.getAsJsonObject();
            result = gson.toJson(jsonObject);
        } else if (jElement instanceof JsonArray) {
            JsonArray jsonArray = jElement.getAsJsonArray();
            result = gson.toJson(jsonArray);
        }
        return result;
    }

}
