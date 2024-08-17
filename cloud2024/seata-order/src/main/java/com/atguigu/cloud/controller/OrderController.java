package com.atguigu.cloud.controller;

import com.atguigu.cloud.pojo.Order;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.serveic.OrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Resource
    private OrderService orderService;
    @GetMapping("/order/create")
    public ResultData createOrder(Order order){
        orderService.create(order);
        return ResultData.success(order);
    }
}
