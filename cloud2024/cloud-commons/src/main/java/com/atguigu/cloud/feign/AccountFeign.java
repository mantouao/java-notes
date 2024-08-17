package com.atguigu.cloud.feign;

import com.atguigu.cloud.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("seata-account")
public interface AccountFeign {
    @PostMapping("/account/decrease")
    ResultData decrease(@RequestParam("userId") Long userId,@RequestParam("money") Double money);
}
