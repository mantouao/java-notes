package com.atguigu.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StorageMapper {

    //扣减库存
    @Update("update t_storage set used = used + #{count}, residue = residue - #{count} " +
            "where product_id = #{productId} and residue >= #{count}")
    void decrease(@Param("productId") Long productId, @Param("count") Integer count);
}
