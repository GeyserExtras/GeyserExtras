package dev.letsgoaway.geyserextras.core.utils;

public class StringUtils {
    /**
     * escape()
     * Escape a give String to make it safe to be printed or stored.
     *
     * @param s The input String.
     * @return The output String.
     **/
    // thank you https://stackoverflow.com/questions/2406121/how-do-i-escape-a-string-in-java
    public static String escape(String s){
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\'", "\\'")      // <== not necessary
                .replace("\"", "\\\"");
    }
}
