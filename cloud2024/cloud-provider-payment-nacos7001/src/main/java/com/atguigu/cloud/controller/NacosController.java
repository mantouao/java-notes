package com.atguigu.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NacosController {

    @Value("${server.port}")
    private String port;
    @GetMapping(value = "/pay/nacos/info")
    @SentinelResource(value = "nacos-info", blockHandler = "blockHandler")
    public ResultData<String> echo() {
        return ResultData.success("Hello Nacos Discovery" + port);
    }

    public ResultData<String> blockHandler(BlockException e) {
        return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "被限流了");
    }

}
