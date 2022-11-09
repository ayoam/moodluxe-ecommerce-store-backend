package com.ayoam.customerservice.utils;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtDataUtil {

    private String getDataFromJwt(HttpServletRequest request){
        final String authorizationHeader = request.getHeader("Authorization");

        String userID = null;
        String token = null;

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("jwt invalid!");
        }

        token= authorizationHeader.substring(7);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));


        return payload;
    }

    public String extractUserID(HttpServletRequest request){
        String payload = getDataFromJwt(request);
        JSONObject jsonData = new JSONObject(payload);
        return jsonData.get("sub")+"";
    }

    public List<String> extractUserRoles(HttpServletRequest request){
        String payload = getDataFromJwt(request);
        JSONObject jsonData = new JSONObject(payload);
        return jsonData.getJSONObject("realm_access").getJSONArray("roles").toList().stream().map(obj->obj+"").collect(Collectors.toList());
    }
}
