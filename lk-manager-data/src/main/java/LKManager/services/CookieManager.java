package LKManager.services;

import LKManager.DAO_SQL.CustomScheduleDAOImpl;
import LKManager.DAO_SQL.ScheduleDAO;
import LKManager.model.Schedule;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
public  class CookieManager {

    private final CustomScheduleDAOImpl customScheduleDAO;
    private final ScheduleDAO scheduleDAO;
   private HttpServletResponse response;
   private HttpServletRequest request;


    public CookieManager(CustomScheduleDAOImpl customScheduleDAO, ScheduleDAO scheduleDAO, HttpServletResponse response, HttpServletRequest request) {
        this.customScheduleDAO = customScheduleDAO;
        this.scheduleDAO = scheduleDAO;
        this.response = response;
        this.request = request;
    }

    /*
        @Autowired
            public CookieManager(CustomScheduleDAOImpl customScheduleDAO) {
                this.customScheduleDAO = customScheduleDAO;
            }
        */
  /*public  static String  checkCookies(HttpServletResponse response, HttpServletRequest request, String nrRundy, String wybranyTerminarz, TerminarzDAOImpl terminarzDAO) throws UnsupportedEncodingException {




         //check nrRundy
  //   nrRundy=  saveOrUpdateNumerRundyCookie(  nrRundy,wybranyTerminarz,request, response);

        //check terminarz
    CookieManager.saveOrUpdateChosenScheduleCookie(wybranyTerminarz, response, terminarzDAO);

        return nrRundy;
    }*/
    public String saveOrUpdateRoundNumberCookie(String roundNumber, String chosenSchedule) throws UnsupportedEncodingException {
       //zmieniono terminarz, runda =1
       // Cookie   numerRundyCookie= null;
      //  Cookie     wybranyTerminarzCookie=null;
        Cookie   roundNumberCookie = getOrCreateRoundCookie(response, request);


        Cookie   chosenScheduleCookie= getChosenScheduleCookie(request, response, scheduleDAO);


  /*      if(request.getCookies()==null)
        {
            try{
                stworzCookieZOstatniegoTerminarza(response, terminarzDAO);
            }
            catch (Exception e)
            {
                //todo nie ma terminarzy
            }
            //       numerRundyCookie  = Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("numerRundy")).findAny().orElse(null);

        }*/
    /*    else
        {
            wybranyTerminarzCookie=  Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("wybranyTerminarz")).findAny().orElse(null);

        }*/






/*

       if( chosenScheduleCookie==null)
       {
          return "1";
       //    response.addCookie( utworzNowyCookie("numerRundy","1"));
       }
       else */

        //wybrano schedule
        if(chosenSchedule!=null)
       {
           //
           if(  !decodeCookie(chosenScheduleCookie.getValue()).equals(decodeCookie(chosenSchedule)))
           {
               return "1";
           }
           else //cookie i wybrany terminarz są takie same
           {
               //nie podano nr rundy
               if(roundNumber.isEmpty())
               {
            //nie ma pliku cookie
                   if(roundNumberCookie==null)
                   {
                       createNewCookie("roundNumber","1");
                       return  "1";
              /*     //tworzenie cookie
                   numerRundyCookie= new Cookie("numerRundy","1");
                   //   nrRundy="1";
                   numerRundyCookie.setValue("1");//= new Cookie("numerRundy", "1");
                   numerRundyCookie. setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                   numerRundyCookie.setSecure(true);
                   numerRundyCookie.setHttpOnly(true);
                   response.addCookie(numerRundyCookie);*/
                   }
                   else //jest plik cookie
                   {//nic nie robione

                       return roundNumberCookie.getValue();

                   }


               }
               else  //podano rundę
               {
                   if(roundNumberCookie==null) //nie ma pliku cookie
                   {
                       //tworzenie cookie z podaną rundą
                       response.addCookie( createNewCookie("roundNumber",roundNumber));
                       return roundNumber;
                       //  return nrRundy;
                /*   numerRundyCookie= new Cookie("numerRundy",nrRundy);
                   //   numerRundyCookie.setValue(nrRundy);
                   numerRundyCookie. setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                   numerRundyCookie.setSecure(true);
                   numerRundyCookie.setHttpOnly(true);
                   response.addCookie(numerRundyCookie);*/
                   }
                   else //jest plik cookie
                   {
                       //cookie i wybrany różnią się
                       if(roundNumber!=roundNumberCookie.getValue())
                       {
                           response.addCookie( createNewCookie("roundNumber",roundNumber));
                           return roundNumber;
                           //           nrRundy=numerRundyCookie.getValue();
                           //     return nrRundy;
                /*       numerRundyCookie.setValue(nrRundy);
                       numerRundyCookie. setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                       numerRundyCookie.setSecure(true);
                       numerRundyCookie.setHttpOnly(true);
                       response.addCookie(numerRundyCookie);*/

                       }
                       else return roundNumber;


                   }
               }

           }
        }
        //nie podano schedule
else
        {
            if( roundNumberCookie!=null)
            {

                //    response.addCookie( createNewCookie("roundNumber",roundNumberCookie.getValue()));
                return roundNumberCookie.getValue();
                //           nrRundy=numerRundyCookie.getValue();
                //     return nrRundy;
                /*       numerRundyCookie.setValue(nrRundy);
                       numerRundyCookie. setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                       numerRundyCookie.setSecure(true);
                       numerRundyCookie.setHttpOnly(true);
                       response.addCookie(numerRundyCookie);*/


            }
            else  return "1";

        }






   //    return nrRundy;
   }

    private static Cookie getChosenScheduleCookie(HttpServletRequest request, HttpServletResponse response, ScheduleDAO scheduleDAO) {
        Cookie chosenScheduleCookie= null;
        if (request.getCookies() == null) {
            try {
                chosenScheduleCookie = createCookieFromTheLastSchedule(response, scheduleDAO);

            } catch (Exception e) {
                //todo nie ma terminarzy
            }
            //       numerRundyCookie  = Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("numerRundy")).findAny().orElse(null);

        } else {


            chosenScheduleCookie = Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("chosenSchedule")).findAny().orElse(null);

        }

        return chosenScheduleCookie;
    }

    private static Cookie getOrCreateRoundCookie(HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie roundNumberCookie;


        if(request.getCookies()==null)
        {

            roundNumberCookie= createNewCookie("roundNumber","1");
            response.addCookie(roundNumberCookie);
        }
        else
        {

            roundNumberCookie  = Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("roundNumber")).findAny().orElse(null);
       if(roundNumberCookie==null)
       {

           roundNumberCookie= createNewCookie("roundNumber","1");
           response.addCookie(roundNumberCookie);
        //   response.addCookie(roundNumberCookie);
       }

        }
        return roundNumberCookie;
    }

    public String saveOrUpdateChosenScheduleCookie(String chosenSchedule) throws UnsupportedEncodingException {


        Cookie  chosenScheduleCookie = getChosenScheduleCookie(request, response, scheduleDAO);


        //   Cookie cookieTerminarz=null;
        //nie podano terminara
    //    Optional<Cookie> wybranyTerminarzCookieOptional  = Optional.ofNullable(Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("wybranyTerminarz")).findAny().orElse(null));


        if(chosenSchedule==null) {
            //jest cookie
            if(chosenScheduleCookie!=null)
            {
               //cookie nie ma podanego terminarza -> szukanie najnowszego
                if(chosenScheduleCookie.getValue().equals("null"))
                {
                 return  decodeCookie(createCookieFromTheLastSchedule(response, scheduleDAO).getValue());
                    //   Terminarz terminarz= null;

             //   Cookie utworzoneCookie=    utworzNowyCookie(response, wybranyTerminarzCookieOptional);


               /*     Session s = sessionFactory.openSession();
                    s.beginTransaction();


                    String hql =   " from Terminarz t where t.id=" +
                            "(select max(t.id) from Terminarz t)";

                    Query query = s.createQuery(hql);
                    Terminarz terminarz2= (Terminarz) query.getSingleResult();
                    s.getTransaction().commit();
                    s.close();
                    */

                }
                else
                {

                    return  decodeCookie(chosenScheduleCookie.getValue());
        //    wybranyTerminarz="ja i kyo";
                   // wybranyTerminarz=      wybranyTerminarz;
                    /*  ///to chyba niepotrzebne (tworzenie cookie, bo już był i nim jest podmieniony wybrany terminarz)

                    wybranyTerminarzCookieOptional.get().setValue(URLEncoder.encode(wybranyTerminarzCookieOptional.get().getValue()));// = new Cookie("wybranyTerminarz", URLEncoder.encode(wybranyTerminarzCookie,"utf-8") );
                    //      cookieTerminarz = new Cookie("wybranyTerminarz", URLEncoder.encode("null","utf-8") );
                    wybranyTerminarzCookieOptional.get().setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                    wybranyTerminarzCookieOptional.get().setSecure(true);
                    wybranyTerminarzCookieOptional.get().setHttpOnly(true);


                    response.addCookie(wybranyTerminarzCookieOptional.get());*/

                }
            }
            //nie ma cookie
            else
            {//tworzenie cookie
                return  decodeCookie(createCookieFromTheLastSchedule(response, scheduleDAO).getValue());


            }





        }
        //został podany terminarz
        else
        {

String cookie=decodeCookie(chosenSchedule);
Cookie tempCookie= createNewCookie("chosenSchedule",cookie);
    /*        wybranyTerminarzCookieOptional.get().setValue(cookie);// = new Cookie("wybranyTerminarz", cookie);

            wybranyTerminarzCookieOptional.get().setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
            wybranyTerminarzCookieOptional.get().setSecure(true);
            wybranyTerminarzCookieOptional.get().setHttpOnly(true);
            //add cookie to response
*/
            response.addCookie(tempCookie);
            return cookie;
        //    wybranyTerminarz=  wybranyTerminarz;
        }



    }

    private static Cookie createCookieFromTheLastSchedule(HttpServletResponse response, ScheduleDAO scheduleDAO) throws UnsupportedEncodingException {
    //   String wybranyTerminarz=null;
        Schedule schedule = scheduleDAO.findLastById();

        Cookie tempCookie = null;
        if (schedule != null) {
            tempCookie = createNewCookie("chosenSchedule", schedule.getName());
            response.addCookie(tempCookie);
            return    tempCookie;

        } else {
            return null;
//todo przeniesienie do tworzenia terminarza bo nie ma żadnego
        }
    }

    private static Cookie createNewCookie(String name, String value){//HttpServletResponse response, Optional<Cookie> wybranyTerminarzCookieOptional) {
   //     String wybranyTerminarz;
    /*    //bkpbkpbkpbkp
    Terminarz terminarz=      terminarzDAO.findLastById();
        if(terminarz!=null)
        {

            wybranyTerminarzCookieOptional.get().setValue(URLEncoder.encode(terminarz.getName()));// = new Cookie("wybranyTerminarz", URLEncoder.encode(terminarz.getName(),"utf-8") );
            wybranyTerminarzCookieOptional.get().setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
            wybranyTerminarzCookieOptional.get().setSecure(true);
            wybranyTerminarzCookieOptional.get().setHttpOnly(true);
            response.addCookie(wybranyTerminarzCookieOptional.get());

            return wybranyTerminarzCookieOptional.get();
        }
        else
        {
            return null;
            //todo przeniesienie do tworzenia terminarza bo nie ma żadnego
        }
        */

            Cookie tempCookie= new Cookie(name,URLEncoder.encode(value));
         //   tempCookie.setValue(URLEncoder.encode(nazwa);// = new Cookie("wybranyTerminarz", URLEncoder.encode(terminarz.getName(),"utf-8") );
            tempCookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
            tempCookie.setSecure(true);
            tempCookie.setHttpOnly(true);
            tempCookie.setPath("/");
        //    tempCookie.setDomain("http://localhost:8080/terminarz");


            return tempCookie;


    }


    /**  */
public static String decodeCookie(String chosenScheduleCookie) throws UnsupportedEncodingException {
    return URLDecoder.decode(chosenScheduleCookie, StandardCharsets.UTF_8);
}
    private String encodeCookie(String chosenScheduleCookie) throws UnsupportedEncodingException {
     return    URLEncoder.encode(chosenScheduleCookie, StandardCharsets.UTF_8);

    }

}
