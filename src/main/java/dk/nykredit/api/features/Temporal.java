package dk.nykredit.api.features;

import java.time.LocalDateTime;

/**
 * API feature temporal
 *   
 * @author LAJE
 */
public abstract class Temporal {

    public static class Interval extends Temporal {
        private LocalDateTime start;
        private LocalDateTime end;
    }

    public static class Period extends Temporal {
        private java.time.Period period;
    }

    public static final Temporal parse(String input) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
