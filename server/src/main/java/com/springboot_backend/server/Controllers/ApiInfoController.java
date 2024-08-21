package com.springboot_backend.server.Controllers;

import com.springboot_backend.server.Storage.StorageInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiInfoController {

    private StorageInterface storageHandler;

    public ApiInfoController(StorageInterface storageHandler) {
        this.storageHandler = storageHandler;
    }

    @CrossOrigin(origins = "https://peer-mediation.github.io")
    @GetMapping("/get-api-info")
    public Object getApiInfo(@RequestParam(value = "uid", defaultValue = "") String uid,
                         HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        if (!OriginVerifier.isAccessAllowed(request, responseMap)) {
            return responseMap;
        }

        try {
            System.out.println(uid);

            List<Map<String, Object>> apiInfo = this.storageHandler.getCollection(uid, "apiInfo");

            for (Map<String, Object> map : apiInfo) {
                for (String key : map.keySet()) {
                    responseMap.put(key, map.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("response_type", "failure");
            responseMap.put("error", e.getMessage());
        }

        return Utils.toMoshiJson(responseMap);
    }
}
