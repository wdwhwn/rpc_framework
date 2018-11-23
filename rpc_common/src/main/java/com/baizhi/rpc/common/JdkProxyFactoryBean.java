package com.baizhi.rpc.common;

import com.baizhi.rpc.common.pojo.HostAndPort;
import com.baizhi.rpc.common.pojo.MethodInvokeData;
import com.baizhi.rpc.common.pojo.MethodInvokeDataWrap;
import com.baizhi.rpc.common.pojo.Result;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
//基于jdk的序列化和反序列方案  实现对象类型数据传输
public class JdkProxyFactoryBean implements FactoryBean {
//  接口
    private Class<?> targetInterface;
//  注册中心
    private Registry registry;
//   负载均衡器
    private LoadBalancer loadBalancer;
//   真正的rpc远程调用
    private RpcTransporter rpcTransporter;
//   不提供无参构造 ，全部使用spring的有参构造注入
    public JdkProxyFactoryBean(Class<?> targetInterface,Registry registry,LoadBalancer loadBalancer,RpcTransporter rpcTransporter) {
        this.targetInterface = targetInterface;
        this.registry = registry;
        this.loadBalancer = loadBalancer;
        this.rpcTransporter = rpcTransporter;
    }

    /**
     * HelloService
     *      HelloService helloService = applicationContext.getBean("HelloService");
     *      String sayHello = helloService.sayHello("zs");
     * 1. 通过代理对象调用远程方法 返回结果
     * 2. 透明化调用
     *
     * @return
     * @throws Exception
     */
//    获取代理对象
    public Object getObject() throws Exception {
        // 目标接口对象的代理对象 代理对象调用远程方法 实现所谓的透明化调用
        return Proxy.newProxyInstance(JdkProxyFactoryBean.class.getClassLoader(), new Class[]{targetInterface}, new InvocationHandler() {
            /**
             *
             * @param proxy 代理对象
             * @param method 调用方法
             * @param args   调用方法的参数列表
             * @return
             * @throws Throwable
             */
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // 封装请求数据对象
                MethodInvokeData methodInvokeData = new MethodInvokeData(targetInterface,method.getName(),args,method.getParameterTypes());
                // 获取请求数据对象的加强对象
                MethodInvokeDataWrap methodInvokeDataWrap = new MethodInvokeDataWrap(methodInvokeData, null);
                // 获取服务提供方的节点列表
                List<HostAndPort> rpcServiceList = registry.getRpcServiceList(targetInterface.getName());
                // 订阅 节点数据改变
                registry.subscribeRpcService(targetInterface.getName(),rpcServiceList);
                // 负载均衡
                HostAndPort hostAndPort = loadBalancer.select(rpcServiceList);
                // 真正的rpc
                Result result = rpcTransporter.invoke(methodInvokeDataWrap, hostAndPort);

                return result.getReturnData();
            }
        });
    }

    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return false;
    }
}
