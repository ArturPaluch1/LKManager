package LKManager.controllers.LK;

import LKManager.services.MatchService;
import LKManager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping({"tabela.html", "tabela", "tabela"} )
public class tabelaController {
    private final UserService userService;
    private final MatchService matchService;
  //  private List<UserData> skladTM = new ArrayList<>();

    public tabelaController(UserService userService, MatchService matchService) {
        this.userService = userService;
        this.matchService = matchService;
    }


   @GetMapping({"tabela.html", "tabela", "tabela"} )
    public String index()  {




        return "LK/tabela";
    }

}
