package com.nexusprocure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class VersionController {
    @GetMapping("/version")
    public Map<String, String> version(){
        return Map.of("application", "NexusProcure", "version", "v4");

    }
}
