package org.mustangcoder.web;

import org.mustangcoder.web.response.Response;

public abstract class Servlet {

    public void service(Request request, Response response) {

        System.out.println("receive:" + System.lineSeparator() + request.getMethod() + " " + request.getUrl());

        if ("GET".equals(request.getMethod())) {
            doGet(request, response);
        } else if ("POST".equals(request.getMethod())) {
            doPost(request, response);
        }
    }

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);

}
