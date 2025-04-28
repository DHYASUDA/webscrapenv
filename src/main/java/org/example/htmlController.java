package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class htmlController {

    @GetMapping("/api/AQITEST")
    public String showIndex(Model model) {
        // Always fetch citiesAQI for the graph
        model.addAttribute("citiesAQI", Main.getAllCitiesAQI());
        // Add city data for comparison
        model.addAttribute("city1Name", Main.getCity1Name());
        model.addAttribute("city1AQI", Main.getCity1AQI());
        model.addAttribute("city2Name", Main.getCity2Name());
        model.addAttribute("city2AQI", Main.getCity2AQI());
        return "index";
    }

    @PostMapping("/api/AQITEST")
    public String compareCities(@RequestParam("city1") String city1,
                               @RequestParam("city2") String city2,
                               Model model) {
        Main.scrap(city1, city2);
        model.addAttribute("citiesAQI", Main.getAllCitiesAQI());
        model.addAttribute("city1Name", Main.getCity1Name());
        model.addAttribute("city1AQI", Main.getCity1AQI());
        model.addAttribute("city2Name", Main.getCity2Name());
        model.addAttribute("city2AQI", Main.getCity2AQI());
        return "index";
    }
}