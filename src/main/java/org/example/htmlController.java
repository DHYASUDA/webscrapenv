package org.example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
@Controller
public class htmlController {
    @GetMapping("/api/AQITEST")
    public String getHtml(Model model){
        int aqi = 54;
        int testaqi = Main.city1aqi();
        model.addAttribute("aqivalue", testaqi);//sends it to the html template, edit over there.
        return "index";

    }
}
