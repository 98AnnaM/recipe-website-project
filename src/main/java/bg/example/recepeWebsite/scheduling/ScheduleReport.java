package bg.example.recepeWebsite.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleReport {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ScheduleReport.class);
    private final ReportService reportService;

    public ScheduleReport(ReportService reportService) {
        this.reportService = reportService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void generateDailyReport() {
        log.info("Start creating report");
        reportService.generateDailyReport();
        log.info("End report - report was sent");
    }
}
