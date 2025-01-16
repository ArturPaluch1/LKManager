package LKManager.services.Scheduling;

import LKManager.services.ResultsService;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Data
@Service
public class RoundResultsUpdateService {

private final ResultsService resultsService;
    public RoundResultsUpdateService(ResultsService resultsService) {
        this.resultsService = resultsService;

    }

    @Scheduled(cron = "0 55 10,11,18,19,20,21,23 ? * *" )

    public void updateResults() {
        try {
            System.out.println("Update results task performed on: " + new Date() + "\n" + "Thread's name: " + Thread.currentThread().getName());
            // resultsService.updateRoundResultsForDate(LocalDate.now());
            //todo po testach zamienić na to wyżej
            resultsService.updateRoundResultsForDate(LocalDate.now());


        } catch (Exception e) {
            System.err.println("Błąd podczas aktualizacji wyników: " + e.getMessage());
            e.printStackTrace();
            // Możesz dodać logikę retry, jeśli to konieczne
        }
    }




    }

