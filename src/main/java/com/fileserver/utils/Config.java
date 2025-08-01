package com.fileserver.utils;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (var input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("No se Puedo encontrar el archivo config.properties");
            } else {
                props.load(input);
            }
        }catch (IOException e) {
            System.err.println("Error de Entrada/Salida: " + e.getMessage());
        }
    }

    // Obtener valor como String
    public static String get(String key) {
        return props.getProperty(key);
    }

}
