package LKManager.controllers.LK;

import LKManager.services.LKUserService;
import LKManager.services.MZUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class graczeController {
private final LKUserService lkUserService;
private final MZUserService mzUserService;
    public graczeController(LKUserService lkUserService, MZUserService mzUserService) {
        this.lkUserService = lkUserService;
        this.mzUserService = mzUserService;
    }

    @RequestMapping(value="/gracze")
public String wyswietlGraczy(Model model)
{
   var gracze= lkUserService.wczytajGraczyZXML();

    gracze= gracze.stream().sorted(
            (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
    ).collect(Collectors.toList());

   model.addAttribute("gracze", gracze);
    return "LK/gracze";
}

@PostMapping(value="/dodajGracza")
public String dodajGracza(@RequestParam(value = "wybranyGracz" , required = false) String wybranyGracz, @RequestParam(value = "wybraniGracze" , required = false) List<String>  wybraniGracze)
{
    //sprawdzanie poprawnosci  wpisanego nicka z MZ
    try
    {
        //wpisany z input
        if(wybranyGracz!="")
        {
            //czy jest o takim id w mz
            var graczMZ= mzUserService.findByUsername(wybranyGracz);
          var gracze=lkUserService.wczytajGraczyZXML();
            gracze= gracze.stream().sorted(
                    (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
            ).collect(Collectors.toList());

          if(gracze!=null)
          {
              if(!gracze.stream()
                      .filter(a->a.getUserId().equals(graczMZ.getUserId())
                      ).findFirst().isEmpty()
              )
              {
                  //todo taki gracz juz byl dodany
                  int i=0;
              }
              else
              {
                  lkUserService.dodajGraczaDoXML(graczMZ);
              }
          }
          else
          {
              lkUserService.dodajGraczaDoXML(graczMZ);
          }


        }
//z checkboxow
        if(wybraniGracze!= null)
        {
            //czy jest o takim id w mz
            var gracze=lkUserService.wczytajGraczyZXML();
            for (var item: wybraniGracze
            ) {
               var gracz= mzUserService.findByUsername(item);
                if(!gracze.stream()
                        .filter(a->a.getUserId().equals(gracz.getUserId())
                        ).findFirst().isEmpty()
                )
                {
                    //todo taki gracz juz byl dodany
                    int i=0;
                }
                else
                {
                    lkUserService.dodajGraczaDoXML(gracz);
                }
            }


        }






            return "redirect:/gracze";

//todo w razie pustego/zlego nicku i  inputa i checkboxa
    }
    catch (Exception e)
    {

        return "LK/temp";
    }

  //
  //  return null;
}


    @PostMapping(value="/usunGracza")
    public String usunGracza(@RequestParam(value = "wybranyGracz" , required = false) String wybranyGracz, @RequestParam(value = "wybraniGracze" , required = false) List<String>  wybraniGracze)
    {

        //sprawdzanie poprawnosci  wpisanego nicka z MZ
        try
        {
            var gracze=lkUserService.wczytajGraczyZXML();
            //wpisany z input
            if(wybranyGracz!="")
            {
                //czy jest o takim id w mz
                var graczMZ= mzUserService.findByUsername(wybranyGracz);

                if(!gracze.stream()
                        .filter(a->a.getUserId().equals(graczMZ.getUserId())
                        ).findFirst().isEmpty()
                )
                {
                    lkUserService.usunGraczaZXML(graczMZ);
                }
                else
                {
                    //todo nie bylo takiego grajka

                }

            }
//z checkboxow
            if(wybraniGracze!= null)
            {
                //czy jest o takim id w mz

                for (var item: wybraniGracze
                ) {
                    var gracz= mzUserService.findByUsername(item);
                    if(!gracze.stream()
                            .filter(a->a.getUserId().equals(gracz.getUserId())
                            ).findFirst().isEmpty()
                    )
                    {
                        lkUserService.usunGraczaZXML(gracz);


                    }
                    else
                    {
                        //todo nie bylo takiego grajka
                        // tutaj nie powinno sie wydarzyc, bo przy dodawaniu sprawdzane jest czy
                        // jest taki w mz, a tutaj usuwane są z checkboxów ustawianych z bazy

                    }
                }


            }






            return "redirect:/gracze";

//todo w razie pustego i  inputa i checkboxa
        }
        catch (Exception e)
        {

            return "LK/temp";
        }

    }






}
