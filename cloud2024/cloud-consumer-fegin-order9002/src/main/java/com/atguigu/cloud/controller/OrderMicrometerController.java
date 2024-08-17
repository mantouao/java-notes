package com.atguigu.cloud.controller;

import com.atguigu.cloud.feign.FeignAPI;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderMicrometerController {
    @Resource
    private FeignAPI feignAPI;
    @GetMapping("/feign/micrometer/{id}")
    public String feignMicrometer(@PathVariable("id") Integer id) {
        return feignAPI.myMicrometerTest(id);
    }
}
