package poc.ka283843.wipro.com.weatherpoc.data;

import java.util.ArrayList;

/**
 * Created by KA283843 on 3/8/2017.
 */

public class CityData {


    private double latitude;
    private double longitude;
    private String city;
    private String country;
    private boolean isError = false;
    private ArrayList<WeatherData> weatherData;


    public ArrayList<WeatherData> getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(ArrayList<WeatherData> weatherData) {
        this.weatherData = weatherData;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
