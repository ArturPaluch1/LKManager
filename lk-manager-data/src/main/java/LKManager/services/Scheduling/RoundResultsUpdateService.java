package LKManager.services.Scheduling;

import LKManager.services.ResultsService;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@Service
public class RoundResultsUpdateService {

private final ResultsService resultsService;
    public RoundResultsUpdateService(ResultsService resultsService) {
        this.resultsService = resultsService;

    }

    @Scheduled(cron = "0 55 10,11,18,19,20,21,23 ? * *" , zone = "Europe/Warsaw")

    public void updateResults() {
        try {
            ZonedDateTime warsawTime = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);

            System.out.println("Update results task performed on: " + warsawTime.format(formatter) + "\n" + "Thread's name: " + Thread.currentThread().getName());
            // resultsService.updateRoundResultsForDate(LocalDate.now());
            //todo po testach zamienić na to wyżej
            resultsService.updateRoundResultsForDate(warsawTime.toLocalDate());


        } catch (Exception e) {
            System.err.println("Błąd podczas aktualizacji wyników: " + e.getMessage());
            e.printStackTrace();
            // Możesz dodać logikę retry, jeśli to konieczne
        }
    }




    }

