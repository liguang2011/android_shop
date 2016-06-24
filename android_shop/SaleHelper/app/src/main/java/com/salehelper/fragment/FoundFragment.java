package com.salehelper.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.salehelper.activity.ProductActivity;
import com.salehelper.adapter.ProductListAdapter;
import com.salehelper.models.Product;
import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;


//发现页面

public class FoundFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "com.salehelper.FoundFragment";
    private ArrayList<Product> list = new ArrayList<Product>();
    private ListView mList;
    private ProductListAdapter adapter;
    private NetConnect net;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "调用了");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_found, container, false);
        initView(v);
        update();
        return v;
    }

    private void initView(View v) {
        mList = (ListView) v.findViewById(R.id.productItem);
        adapter = new ProductListAdapter(getActivity(), list);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(this);
        net = new NetConnect(getActivity());
        v.findViewById(R.id.btnRefresh).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                update();
        }
    }

    public void update() {
        list.clear();
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "联网中", "获取列表...");
        net.LoadList(Config.GET_PRODUCT_LIST, new NetConnect.ListSuccess() {
            @Override
            public void OnSuccess(JSONArray response) {
                try {
                    for (int i = 0; i < 3; i++) {
                        Product o = new Product(response.getJSONObject(i).getInt("productid"), response.getJSONObject(i).getString("name"), response.getJSONObject(i).getDouble("price") * 0.8, response.getJSONObject(i).getString("describe"), response.getJSONObject(i).getInt("num"), Config.url + response.getJSONObject(i).getString("image"));
                        //生成随机数
                        Random r = new Random();
                        int a = r.nextInt(i + 1);
                        list.add(a, o);
                    }
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                    Toast.makeText(getActivity(), R.string.getSuccess, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "解析悲剧", Toast.LENGTH_SHORT).show();
                }
            }
        }, new NetConnect.Failed() {
            @Override
            public void OnFail() {
                pd.dismiss();
                Toast.makeText(getActivity(), R.string.getFail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), ProductActivity.class);
        i.putExtra("productid", list.get(position).getProductid());
        i.putExtra("zhekou", true);
        startActivity(i);
    }
}
