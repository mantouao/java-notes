package com.atguigu.cloud.feign;

import com.atguigu.cloud.fallback.NacosFeignFallback;
import com.atguigu.cloud.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "cloud-provider-payment-nacos", fallback =  NacosFeignFallback.class)
public interface NacosFeign {
    @GetMapping(value = "/pay/nacos/info")
    public ResultData<String> echo();
}
