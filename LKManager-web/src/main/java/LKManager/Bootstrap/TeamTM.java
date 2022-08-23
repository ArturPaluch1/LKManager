package LKManager.Bootstrap;

import LKManager.model.UserMZ.Team;
import LKManager.model.UserMZ.UserData;
import LKManager.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public class TeamTM {

    private final UserService userService;
    private List<UserData> skladTM;

    public TeamTM(UserService userService) {
        this.userService = userService;
        skladTM = new ArrayList<>();

    }

    public List<UserData> LoadTMRzeszow() {
        UserData yarek = new UserData();
        yarek.setUsername("yarek");
        yarek.setTeamlist(new Team());
        yarek.getTeamlist().get(0).setTeamName("Przemysl");

        UserData kingsajz = new UserData();
        kingsajz.setUsername("kingsajz");
        kingsajz.setTeamlist(new Team());
        kingsajz.getTeamlist().get(0).setTeamName("Wisła Kraków Old Boys");
        // kingsajz.setTeam("Wisła Kraków Old Boys");

        UserData jerzykw = new UserData();
        jerzykw.setUsername("jerzykw");
        jerzykw.setTeamlist(new Team());
        jerzykw.getTeamlist().get(0).setTeamName("FC Harnaś Sanok");
        //  jerzykw.setTeam("FC Harnaś Sanok");

        UserData marc0888 = new UserData();
        marc0888.setUsername("marc0888");
        marc0888.setTeamlist(new Team());
        marc0888.getTeamlist().get(0).setTeamName("SMOCZANKA MIELEC");
        //  marc0888.setTeam("SMOCZANKA MIELEC");

        skladTM.add(yarek);
        skladTM.add(kingsajz);
        skladTM.add(jerzykw);
        skladTM.add(marc0888);
        return  skladTM;
    }

    public void addTeam(String username, String teamName)
    {
        UserData tempUser = new UserData();
        tempUser.setUsername(username);
        tempUser.setTeamlist(new Team());
        tempUser.getTeamlist().get(0).setTeamName(teamName);


        skladTM.add(tempUser);
    }



}
