package org.mustangcoder.processor;

import org.mustangcoder.common.Context;
import org.mustangcoder.web.Request;
import org.mustangcoder.web.Servlet;
import org.mustangcoder.web.response.NIOResponse;
import org.mustangcoder.web.response.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class NIOProcessor implements Processor {
    @Override
    public void process() {
        try {
            int port = Context.getInstance().getPort();
            Map<String, Servlet> servletMap = Context.getInstance().getServletMap();

            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(port));

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Mustang Engine started! port:" + port);

            List<Request> requestList = new ArrayList<>();
            List<Response> responseList = new ArrayList<>();

            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    if (selectionKey.isAcceptable()) {
                        System.out.println("nio: OP_ACCEPT");
                        ServerSocketChannel keyChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = keyChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        System.out.println("nio: OP_READ");
                        Request request = new Request(selectionKey);
                        requestList.add(request);
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    } else if (selectionKey.isWritable()) {
                        System.out.println("nio: OP_WRITE");
                        Response response = new NIOResponse(selectionKey);
                        responseList.add(response);
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    }
                    //  等待一对请求和响应均准备好时处理
                    if (!requestList.isEmpty() && !responseList.isEmpty()) {
                        Request request = requestList.remove(0);
                        Response response = responseList.remove(0);
                        String url = request.getUrl();
                        if (url == null || "".equals(url)) {
                            url = "/";
                        }

                        if (servletMap.containsKey(url)) {
                            servletMap.get(url).service(request, response);
                        } else {
                            response.write("404 - Not Found");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
