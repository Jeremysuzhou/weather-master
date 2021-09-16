package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Set;

public class JSONUtils {
    public static String getCityOrProvinceData(String json, String param) {

        JSONObject obj = JSON.parseObject(json);
        Set<String> keys = obj.keySet();
        for (String key : keys) {
            String name = (String) obj.get(key);
            if (param.equals(name)) {
                return key;
            }

        }
        return null;
    }

    public static boolean validateJson(String json) {
        boolean result = false;
        try {
            Object obj = JSON.parse(json);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static float getTemp(String weatherResult) {
        JSONObject obj = JSON.parseObject(weatherResult);
        obj = obj.getJSONObject("weatherinfo");

        return obj.getFloat("temp");
    }
}
