package com.imagesearchsample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.imagesearchsample.R;
import com.imagesearchsample.apapter.ImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import helper.asyncTask.GetSearchResult;
import helper.cache.FileCache;
import helper.connection.ConnectionDetector;
import helper.property.ImagePrp;

public class MainActivity extends Activity implements View.OnClickListener {

    //Constant variable
    public final String cx = "014342063005246522380:lmzqr5geodm";
    public final String fileType = "jpg";
    public final String searchType = "image";
    public final String key = "AIzaSyBNTEiU7D8cis7Hnl2miaYmlfLCkL3ur_A";
    public final String imgSize = "large";
    int startIndex = 1;
    EditText edSearch;
    GridView gridSearchImage;
    Button btnSearch;
    ConnectionDetector connectionDetector;
    FileCache fileCache;
    int lastPostion = 0;
    private int lastSize;
    int mPrevTotalItemCount = 0;
    private String seaarcText = "";
    List<ImagePrp> searchImageList = new ArrayList<ImagePrp>();
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileCache = new FileCache(getApplicationContext(), key);

        //Connection
        connectionDetector = new ConnectionDetector(getApplicationContext());

        edSearch = (EditText) findViewById(R.id.edt_Search);
        gridSearchImage = (GridView) findViewById(R.id.gridSearchImage);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSearch:
                seaarcText = edSearch.getText().toString();
                if (seaarcText.replace(" ", "").length() == 0) {
                    Toast.makeText(getApplicationContext(), "Enter some keyWord", Toast.LENGTH_LONG).show();
                } else {

                    if (connectionDetector.isConnectingToInternet()) {

                        String query = "https://www.googleapis.com/customsearch/v1?" +
                                "q=" + edSearch.getText().toString() +
                                "&start=" + startIndex +
                                "&cx=" + cx +
                                "&fileType=" + fileType +
                                "&imgSize=" + imgSize +
                                "&searchType=" + searchType +
                                "&key=" + key;
                        new GetSearchResult(MainActivity.this, seaarcText).execute(query.replace(" ", "%20"));
                    } else {
                        try {
                            File cacheDir = new File(Environment.getExternalStorageDirectory(), "IMAGE_SEARCH/" + seaarcText.trim());
                            File[] files = cacheDir.listFiles();

                            if (files.length == 0) {

                            } else {
                                ImageAdapter imageAdapter = new ImageAdapter(MainActivity.this, files, seaarcText.trim(), false);
                                gridSearchImage.setAdapter(imageAdapter);
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(getApplicationContext(), "Some problem Please try again", Toast.LENGTH_LONG).show();
                        }


                    }
                }


                break;

        }

    }


    public void setData(List<ImagePrp> ImageList, String key) {

        searchImageList.addAll(ImageList);

        if (ImageList.size() == 0) {

            Toast.makeText(getApplicationContext(), "No more Images display", Toast.LENGTH_LONG)
                    .show();

        } else {

            if (searchImageList.size() == 0) {

            } else {

                if (searchImageList.size() <= 10) {

                    imageAdapter = new ImageAdapter(MainActivity.this, searchImageList, key, true);
                    gridSearchImage.setAdapter(imageAdapter);

                    lastSize = searchImageList.size();
                    getScrollData();
                } else {

                    if (lastSize == imageAdapter.getCount()) {
                    } else {
                        imageAdapter.notifyDataSetChanged();
                        lastSize = searchImageList.size();
                        getScrollData();
                    }

                }

            }
        }
       /* ImageAdapter imageAdapter = new ImageAdapter(MainActivity.this, searchImageList, key, true);
        gridSearchImage.setAdapter(imageAdapter);
*/
    }


    public void getScrollData() {
        gridSearchImage.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                lastPostion = firstVisibleItem;
                if (view.getAdapter() != null
                        && ((firstVisibleItem + visibleItemCount) >= totalItemCount)
                        && totalItemCount != mPrevTotalItemCount) {
                    Log.e("Reached at end", "");
                    Toast.makeText(getApplicationContext(), "Reached at end", Toast.LENGTH_LONG).show();
                    mPrevTotalItemCount = totalItemCount;
                    startIndex = startIndex + 10;
                    String query = "https://www.googleapis.com/customsearch/v1?" +
                            "q=" + edSearch.getText().toString() +
                            "&start=" + startIndex +
                            "&cx=" + cx +
                            "&fileType=" + fileType +
                            "&imgSize=" + imgSize +
                            "&searchType=" + searchType +
                            "&key=" + key;
                    new GetSearchResult(MainActivity.this, seaarcText).execute(query.replace(" ", "%20"));


                }

            }

        });

    }


}
