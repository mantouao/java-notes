package com.atguigu.cloud.mapper;

import com.atguigu.cloud.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

@Mapper
public interface OrderMapper {
    // 新添订单
//    @Insert("insert into t_order(user_id,product_id,count,money,status) values(#{userId},#{productId},#{count},#{money},#{status})")
    int create(Order order);
    @Select("select * from t_order where id = #{id}")
    Order selectOrder(Order order);
    @Update("update t_order set status = #{status} where id = #{id}")
    int updateOrderStatus(Order order);
}
