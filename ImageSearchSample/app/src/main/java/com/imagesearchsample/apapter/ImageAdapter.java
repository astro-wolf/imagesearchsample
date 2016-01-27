package com.imagesearchsample.apapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imagesearchsample.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import helper.cache.ImageLoaderCache;
import helper.property.ImagePrp;

/**
 * Created by Sony on 24-Jan-16.
 */
public class ImageAdapter extends BaseAdapter {
    private final ImageLoaderCache imageLoader;
    Activity activity;
    List<ImagePrp> prpList;
    String searchKey;
    boolean isNetON;
    File[] filesLists;
    private File cacheDir;

    public ImageAdapter(Activity act, List<ImagePrp> list, String key, boolean isNetON) {
        this.activity = act;
        this.prpList = list;
        this.searchKey = key;
        this.isNetON = isNetON;
        imageLoader = new ImageLoaderCache(act, key);


    }

    public ImageAdapter(Activity act, File[] list, String key, boolean isNetON) {
        this.activity = act;
        this.filesLists = list;
        this.searchKey = key;
        this.isNetON = isNetON;
        imageLoader = new ImageLoaderCache(act, key);


    }

    @Override
    public int getCount() {
        if (isNetON) {
            return prpList.size();
        } else {
            return filesLists.length;
        }
    }

    @Override
    public Object getItem(int position) {

        if (isNetON) {
            return prpList.get(position);
        } else {
            return filesLists[position];
        }


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.view_image, null, true);
        ImageView img_view = (ImageView) view.findViewById(R.id.img_View);
        if (isNetON) {
            imageLoader.DisplayImage(prpList.get(position).getThumbnailLink(), img_view, searchKey);
        } else {
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "IMAGE_SEARCH/" + searchKey);
            File f = new File(cacheDir, filesLists[position].getName());
            img_view.setImageBitmap(decodeFile(f));
        }
        return view;
    }

    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
