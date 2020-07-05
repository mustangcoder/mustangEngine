package org.mustangcoder.engine.web.response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class NIOResponse extends Response {
    private SelectionKey selectionKey;

    /**
     * nio模式
     *
     * @param selectionKey
     */
    public NIOResponse(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void write(String data) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK").append(System.lineSeparator())
                .append("Content-Type: text/html;").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(data);

        SocketChannel channel = (SocketChannel) selectionKey.channel();
        System.out.println("return data:" + System.lineSeparator() + stringBuilder);
        ByteBuffer bb = ByteBuffer.wrap(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        long len = channel.write(bb);   //  向通道中写入数据
        if (len == -1) {
            selectionKey.cancel();
        }
        bb.flip();
        channel.close();
        selectionKey.cancel();
    }

}
