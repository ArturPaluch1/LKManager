package LKManager.TimerTasks;

import LKManager.services.ResultsService;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Data
public class RoundResultsUpdateTimer extends Timer {
    private TimerTask task;
private final ResultsService resultsService;
    public RoundResultsUpdateTimer(ResultsService resultsService) {
        this.resultsService = resultsService;
        this.task = new TimerTask() {

            @Override
            public void run() {
                try {
                    System.out.println("Update results task performed on: " + new Date() + "\n" + "Thread's name: " + Thread.currentThread().getName());
                    // resultsService.updateRoundResultsForDate(LocalDate.now());
                    //todo po testach zamienić na to wyżej
                    resultsService.updateRoundResultsForDate(LocalDate.now());

//.plusDays(1)
                }
                catch (Exception e)
                {
                    System.err.println("Błąd podczas aktualizacji wyników: " + e.getMessage());
                    e.printStackTrace();

                    scheduleRetry();
                }
            }
            private void scheduleRetry() {
                // Ponowne uruchomienie zadania po RETRY_DELAY
                RoundResultsUpdateTimer.this.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        RoundResultsUpdateTimer.this.task.run();
                    }
                }, 5000);
            }
    };


    };
}
