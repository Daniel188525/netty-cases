package com.mwl.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * @author mawenlong
 * @date 2019/02/11
 */
public class PoolByteBufPerformanceTest {

    @Test
    public void unPoolTest() {
        //非内存池模式
        long beginTime = System.currentTimeMillis();
        ByteBuf buf = null;
        int maxTimes = 10000000;
        for (int i = 0; i < maxTimes; i++) {
            buf = Unpooled.buffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time : "
                           + (System.currentTimeMillis() - beginTime));
    }

    @Test
    public void poolTest() {
        //内存池模式
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        long beginTime = System.currentTimeMillis();
        ByteBuf buf = null;
        int maxTimes = 10000000;
        for (int i = 0; i < maxTimes; i++) {
            buf = allocator.heapBuffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time : "
                           + (System.currentTimeMillis() - beginTime));
    }
}
