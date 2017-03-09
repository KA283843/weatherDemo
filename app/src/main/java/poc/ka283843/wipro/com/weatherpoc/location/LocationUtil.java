package poc.ka283843.wipro.com.weatherpoc.location;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import poc.ka283843.wipro.com.weatherpoc.data.BaseActivity;
import poc.ka283843.wipro.com.weatherpoc.ui.WeatherForecast;

/**
 * Created by KA283843 on 3/8/2017.
 */

public class LocationUtil implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE = 0; // meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME = 1000 * 5 * 1; //  milliseconds
    public static double LATITUDE = 0.0;
    public static double LONGITUDE = 0.0;
    private static BaseActivity sActivity;
    private static LocationUtil sLocationUtil;
    // Declaring a Location Manager
    protected LocationManager locationManager;

    public static void initLocationListener(BaseActivity activity) {
        sLocationUtil = new LocationUtil();
        sActivity = activity;
    }

    public static boolean checkGPS(Context context) {

        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        // getting network status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.
                isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled == false && isNetworkEnabled == false) {
            return false;
        }
        /*
         * if (isGPSEnabled && isNetworkEnabled) { Toast.makeText(context,
		 * "You are good to go now", Toast.LENGTH_SHORT) .show(); return true; }
		 */
        return true;

    }

    public static void getLocation(Context context) {
        try {

            LocationManager locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            Location location = null;
            // getting network status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager.
                    isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME, MIN_DISTANCE, sLocationUtil);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME, MIN_DISTANCE, sLocationUtil);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                LATITUDE = location.getLatitude();
                LONGITUDE = location.getLongitude();
            }

            ((WeatherForecast) sActivity).LocationResponse(LATITUDE, LONGITUDE);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeUpdates(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        try {
            int hasLocationPermissionGPS = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasLocationPermissionGPS != PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(sLocationUtil);
            }
            sLocationUtil = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getLATITUDE() {
        return LATITUDE;
    }

    public static double getLONGITUDE() {
        return LONGITUDE;
    }

    @Override
    public void onLocationChanged(Location location) {
        LATITUDE = location.getLatitude();
        LONGITUDE = location.getLongitude();
        //Log.d("", "Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude());
        //Toast.makeText(sActivity, "Latitude = " + LATITUDE + ", Longitude = " + LONGITUDE, Toast.LENGTH_SHORT).show();
        ((WeatherForecast) sActivity).LocationResponse(LATITUDE, LONGITUDE);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }
}
