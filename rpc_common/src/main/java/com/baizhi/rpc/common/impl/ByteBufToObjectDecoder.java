package com.baizhi.rpc.common.impl;

import com.baizhi.rpc.common.ObjectSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class ByteBufToObjectDecoder extends MessageToMessageDecoder {

    private ObjectSerialization objectSerialization;

    public ByteBufToObjectDecoder(ObjectSerialization objectSerialization) {
        this.objectSerialization = objectSerialization;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {
        ByteBuf byteBuf = (ByteBuf) o;

        byte[] bytes = new byte[byteBuf.readableBytes()];

        byteBuf.readBytes(bytes);

        Object obj = objectSerialization.deserialization(bytes);

        list.add(obj);
    }
}
