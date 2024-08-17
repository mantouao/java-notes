package com.atguigu.cloud.controller;


import com.atguigu.cloud.resp.ResultData;

import com.atguigu.cloud.serveic.StorageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StorageController {
    @Resource
    private StorageService storageService;
    @PostMapping("/storage/decrease")
    public ResultData desStorage(@RequestParam("productId") Long productId,@RequestParam("count") Integer count){
        log.info(productId+"----"+count);
        storageService.decrease(productId, count);
        return ResultData.success("库存扣减成功");
    }
}
