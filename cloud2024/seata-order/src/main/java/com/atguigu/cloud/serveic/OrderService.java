package com.atguigu.cloud.serveic;

import com.atguigu.cloud.pojo.Order;
import org.springframework.stereotype.Service;

public interface OrderService {
    void create(Order order);
}
