package de.delbertooo.payoff.apiserver.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class SystemEmailService {
    private String systemMailFrom;

    @Autowired
    public SystemEmailService(@Value("${app.system-email.from}") String systemEmailFrom) {
        this.systemMailFrom = systemEmailFrom;
    }

    public void addSystemEmailFrom(MimeMessageHelper message) {
        try {
            message.setFrom(systemMailFrom);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
