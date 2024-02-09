package com.rosevvi.mallchat.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author: rosevvi
 * @date: 2024/2/8 21:38
 * @version: 1.0
 * @description:
 */
@Slf4j
@Configuration
public class NettyWebSocketServer {
    // websocket链接端口
    public static final int WEB_SOCKET_PORT = 8090;
    // 请求链接的group
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    // 实际处理业务的group
    private EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());
    @Autowired
    private ServerChannelInitializer childInitializer;

    /**
     * 开始
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        run();
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destory(){
        // 把连接关闭
        Future<?> future = bossGroup.shutdownGracefully();
        Future<?> future1 = workerGroup.shutdownGracefully();
        future.syncUninterruptibly();
        future1.syncUninterruptibly();
        log.info("关闭 ws server 成功");
    }

    private void run() throws InterruptedException {
        // 服务器启动引导对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)// SO_BACKLOG 对应的是tcp/ip 协议，128 为服务端可连接数量：linux内核维护两个队列，一个已连接队列，一个未连接队列，
                .option(ChannelOption.SO_KEEPALIVE,true)// 保持链接 - 长连接
                .handler(new LoggingHandler(LogLevel.INFO))// 为bossGroup 添加日志处理器
                .childHandler(this.childInitializer);
        // 启动服务，监听端口，阻塞直到启动成功
        serverBootstrap.bind(WEB_SOCKET_PORT).sync();
    }
}
