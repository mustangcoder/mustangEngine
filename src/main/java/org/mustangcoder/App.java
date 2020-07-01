package org.mustangcoder;

import org.mustangcoder.common.Context;
import org.mustangcoder.processor.ProcessorFactory;

import java.util.Locale;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        new App().start();
    }

    private void start() {
        Context.getInstance().init();
        String way = Context.getInstance().getProp("server.communication.way", "").toLowerCase(Locale.ENGLISH);
        ProcessorFactory.getProcessor(way).process();
    }
}
