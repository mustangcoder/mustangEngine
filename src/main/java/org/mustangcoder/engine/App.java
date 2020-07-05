package org.mustangcoder.engine;

import org.mustangcoder.engine.common.Context;
import org.mustangcoder.engine.processor.ProcessorFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Locale;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        new App().start();
    }

    private void start() {
        loadSlogan();
        Context.getInstance().init();
        String way = Context.getInstance().getProp("server.communication.way", "").toLowerCase(Locale.ENGLISH);
        ProcessorFactory.getProcessor(way).process();
    }

    private void loadSlogan() {
        String dir = App.class.getResource("/").getPath();
        try {
            FileReader fileReader = new FileReader(dir + "slogan.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
            System.out.println(stringBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
