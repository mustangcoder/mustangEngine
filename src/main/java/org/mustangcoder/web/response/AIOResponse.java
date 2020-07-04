package org.mustangcoder.web.response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AIOResponse extends Response {

    private AsynchronousSocketChannel result;

    public AIOResponse(AsynchronousSocketChannel result) {
        this.result = result;
    }

    @Override
    public void write(String data) throws IOException {
        try {
            // 给客户端发送数据并等待发送完成
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HTTP/1.1 200 OK").append(System.lineSeparator())
                    .append("Content-Type: text/html;").append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append(data);
            Future future = result.write(ByteBuffer.wrap(stringBuilder.toString().getBytes(StandardCharsets.UTF_8)));
            future.get();
            result.shutdownOutput();
            result.shutdownInput();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
