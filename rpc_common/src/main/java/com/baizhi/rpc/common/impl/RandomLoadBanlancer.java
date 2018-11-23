package com.baizhi.rpc.common.impl;

import com.baizhi.rpc.common.LoadBalancer;
import com.baizhi.rpc.common.pojo.HostAndPort;

import java.util.List;
import java.util.Random;

/**
 * 随机的负载均衡器
 */
public class RandomLoadBanlancer implements LoadBalancer {

    public HostAndPort select(List<HostAndPort> hostAndPorts) {

        if(hostAndPorts != null && hostAndPorts.size() != 0){
            // 产生随机数 范围集合范围内
            Random random = new Random();
            int randomData = random.nextInt(hostAndPorts.size()); // 0 - (size-1)
            return hostAndPorts.get(randomData);
        }
        return null;
    }
}
