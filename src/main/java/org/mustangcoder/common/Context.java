package org.mustangcoder.common;

import org.mustangcoder.App;
import org.mustangcoder.web.Servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Context {

    private static final Context INSTASNCE = new Context();

    private Properties properties = new Properties();

    private Map<String, Servlet> servletMap = new HashMap<>();

    private Context() {
    }

    public static Context getInstance() {
        return INSTASNCE;
    }

    public void init() {
        System.out.println("Mustang Engine init!");
        String dir = App.class.getResource("/").getPath();
        System.out.println("Mustang Engine load config from:" + dir);
        try {
            FileInputStream fileInputStream = new FileInputStream(dir + "web.properties");
            properties.load(fileInputStream);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = String.valueOf(entry.getKey());
                if (key.endsWith(".url")) {
                    String servletName = key.replace(".url", "");
                    String url = String.valueOf(entry.getValue());
                    String className = properties.getProperty(servletName + ".className");
                    Servlet servlet = (Servlet) Class.forName(className).newInstance();
                    servletMap.put(url, servlet);
                    System.out.println("Mustang Engine register servlet:" + url + " " + className);
                }
            }
            Servlet servlet = (Servlet) Class.forName("org.mustangcoder.web.HomeServlet").newInstance();
            servletMap.put("/", servlet);

        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("server.port", "8080"));
    }

    public String getProp(String key, String defaultVal) {
        return properties.getProperty(key, defaultVal);
    }

    public Map<String, Servlet> getServletMap() {
        return servletMap;
    }
}
