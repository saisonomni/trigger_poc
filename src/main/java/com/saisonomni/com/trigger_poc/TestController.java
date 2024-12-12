package com.saisonomni.com.trigger_poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired private TestServiceImpl testService;
    @PostMapping("/test")
    public String test(){
        return "OK";
    }
    @PostMapping("/register-trigger")
    public String registerTrigger() {
        try {
            testService.registerTrigger();
            return "Trigger registered successfully.";
        } catch (Exception e) {
            return "Failed to register trigger: " + e.getMessage();
        }
    }
}
