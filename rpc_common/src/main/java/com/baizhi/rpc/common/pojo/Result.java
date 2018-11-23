package com.baizhi.rpc.common.pojo;

import java.io.Serializable;

/**
 * rpc响应数据对象
 * 1  当出现异常时  就返回异常
 * 2  当没有出现异常时  就获取正常执行的结果
 */
public class Result implements Serializable {

    private Object returnData;

    private Throwable throwable;

    public Result() {
    }

    public Result(Object returnData, Throwable throwable) {
        this.returnData = returnData;
        this.throwable = throwable;
    }

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
