package com.mwl.chapter18;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mawenlong
 * @date 2019-02-17 18:41
 */
public class HttpSessions {
    public static Map<String, NioSocketChannel> channelMap = new ConcurrentHashMap<>();
}
