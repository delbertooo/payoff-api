package de.delbertooo.payoff.apiserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class LocaleConfiguration {

    @Bean
    public Locale locale(@Value("${app.locale}") String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }

}
