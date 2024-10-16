package LKManager.user;

//import LKManager.DAO.Exceptions.GetUsersUserDatabaseAccessFailureException;

import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.account.SignUpForm;
import LKManager.services.LKUserService;
import LKManager.services.MZUserService;
import LKManager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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


    @GetMapping(value="/admin/LK/users")
public String showUsers(Model model, RedirectAttributes attributes )
{


    /*---------------z xmla---------------
   var gracze= lkUserService.wczytajGraczyZXML();
***************************************************/
    List<UserDataDTO>users=null;

    users=userService.findAllMZUsers(true,false);

   // users=userService.findUsers_NotDeletedWithoutPause();



if(users!=null){
    users= users.stream().sorted(
            (o1,o2)->o1.getUsername().compareToIgnoreCase(o2.getUsername())
    ).collect(Collectors.toList());
}

   model.addAttribute("users", users);
    return "admin/LK/users";
}


@PostMapping(value="/admin/LK/users/addUser")
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
    System.out.println("dddd");
          if(chosenUser!="")
        {
            try {

            UserDataDTO user=    userService.addUser( new SignUpForm(chosenUser,null,null));

            } catch (Exception e) {
//failedDatabaseOperationRepository.addFailedOperation(new AddUserFailedDatabaseOperation(SQLOperation.AddUserDatabaseAccessFailureException,chosenUser));
                }
        }
else
        {
            //todo w razie pustego/zlego nicku i  inputa i checkboxa
        }

            return "redirect:/admin/LK/users";



}


    @PostMapping(value="/admin/LK/users/deleteUser")
    public String deleteUser(@RequestParam(value = "chosenUser" , required = false) String chosenUser, @RequestParam(value = "chosenUsers" , required = false) List<String>  chosenUsers ,RedirectAttributes attributes)
    {


        //sprawdzanie poprawnosci  wpisanego nicka z MZ
        try
        {
     //       var gracze=lkUserService.wczytajGraczyZXML();

//updatuje też cache
     userService.deactivateUser(chosenUser,chosenUsers);




            return "redirect:/admin/LK/users";

//todo w razie pustego i  inputa i checkboxa
        }
        catch (Exception e)
        {
//failedDatabaseOperationRepository.addFailedOperation(new DeleteUserFailedDatabaseOperation(SQLOperation.DeleteUserDatabaseAccessFailureException, chosenUser,chosenUsers));

            attributes.addAttribute("errorMessage", "błąd w usuwaniu");
            return "redirect:/errorMessage";
        }

    }






}
