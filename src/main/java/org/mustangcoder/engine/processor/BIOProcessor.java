package org.mustangcoder.engine.processor;

import org.mustangcoder.engine.common.Context;
import org.mustangcoder.engine.web.Request;
import org.mustangcoder.engine.web.Servlet;
import org.mustangcoder.engine.web.response.BIOResponse;
import org.mustangcoder.engine.web.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class BIOProcessor implements Processor {

    public BIOProcessor() {
    }

    @Override
    public void process() {
        try {
            int port = Context.getInstance().getPort();
            Map<String, Servlet> servletMap = Context.getInstance().getServletMap();

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Mustang Engine started! port:" + port);
            while (true) {
                Socket client = serverSocket.accept();

                InputStream inputStream = client.getInputStream();
                OutputStream outputStream = client.getOutputStream();
                Request request = new Request(inputStream);
                Response response = new BIOResponse(outputStream);
                String url = request.getUrl();

                if (url == null || "".equals(url)) {
                    url = "/";
                }

                if (servletMap.containsKey(url)) {
                    servletMap.get(url).service(request, response);
                } else {
                    response.write("404 - Not Found");
                }
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
