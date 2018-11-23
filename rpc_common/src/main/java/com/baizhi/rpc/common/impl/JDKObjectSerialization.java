package com.baizhi.rpc.common.impl;

import com.baizhi.rpc.common.ObjectSerialization;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;

/**
 * 基于jdk的序列化方案
 */
public class JDKObjectSerialization implements ObjectSerialization {

    public byte[] serialization(Object obj) {
        return SerializationUtils.serialize((Serializable)obj);
    }

    public Object deserialization(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
}
