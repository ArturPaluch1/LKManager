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
                System.out.println("Update results task performed on: " + new Date()  + "\n" +"Thread's name: " + Thread.currentThread().getName());
resultsService.updateRoundResultsForDate(LocalDate.now());


            }
    };


    };
}
