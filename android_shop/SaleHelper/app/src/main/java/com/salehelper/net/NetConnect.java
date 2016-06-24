package com.salehelper.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.salehelper.salehelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络通讯类  基于Volley框架
 */
public class NetConnect {

    private RequestQueue mQueue;
    private Context context;

    public NetConnect(Context context) {
        this.context = context;
        mQueue = Volley.newRequestQueue(context);
    }

    //下载图片
    public void LoadImage(ImageView img, String url) {

        ImageLoader imageLoader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(img,
                R.drawable.wedding_photo_default, R.drawable.wedding_photo_error);
        imageLoader.get(url, listener);
    }

    //下载JSON串 信息
    public void LoadData(String url, final Success success, final Failed failed) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        success.OnSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //出错执行
                failed.OnFail();
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public void LoadList(String url, final ListSuccess success, final Failed failed) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                success.OnSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                failed.OnFail();
            }
        });
        mQueue.add(jsonArrayRequest);
    }

    public interface Success {
        void OnSuccess(JSONObject response);
    }

    public interface ListSuccess {
        void OnSuccess(JSONArray response);
    }

    public interface Failed {
        void OnFail();
    }
}
