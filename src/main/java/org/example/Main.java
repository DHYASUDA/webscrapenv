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


    public static Object[][] cityData = new Object[2][3];

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        scrap("Chicago", "ukiah");
        }
        public static int scrap(String name, String name1){
        int aqi = Integer.MAX_VALUE;
        String curCity = name;
        for(int i = 0; i < 2; i++){
            try{

                //String token = "ff8450f67a67d7ca540be09b469fc7a6936795fb";
                String url = "https://api.waqi.info/feed/" + curCity + "/?token=" + token;
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
                    aqi = data.get("aqi").getAsInt();


                    String cityName = data.getAsJsonObject("city").get("name").getAsString();


                    String time = data.getAsJsonObject("time").get("s").getAsString();
                    cityData[i][i] = cityName;
                    cityData[i][i+1] = aqi;
                    System.out.println(aqi + " " + time + " " + cityName);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            curCity = name1;
        }

            return aqi;
        }
        //methods to getCity names/aqi
    public static String getCity1Name(){
        return cityData[0][0].toString();
    }
        public static int getCity1AQI(){
            return (int)cityData[0][1];
        }

    public static String getCity2Name(){
        return cityData[1][1].toString();
    }
    public static int getCity2AQI(){
        return (int)cityData[1][2];
    }

    }
