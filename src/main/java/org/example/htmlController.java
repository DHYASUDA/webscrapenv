package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;
@Controller
public class htmlController {

    @GetMapping("/api/AQITEST")
    public String getHtml(@RequestParam(value = "city1", required = false) String city1,
                          @RequestParam(value = "city2", required = false) String city2,
                          Model model) {
        if (city1 == null || city1.trim().isEmpty()) {
            city1 = "Chicago";
        }
        if (city2 == null || city2.trim().isEmpty()) {
            city2 = "Ukiah";
        }

        System.out.println("Received cities: city1=" + city1 + ", city2=" + city2);

        // Scrape the selected cities
        Main.scrap(city1, city2);

        // Add AQI data for selected cities
        model.addAttribute("city1name", Main.getCity1Name());
        model.addAttribute("aqivalue", Main.getCity1AQI() >= 0 ? Main.getCity1AQI() : "N/A");
        model.addAttribute("city2name", Main.getCity2Name());
        model.addAttribute("aqivalue2", Main.getCity2AQI() >= 0 ? Main.getCity2AQI() : "N/A");

        // Add AQI data for all cities for the graph
        List<Map<String, Object>> allCitiesAQI = Main.getAllCitiesAQI();
        model.addAttribute("citiesAQI", allCitiesAQI);
        System.out.println("Passing to index: citiesAQI=" + allCitiesAQI);

        return "index";
    }
}