package vv.config;

import java.util.Objects;
import java.util.Properties;

public class PropertyReaders {
    
    public static Integer readIntegerProperty(Properties p, String property) {
        return Integer.valueOf(readOrThrow(p, property));
    }

    public static Boolean readBooleanProperty(Properties p, String property) {
        return Boolean.valueOf(readOrThrow(p, property));
    }

    public static Double readDoubleProperty(Properties p, String property) {
        return Double.valueOf(readOrThrow(p, property));
    }

    private static String readOrThrow(Properties p, String property) {
        var rawProperty = p.getProperty(property);
        Objects.requireNonNull(rawProperty, "Property '%s' not found in properties".formatted(property));
        return rawProperty;
    }
}
