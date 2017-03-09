package poc.ka283843.wipro.com.weatherpoc.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import poc.ka283843.wipro.com.weatherpoc.R;
import poc.ka283843.wipro.com.weatherpoc.data.BaseActivity;
import poc.ka283843.wipro.com.weatherpoc.data.Fetchdata;
import poc.ka283843.wipro.com.weatherpoc.ui.WeatherForecast;


/**
 * Created by KA283843 on 3/8/2017.
 */

//Params, Progress, Result
public class NetworkLoader extends AsyncTask<Object, String, Object> {

    private int action;
    private BaseActivity mCallingActivity;
    private Context mCallingContex;

    private ProgressDialog mProgressDialog;

    public NetworkLoader(BaseActivity activity, Object extra) {
        super();
        mCallingActivity = activity;
        mCallingContex = activity;
        // mExtraObject = extra;
    }

    @Override
    protected Object doInBackground(Object... args) {

        Object resultDataObject = null;
        action = (int) args[0];
        String envelope = (String) args[1];
        switch (action) {
            case NetworkConst.GET_WEATHER:
                publishProgress("Get Weather");
                String response = null;

                // initiate connection
                response = HttpUrl.callWebService(mCallingContex,
                        NetworkConst.REQUEST_URL, envelope);

                /*if (response == null)
                    response = "response is null";*/
                Log.d("Network loader", "RESPONSE" + response);
                // parse response
                resultDataObject = Fetchdata.parseData(response);
                break;

            case NetworkConst.GET_IMAGE:
                //resultDataObject = HttpUrl.getWeatherImage(mCallingContex,
                // NetworkConst.IMG_URL, envelope);

                resultDataObject = getBitmapFromURL(NetworkConst.IMG_URL + envelope);
                Log.d("Network loader", "RESPONSE IMG" + resultDataObject);
                break;
            default:
                break;
        }

        return resultDataObject;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Object result) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.cancel();
        ((WeatherForecast) mCallingActivity).networkResponse(action, result, null);
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mCallingContex);
        String loadingMsg = mCallingContex.getString(R.string.progress);
        mProgressDialog.setMessage(loadingMsg);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        super.onPreExecute();
    }


    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
