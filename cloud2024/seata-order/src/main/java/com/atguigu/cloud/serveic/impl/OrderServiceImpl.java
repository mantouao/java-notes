package com.atguigu.cloud.serveic.impl;

import com.atguigu.cloud.feign.AccountFeign;
import com.atguigu.cloud.feign.StorageFeign;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.pojo.Order;
import com.atguigu.cloud.serveic.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private StorageFeign storageFeign;
    @Resource
    private AccountFeign accountFeign;
    @GlobalTransactional(name = "fsp-create-order", rollbackFor = Exception.class)
    @Override
    public void create(Order order) {
        String xid = RootContext.getXID();
        log.info("[create] 当前XID: {}", xid);
        order.setStatus(0);
        int result = orderMapper.create(order);
        log.info("[result] = " + result);
        if (result > 0) {
            Order orderDTO = orderMapper.selectOrder(order);
            log.info("[orderDTO] = " + orderDTO);
            // 1. 扣减库存
            log.info("扣减库存----------------》");
            storageFeign.decrease(orderDTO.getProductId(), orderDTO.getCount());
            log.info("扣减库存完成----------------》");
            // 2. 扣减账户
            log.info("扣减账户----------------》");
            accountFeign.decrease(orderDTO.getUserId(), orderDTO.getMoney());
            log.info("扣减账户完成----------------》");
            // 3. 修改订单状态
            orderDTO.setStatus(1);
            int updateResult = orderMapper.updateOrderStatus(orderDTO);
        }

    }
}
