
package org.example;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SpringBootApplication
@Controller
public class Main {

    static final private String token = "ff8450f67a67d7ca540be09b469fc7a6936795fb";

    public static Object[][] cityData = new Object[2][2];

    // Map for transforming dropdown city names to API-compatible names
    private static final Map<String, String> CITY_NAME_MAPPING = new HashMap<>();

    // List of all cities from the dropdown
    private static final List<String> ALL_CITIES = List.of(
        "Chicago", "Los Angeles", "San Francisco", "Houston", "Phoenix",
        "New York", "Philadelphia", "Austin", "Dallas", "Miami",
        "Toronto", "Honolulu", "Galveston", "Bogota", "Tokyo",
        "London", "Paris", "Rome", "Berlin", "Stockholm",
        "Dublin", "Beijing", "Lima"
    );

    

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        // When app first starts, scrape Chicago and Ukiah
        scrap("Chicago", "Ukiah");
    }

    public static void scrap(String name, String name1) {
        String[] cities = {name, name1};
        cityData = new Object[2][2];

        for (int i = 0; i < 2; i++) {
            try {
                String city = cities[i];
                if (city == null || city.trim().isEmpty()) {
                    cityData[i][0] = "Unknown";
                    cityData[i][1] = -1;
                    System.out.println("Error: City " + (i+1) + " is null or empty");
                    continue;
                }

                String apiCityName = CITY_NAME_MAPPING.getOrDefault(city, city);
                System.out.println("Processing city " + (i+1) + ": " + city + " (API name: " + apiCityName + ")");

                String url = "https://api.waqi.info/feed/" + apiCityName + "/?token=" + token;
                URL u = new URL(url);
                HttpURLConnection request = (HttpURLConnection) u.openConnection();
                request.setConnectTimeout(5000);
                request.setReadTimeout(5000);
                request.connect();

                JsonElement root = JsonParser.parseReader(new InputStreamReader(request.getInputStream()));
                JsonObject jsonObject = root.getAsJsonObject();

                if (jsonObject.get("status").getAsString().equals("ok")) {
                    JsonObject data = jsonObject.getAsJsonObject("data");
                    int aqi = data.get("aqi").getAsInt();
                    String cityName = data.getAsJsonObject("city").get("name").getAsString();

                    cityData[i][0] = cityName;
                    cityData[i][1] = aqi;

                    System.out.println("Success: City " + (i+1) + " (" + cityName + ") AQI: " + aqi);
                } else {
                    cityData[i][0] = city;
                    cityData[i][1] = -1;
                    System.out.println("API error for city " + (i+1) + " (" + apiCityName + "): " + jsonObject.get("data").getAsString());
                }
            } catch (Exception e) {
                cityData[i][0] = cities[i];
                cityData[i][1] = -1;
                System.out.println("Failed to scrape AQI for city " + (i+1) + " (" + cities[i] + "): " + e.getMessage());
            }
        }
        System.out.println("Compare Button Clicked!");
        System.out.println("Selected cities: " + (cityData[0][0] != null ? cityData[0][0] : "None") + 
                          " and " + (cityData[1][0] != null ? cityData[1][0] : "None"));
    }

    // Fetch AQI for all cities
    public static List<Map<String, Object>> getAllCitiesAQI() {
        List<Map<String, Object>> result = new ArrayList<>();
        System.out.println("Fetching AQI for all cities...");

        for (String city : ALL_CITIES) {
            try {
                String apiCityName = CITY_NAME_MAPPING.getOrDefault(city, city);
                System.out.println("Fetching AQI for " + city + " (API name: " + apiCityName + ")");

                String url = "https://api.waqi.info/feed/" + apiCityName + "/?token=" + token;
                URL u = new URL(url);
                HttpURLConnection request = (HttpURLConnection) u.openConnection();
                request.setConnectTimeout(5000);
                request.setReadTimeout(5000);
                request.connect();

                JsonElement root = JsonParser.parseReader(new InputStreamReader(request.getInputStream()));
                JsonObject jsonObject = root.getAsJsonObject();

                if (jsonObject.get("status").getAsString().equals("ok")) {
                    JsonObject data = jsonObject.getAsJsonObject("data");
                    int aqi = data.get("aqi").getAsInt();
                    String cityName = data.getAsJsonObject("city").get("name").getAsString();

                    Map<String, Object> cityData = new HashMap<>();
                    cityData.put("city", cityName);
                    cityData.put("aqi", aqi);
                    result.add(cityData);

                    System.out.println("Fetched AQI for " + cityName + ": " + aqi);
                } else {
                    System.out.println("API error for " + apiCityName + ": " + jsonObject.get("data").getAsString());
                    Map<String, Object> cityData = new HashMap<>();
                    cityData.put("city", city);
                    cityData.put("aqi", -1);
                    result.add(cityData);
                }
            } catch (Exception e) {
                //System.out.println("Failed to fetch AQI for " + city + " (API name: " + apiCityName + "): " + e.getMessage());
                Map<String, Object> cityData = new HashMap<>();
                cityData.put("city", city);
                cityData.put("aqi", -1);
                result.add(cityData);
            }
        }
        System.out.println("Fetched AQI for " + result.size() + " out of " + ALL_CITIES.size() + " cities");
        return result;
    }

    public static String getCity1Name() {
        return cityData[0][0] != null ? cityData[0][0].toString() : "Unknown";
    }

    public static int getCity1AQI() {
        return cityData[0][1] != null ? (int) cityData[0][1] : -1;
    }

    public static String getCity2Name() {
        return cityData[1][0] != null ? cityData[1][0].toString() : "Unknown";
    }

    public static int getCity2AQI() {
        return cityData[1][1] != null ? (int) cityData[1][1] : -1;
    }
}
