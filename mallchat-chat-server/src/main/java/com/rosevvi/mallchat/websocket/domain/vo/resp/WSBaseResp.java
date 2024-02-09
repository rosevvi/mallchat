package com.rosevvi.mallchat.websocket.domain.vo.resp;

import com.rosevvi.mallchat.websocket.domain.enums.WSRespTypeEnum;
import lombok.Data;

/**
 * ws的基本返回信息体
 * @param <T>
 */
@Data
public class WSBaseResp<T> {
    /**
     * ws推送给前端的消息
     *
     * @see WSRespTypeEnum
     */
    private Integer type;
    private T data;
}
