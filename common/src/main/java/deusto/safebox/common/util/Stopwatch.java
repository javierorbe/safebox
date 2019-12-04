package deusto.safebox.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stopwatch {

    private static final Logger logger = LoggerFactory.getLogger(Stopwatch.class);

    private long start;

    public Stopwatch() {
        reset();
    }

    public void reset() {
        start = System.currentTimeMillis();
    }

    public long elapsedMillis()  {
        return System.currentTimeMillis() - start;
    }

    public double elapsedSeconds() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }

    public void print() {
        logger.debug(elapsedSeconds() + "s");
    }

    public void printAndReset() {
        print();
        reset();
    }
}
