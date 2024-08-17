package com.atguigu.cloud.serveic;


public interface AccountService {
    /**
     * 扣减库存
     */
    void decrease(Long userId, double money);
}
