package org.example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
@Controller
public class htmlController {
    @GetMapping("/api/AQITEST")
    public String getHtml(Model model){
        //city 1
        int city1AQI = Main.getCity1AQI();
        String city1Name = Main.getCity1Name();
        //city 2
        int city2AQI = Main.getCity2AQI();
        String city2Name = Main.getCity2Name();
        model.addAttribute("aqivalue", city1AQI);//sends it to the html template, edit over there.
        model.addAttribute("city1name", city1Name);

        model.addAttribute("aqivalue2", city2AQI);//sends it to the html template, edit over there.
        model.addAttribute("city2name", city2Name);
        return "index";

    }
}
