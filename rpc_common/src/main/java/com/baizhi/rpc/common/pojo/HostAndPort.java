package com.baizhi.rpc.common.pojo;

/**
 * 服务提供方的节点信息对象
 */
public class HostAndPort {

    private String host;

    private int port;

    public HostAndPort() {
    }

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
