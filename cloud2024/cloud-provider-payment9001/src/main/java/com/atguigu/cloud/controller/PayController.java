package com.atguigu.cloud.controller;

import cn.hutool.core.bean.BeanUtil;
import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import com.atguigu.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "支付模块", description = "支付crud")
public class PayController {
    @Resource
    private PayService payService;

    @PostMapping("/pay/add")
    @Operation(summary = "增添", description = "增添支付信息")
    public ResultData<String> addPay(@RequestBody Pay pay) {
        int i = -1;
        try {
            System.out.println(pay.toString());
            i = payService.add(pay);
        } catch (Exception e) {
            e.printStackTrace();
           return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "添加失败");
        }
        return ResultData.success("成功添加记录，返回值:" + i);

    }

    @GetMapping("/pay/delete/{id}")
    @Operation(summary = "删除", description = "删除支付信息")
    public ResultData<String> deletePay(@PathVariable("id") Integer id) {
        int i = -1;
        try {
            System.out.println(id);
            i = payService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "删除失败");
        }
        return ResultData.success("成功删除记录，返回值:" + i);
    }

    @PostMapping("/pay/update")
    @Operation(summary = "修改", description = "修改支付信息")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO) {
        int i = -1;
        try {
            Pay pay = new Pay();
            BeanUtil.copyProperties(payDTO, pay);
            System.out.println(pay.toString());
            i = payService.update(pay);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "修改失败");
        }
        return ResultData.success("成功修改记录，返回值:" + i);
    }

    @GetMapping("/pay/get/{id}")
    @Operation(summary = "查询一个", description = "根据id查询支付信息")
    public ResultData<Pay> getPay(@PathVariable("id") Integer id) {
        Pay pay = null;
        try {
            pay = payService.getPayById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "查询失败");
        }
        return ResultData.success(pay);
    }

    @GetMapping("/pay/getall")
    @Operation(summary = "查询全部", description = "查询全部支付信息")
    public ResultData<List<Pay>> getAllPay(){
        List<Pay> payAll = null;
        try {
            payAll = payService.getPayAll();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error(ReturnCodeEnum.FAIL.getCode(), "查询失败");
        }
        return ResultData.success(payAll);
    }
    @GetMapping("/getConsulInfo")
    public ResultData<String> getConsulInfo(@Value("${binbin.info}") String info, @Value("9001") String port){
        return ResultData.success(info + "port : " + port);
    }
}
