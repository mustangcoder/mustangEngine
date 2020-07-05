package org.mustangcoder.engine.processor;

import org.mustangcoder.engine.common.Context;
import org.mustangcoder.engine.web.Request;
import org.mustangcoder.engine.web.response.AIOResponse;
import org.mustangcoder.engine.web.response.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AIOProcessor implements Processor {
    @Override
    public void process() {
        try {
            final int port = Context.getInstance().getPort();
            //首先打开一个ServerSocket通道并获取AsynchronousServerSocketChannel实例：
            final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
            //绑定需要监听的端口到serverSocketChannel:
            serverSocketChannel.bind(new InetSocketAddress(port));
            //实现一个CompletionHandler回调接口handler，
            //之后需要在handler的实现中处理连接请求和监听下一个连接、数据收发，以及通信异常。
            serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel,
                    Object>() {
                @Override
                public void completed(final AsynchronousSocketChannel result, final Object attachment) {
                    // 继续监听下一个连接请求
                    serverSocketChannel.accept(attachment, this);
                    Request request = new Request(result);
                    Response response = new AIOResponse(result);
                    String url = request.getUrl();
                    if (Context.getInstance().getServletMap().containsKey(url)) {
                        Context.getInstance().getServletMap().get(url).service(request, response);
                    } else {
                        try {
                            response.write("404 - Not Found");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failed(final Throwable exc, final Object attachment) {
                    System.out.println("出错了：" + exc.getMessage());
                    exc.printStackTrace();
                }
            });
            System.out.println("Mustang Engine started! port:" + port);
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
