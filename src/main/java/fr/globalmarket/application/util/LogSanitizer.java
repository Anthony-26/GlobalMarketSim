package fr.globalmarket.application.util;

public class LogSanitizer {

    private LogSanitizer() {
    }

    public static String santize(String input) {
        return input == null ?
                null :
                input.replace("\n", "_").replace("\r", "_").replace("\t", "_");
    }

}
