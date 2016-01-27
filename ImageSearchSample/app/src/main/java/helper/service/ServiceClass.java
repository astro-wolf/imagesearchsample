package helper.service;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Sony on 03-Jan-16.
 */
public class ServiceClass {

    Context context;
    public static final int GET = 0;
    public static final int POST = 1;

    public ServiceClass(Context con) {
        context = con;
    }

    public String get_response(int serviceType, String url, RequestBody formBody) {
        Request request = null;
        Response response = null;
        String res = "";
        try {

            OkHttpClient client = new OkHttpClient();
            if (serviceType == POST) {

                request = new Request.Builder()
                        .url(url).post(formBody)
                        .build();


            } else {
                request = new Request.Builder()
                        .url(url)
                        .build();
            }

            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {

                res = response.message().toString();
            } else {
                res = response.body().string();


            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }


}
