package com.springboot_backend.server.Controllers;

import com.springboot_backend.server.LoginInfo.LoginInfoRetriever;
import com.springboot_backend.server.Storage.StorageInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class VerifyAdminController {
    private StorageInterface storageHandler;

    public VerifyAdminController(StorageInterface storageHandler) {
        this.storageHandler = storageHandler;
    }

    @CrossOrigin(origins = "https://peer-mediation.github.io")
    @GetMapping("/verify-admin")
    public Object verifyAdmin(@RequestParam(value = "username") String username,
                              @RequestParam(value = "password") String password,
                              HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        if (!OriginVerifier.isAccessAllowed(request, responseMap)) {
            return responseMap;
        }

        try {
            if (username == null || password == null) {
                responseMap.put("failure", "must specify query parameters");
                return responseMap;
            }

            responseMap.put("isVerified", username.equals(LoginInfoRetriever.username)
                    && password.equals(LoginInfoRetriever.password));

        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("response_type", "failure");
            responseMap.put("error", e.getMessage());
        }

        return Utils.toMoshiJson(responseMap);
    }
}
