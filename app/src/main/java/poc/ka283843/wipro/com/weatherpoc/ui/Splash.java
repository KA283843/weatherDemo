package poc.ka283843.wipro.com.weatherpoc.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import poc.ka283843.wipro.com.weatherpoc.R;
import poc.ka283843.wipro.com.weatherpoc.data.BaseActivity;

public class Splash extends BaseActivity {

    private int mTimer = 1000;
    private int SPLASH_TIME = 2000;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_splash);
        new CountDownTimer(SPLASH_TIME, mTimer) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent intent = new Intent(mContext, WeatherForecast.class);
                Splash.this.finish();
                mContext.startActivity(intent);

            }
        }.start();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }
}
