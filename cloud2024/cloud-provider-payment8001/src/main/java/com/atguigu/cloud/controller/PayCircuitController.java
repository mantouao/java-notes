package com.atguigu.cloud.controller;

import com.atguigu.cloud.resp.ResultData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class PayCircuitController {
    @GetMapping("/pay/circuit/{id}")
    public ResultData circuitBreakerTest(@PathVariable("id") Integer id) {
        if (id == -1) {
            throw new RuntimeException("id 不能为负数");
        }
        if (id == 9) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return ResultData.success("CircuitBreaker is close" + "\t" + id + "\t" + UUID.randomUUID());
    }

    @GetMapping("/pay/bulkhead/{id}")
    public ResultData bulkheadTest(@PathVariable("id") Integer id) {
        if (id == -1) {
            throw new RuntimeException("id 不能为负数");
        }
        if (id == 9) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return ResultData.success("hello! bulkhead" + "\t" + id + "\t" + UUID.randomUUID());
    }
    @GetMapping("/pay/retelimit/{id}")
    public ResultData rateLimitTest(@PathVariable("id") Integer id) {
        return ResultData.success("hello! retry" + "\t" + id + "\t" + UUID.randomUUID());
    }
}
