package poc.ka283843.wipro.com.weatherpoc.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by KA283843 on 3/8/2017.
 */

public class Fetchdata {

    public static CityData parseData(String data) {
        JSONObject jObj = null;
        CityData cityData = new CityData();
        if (data == null) {
            cityData.setError(true);
            return cityData;
        }

        try {
            jObj = new JSONObject(data);


            JSONArray jsonArrayList = null;
            JSONObject jsonObjCity = null;
            if (jObj != null) {
                jsonArrayList = jObj.getJSONArray("list");
                jsonObjCity = jObj.getJSONObject("city");
                System.out.println("" + jsonArrayList.length());
            }

            if (jsonObjCity != null) {
                cityData.setCity(jsonObjCity.getString("name"));
                cityData.setCountry(jsonObjCity.getString("country"));
                JSONObject j = jsonObjCity.getJSONObject("coord");
                cityData.setLatitude(j.getDouble("lat"));
                cityData.setLatitude(j.getDouble("lon"));
            }

            if (jsonArrayList != null) {
                ArrayList<WeatherData> weatherDataArrayList = new ArrayList<WeatherData>();
                for (int i = 0; i < jsonArrayList.length(); i++) {
                    WeatherData weatherData = new WeatherData();
                    JSONObject jObj1 = jsonArrayList.getJSONObject(i);
                    System.out.println("Object " + i + " == " + jObj1);

                    JSONObject jObjValue;
                    if (jObj1 != null) {
                        jObjValue = jObj1.getJSONObject("main");
                        if (jObjValue != null) {

                            DecimalFormat df = new DecimalFormat("#.##");
                            //time = Double.valueOf(df.format(time));
                            weatherData.setTemp(Double.valueOf(df.format(jObjValue.getDouble("temp") - 273)));
                            weatherData.setMinTemp(Double.valueOf(df.format(jObjValue.getDouble("temp_min") - 273)));
                            weatherData.setMaxTemp(Double.valueOf(df.format(jObjValue.getDouble("temp_max") - 273)));
                            weatherData.setPressure(jObjValue.getInt("pressure"));
                            weatherData.setHumidity(jObjValue.getInt("humidity"));
                        }

                        JSONArray jsonArrayList1 = jObj1.getJSONArray("weather");
                        if (jsonArrayList1 != null) {
                            for (int j = 0; j < jsonArrayList1.length(); j++) {
                                JSONObject jObjValue1 = jsonArrayList1.getJSONObject(j);
                                weatherData.setMain(jObjValue1.getString("main"));
                                weatherData.setDescription(jObjValue1.getString("description"));
                                weatherData.setIcon(jObjValue1.getString("icon"));
                            }
                        }
                        if (jObj1.has("clouds")) {
                            jObjValue = jObj1.getJSONObject("clouds");
                            if (jObjValue != null) {
                                int all = jObjValue.getInt("all");
                            }
                        }
                        if (jObj1.has("wind")) {
                            jObjValue = jObj1.getJSONObject("wind");
                            if (jObjValue != null) {
                                weatherData.setWindSpeed(jObjValue.getDouble("speed"));
                                weatherData.setWindDegree(jObjValue.getDouble("deg"));
                            }
                        }

                        if (jObj1.has("rain")) {
                            jObjValue = jObj1.getJSONObject("rain");
                            if (jObjValue != null) {
                            }
                        }


                        String dt_txt = jObj1.getString("dt_txt");
                        weatherData.setDate(dt_txt);
                        //System.out.println("dt_txt " + i + " == " + dt_txt);
                    }

                    //System.out.println("Object " + i + " == " + jObj1);

                    weatherDataArrayList.add(weatherData);
                }
                cityData.setWeatherData(weatherDataArrayList);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityData;
    }


}
