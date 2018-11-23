package com.baizhi.rpc.common;

/**
 * 对象序列化接口
 */
public interface ObjectSerialization {

    /**
     * 序列化方法  对象转换为字节数组
     */
    public byte[] serialization(Object obj);

    /**
     * 反序列化方法  字节数组转换为对象
     */
    public Object deserialization(byte[] bytes);
}
