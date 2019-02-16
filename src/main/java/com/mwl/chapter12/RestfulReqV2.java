package com.mwl.chapter12;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Arrays;

/**
 * @author mawenlong
 * @date 2019-02-16 12:57
 */
public class RestfulReqV2 {
    private HttpMethod method;

    private HttpVersion version;

    private byte[] body;

    public RestfulReqV2(byte[] body) {
        this.body = body;
    }

    public byte[] body() {
        return this.body;
    }

    public byte[] bodyCopy() {
        if (this.body != null) {
            return Arrays.copyOf(this.body, this.body.length);
        }
        return null;
    }
}
