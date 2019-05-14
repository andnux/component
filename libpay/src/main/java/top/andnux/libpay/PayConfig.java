package top.andnux.libpay;

import java.util.HashMap;
import java.util.Map;

public class PayConfig {

    private static Map<String, Map<String, String>> sMap = new HashMap<>();

    public static void setWxConfig(String appId, String secretKey) {
        Map<String, String> map = new HashMap<>();
        map.put("id", appId);
        map.put("kay", secretKey);
        sMap.put("wx", map);
    }

    public static Map<String, String> getWxConfig() {
        return sMap.get("wx");
    }
}
