package com.rosevvi.mallchat.websocket;

import cn.hutool.json.JSONUtil;
import com.rosevvi.mallchat.websocket.domain.enums.WSReqTypeEnum;
import com.rosevvi.mallchat.websocket.domain.vo.req.WSBaseReq;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author: rosevvi
 * @date: 2024/2/8 22:44
 * @version: 1.0
 * @description:
 */
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            System.out.println("握手完成");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            case LOGIN:
                System.out.println("尝试获取微信二维码登录");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("相应"));
            case AUTHORIZE:
            case HEARTBEAT:
        }
        System.out.println(text);
    }
}
