package com.futureprocessing.wsiln.tools;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeCounter {

    private final Logger log = LoggerFactory.getLogger(TimeCounter.class);

    private String name;
    private DateTime start;
    private DateTime end;

    public TimeCounter(String name) {
        this.name = name;
    }

    public TimeCounter start() {
        start = new DateTime();
        return this;
    }

    public void end() {
        end = new DateTime();
        Duration duration = new Duration(start, end);

        log.info("Timer " + name + " took: " + duration.getMillis() + " milis");
    }

}
