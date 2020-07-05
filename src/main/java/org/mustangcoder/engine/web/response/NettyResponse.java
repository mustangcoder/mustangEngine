package org.mustangcoder.engine.web.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NettyResponse extends Response {

    private ChannelHandlerContext ctx;

    /**
     * netty模式
     *
     * @param ctx
     * @param httpRequest
     */
    public NettyResponse(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        this.ctx = ctx;
    }

    public void write(String data) throws IOException {
        try {
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(data.getBytes(StandardCharsets.UTF_8)));
            fullHttpResponse.headers().set("Content-Type", "text/html;");
            ctx.write(fullHttpResponse);
        } finally {
            ctx.flush();
            ctx.close();
        }

    }
}
