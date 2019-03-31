package de.delbertooo.payoff.apiserver.purchases.weeklyreport;

import de.delbertooo.payoff.apiserver.common.SystemEmailService;
import de.delbertooo.payoff.apiserver.users.UserRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Service
public class WeeklyReportService {

    private UserRepository userRepository;
    private UserToWeeklyReportEmailTransformer userToWeeklyReportEmailTransformer;
    private JavaMailSender javaMailSender;
    private WeeklyReportEmailService weeklyReportEmailService;
    private SystemEmailService systemEmailService;
    private Locale locale;

    public WeeklyReportService(UserRepository userRepository,
                               UserToWeeklyReportEmailTransformer userToWeeklyReportEmailTransformer,
                               JavaMailSender javaMailSender,
                               WeeklyReportEmailService weeklyReportEmailService,
                               SystemEmailService systemEmailService) {
        this.userRepository = userRepository;
        this.userToWeeklyReportEmailTransformer = userToWeeklyReportEmailTransformer;
        this.javaMailSender = javaMailSender;
        this.weeklyReportEmailService = weeklyReportEmailService;
        this.systemEmailService = systemEmailService;
        this.locale = Locale.GERMANY;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Long> findUserIdsForReport() {
        return userRepository.findUserIdsForReport();
    }

    @Transactional
    public void send(Long forUser) throws MessagingException {
        val user = userRepository.requireById(forUser);
        val email = userToWeeklyReportEmailTransformer.toEmail(user, locale);
        send(email);
    }

    @Transactional
    public void send(Email email) throws MessagingException {
        val mimeMessage = javaMailSender.createMimeMessage();
        val message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        weeklyReportEmailService.addContent(message, email, locale);
        systemEmailService.addSystemEmailFrom(message);

        javaMailSender.send(mimeMessage);
    }


    @Getter
    @Setter
    public static class Email {
        private String to;
        private String subject;
        private String name;
        private String appUrl;
        private List<Balance> positiveBalances;
        private List<Balance> negativeBalances;

        Context toContext(Locale locale) {
            val ctx = new Context(locale);
            ctx.setVariable("name", getName());
            ctx.setVariable("appUrl", getAppUrl());
            ctx.setVariable("negativeBalances", getNegativeBalances());
            ctx.setVariable("positiveBalances", getPositiveBalances());
            return ctx;
        }

        @Getter
        @Setter
        public static class Balance {
            private String name;
            private String formattedPrice;
        }
    }
}
