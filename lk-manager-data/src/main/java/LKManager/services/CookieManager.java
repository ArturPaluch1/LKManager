package LKManager.services;

import LKManager.DAO.TerminarzDAO;
import LKManager.DAO.TerminarzDAOImpl;
import LKManager.LK.Terminarz;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;

@Service
public  class CookieManager {

    private TerminarzDAOImpl terminarzDAO;

@Autowired
    public CookieManager(TerminarzDAOImpl terminarzDAO) {
        this.terminarzDAO = terminarzDAO;
    }

  /*public  static String  checkCookies(HttpServletResponse response, HttpServletRequest request, String nrRundy, String wybranyTerminarz, TerminarzDAOImpl terminarzDAO) throws UnsupportedEncodingException {




         //check nrRundy
  //   nrRundy=  saveOrUpdateNumerRundyCookie(  nrRundy,wybranyTerminarz,request, response);

        //check terminarz
    CookieManager.saveOrUpdateChosenScheduleCookie(wybranyTerminarz, response, terminarzDAO);

        return nrRundy;
    }*/
    public static String saveOrUpdateNumerRundyCookie(String nrRundy, String wybranyTerminarz, HttpServletResponse response, HttpServletRequest request, TerminarzDAO terminarzDAO) throws UnsupportedEncodingException {
       //zmieniono terminarz, runda =1
       // Cookie   numerRundyCookie= null;
      //  Cookie     wybranyTerminarzCookie=null;
        Cookie   numerRundyCookie = getOrCreateRoundCookie(request);


        Cookie   wybranyTerminarzCookie=getOrCreateScheduleCookie(request, response, terminarzDAO);


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








       if(!decodeCookie(wybranyTerminarzCookie.getValue()).equals(wybranyTerminarz)
      && decodeCookie(wybranyTerminarzCookie.getValue()).equals(null)
       )
       {
          return "1";
       //    response.addCookie( utworzNowyCookie("numerRundy","1"));
       }
       else
       {
           if(nrRundy ==null) //nie podano nr rundy
           {

               if(numerRundyCookie==null)//nie ma pliku cookie
               {
                 utworzNowyCookie("numerRundy","1");
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

                   return numerRundyCookie.getValue();

               }


           }
           else //podano nr rundy
           {
               if(numerRundyCookie==null) //nie ma pliku cookie
               {
                  response.addCookie( utworzNowyCookie("numerRundy",nrRundy));
                  return nrRundy;
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
                   if(nrRundy!=numerRundyCookie.getValue())
                   {
                      response.addCookie( utworzNowyCookie("numerRundy",nrRundy));
                      return nrRundy;
      //           nrRundy=numerRundyCookie.getValue();
                  //     return nrRundy;
                /*       numerRundyCookie.setValue(nrRundy);
                       numerRundyCookie. setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
                       numerRundyCookie.setSecure(true);
                       numerRundyCookie.setHttpOnly(true);
                       response.addCookie(numerRundyCookie);*/

                   }
                   else return nrRundy;


               }

           }
       }

   //    return nrRundy;
   }

    private static Cookie getOrCreateScheduleCookie(HttpServletRequest request,  HttpServletResponse response, TerminarzDAO terminarzDAO) {
        Cookie wybranyTerminarzCookie= null;
        if (request.getCookies() == null) {
            try {
                wybranyTerminarzCookie = stworzCookieZOstatniegoTerminarza(response, terminarzDAO);

            } catch (Exception e) {
                //todo nie ma terminarzy
            }
            //       numerRundyCookie  = Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("numerRundy")).findAny().orElse(null);

        } else {
            wybranyTerminarzCookie = Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("wybranyTerminarz")).findAny().orElse(null);

        }

        return wybranyTerminarzCookie;
    }

    private static Cookie getOrCreateRoundCookie(HttpServletRequest request) {
        Cookie numerRundyCookie;
        if(request.getCookies()==null)
        {
            numerRundyCookie= utworzNowyCookie("numerRundy","1");

        }
        else
        {
            numerRundyCookie  = Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("numerRundy")).findAny().orElse(null);
        }
        return numerRundyCookie;
    }

    public static  String saveOrUpdateChosenScheduleCookie( String wybranyTerminarz, HttpServletResponse response, HttpServletRequest request, TerminarzDAO terminarzDAO) throws UnsupportedEncodingException {


        Cookie  wybranyTerminarzCookie = getOrCreateScheduleCookie(request, response, terminarzDAO);


        //   Cookie cookieTerminarz=null;
        //nie podano terminara
    //    Optional<Cookie> wybranyTerminarzCookieOptional  = Optional.ofNullable(Arrays.stream(request.getCookies()).filter(a -> a.getName().equals("wybranyTerminarz")).findAny().orElse(null));


        if(wybranyTerminarz ==null) {
            //jest cookie
            if(wybranyTerminarzCookie!=null)
            {
               //cookie nie ma podanego terminarza -> szukanie najnowszego
                if(wybranyTerminarzCookie.getValue().equals("null"))
                {
                 return  decodeCookie(stworzCookieZOstatniegoTerminarza(response, terminarzDAO).getValue());
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

                    return  decodeCookie(wybranyTerminarzCookie.getValue());
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
                return  decodeCookie(stworzCookieZOstatniegoTerminarza(response,  terminarzDAO).getValue());


            }





        }
        //został podany terminarz
        else
        {

String cookie=decodeCookie(wybranyTerminarz);
Cookie tempCookie= utworzNowyCookie("wybranyTerminarz",cookie);
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

    private static Cookie stworzCookieZOstatniegoTerminarza(HttpServletResponse response, TerminarzDAO terminarzDAO) throws UnsupportedEncodingException {
    //   String wybranyTerminarz=null;
        Terminarz terminarz = terminarzDAO.findLastById();

        Cookie tempCookie = null;
        if (terminarz != null) {
            tempCookie = utworzNowyCookie("wybranyTerminarz", terminarz.getName());
            response.addCookie(tempCookie);
            return    tempCookie;

        } else {
            return null;
//todo przeniesienie do tworzenia terminarza bo nie ma żadnego
        }
    }

    private static Cookie utworzNowyCookie(String nazwa, String wartosc){//HttpServletResponse response, Optional<Cookie> wybranyTerminarzCookieOptional) {
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

            Cookie tempCookie= new Cookie(nazwa,URLEncoder.encode(wartosc));
         //   tempCookie.setValue(URLEncoder.encode(nazwa);// = new Cookie("wybranyTerminarz", URLEncoder.encode(terminarz.getName(),"utf-8") );
            tempCookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
            tempCookie.setSecure(true);
            tempCookie.setHttpOnly(true);


            return tempCookie;


    }


    /**  */
public static String decodeCookie(String wybranyTerminarzCookie) throws UnsupportedEncodingException {
    return URLDecoder.decode(wybranyTerminarzCookie, "utf-8");
}
    private String encodeCookie(String wybranyTerminarzCookie) throws UnsupportedEncodingException {
     return    URLEncoder.encode(wybranyTerminarzCookie, "utf-8");

    }

}
