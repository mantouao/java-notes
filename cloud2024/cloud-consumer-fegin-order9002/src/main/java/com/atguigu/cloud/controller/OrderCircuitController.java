package com.atguigu.cloud.controller;

import com.atguigu.cloud.feign.FeignAPI;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class OrderCircuitController {
    @Resource
    private FeignAPI feignAPI;
    @GetMapping("/feign/circuit/{id}")
    @CircuitBreaker(name = "cloud-provider-payment", fallbackMethod = "myCircuitFallback")
    public ResultData circuitBreakerTest(@PathVariable("id") Integer id) {
        return feignAPI.circuitBreakerTest(id);
    }
    public ResultData myCircuitFallback(Integer id, Throwable throwable) {
        // 降级逻辑
        return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "服务异常，请稍后再试！");
    }
    @GetMapping("/feign/bulkhead/{id}")
    @Bulkhead(name = "cloud-provider-payment", fallbackMethod = "myBulkheadFallback", type = Bulkhead.Type.SEMAPHORE)
    public ResultData bulkheadTest(@PathVariable("id") Integer id) {
        return feignAPI.bulkheadTest(id);
    }
    public ResultData myBulkheadFallback(Integer id, Throwable throwable) {
        // 降级逻辑
        return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "服务异常，请稍后再试！");
    }

    @GetMapping("/feign/bulkheadTHREADPOOL/{id}")
    @Bulkhead(name = "cloud-provider-payment", fallbackMethod = "myBulkheadFallback", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<ResultData> bulkheadTHREADPOOLTest(@PathVariable("id") Integer id) {
        System.out.println("---------------->");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("<----------------");
        return CompletableFuture.supplyAsync(() -> feignAPI.bulkheadTest(id));
    }
    public CompletableFuture<ResultData> myBulkheadTHREADPOOLFallback(Integer id, Throwable throwable) {
        // 降级逻辑
        return CompletableFuture.supplyAsync(() -> ResultData.error(ReturnCodeEnum.FAIL.getCode(), "服务异常，请稍后再试！"));
    }

    @GetMapping("/feign/ratelimit/{id}")
    @RateLimiter(name = "cloud-provider-payment", fallbackMethod = "myRateLimitFallback")
    public ResultData rateLimitTest(@PathVariable("id") Integer id) {
        return feignAPI.rateLimitTest(id);
    }
    public ResultData myRateLimitFallback(Integer id, Throwable throwable) {
        // 降级逻辑
        return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "被限流，请稍后再试！");
    }
}
