package com.atguigu.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigReadController {
    @GetMapping("/config/getConfigInfo")
    public String getConfigInfo(@Value("${binbin.info}") String configValue) {
        return "configValue: " + configValue + "\tport:3377";
    }
}
