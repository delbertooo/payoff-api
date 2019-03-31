package de.delbertooo.payoff.apiserver.purchases.weeklyreport;

import de.delbertooo.payoff.apiserver.purchases.PurchaserBalance;
import de.delbertooo.payoff.apiserver.users.User;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class UserToWeeklyReportEmailTransformer {

    public UserToWeeklyReportEmailTransformer() {
    }

    public WeeklyReportService.Email toEmail(User user, Locale locale) {
        return new WeeklyReportService.Email()
                .setName(user.getName())
                .setAppUrl(null)
                .setSubject(LocalDate.now().format(DateTimeFormatter.ofPattern("'Your weekly report, week' w, yyyy")))
                .setTo(user.getEmail())
                .setNegativeBalances(user.streamNegativeBalances()
                        .sorted(PurchaserBalance.byAbsBalance().reversed())
                        .map(p -> toBalance(p.getParticipant(), -p.getBalance(), locale))
                        .collect(Collectors.toList()))
                .setPositiveBalances(user.streamPositiveBalances()
                        .sorted(PurchaserBalance.byAbsBalance().reversed())
                        .map(p -> toBalance(p.getParticipant(), p.getBalance(), locale))
                        .collect(Collectors.toList()))
                ;
    }

    private WeeklyReportService.Email.Balance toBalance(User user, double balance, Locale locale) {
        return new WeeklyReportService.Email.Balance()
                .setName(user.getName())
                .setFormattedPrice(NumberFormat.getCurrencyInstance(locale).format(balance))
                ;
    }

}
