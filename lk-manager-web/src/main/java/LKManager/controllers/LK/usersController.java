package LKManager.controllers.LK;

//import LKManager.DAO.Exceptions.GetUsersUserDatabaseAccessFailureException;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
//private FailedDatabaseOperationRepository failedDatabaseOperationRepository;


    @RequestMapping(value="/users")
public String showUsers(Model model, RedirectAttributes attributes )
{

    /*---------------z xmla---------------
   var gracze= lkUserService.wczytajGraczyZXML();
***************************************************/
    List<UserData>users=null;
try{
    users=userService.findUsers_NotDeletedWithoutPause();
}
catch (Exception e)
{
  //  failedDatabaseOperationRepository. addFailedOperation(new GetUsersFailedDatabaseOperation(SQLOperation.GetUsersDatabaseAccessFailureException));
/*e.handleDataAccessResourceFailureException();
e.redirectToErrorPage(attributes,"Błąd dostępu do bazy danych - Users");*/
}



    users= users.stream().sorted(
            (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
    ).collect(Collectors.toList());
   model.addAttribute("users", users);
    return "LK/users";
}


@PostMapping(value="/addUser")
public String addUser(@RequestParam(value = "chosenUser" , required = false) String chosenUser, @RequestParam(value = "chosenUsers" , required = false) List<String>  chosenUsers) throws JAXBException, IOException, ParserConfigurationException, SAXException{//, GetUsersUserDatabaseAccessFailureException {
    //sprawdzanie poprawnosci  wpisanego nicka z MZ

        //jeśli są podane i checkboksy i w inpucie to usuwa tylko checkboxy
//z checkboxow
        //to chyba nigdy sie nie dzieje...
 /*       if(chosenUsers!= null)
        {
            try {
                userService.AddUsers(chosenUsers);
            } catch (AddUserUserDatabaseAccessFailureException e) {

            }

        }
        //wpisany z input
      else
          */
          if(chosenUser!="")
        {
            try {
                userService.AddUser(chosenUser);
            } catch (Exception e) {
//failedDatabaseOperationRepository.addFailedOperation(new AddUserFailedDatabaseOperation(SQLOperation.AddUserDatabaseAccessFailureException,chosenUser));
            }
        }
else
        {
            //todo w razie pustego/zlego nicku i  inputa i checkboxa
        }

            return "redirect:/users";



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
//failedDatabaseOperationRepository.addFailedOperation(new DeleteUserFailedDatabaseOperation(SQLOperation.DeleteUserDatabaseAccessFailureException, chosenUser,chosenUsers));
            return "LK/temp";
        }

    }






}
