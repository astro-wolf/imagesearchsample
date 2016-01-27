package helper.util;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by Sony on 24-Jan-16.
 */
public class Dialog {
    private static ProgressDialog dia;

    public static void startProgressDialog(Activity act) {
        dia = new ProgressDialog(act);
        dia.setMessage("Loading...");
        dia.setCancelable(false);
        dia.show();
    }


    public static void stopProgressDialog() {
        if (dia.isShowing())
            dia.dismiss();
    }
}
