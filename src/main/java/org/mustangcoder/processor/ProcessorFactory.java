package org.mustangcoder.processor;

public class ProcessorFactory {

    public static Processor getProcessor(String way) {
        if ("bio".equals(way)) {
            return new BIOProcessor();
        } else if ("nio".equals(way)) {
            return new NIOProcessor();
        } else if ("aio".equals(way)) {
            return new BIOProcessor();
        } else if ("netty".equals(way)) {
            return new BIOProcessor();
        } else if ("mina".equals(way)) {
            return new BIOProcessor();
        } else {
            return new BIOProcessor();
        }
    }
}