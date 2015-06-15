package com.futureprocessing.wsiln.mapreduce;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TechnologiesFormatter {
    static Logger log = Logger.getLogger(TechnologiesFormatter.class);

    private static final String VERSION_PATTERN = "-\\d";
    private static final Pattern pattern = Pattern.compile(VERSION_PATTERN);

    public static String removeVersionFromName(String text) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            log.debug("Removed " + text.substring(matcher.start()) + " from " + text);
            return text.substring(0, matcher.start());
        }
        return text;
    }


    public static List removeVersion(String[] text) {
      List result = new ArrayList();
        for(String word: text){
            result.add(removeVersionFromName(word));
        }
        return result;
    }
}
