package poc.ka283843.wipro.com.weatherpoc.ui;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import poc.ka283843.wipro.com.weatherpoc.R;
import poc.ka283843.wipro.com.weatherpoc.data.BaseActivity;
import poc.ka283843.wipro.com.weatherpoc.data.CityData;
import poc.ka283843.wipro.com.weatherpoc.data.WeatherData;
import poc.ka283843.wipro.com.weatherpoc.location.LocationUpdateListener;
import poc.ka283843.wipro.com.weatherpoc.location.LocationUtil;
import poc.ka283843.wipro.com.weatherpoc.network.NetworkConst;
import poc.ka283843.wipro.com.weatherpoc.network.NetworkLoader;
import poc.ka283843.wipro.com.weatherpoc.network.NetworkLoaderListener;

import static poc.ka283843.wipro.com.weatherpoc.network.NetworkConst.GET_WEATHER;

public class WeatherForecast extends BaseActivity implements LocationUpdateListener, NetworkLoaderListener {


    private static final int REQUEST_LOCATION_PERMISSIONS = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        LocationUtil.initLocationListener(this);
        int hasLocationPermissionGPS = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int hasLocationPermissionNetwork = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasLocationPermissionGPS != PackageManager.PERMISSION_GRANTED || hasLocationPermissionNetwork != PackageManager.PERMISSION_GRANTED) {
            WeatherForecast.super.requestAppPermissions(new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, R.string.permissions
                    , REQUEST_LOCATION_PERMISSIONS);

        }
        if (!LocationUtil.checkGPS(this)) {
            LocationWarning(this, "Location Disabled", "Please enable location.");
        } else {
            LocationUtil.getLocation(WeatherForecast.this);
            //Toast.makeText(WeatherForecast.this, "Requesting Location", Toast.LENGTH_SHORT).show();
            requestWeatherData(GET_WEATHER, null);
        }


    }

    @Override
    public void LocationResponse(double latitude, double longitude) {
        //Toast.makeText(WeatherForecast.this, "Latitude = " + latitude + ", Longitude = " + longitude, Toast.LENGTH_SHORT).show();
    }


    private void LocationWarning(final BaseActivity activity, String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setPositiveButton(activity.getResources().getString(R.string.settings),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!LocationUtil.checkGPS(this)) {
            LocationWarning(this, "Location Disabled", "Please enable location.");
        } else {
            LocationUtil.getLocation(WeatherForecast.this);
            requestWeatherData(GET_WEATHER, null);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(this, "Permissions Granted.", Toast.LENGTH_LONG).show();
                } else {
                    // Permission Denied
                    Toast.makeText(WeatherForecast.this, "LOCATION ACCESS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        //Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }


    @Override
    public void networkResponse(int requestId, Object data1, Object data2) {
        switch (requestId) {
            case GET_WEATHER:
                //System.out.println("GET_WEATHER == " + data1);
                CityData c = (CityData) data1;
                updateData(c);
                requestWeatherData(NetworkConst.GET_IMAGE, c.getWeatherData().get(0).getIcon());
                break;

            case NetworkConst.GET_IMAGE:
                ImageView imgView = (ImageView) findViewById(R.id.imgViewIcon);
                Bitmap bitmapScaled = Bitmap.createScaledBitmap((Bitmap) data1, 200, 200, true);
                imgView.setImageBitmap(bitmapScaled);
                break;
            default:
                break;

        }
    }

    private void requestWeatherData(int action, Object data) {
        switch (action) {
            case GET_WEATHER:
                String latlong = "lat=" + LocationUtil.LATITUDE + "&" + "lon=" + LocationUtil.LONGITUDE + NetworkConst.API_URL;
                NetworkLoader networkLoader = new NetworkLoader(WeatherForecast.this, null);
                networkLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, action, latlong);
                break;

            case NetworkConst.GET_IMAGE:
                String icon = (String) data + ".png";
                NetworkLoader networkLoader1 = new NetworkLoader(WeatherForecast.this, null);
                networkLoader1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, action, icon);
                break;
            default:
                break;

        }
    }

    //update UI
    void updateData(CityData cityData) {
        String date1 = null;
        TextView city = (TextView) findViewById(R.id.textViewCity);
        TextView date = (TextView) findViewById(R.id.textViewDate);
        TextView mainDes = (TextView) findViewById(R.id.textViewMainDes);
        TextView curtemp = (TextView) findViewById(R.id.textViewTemp);
        TextView humidity = (TextView) findViewById(R.id.textViewHumidity);
        TextView pressure = (TextView) findViewById(R.id.textViewPressure);
        TextView windSpeed = (TextView) findViewById(R.id.textViewWind);
        TextView windDeg = (TextView) findViewById(R.id.textViewWindDeg);
        //TextView imgView = (ImageView) findViewById(R.id.condIcon);

        city.setText(cityData.getCity() + ", " + cityData.getCountry());

        ArrayList<WeatherData> weatherDataArrayList = cityData.getWeatherData();
        WeatherData weatherData = weatherDataArrayList.get(0);

        date1 = spiltString(weatherData.getDate());
        if (date1 != null) {
            date.setText(date1);
        }

        mainDes.setText(weatherData.getMain() + ", " + weatherData.getDescription());
        curtemp.setText(String.valueOf(weatherData.getTemp()) + " C");
        humidity.setText("Humidity " + String.valueOf(weatherData.getHumidity()) + "%");
        pressure.setText("Pressure " + String.valueOf(weatherData.getPressure()) + " mb");
        windSpeed.setText("Wind Speed " + weatherData.getWindSpeed() + " mph,");
        windDeg.setText("" + weatherData.getWindDegree() + "\u00B0");


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear1);
        linearLayout.removeAllViews();

        for (int i = 1; i < weatherDataArrayList.size(); i++) {
            WeatherData w = weatherDataArrayList.get(i);
            String date2 = spiltString(w.getDate());
            if (date2.equals(date1)) {
                continue;
            } else {
                date1 = date2;
                TextView tDate = new TextView(this);
                TextView tempMax = new TextView(this);
                TextView tempMin = new TextView(this);

                tDate.setText(spiltString(w.getDate()));
                tempMax.setText("Max Temp " + w.getMaxTemp() + " C");
                tempMin.setText("Min Temp " + w.getMinTemp() + " C");

                linearLayout.addView(tDate);
                linearLayout.addView(tempMax);
                linearLayout.addView(tempMin);

            }
        }

    }

    String spiltString(String string) {
        try {
            String[] s = string.split(" ");
            if (s != null) {
                return s[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }

    @Override
    public void onBackPressed() {
        //LocationUtil.removeUpdates(this);
        super.onBackPressed();
    }

}
