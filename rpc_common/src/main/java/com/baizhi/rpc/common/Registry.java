package com.baizhi.rpc.common;

import com.baizhi.rpc.common.pojo.HostAndPort;

import java.util.List;

/**
 * 注册中心接口
 *    1. 注册rpc服务
 *    2. 订阅rpc服务
 *    3. 获取rpc服务提供方节点列表方法
 */
public interface Registry {

    public static final String RPC_PREFIX="/rpc";

    public static final String RPC_SUFFIX="/providers";

    /**
     * 注册rpc服务
     *      /rpc/targetInterfaceName/providers/hostAndPort
     */
    public void registerRpcService(String targetInterfaceName, HostAndPort hostAndPort);


    /**
     * 订阅rpc服务
     *     一旦服务提供方的节点信息发生改变, 使用新的节点列表更新历史节点类表
     */
    public void subscribeRpcService(String targetInterfaceName, List<HostAndPort> existHostAndPorts);

    /**
     * 获取rpc服务提供方节点列表方法
     */
    public List<HostAndPort> getRpcServiceList(String targetInterfaceName);
}
