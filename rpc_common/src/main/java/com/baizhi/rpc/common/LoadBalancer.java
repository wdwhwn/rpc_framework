package com.baizhi.rpc.common;

import com.baizhi.rpc.common.pojo.HostAndPort;

import java.util.List;

/**
 * rpc负载均衡器接口  随机
 */
public interface LoadBalancer {

    /**
     * 选择方法
     */
    public HostAndPort select(List<HostAndPort> hostAndPorts);
}
