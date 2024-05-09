package com.react2code.springbootlibrary.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJwt {


    public static String extractJwtPayload(String token, String extraction) {

        String[] chunks = token.replace("Bearer ", "")
                .split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));

        String[] entries = payload.split(",");
        Map<String, String> payloadMap = new HashMap<String, String>();
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            if (keyValue[0].equals(extraction)) {
                int remove = 1;
                if (keyValue[1].endsWith("}")) {
                    remove = 2;
                }
                keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
                keyValue[1] = keyValue[1].substring(1);
                payloadMap.put(keyValue[0], keyValue[1]);
            }

            if(payloadMap.containsKey(extraction)) {
                return payloadMap.get(extraction);
            }

        }
        return null;
    }
    }


