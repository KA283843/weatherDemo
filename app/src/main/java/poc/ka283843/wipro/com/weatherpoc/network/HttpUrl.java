package poc.ka283843.wipro.com.weatherpoc.network;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by KA283843 on 3/8/2017.
 */

public class HttpUrl {

    public HttpUrl() {

    }

    public static HttpURLConnection setUpHttpConnection(String urlString, String envelope,
                                                        Context context) {
        try {
            URL url = null;
            try {
                url = new URL(urlString + envelope);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection urlConnection = null;

            try {
                urlConnection = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();

            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setConnectTimeout(10000); //set timeout to 10 seconds

            return httpURLConnection;

        } catch (Exception ex) {
            Log.e("HTTPURL", "Failed to establish HTTP connection to server: "
                    + ex.toString());
            return null;
        }

    }


    public static String callWebService(Context context, String targetURL, String envelope) {

        HttpURLConnection connection = null;
        try {
            //Create connection
            connection = setUpHttpConnection(targetURL, envelope, context);

            if (connection != null) {

                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                // Get Response
                return getResponseInString(connection.getInputStream());
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP URL", "Creating HTTP connection" + e.toString());
            return null;

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String getResponseInString(InputStream is) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        try {
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return response.toString();

    }

    public static byte[] getWeatherImage(Context context, String targetURL, String envelope) {

        HttpURLConnection connection = null;
        try {
            //Create connection
            connection = setUpHttpConnection(targetURL, envelope, context);

            if (connection != null) {

                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                // Get Response
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                while (inputStream.read(buffer) != -1)
                    byteArrayOutputStream.write(buffer);

                return byteArrayOutputStream.toByteArray();
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("HTTP URL", "Creating HTTP connection" + e.toString());
            return null;

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
