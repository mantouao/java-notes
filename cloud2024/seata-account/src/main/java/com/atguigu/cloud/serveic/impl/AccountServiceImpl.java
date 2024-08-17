package com.atguigu.cloud.serveic.impl;

import com.atguigu.cloud.feign.AccountFeign;
import com.atguigu.cloud.feign.StorageFeign;
import com.atguigu.cloud.mapper.AccountMapper;
import com.atguigu.cloud.serveic.AccountService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountMapper accountMapper;


    @Override
    public void decrease(Long userId, double money) {
        log.info("------->account-service中扣减账户开始");
        accountMapper.decrease(userId,money);
//        int i = 10 / 0;
        runtime();
        log.info("------->account-service中扣减账户结束");
    }
    private void runtime(){
        try {
            Thread.sleep(61000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
