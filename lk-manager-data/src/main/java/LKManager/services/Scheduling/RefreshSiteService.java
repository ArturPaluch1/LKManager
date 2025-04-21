package LKManager.services.Scheduling;

import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@Service
public class RefreshSiteService  {

    private int i;
    public RefreshSiteService() {
        i=0;
    }

    @Scheduled(cron = "0 */12 * * * ?", zone = "Europe/Warsaw")// Co 12 minut
    public void RefreshSite() {


        ZonedDateTime warsawTime = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);

        System.out.println("Update site task performed on: " + warsawTime.format(formatter) + "\n" +
                        "Thread's name: " + Thread.currentThread().getName());
                if (i == 2147482999)
                    i = 0;
                i++;
                URL url = null;
                try {
                    url = new URL("https://lkm-fgim.onrender.com/");



                } catch (MalformedURLException e) {
                 //   throw new RuntimeException("Niepoprawny URL", e);
                    System.err.println("Błąd odświeżania strony: " + e.getMessage());

                } catch (IOException e) {
                   // throw new RuntimeException("Błąd połączenia", e);
                    System.err.println("Błąd odświeżania strony: " + e.getMessage());

                }

                URLConnection urlConnection = null;
                try {

                    urlConnection = url.openConnection();
                    urlConnection.connect();


                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                            StandardCharsets.UTF_8));


                    System.out.println("refreshed site "+i+" times");
                } catch (IOException e) {
                    System.out.println("refresh site failed ");
                }






    }


}
