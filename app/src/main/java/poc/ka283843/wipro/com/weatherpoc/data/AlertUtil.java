package poc.ka283843.wipro.com.weatherpoc.data;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;

import poc.ka283843.wipro.com.weatherpoc.R;

/**
 * Created by KA283843 on 3/8/2017.
 */

public class AlertUtil {


    public static void LocationWarning(final BaseActivity activity, String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setPositiveButton(activity.getResources().getString(R.string.settings),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alert.show();
    }

}
