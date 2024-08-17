package com.atguigu.cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PayMicrometerController {
    @GetMapping("/pay/micrometer/{id}")
    public String myMicrometerTest(@PathVariable("id") Integer id) {
        return "hello! micrometer" + id + "\t" + UUID.randomUUID();
    }
}
