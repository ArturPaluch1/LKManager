package LKManager.controllers.LK;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

@Controller
public class scheduleSwissController {

    @GetMapping("/scheduleSwiss")
    public String getSchedule(RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request, Model model, @RequestParam(value = "roundNumber", required = false) String roundNumber, @RequestParam(value = "chosenSchedule", required = false) String chosenSchedule) throws ParserConfigurationException, IOException, SAXException, JAXBException, DatatypeConfigurationException, URISyntaxException {

return null;
    }

    @GetMapping(value = "/addScheduleSwiss")
    public String createSchedule(Model model) throws JAXBException, IOException, ParserConfigurationException, SAXException {

        return null;
    }





    }
