package org.mustangcoder.engine.web.response;

import java.io.IOException;

public abstract class Response {

    public abstract void write(String data) throws IOException;

}
