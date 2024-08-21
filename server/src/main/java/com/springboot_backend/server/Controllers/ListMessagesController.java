package com.springboot_backend.server.Controllers;

import com.springboot_backend.server.Storage.StorageInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ListMessagesController {

    private StorageInterface storageHandler;

    public ListMessagesController(StorageInterface storageHandler) {
        this.storageHandler = storageHandler;
    }

    @CrossOrigin(origins = "https://peer-mediation.github.io")
    @GetMapping("/list-messages")
    public Object getCookies(@RequestParam(value = "uid", defaultValue = "") String uid, HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        if (!OriginVerifier.isAccessAllowed(request, responseMap)) {
            return responseMap;
        }

        try {
            if (uid.equals("")) {
                throw new Exception("uid must be specified");
            }
            System.out.println("listing words for user: " + uid);

            // get all the words for the user
            List<Map<String, Object>> vals = this.storageHandler.getCollection(uid, "words");

            // convert the key,value map to just a list of the words.
            List<String> words = vals.stream().map(word -> word.get("word").toString()).toList();

            responseMap.put("response_type", "success");
            responseMap.put("words", words);
        } catch (Exception e) {
            // error likely occurred in the storage handler
            e.printStackTrace();
            responseMap.put("response_type", "failure");
            responseMap.put("error", e.getMessage());
        }

        return Utils.toMoshiJson(responseMap);
    }
}
