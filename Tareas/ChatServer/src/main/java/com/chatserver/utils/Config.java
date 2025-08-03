package com.chatserver.utils;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (var input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("No se puede encontrar el archivo config.properties");
            } else {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error de Entrada/Salida: " + e.getMessage());
        }
    }

    public static String get(final String key) {
        return props.getProperty(key);
    }
}
