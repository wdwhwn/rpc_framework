package com.baizhi.rpc.common.impl;

import com.baizhi.rpc.common.Registry;
import com.baizhi.rpc.common.pojo.HostAndPort;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于zk的注册中心
 */
public class ZKRegistry implements Registry {

    private ZkClient zkClient;

    // 通过有参的构造方法初始化zkClient对象
    public ZKRegistry(String adress){
        zkClient = new ZkClient(adress);
    }

    public void registerRpcService(String targetInterfaceName, HostAndPort hostAndPort) {
        String path = RPC_PREFIX + "/"+targetInterfaceName+RPC_SUFFIX;
        // 不存在创建父级的znode
        if(!zkClient.exists(path)){
            zkClient.createPersistent(path,true);
        }
        String myPath = path +"/"+hostAndPort.getHost()+":"+hostAndPort.getPort();
        if(zkClient.exists(myPath)){
           zkClient.delete(myPath);
        }
        zkClient.createEphemeral(myPath);
    }

    public void subscribeRpcService(String targetInterfaceName, final List<HostAndPort> existHostAndPorts) {
        String path = RPC_PREFIX + "/"+targetInterfaceName+RPC_SUFFIX;
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            /**
             *
             * @param s
             * @param list 最新可以提供功能的节点列表
             * @throws Exception
             */
            public void handleChildChange(String s, List<String> list) throws Exception {
                // 清空历史节点信息
                existHostAndPorts.clear();
                // 192.168.0.3:20880
                for (String node : list) {
                    String[] split = node.split(":");
                    existHostAndPorts.add(new HostAndPort(split[0],Integer.parseInt(split[1])));
                }
            }
        });
    }

    public List<HostAndPort> getRpcServiceList(String targetInterfaceName) {
        String path = RPC_PREFIX + "/"+targetInterfaceName+RPC_SUFFIX;
        List<String> list = zkClient.getChildren(path);
        List<HostAndPort> hostAndPorts = new ArrayList<HostAndPort>();
        for (String node : list) {
            String[] split = node.split(":");
            hostAndPorts.add(new HostAndPort(split[0],Integer.parseInt(split[1])));
        }
        return hostAndPorts;
    }
}
