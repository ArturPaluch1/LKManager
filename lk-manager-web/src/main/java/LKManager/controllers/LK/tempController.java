package LKManager.controllers.LK;

import LKManager.services.*;
import LKManager.services.FilesService_unused.PlikiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class tempController {
    private final LKManager.services.MZUserService MZUserService;

    private final ScheduleService scheduleService;


    private final PlikiService plikiService;

    private  final ResultsService resultsService;

    public tempController(LKManager.services.MZUserService mzUserService, ScheduleService scheduleService, PlikiService plikiService, ResultsService resultsService) {
        MZUserService = mzUserService;

        this.scheduleService = scheduleService;
        this.plikiService = plikiService;
        this.resultsService = resultsService;
    }




    @GetMapping({"/temp"} )
    public String index(Model model) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {

model.addAttribute("error",model.getAttribute("errorMessage"));
        return "LK/temp";
    }





}
