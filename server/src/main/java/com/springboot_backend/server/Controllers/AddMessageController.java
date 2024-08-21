package com.springboot_backend.server.Controllers;

import com.springboot_backend.server.Storage.StorageInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AddMessageController {

    private StorageInterface storageHandler;

    public AddMessageController(StorageInterface storageHandler) {
        this.storageHandler = storageHandler;
    }

    @CrossOrigin(origins = "https://peer-mediation.github.io")
    @GetMapping("/add-message")
    public Object sendMessage(@RequestParam(value = "uid", defaultValue = "") String uid,
                          @RequestParam(value = "message", defaultValue = "") String message,
                          HttpServletRequest request) {

        Map<String, Object> responseMap = new HashMap<>();
        if (!OriginVerifier.isAccessAllowed(request, responseMap)) {
            return responseMap;
        }

        try {
            Map<String, Object> data = new HashMap<>();
            if (message.equals("")) {
                throw new Exception("must specify the messsage to send");
            }
            data.put("word", message);

            System.out.println("adding word: " + message + " for user: " + uid);

            // get the current word count to make a unique word_id by index.
            int wordCount = this.storageHandler.getCollection(uid, "words").size() + 1000000;
            String wordId = "word-" + wordCount;

            // use the storage handler to add the document to the database
            this.storageHandler.addDocument(uid, "words", wordId, data);

            responseMap.put("response_type", "success");
            responseMap.put("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("response_type", "failure");
            responseMap.put("error", e.getMessage());
        }

        return Utils.toMoshiJson(responseMap);
    }
}
