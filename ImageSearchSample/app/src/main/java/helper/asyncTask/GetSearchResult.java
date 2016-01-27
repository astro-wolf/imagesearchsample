package helper.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.imagesearchsample.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import helper.property.ImagePrp;
import helper.service.ServiceClass;
import helper.util.Dialog;

public class GetSearchResult extends AsyncTask<String, String, String> {
    MainActivity act;
    List<ImagePrp> searchImageList = new ArrayList<ImagePrp>();
    String key;

    public GetSearchResult(MainActivity activity, String searchKey) {
        this.key = searchKey;
        this.act = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Dialog.startProgressDialog(act);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.e("Url", params[0] + "");
            ServiceClass serviceClass = new ServiceClass(act);
            String result = serviceClass.get_response(ServiceClass.GET, params[0], null);
            Log.e("result  ", result + "");
            JSONObject object = new JSONObject(result);
            JSONArray itemArray = object.getJSONArray("items");
            for (int i = 0; i < itemArray.length(); i++) {
                ImagePrp prp = new ImagePrp();
                JSONObject objectItem = itemArray.getJSONObject(i);
                prp.setLink(objectItem.getString("link"));
                prp.setTitle(objectItem.getString("title"));
                JSONObject objectImage = objectItem.getJSONObject("image");
                prp.setThumbnailLink(objectImage.getString("thumbnailLink"));
                searchImageList.add(prp);
            }


        } catch (JSONException e) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Dialog.stopProgressDialog();
        Log.e("searchImageList  ", searchImageList.size() + "");
        act.setData(searchImageList, key);

    }
}