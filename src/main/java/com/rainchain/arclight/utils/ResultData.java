package com.rainchain.arclight.utils;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//统一返回工具类
public class ResultData<T> {
    @JSONField
    private boolean succ;
    @JSONField(ordinal = 1)
    private String err_msg;
    @JSONField(ordinal = 2)
    private T data;

    //成功时的返回
    public static <T> ResultData<T> success(T data) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setSucc(true);
        resultData.setErr_msg(null);
        resultData.setData(data);
        return resultData;
    }

    //不成功的返回
    public static <T> ResultData<T> fail(String errMsg) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setSucc(false);
        resultData.setErr_msg(errMsg);
        return resultData;
    }

}
