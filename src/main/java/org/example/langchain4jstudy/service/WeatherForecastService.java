package org.example.langchain4jstudy.service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

record WeatherForecast(String location,String forecast, int temperature){}

@Service
public class WeatherForecastService {
    @Tool("Get the weather forecast for a location")
    WeatherForecast getForecast(@P("location to get the forecast for") String location){
        if (location.equals("Paris")) {
            return new WeatherForecast("Paris", "sunny", 20);
        } else if (location.equals("London")) {
            return new WeatherForecast("London", "rainy", 15);
        } else if (location.equals("Tokyo")) {
            return new WeatherForecast("Tokyo", "warm", 32);
        } else {
            return new WeatherForecast("Unknown", "unknown", 0);
        }
    }
}


