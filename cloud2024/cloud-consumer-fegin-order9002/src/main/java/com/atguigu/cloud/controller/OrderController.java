package com.atguigu.cloud.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.feign.FeignAPI;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class OrderController {
    @Resource
    private FeignAPI feignAPI;

    @PostMapping ("/feign/pay/add")
    public ResultData addOrder(@RequestBody PayDTO payDTO){
        return feignAPI.addOrder(payDTO);
    }
    @GetMapping("/feign/pay/delete/{id}")
    public ResultData deleteOrder(@PathVariable("id") Long id){
        return feignAPI.deleteOrder(id);
    }
    @GetMapping("/feign/pay/update")
    public ResultData updateOrder(PayDTO payDTO){
        return feignAPI.updateOrder(payDTO);
    }
    @GetMapping("/feign/pay/get/{id}")
    public ResultData getOrder(@PathVariable("id") Long id){
        return feignAPI.getOrder(id);
    }
    @GetMapping("/feign/pay/getall")
    public ResultData listOrder(){
        ResultData resultData = null;
        try {
            System.out.println("开始调用feign" + DateUtil.now());
            resultData = feignAPI.listOrder();
        } catch (Exception e) {
            System.out.println("结束调用feign" + DateUtil.now());
            e.printStackTrace();
        }
        return resultData;
    }
    @GetMapping("/feign/getConsulInfo")
    public ResultData getConsulInfo(){
        return feignAPI.getConsulInfo();
    }
    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/discovery")
    public ResultData discovery(){
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            System.out.println(service);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-provider-payment");
        for (ServiceInstance instance : instances) {
            System.out.println(instance.getInstanceId() + "\t" + instance.getHost() + "\t" +  instance.getPort() + "\t" + instance.getUri());
        }
        return ResultData.success(instances);
    }

}
