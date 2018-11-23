package com.baizhi.rpc.common;

import com.baizhi.rpc.common.pojo.HostAndPort;
import com.baizhi.rpc.common.pojo.MethodInvokeData;
import com.baizhi.rpc.common.pojo.MethodInvokeDataWrap;
import com.baizhi.rpc.common.pojo.Result;

/**
 * rpc传输
 *      完成真正的rpc远程程序调用
 */
public interface RpcTransporter {

    /**
     * 调用方法
     */
    public Result invoke(MethodInvokeDataWrap methodInvokeDataWrap, HostAndPort host);
}
