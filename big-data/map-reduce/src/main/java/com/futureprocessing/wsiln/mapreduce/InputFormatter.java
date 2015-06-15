package com.futureprocessing.wsiln.mapreduce;

public class InputFormatter {
    private static final String UNWANTED_CHARACTERS = "<p>|</p>|&lt;|&gt;|&#xA;|/p|[ ;\"'\\/.,_=&<>]";
    private static final String WHITE_CHARACTERS = "\\s+";


    public static String[] splitInputString(String value) {

        if (value == null) {
            return null;
        }
        String formattedString = value.replaceAll(UNWANTED_CHARACTERS, " ");
        formattedString = formattedString.trim().toLowerCase();
        return formattedString.split(WHITE_CHARACTERS);
    }
}
