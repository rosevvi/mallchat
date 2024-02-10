package com.rosevvi.mallchat.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

/**
 * @author: rosevvi
 * @date: 2024/2/8 22:28
 * @version: 1.0
 * @description:
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 没有向服务器发起心跳则关闭连接  读：前端有没有向后端发  写：后端有没有向前端发
        pipeline.addLast(new IdleStateHandler(30,0,0));
        // 因为使用http协议所以使用http的解码器和编码器
        pipeline.addLast(new HttpServerCodec());
        // 以块的方式写，添加ChunkedWrite 处理器
        pipeline.addLast(new ChunkedWriteHandler());
        /**
         * 说明：
         * 1、http数据在传输过程中是分段的，HttpObjectAggregator可以把多段组合在一起
         * 2、这就是为什么当浏览器发送大量数据时会发出多次http请求的原因。
         */
        pipeline.addLast(new HttpObjectAggregator(8192));
        /**
         * 说明：
         * 1、对于websocket，他的数据是以帧frame的形式传递的
         * 2、可以看到WebSocketFrame下面有六个子类
         * 3、浏览器发送请求时： ws://localhost:7000/hello 表示请求的uri
         * 4、WebSocketServerProtocolHandler 核心功能是吧http协议升级为websocket协议，保持长连接：
         *    是通过一个状态码101来实现的。
         * 5、参数表示在这个路径下的所有请求都会转换成websocket的形式
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
        // 自定义的handle，处理业务逻辑
        pipeline.addLast(new NettyWebSocketServerHandler());
    }
}
