package com.baizhi.rpc.common.impl;

import com.baizhi.rpc.common.ObjectSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class ObjectToByteBufEncoder extends MessageToMessageEncoder {

    public ObjectSerialization objectSerialization;

    public ObjectToByteBufEncoder(ObjectSerialization objectSerialization){
        this.objectSerialization = objectSerialization;
    }

    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {

        byte[] bytes = objectSerialization.serialization(o);

        ByteBuf byteBuf = Unpooled.buffer();

        byteBuf.writeBytes(bytes);

        list.add(byteBuf);
    }
}
