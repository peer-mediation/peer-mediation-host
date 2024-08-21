package com.springboot_backend.server.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    @CrossOrigin(origins = "*")
    public String setInitialMessage() {
        return "Peer mediation backend";
    }
}
