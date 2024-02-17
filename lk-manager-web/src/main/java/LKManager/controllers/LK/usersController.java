package LKManager.controllers.LK;

import LKManager.model.UserMZ.UserData;
import LKManager.services.LKUserService;
import LKManager.services.MZUserService;
import LKManager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
//@AllArgsConstructor
public class usersController {
private final LKUserService lkUserService;
private final MZUserService mzUserService;
//@Autowired
//private MZCache mzCache;

//  private final UserDAO userDAOImpl;
//private final UserDAO userDAO;
private final UserService userService;



    @RequestMapping(value="/users")
public String showUsers(Model model)
{
/*
UserData tempuser=userDAO.findById(0).get();
    Team tempTeam= new Team();
    tempTeam.setTeamName(" ");
    tempTeam.setTeamId(0);
    tempTeam.setUser(tempuser);
    List<Team> tempTeams= new ArrayList<>();
    tempTeams.add(tempTeam);
    tempuser.setTeamlist(tempTeams);
    userDAO.save(tempuser);

*/
    /*---------------z xmla---------------
   var gracze= lkUserService.wczytajGraczyZXML();
***************************************************/

   // List<UserData>users=userDAO.findUsers_NotDeletedWithoutPause();
    List<UserData>users=userService.findUsers_NotDeletedWithoutPause();

    //próbowanie pobrania graczy z cache
 /*   List<UserData>users= mzCache.getUsers();//userDAO.findAllUsersFromCache();
           if(users.size()==0)
           {
               users=userDAO.findNotDeletedUsers();
               mzCache.setUsers(users);
           }
*/
   // List<UserData>users  =userDAO.findAll();




  //  List<UserData>users=userDAO.findAll(false);



    users= users.stream().sorted(
            (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
    ).collect(Collectors.toList());

  //  System.out.println("gracze(0)"+gracze.get(0));



   // this.userDAOImpl.save(gracze.get(1));





   model.addAttribute("users", users);
    return "LK/users";
}


@PostMapping(value="/addUser")
public String addUser(@RequestParam(value = "chosenUser" , required = false) String chosenUser, @RequestParam(value = "chosenUsers" , required = false) List<String>  chosenUsers) throws JAXBException, IOException, ParserConfigurationException, SAXException {
    //sprawdzanie poprawnosci  wpisanego nicka z MZ
 /*   try
    {*/


        //jeśli są podane i checkboksy i w inpucie to usuwa tylko checkboxy
//z checkboxow
        //to chyba nigdy sie nie dzieje...
        if(chosenUsers!= null)
        {
            userService.AddUsers(chosenUsers);



        }


        //wpisany z input
      else  if(chosenUser!="")
        {
            userService.AddUser(chosenUser);




        }
else
        {
            //todo w razie pustego/zlego nicku i  inputa i checkboxa
        }


            return "redirect:/users";


 //   }
   /* catch (Exception e)
    {

        return "LK/temp";
    }*/

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

//updatuje też cache
     userService.deleteUser(chosenUser,chosenUsers);




            return "redirect:/users";

//todo w razie pustego i  inputa i checkboxa
        }
        catch (Exception e)
        {

            return "LK/temp";
        }

    }






}
