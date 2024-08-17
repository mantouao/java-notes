package com.atguigu.cloud.controller;

import com.atguigu.cloud.feign.FeignAPI;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderGateWayController {
    @Resource
    private FeignAPI feignAPI;
    @GetMapping("/feign/pay/gateway/get/{id}")
    public ResultData getById(@PathVariable("id") Integer id) {
        return feignAPI.getPay(id);
    }
    @GetMapping("/feign/pay/gateway/info")
    public ResultData getGateWayInfo() {
        return feignAPI.getInfo();
    }
}
