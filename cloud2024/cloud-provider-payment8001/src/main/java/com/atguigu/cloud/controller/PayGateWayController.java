package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import com.atguigu.cloud.service.PayService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PayGateWayController {
    @Resource
    private PayService payService;
    @GetMapping("/pay/gateway/get/{id}")
    public ResultData<Pay> getPay(@PathVariable("id") Integer id) {
        Pay pay = null;
        try {
            pay = payService.getPayById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "查询失败");
        }
        return ResultData.success(pay);
    }
    @GetMapping("/pay/gateway/info")
    public ResultData<String> getInfo() {
        return ResultData.success("gateway info : " + UUID.randomUUID());
    }

}
