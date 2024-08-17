package com.atguigu.cloud.controller;

import com.atguigu.cloud.feign.NacosFeign;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {
    @Resource
    private RestTemplate restTemplate;
    @Value("${service-url.nacos-user-service}")
    private String URL;

    @Resource
    private NacosFeign nacosFeign;
    @GetMapping("/consumer/pay/nacos/info")
    public ResultData<String> getInfo() {
//        return restTemplate.getForObject(URL + "/pay/nacos/info", ResultData.class);
        return nacosFeign.echo();
    }
}
