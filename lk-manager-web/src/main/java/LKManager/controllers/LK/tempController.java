package LKManager.controllers.LK;

import LKManager.LK.Runda;
import LKManager.LK.Terminarz;
import LKManager.model.MatchesMz.Match;
import LKManager.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class tempController {
    private final LKManager.services.MZUserService MZUserService;
    private final MatchService matchService;
    private final TerminarzService terminarzService;
   // private Integer numerRundy;
    private Terminarz terminarz;
    private final PlikiService plikiService;
    private String poprzednioWybranyTerminarz;
    private  final WynikiService wynikiService;
   private File[] terminarze;
    public tempController(LKManager.services.MZUserService mzUserService, MatchService matchService, TerminarzService terminarzService, PlikiService plikiService, WynikiService wynikiService) {
        MZUserService = mzUserService;
        this.matchService = matchService;
        this.terminarzService = terminarzService;
        this.plikiService = plikiService;
        this.wynikiService = wynikiService;
    }




    @GetMapping({"/temp"} )
    public String index(Model model) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {


        return "LK/temp";
    }





}
