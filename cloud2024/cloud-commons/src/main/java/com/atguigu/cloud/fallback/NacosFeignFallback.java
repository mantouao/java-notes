package com.atguigu.cloud.fallback;

import com.atguigu.cloud.feign.NacosFeign;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import org.springframework.stereotype.Component;

@Component
public class NacosFeignFallback  implements NacosFeign {
    @Override
    public ResultData<String> echo() {
        return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "fallback服务降级");
    }
}
