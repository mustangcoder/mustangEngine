package org.mustangcoder.web;

import org.mustangcoder.web.response.Response;

import java.io.IOException;

public class HomeServlet extends Servlet {
    @Override
    public void doGet(Request request, Response response) {
        doPost(request, response);
    }

    @Override
    public void doPost(Request request, Response response) {
        try {
            response.write("Welcome to Mustang Engine!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
