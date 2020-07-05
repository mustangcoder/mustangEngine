package org.mustangcoder.engine.web.response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BIOResponse extends Response {
    private OutputStream outputStream;

    /**
     * bio模式
     *
     * @param outputStream
     */
    public BIOResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String data) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK").append(System.lineSeparator())
                .append("Content-Type: text/html;").append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(data);
        outputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

}
