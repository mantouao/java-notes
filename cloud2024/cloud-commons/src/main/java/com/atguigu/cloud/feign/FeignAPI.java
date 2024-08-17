package com.atguigu.cloud.feign;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(value = "cloud-provider-payment")
@FeignClient(value = "cloud-gateway")
public interface FeignAPI {
    @PostMapping("/pay/add")
    public ResultData addOrder(@RequestBody PayDTO payDTO);
    @GetMapping("/pay/delete/{id}")
    public ResultData deleteOrder(@PathVariable("id") Long id);
    @GetMapping("/pay/update")
    public ResultData updateOrder(@RequestBody PayDTO payDTO);
    @GetMapping("/pay/get/{id}")
    public ResultData getOrder(@PathVariable("id") Long id);
    @GetMapping("/pay/getall")
    public ResultData listOrder();
    @GetMapping("/getConsulInfo")
    public ResultData getConsulInfo();
    @GetMapping("/pay/circuit/{id}")
    public ResultData circuitBreakerTest(@PathVariable("id") Integer id);

    @GetMapping("/pay/bulkhead/{id}")
    public ResultData bulkheadTest(@PathVariable("id") Integer id);
    @GetMapping("/pay/retelimit/{id}")
    public ResultData rateLimitTest(@PathVariable("id") Integer id);
    @GetMapping("/pay/micrometer/{id}")
    public String myMicrometerTest(@PathVariable("id") Integer id);
    @GetMapping("/pay/gateway/get/{id}")
    public ResultData getPay(@PathVariable("id") Integer id);
    @GetMapping("/pay/gateway/info")
    public ResultData<String> getInfo();
}
