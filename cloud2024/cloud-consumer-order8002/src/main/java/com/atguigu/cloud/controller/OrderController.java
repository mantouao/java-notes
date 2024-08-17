package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class OrderController {
//    public static final String url = "http://localhost:8001";
    public static final String url = "http://cloud-provider-payment";
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/pay/add")
    public ResultData addOrder(@RequestBody PayDTO payDTO){
        return restTemplate.postForObject(url + "/pay/add", payDTO, ResultData.class);
    }
    @GetMapping("/consumer/pay/delete/{id}")
    public ResultData deleteOrder(@PathVariable("id") Long id){
        return restTemplate.getForObject(url + "/pay/delete/" + id, ResultData.class);
    }
    @GetMapping("/consumer/pay/update")
    public ResultData updateOrder(PayDTO payDTO){
        return restTemplate.postForObject(url + "/pay/update", payDTO, ResultData.class);
    }
    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getOrder(@PathVariable("id") Long id){
        return restTemplate.getForObject(url + "/pay/get/" + id, ResultData.class);
    }
    @GetMapping("/consumer/pay/getall")
    public ResultData listOrder(){
        return restTemplate.getForObject(url + "/pay/getall", ResultData.class);
    }
    @GetMapping("/consumer/getConsulInfo")
    public ResultData getConsulInfo(){
        return restTemplate.getForObject(url + "/getConsulInfo", ResultData.class);
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
