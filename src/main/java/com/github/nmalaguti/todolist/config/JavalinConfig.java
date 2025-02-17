package com.github.nmalaguti.todolist.config;

import freemarker.template.Configuration;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinFreemarker;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.LogManager;

public class JavalinConfig {
    public static Javalin createApp() {
        // Redirect Java Util Logging (JUL) to SLF4J
        setupLogging();

        // Configure FreeMarker
        var freeMarkerConfig = setupFreeMarker();

        // Create and return configured Javalin instance
        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinFreemarker(freeMarkerConfig));
        });
    }

    private static void setupLogging() {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();
    }

    private static Configuration setupFreeMarker() {
        var freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_34);
        freeMarkerConfig.setClassForTemplateLoading(JavalinConfig.class, "/templates");
        return freeMarkerConfig;
    }
}
