package helper.cache;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class FileCache {

    private File cacheDir;

    public FileCache(Context context, String key) {

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "IMAGE_SEARCH");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        FileCacheSub(context, key);
    }


    public void FileCacheSub(Context context, String key) {

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "IMAGE_SEARCH/" + key);
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url) {
        Log.e("url ", url);
        String filename = String.valueOf(url.hashCode());
        Log.e("has code ", url.hashCode() + "");
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}