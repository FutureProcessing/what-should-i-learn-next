package com.futureprocessing.wsiln.mapreduce;

public class InputFormatter {
    private static final String REMOVE_REGEX = "<p>|</p>|&lt;|&gt;|&#xA;|/p|[ ;\"'\\/.,_=&<>]";
    private static final String SPLIT_REGEX = "\\s+";


    public static String[] splitInputString(String value) {

        if (value == null) {
            return null;
        }
        String formattedString = value.replaceAll(REMOVE_REGEX, " ");
        formattedString = formattedString.trim().toLowerCase();
        return formattedString.split(SPLIT_REGEX);
    }
}
