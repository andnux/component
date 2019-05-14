package top.andnux.libbase.network.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class SupportUtil {

    public static String jointParams(String url,
                                     Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    public static String getContentString(InputStream result) throws Exception {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(result));
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
