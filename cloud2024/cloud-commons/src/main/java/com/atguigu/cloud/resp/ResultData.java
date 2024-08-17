package com.atguigu.cloud.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultData<T> {
    private String code;
    private String massage;
    private T data;
    private Long timestamp;
    public ResultData(){
        timestamp = System.currentTimeMillis();
    }
    public static <T> ResultData<T> success(T data){
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.SUCCESS.getCode());
        resultData.setMassage(ReturnCodeEnum.SUCCESS.getMessage());
        resultData.setData(data);
        return resultData;
    }
    public static <T> ResultData<T> error(String code,String message){
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(code);
        resultData.setMassage(message);
        return resultData;
    }
}
