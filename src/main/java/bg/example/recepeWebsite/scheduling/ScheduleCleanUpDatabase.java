package bg.example.recepeWebsite.scheduling;

import bg.example.recepeWebsite.service.SecureTokenService;
import bg.example.recepeWebsite.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCleanUpDatabase {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ScheduleReport.class);
    private final UserService userService;
    private final SecureTokenService secureTokenService;

    public ScheduleCleanUpDatabase(UserService userService, SecureTokenService secureTokenService) {
        this.userService = userService;
        this.secureTokenService = secureTokenService;
    }

    @Scheduled(cron = "0 0/16 * * * ?" ) //On every 16 minutes
    private void generateDailyReport() {
        log.info("Start cleaning up database");
        secureTokenService.cleanUpSecureTokens();
        userService.cleanUpNotVerifiedUsers();
        log.info("End cleaning up database - database was cleaned up");
    }
}
