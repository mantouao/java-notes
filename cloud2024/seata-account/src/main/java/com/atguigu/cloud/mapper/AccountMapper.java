package com.atguigu.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AccountMapper {

    //扣减账户
    @Update("update t_account set used = used + #{money}, residue = residue - #{money} " +
            "where user_id = #{userId} and residue >= #{money}")
    void decrease(@Param("userId") Long userId,@Param("money") double money);


}
