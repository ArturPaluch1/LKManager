package LKManager.services;

import LKManager.DAO.CustomUserDAO;
import LKManager.DAO.UserDAO;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Cache.MZCache;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@AllArgsConstructor
public class UserServiceImpl implements UserService {
  private final   UserDAO userDAO;
   //private final UserService userService;
   private final MZUserService mzUserService;
    @Autowired
    MZCache mzCache;



    private UserData saveUser(UserData playerMZ) {
        //dwustronny dostęp: user->team, team->user
        playerMZ.getTeamlist().get(0).setUser(playerMZ);


        return  this.userDAO.save(playerMZ);
    }

    @Override
    public void deleteUser(String chosenUser, List<String> chosenUsers) throws JAXBException, IOException, ParserConfigurationException, SAXException {

/*
        //wpisany z input
        if(chosenUser!="")
        {
            List<UserData>users=userDAO.findUsers_NotDeletedWithoutPause();
           List<UserData>users= userDAO.findUsersFromCache_All();


            if(users==null)//users=userDAO.findAll(false);
                users=userDAO.findNotDeletedUsers();



            //czy jest o takim id w mz
            var playerMZ= mzUserService.findByUsername(chosenUser);

            if(!users.stream()
                    .filter(a->a.getUserId().equals(playerMZ.getUserId())
                    ).findFirst().isEmpty()
            )
            {
                //     this.userDAO.delete(playerMZ);
                ( (CustomUserDAO) userDAO).delete(playerMZ);
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
            List<UserData>users= userDAO.findUsersFromCache_All();
            if(users==null)//users=userDAO.findAll(false);
                users=userDAO.findNotDeletedUsers();
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

                    //   this.userDAO.delete(playerMZ);
                    ( (CustomUserDAO) userDAO).delete(playerMZ);
                }
                else
                {
                    int t=0;
                    //todo nie bylo takiego grajka
                    // tutaj nie powinno sie wydarzyc, bo przy dodawaniu sprawdzane jest czy
                    // jest taki w mz, a tutaj usuwane są z checkboxów ustawianych z bazy

                }
            }


        }
*/



            //wpisany z input
            if(chosenUser!="")
            {
                //todo to chyba nie jest konieczne, bo skoro został dodany sprawdzając czy jest w mz, to usuwając też powinien być
                var playerMZ= mzUserService.findByUsername(chosenUser);
                if(playerMZ==null)
                {
                    //todo nie ma takiego usera w mzcie
                }
                else
                {
                    List<UserData>users=userDAO.findUsers_NotDeletedWithoutPause();



                    if(!users.stream()
                            .filter(a->a.getUserId().equals(playerMZ.getUserId())
                            ).findFirst().isEmpty()
                    )
                    {
                        //customdao nie usuwa tylko ustawia deleted na true
                        ( (CustomUserDAO) userDAO).delete(playerMZ);

                    }
                    else
                    {
                        //todo nie bylo takiego grajka
                        int i=0;
                    }
                    mzCache.setUsers( userDAO.findNotDeletedUsers() );
                }


            }
//z checkboxow
            else   if(chosenUsers!= null)
            {
                List<UserData>users=userDAO.findUsers_NotDeletedWithoutPause();

                for (var item: chosenUsers
                ) {

               UserData userInDb=     users.stream()
                            .filter(a->a.getUsername().trim().toLowerCase().equals(item.trim().toLowerCase())
                            ).findFirst().get();
                  //todo sprawdzić tego ifa
                    if(userInDb!=null
                    )
                    {
                        ( (CustomUserDAO) userDAO).delete(userInDb);
                    }
                    else
                    {
                        int t=0;
                        //todo nie bylo takiego grajka
                        // tutaj nie powinno sie wydarzyc, bo przy dodawaniu sprawdzane jest czy
                        // jest taki w mz, a tutaj usuwane są z checkboxów ustawianych z bazy

                    }
                }

                mzCache.setUsers( userDAO.findNotDeletedUsers() );
            }










    }

    @Override
    public List<UserData> findAllUsersFromCache() {



        return mzCache.getUsers();
    }

    @Override
    public List<UserData> findUsers_NotDeletedWithoutPause() {
        //próbowanie z cache
List<UserData> users=userDAO.findUsersFromCache_NotDeletedWithoutPause();
if(users.size()!=0)
{
    return users;
}
else
{
  return userDAO.findUsers_NotDeletedWithoutPause();

}

    }

    @Override
    public List<UserData> findUsersFromCache_NotDeletedWithPause() {
        return null;
    }


    @Override
    public UserData AddUser(String userToAdd) throws JAXBException, IOException, ParserConfigurationException, SAXException {
        //czy jest o takim id w mz
        var playerMZ= mzUserService.findByUsername(userToAdd);
        //    var gracze=lkUserService.wczytajGraczyZXML();

        if(playerMZ==null)
        {
            //todo return że nie ma takiego usera w MZ
            System.out.println("nie ma takiego usera w mz");
            return null;
        }
        else
        {
            if(playerMZ.getTeamlist().get(0).getTeamName().equals(""))
            {
                System.out.println("nie ma takiej druzyny w mz");
                return null;
            }


            List<UserData>users= userDAO.findUsersFromCache_All();
            if(users==null)
                //  users=userDAO.findAll(false);
                users=userDAO.findNotDeletedUsers();

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
                 //   playerMZ.getTeamlist().get(0).setUser(playerMZ);
                    //       this.userDAO.save(playerMZ);
              var  savedUser=    this.saveUser(playerMZ);
                    mzCache.addUser(playerMZ);
return savedUser;
                }
            }
            else
            {
                //        lkUserService.dodajGraczaDoXML(graczMZ);
                // this.userDAO.save(playerMZ);
                var  savedUser=   this.saveUser(playerMZ);
                mzCache.addUser(playerMZ);
                return savedUser;
            }

            return null;
        }



    }

    @Override
    public List<UserData> AddUsers(List<String> usersToAdd) {
        // todo to sie chyba nigdy nie dzieje bo jak dodać kilku na raz??




        //czy jest o takim id w mz
        // var gracze=lkUserService.wczytajGraczyZXML();
        //          var users= userDAO.findAll(false);

        var users =userDAO.findNotDeletedUsers();
        for (var item: usersToAdd
        ) {
            UserData playerMZ= null;
            try {

                playerMZ = mzUserService.findByUsername(item);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
            UserData finalPlayerMZ = playerMZ;
            if(!users.stream()
                    .filter(a->a.getUserId().equals(finalPlayerMZ.getUserId())
                    ).findFirst().isEmpty()
            )
            {
                //todo taki gracz juz byl dodany
                int i=0;
            }
            else
            {
                //              lkUserService.dodajGraczaDoXML(gracz);

                //  this.userDAO.save(playerMZ);
                playerMZ.getTeamlist().get(0).setUser(playerMZ);
                this.saveUser(playerMZ);


            }
        }

        return null;
    }


}
