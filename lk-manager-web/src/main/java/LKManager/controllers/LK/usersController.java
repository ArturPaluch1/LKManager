package LKManager.controllers.LK;

import LKManager.DAO.UserDAOImpl;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
import LKManager.services.LKUserService;
import LKManager.services.MZUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class usersController {
private final LKUserService lkUserService;
private final MZUserService mzUserService;
@Autowired
private MZCache mzCache;

  private final UserDAOImpl userDAOImpl;



    @RequestMapping(value="/users")
public String showUsers(Model model)
{

    /*---------------z xmla---------------
   var gracze= lkUserService.wczytajGraczyZXML();
***************************************************/

    //próbowanie pobrania graczy z cache
    List<UserData>users= userDAOImpl.findAllUsersFromCache();
            if(users==null)users=userDAOImpl.findAll(false);
//var gracze= userDAOImpl.findAll(false);


    users= users.stream().sorted(
            (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
    ).collect(Collectors.toList());

  //  System.out.println("gracze(0)"+gracze.get(0));



   // this.userDAOImpl.save(gracze.get(1));





   model.addAttribute("users", users);
    return "LK/users";
}


@PostMapping(value="/addUser")
public String addUser(@RequestParam(value = "chosenUser" , required = false) String chosenUser, @RequestParam(value = "chosenUsers" , required = false) List<String>  chosenUsers)
{
    //sprawdzanie poprawnosci  wpisanego nicka z MZ
    try
    {
        //wpisany z input
        if(chosenUser!="")
        {
            //czy jest o takim id w mz
            var playerMZ= mzUserService.findByUsername(chosenUser);
      //    var gracze=lkUserService.wczytajGraczyZXML();
            List<UserData>users= userDAOImpl.findAllUsersFromCache();
            if(users==null)users=userDAOImpl.findAll(false);

            users= users.stream().sorted(
                    (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
            ).collect(Collectors.toList());

          if(users!=null)
          {
              if(!users.stream()
                      .filter(a->a.getUserId().equals(playerMZ.getUserId())
                      ).findFirst().isEmpty()
              )
              {
                  //todo taki gracz juz byl dodany
                  int i=0;
              }
              else
              {
             //     lkUserService.dodajGraczaDoXML(graczMZ);
                  this.userDAOImpl.save(playerMZ);

                mzCache.addUser(playerMZ);

              }
          }
          else
          {
      //        lkUserService.dodajGraczaDoXML(graczMZ);
              this.userDAOImpl.save(playerMZ);
              mzCache.addUser(playerMZ);
          }


        }
//z checkboxow
        //to chyba nigdy sie nie dzieje...
        if(chosenUsers!= null)
        {
            //czy jest o takim id w mz
           // var gracze=lkUserService.wczytajGraczyZXML();
            var users= userDAOImpl.findAll(false);

            for (var item: chosenUsers
            ) {
               var playerMZ= mzUserService.findByUsername(item);
                if(!users.stream()
                        .filter(a->a.getUserId().equals(playerMZ.getUserId())
                        ).findFirst().isEmpty()
                )
                {
                    //todo taki gracz juz byl dodany
                    int i=0;
                }
                else
                {
      //              lkUserService.dodajGraczaDoXML(gracz);
                    this.userDAOImpl.save(playerMZ);
                }
            }


        }






            return "redirect:/users";

//todo w razie pustego/zlego nicku i  inputa i checkboxa
    }
    catch (Exception e)
    {

        return "LK/temp";
    }

  //
  //  return null;
}


    @PostMapping(value="/deleteUser")
    public String deleteUser(@RequestParam(value = "chosenUser" , required = false) String chosenUser, @RequestParam(value = "chosenUsers" , required = false) List<String>  chosenUsers)
    {

        //sprawdzanie poprawnosci  wpisanego nicka z MZ
        try
        {
     //       var gracze=lkUserService.wczytajGraczyZXML();
            List<UserData>users= userDAOImpl.findAllUsersFromCache();
            if(users==null)users=userDAOImpl.findAll(false);
            //wpisany z input
            if(chosenUser!="")
            {
                //czy jest o takim id w mz
                var playerMZ= mzUserService.findByUsername(chosenUser);

                if(!users.stream()
                        .filter(a->a.getUserId().equals(playerMZ.getUserId())
                        ).findFirst().isEmpty()
                )
                {
                    this.userDAOImpl.delete(playerMZ);
                //    mzCache.setUsers( userDAOImpl.findAll(false) );
            //        lkUserService.usunGraczaZXML(graczMZ);
                }
                else
                {
                    //todo nie bylo takiego grajka
int i=0;
                }

            }
//z checkboxow
         else   if(chosenUsers!= null)
            {
                //czy jest o takim id w mz

                for (var item: chosenUsers
                ) {
                    var playerMZ= mzUserService.findByUsername(item);
                    if(!users.stream()
                            .filter(a->a.getUserId().equals(playerMZ.getUserId())
                            ).findFirst().isEmpty()
                    )
                    {
                //        lkUserService.usunGraczaZXML(gracz);

                        this.userDAOImpl.delete(playerMZ);

                    }
                    else
                    {
                        //todo nie bylo takiego grajka
                        // tutaj nie powinno sie wydarzyc, bo przy dodawaniu sprawdzane jest czy
                        // jest taki w mz, a tutaj usuwane są z checkboxów ustawianych z bazy

                    }
                }


            }



            mzCache.setUsers( userDAOImpl.findAll(false) );


            return "redirect:/users";

//todo w razie pustego i  inputa i checkboxa
        }
        catch (Exception e)
        {

            return "LK/temp";
        }

    }






}
