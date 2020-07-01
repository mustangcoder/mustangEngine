package org.mustangcoder.web;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Request {

    private String method;
    private String url;

    public Request(InputStream inputStream) {
        try {
            String content = "";
            byte[] buffer = new byte[1024];
            int length = 0;
            if ((length = inputStream.read(buffer)) > 0) {
                content = new String(buffer, 0, length);
            }
            if (length < 0) {
                return;
            }
            String line = content.split(System.lineSeparator())[0];
            String[] arr = line.split("\\s");
            this.method = arr[0];
            this.url = arr[1].split("\\?")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Request(SelectionKey selectionKey) {
        try {
            SocketChannel channel = (SocketChannel) selectionKey.channel();
            ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
            int length = channel.read(receiveBuffer);
            if (length > 0) {
                String content = new String(receiveBuffer.array(), 0, length);
                System.out.println("read data:" + content);

                String line = content.split(System.lineSeparator())[0];
                String[] arr = line.split("\\s");
                this.method = arr[0];
                this.url = arr[1].split("\\?")[0];
            } else {
                System.out.println("no data to read");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
