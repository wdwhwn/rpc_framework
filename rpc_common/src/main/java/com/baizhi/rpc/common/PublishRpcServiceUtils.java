package com.baizhi.rpc.common;

import com.baizhi.rpc.common.impl.ByteBufToObjectDecoder;
import com.baizhi.rpc.common.impl.JDKObjectSerialization;
import com.baizhi.rpc.common.impl.ObjectToByteBufEncoder;
import com.baizhi.rpc.common.impl.ZKRegistry;
import com.baizhi.rpc.common.pojo.HostAndPort;
import com.baizhi.rpc.common.pojo.MethodInvokeData;
import com.baizhi.rpc.common.pojo.Result;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

/**
 * 发布rpc服务 启动Netty服务 注册zk
 *
 */
public class PublishRpcServiceUtils {

    private ZKRegistry zkRegistry;

    /**
     * 暴露的bean的map集合
     *    k---> rpc服务接口的全限定名
     *    v---> rpc服务接口的本地实现类
     *
     *    com.baizhi.service.HelloService com.baizhi.service.HelloServiceImpl
     */
    private Map<String,Object> exposeBeans;

    /**
     * rpc服务监听的端口
     */
    private int port;

    /**
     * 实例化 发布rpc服务类   不提供无参构造方法， spring中通过有参构造注入
     * @param zkRegistry
     * @param exposeBeans
     * @param port
     */
    public PublishRpcServiceUtils(ZKRegistry zkRegistry, Map<String, Object> exposeBeans, int port) {
        this.zkRegistry = zkRegistry;
        this.exposeBeans = exposeBeans;
        this.port = port;
    }

    /**
     * 初始化方法   服务方发布
     * 在spring配置文件中 要使这个方法在对象创建时启动执行
     *  init-method=“方法名”
     *
     */
    public void init(){
        // 启动netty server
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
//         创建线程
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
//        绑定线程
        serverBootstrap.group(bossGroup,workerGroup);
//        启动通道服务
        serverBootstrap.channel(NioServerSocketChannel.class);
//        启动通讯管道
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                System.out.println("CCCCCCCCCCCCCCCCCC");
                ChannelPipeline channelPipeline = socketChannel.pipeline();
//                解决拆包和粘包
                channelPipeline.addLast(new LengthFieldPrepender(2)); // 消息头的字节长度
                // 参数一：消息包的最大长度  参数二：长度域的偏移量 参数三:长度字段所占的字节⼤⼩ 参数四:长度补偿 参数五：从数据帧中跳过的字节数
                channelPipeline.addLast(new
                        LengthFieldBasedFrameDecoder(65535,0,2,0,2));
//               序列化
                channelPipeline.addLast(new ObjectToByteBufEncoder(new JDKObjectSerialization()));
//                反序列化
                channelPipeline.addLast(new ByteBufToObjectDecoder(new JDKObjectSerialization()));
                channelPipeline.addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        cause.printStackTrace();
                        ctx.close();
                    }

                    /**
                     * 请求数据和响应数据的处理
                     * @param ctx
                     * @param msg 请求数据对象
                     * @throws Exception
                     */
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        Result result = new Result();
                        try {
//                            获取请求数据
                            MethodInvokeData methodInvokeData = (MethodInvokeData)msg;
//                            获取接口全限定名
                            Class<?> targetInterface = methodInvokeData.getTargetInterface();
//                            获取方法
                            Method method = targetInterface.getMethod(methodInvokeData.getMethodName(), methodInvokeData.getParameterTypes());
//                            从暴露的bean的map集合汇总  获取对象
                            Object targetInterfaceImpl = exposeBeans.get(targetInterface.getName());
//                            执行方法
                            Object returnData = method.invoke(targetInterfaceImpl, methodInvokeData.getArgs());
                            System.out.println(returnData);
//                            没有出现异常时  返回结果
                            result.setReturnData(returnData);
                        } catch (Exception e) {
                            e.printStackTrace();
//                            初心异常时  返回异常
                            result.setThrowable(e);
                        }
//                      添加监听器
                        ChannelFuture channelFuture = ctx.writeAndFlush(result);
                        channelFuture.addListener(ChannelFutureListener.CLOSE);
                        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

                    }
                });
            }
        });
        // 启动netty服务  子线程启动  不然后面的注册没有办法执行
        // netty server占用spring容器的初始化线程 使用子线程启动netty server
        new Thread(new Runnable() {
            public void run() {
                try {
                    ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
//                    释放资源
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                }
            }
        }).start();

        // 注册rpc服务
        Set<String> keySet = exposeBeans.keySet();
        for (String interfaceName : keySet) {
//            所有发布rpc服务的全限定名
            try {
//                注册中心 注册需要：  全限定名、ip和端口号
                zkRegistry.registerRpcService(interfaceName,new HostAndPort(InetAddress.getLocalHost().getHostAddress(),port));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
