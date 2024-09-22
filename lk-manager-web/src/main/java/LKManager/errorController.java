package LKManager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class errorController
{

    @GetMapping("/public/LK/error")
    public String errorMessage(Model model, @RequestParam(value="errorMessage", required = false)String errorMessage )
    {
model.addAttribute("message",errorMessage);
        return  "public/LK/error";
    }

}
