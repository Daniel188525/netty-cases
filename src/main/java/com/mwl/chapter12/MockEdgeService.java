package com.mwl.chapter12;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.concurrent.TimeUnit;

/**
 * @author mawenlong
 * @date 2019/02/15
 */
public class MockEdgeService {
    static void testHotMethod() throws Exception {
        ByteBuf buf = Unpooled.buffer(1024);
        for (int i = 0; i < 1024; i++) {
            buf.writeByte(i);
        }
        RestfulReq req = new RestfulReq(buf.array());
        while (true) {
            byte[] msgReq = req.body();
            TimeUnit.MICROSECONDS.sleep(1);
        }
    }

    public static void main(String[] args) throws Exception {
        testHotMethod();
    }
}
