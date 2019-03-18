package de.delbertooo.payoff.apiserver.common;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class GlobalClock {

    private static final Object lock = new Object();
    private static volatile Clock frozen = null;

    public static Clock current() {
        synchronized (lock) {
            return frozen != null ? frozen : Clock.systemDefaultZone();
        }
    }

    public static void freeze(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        freeze(dateTime.atZone(zoneId).toInstant(), zoneId);
    }

    public static void freeze(Instant instant, ZoneId zoneId) {
        synchronized (lock) {
            frozen = Clock.fixed(instant, zoneId);
        }
    }

    public static void unfreeze() {
        synchronized (lock) {
            frozen = null;
        }
    }
}
