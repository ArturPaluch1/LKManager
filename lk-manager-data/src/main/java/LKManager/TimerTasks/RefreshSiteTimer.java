package LKManager.TimerTasks;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Data
public class RefreshSiteTimer extends Timer {
   private TimerTask task;

    public RefreshSiteTimer() {

     this.task=  new TimerTask() {
            int i = 0;

            public void run() {
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


                    //	System.out.println("content= "+	urlConnection.getContent()+"\n");

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                            StandardCharsets.UTF_8));


                    System.out.println("refreshed site "+i+" times");
                } catch (IOException e) {
                    //	throw new RuntimeException(e);
                    System.out.println("refresh site failed ");
                }

			/*	Object c= null;
				try {
					c = urlConnection.getContent();
				} catch (IOException e) {
				//	throw new RuntimeException(e);
				}*/


            }
        };
    }


}
