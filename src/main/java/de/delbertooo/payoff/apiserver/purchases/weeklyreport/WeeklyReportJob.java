package de.delbertooo.payoff.apiserver.purchases.weeklyreport;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Slf4j
@Component
public class WeeklyReportJob {

    private WeeklyReportService weeklyReportService;

    @Autowired
    public WeeklyReportJob(WeeklyReportService weeklyReportService) {
        this.weeklyReportService = weeklyReportService;
    }

    @Scheduled(cron = "${app.weekly-report-cron}")
    public void sendEmails() {
        val userIds = weeklyReportService.findUserIdsForReport();
        log.debug("Sending weekly report to {} users.", userIds.size());
        userIds.forEach(this::sendEmail);
        log.debug("Done sending weekly report.");
    }

    private void sendEmail(Long userId) {
        try {
            weeklyReportService.send(userId);
        } catch (MessagingException e) {
            log.warn("Error sending weekly report.", e);
        }
    }
}
