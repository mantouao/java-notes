package com.atguigu.cloud.controller;


import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.serveic.AccountService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Resource
    private AccountService accountService;
    @PostMapping("/account/decrease")
    public ResultData desAccount(@RequestParam("userId") Long userId,@RequestParam("money") double money){
        accountService.decrease(userId, money);
        return ResultData.success("账户扣减成功");
    }
}
