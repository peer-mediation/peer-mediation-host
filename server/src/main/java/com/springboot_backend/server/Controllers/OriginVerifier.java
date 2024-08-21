package com.springboot_backend.server.Controllers;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class OriginVerifier {

    public static boolean isAccessAllowed(HttpServletRequest request, Map<String, Object> responseMap) {
        String origin = request.getHeader("Origin");
        if (origin == null || !origin.equals("https://peer-mediation.github.io")) {
            responseMap.put("access denied", origin + " is an invalid origin");
            return false;
        }
        return true;
    }
}
