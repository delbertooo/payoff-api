package de.delbertooo.payoff.apiserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class Application {

    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone(ZoneId.of("Europe/Berlin"));

    public static void main(String[] args) {
        log.info("Setting the JVM timezone to {}.", DEFAULT_TIME_ZONE.getID());
        TimeZone.setDefault(DEFAULT_TIME_ZONE);
        SpringApplication.run(Application.class, args);
    }

}
