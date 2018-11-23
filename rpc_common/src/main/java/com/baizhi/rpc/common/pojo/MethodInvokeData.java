package com.baizhi.rpc.common.pojo;

import java.io.Serializable;

/**
 * rpc请求数据对象   参数：全限定名  方法名   参数列表   参数类型
 */
public class MethodInvokeData implements Serializable {

    private Class<?> targetInterface;

    private String methodName;

    private Object[] args;

    private Class[] parameterTypes;

    public MethodInvokeData() {
    }

    public MethodInvokeData(Class<?> targetInterface, String methodName, Object[] args, Class[] parameterTypes) {
        this.targetInterface = targetInterface;
        this.methodName = methodName;
        this.args = args;
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
