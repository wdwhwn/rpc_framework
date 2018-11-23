package com.baizhi.rpc.common.pojo;

import java.io.Serializable;

/**
 * 增强类(请求数据对象和响应数据对象)
 */
public class MethodInvokeDataWrap implements Serializable {

    private MethodInvokeData methodInvokeData;

    private Result result;

    public MethodInvokeDataWrap() {
    }

    public MethodInvokeDataWrap(MethodInvokeData methodInvokeData, Result result) {
        this.methodInvokeData = methodInvokeData;
        this.result = result;
    }

    public MethodInvokeData getMethodInvokeData() {
        return methodInvokeData;
    }

    public void setMethodInvokeData(MethodInvokeData methodInvokeData) {
        this.methodInvokeData = methodInvokeData;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
