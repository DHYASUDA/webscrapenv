package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    static final private String token = "ff8450f67a67d7ca540be09b469fc7a6936795fb";
     static private int city1;
     private int city2;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        try{
            String city = "Lima";
            //String token = "ff8450f67a67d7ca540be09b469fc7a6936795fb";
            String url = "https://api.waqi.info/feed/" + city + "/?token=" + token;
        //connectiing to the aqi webstite
            URL u = new URL(url);
            HttpURLConnection request = (HttpURLConnection) u.openConnection();
            request.connect();

            //parsing the json file. converts the json data from the website into java readable code
            JsonElement root = JsonParser.parseReader(new InputStreamReader(request.getInputStream()));
            //this turns it into a java object?!? im guessing
            JsonObject jsonObject = root.getAsJsonObject();


            //if website is readable/giving data
            if(jsonObject.get("status").getAsString().equals("ok")){
                //need to turn json stuff into a java object
                JsonObject data = jsonObject.getAsJsonObject("data"); //this is now a java object
                //make it into an int
                int aqi = data.get("aqi").getAsInt();
                city1 = aqi;
                String cityName = data.getAsJsonObject("city").get("name").getAsString();
                String time = data.getAsJsonObject("time").get("s").getAsString();

                System.out.println(city1 + " " + time + " " + cityName);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        public static int city1aqi(){
            System.out.println(city1);
        return city1;
        }
    }
