package com.atguigu.cloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Repository;

@Repository
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer count;
    private Double money;
    private Integer status; // 订单状态：0：创建中；1：已完结
    // 省略getter和setter方法
}
