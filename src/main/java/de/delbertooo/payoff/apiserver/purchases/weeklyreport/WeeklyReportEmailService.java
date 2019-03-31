package de.delbertooo.payoff.apiserver.purchases.weeklyreport;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.util.Locale;

@Service
public class WeeklyReportEmailService {
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    public WeeklyReportEmailService(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    public void addContent(MimeMessageHelper message, WeeklyReportService.Email email, Locale locale) throws MessagingException {
        val ctx = email.toContext(locale);
        val textContent = springTemplateEngine.process("weekly-report.txt", ctx);
        if (message.isMultipart()) {
            val htmlContent = springTemplateEngine.process("weekly-report.html", ctx);
            message.setText(textContent, htmlContent);
        } else {
            message.setText(textContent);
        }
        message.setSubject(email.getSubject());
        message.setTo(email.getTo());
    }

}
