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
import java.util.Date;

@Data
@Service
public class RefreshSiteService  {

    private int i;
    public RefreshSiteService() {
        i=0;
    }

    @Scheduled(cron = "0 */12 * * * ?")// Co 12 minut
    public void RefreshSite() {



                System.out.println("Update site task performed on: " + new Date() + "\n" +
                        "Thread's name: " + Thread.currentThread().getName());
                if (i == 2147482999)
                    i = 0;
                i++;
                URL url = null;
                try {
                    url = new URL("https://lkm-fgim.onrender.com/");



                } catch (MalformedURLException e) {
                    //	throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
