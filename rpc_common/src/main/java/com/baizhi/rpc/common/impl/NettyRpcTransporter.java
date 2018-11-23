package com.baizhi.rpc.common.impl;

import com.baizhi.rpc.common.RpcTransporter;
import com.baizhi.rpc.common.pojo.HostAndPort;
import com.baizhi.rpc.common.pojo.MethodInvokeDataWrap;
import com.baizhi.rpc.common.pojo.Result;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 基于netty的rpc调用类(netty的客户端)
 */
public class NettyRpcTransporter implements RpcTransporter {

    public Result invoke(final MethodInvokeDataWrap methodInvokeDataWrap, HostAndPort host) {
        // 启动netty client
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline channelPipeline = socketChannel.pipeline();
                channelPipeline.addLast(new LengthFieldPrepender(2)); // 消息头的字节长度
                // 参数一：消息包的最大长度  参数二：长度域的偏移量 参数三:长度字段所占的字节⼤⼩ 参数四:长度补偿 参数五：从数据帧中跳过的字节数
                channelPipeline.addLast(new
                        LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                channelPipeline.addLast(new ObjectToByteBufEncoder(new JDKObjectSerialization()));
                channelPipeline.addLast(new ByteBufToObjectDecoder(new JDKObjectSerialization()));
                channelPipeline.addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.out.println("rpc服务消费方异常!");
                        cause.printStackTrace();
                        ctx.close();
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ctx.writeAndFlush(methodInvokeDataWrap.getMethodInvokeData());
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        Result result = (Result) msg;
                        methodInvokeDataWrap.setResult(result);
                    }
                });
            }
        });

        try {
            ChannelFuture channelFuture = bootstrap.connect(host.getHost(), host.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
        // rpc远程程序调用的结果
        return methodInvokeDataWrap.getResult();
    }
}
