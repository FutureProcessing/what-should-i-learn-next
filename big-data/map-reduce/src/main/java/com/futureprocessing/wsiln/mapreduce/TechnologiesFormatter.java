package com.futureprocessing.wsiln.mapreduce;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TechnologiesFormatter {
    static Logger log = Logger.getLogger(TechnologiesFormatter.class);

    static String VERSION_PATTERN = "-\\d";

    public static String removeVersionFromName(String text) {

        Pattern pattern = Pattern.compile(VERSION_PATTERN);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            log.info("Removed " + text.substring(matcher.start()) + " from " + text);
            return text.substring(0, matcher.start());
        }
        return text;
    }
}
