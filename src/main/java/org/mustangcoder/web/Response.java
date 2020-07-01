package org.mustangcoder.web;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Response {
    private OutputStream outputStream;

    private SelectionKey selectionKey;

    public Response(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String data) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK").append(System.lineSeparator())
                .append("Content-Type: text/html;").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(data);

        if (outputStream == null && selectionKey != null) {
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
            return;
        }
        outputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
}
