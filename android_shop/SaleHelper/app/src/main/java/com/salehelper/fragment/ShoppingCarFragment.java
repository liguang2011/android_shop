package com.salehelper.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.salehelper.activity.BuySuccessActivity;
import com.salehelper.activity.ChongzhiActivity;
import com.salehelper.activity.ProductActivity;
import com.salehelper.adapter.CarListAdapter;
import com.salehelper.models.Product;
import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import java.util.ArrayList;

// 购物车页面

public class ShoppingCarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int UPDATE_UI = 1;
    private TextView tv_num; //总价text
    private ArrayList<Product> list = new ArrayList<Product>();
    private ListView mList;
    private CarListAdapter adapter;
    private NetConnect net;
    private double num = 0; //总价格
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    tv_num.setText("总价：" + num);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_car, container, false);
        initView(v);
        update();
        return v;
    }

    private void initView(View v) {
        net = new NetConnect(getActivity());
        tv_num = (TextView) v.findViewById(R.id.moneyNum);
        adapter = new CarListAdapter(getActivity(), list);
        mList = (ListView) v.findViewById(R.id.productItem);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(this);
        v.findViewById(R.id.btnBuy).setOnClickListener(this);
        v.findViewById(R.id.btnRefresh).setOnClickListener(this);
    }

    private void update() {
        list.clear();
        String url = Config.GET_CAR + "userid=" + Config.getCachedUserid(getActivity());
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "刷新购物车", "正在刷新...");
        net.LoadList(url, new NetConnect.ListSuccess() {
            @Override
            public void OnSuccess(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        Product o = new Product(response.getJSONObject(i).getInt("productid"), response.getJSONObject(i).getString("name"), response.getJSONObject(i).getDouble("price"), response.getJSONObject(i).getString("describe"), response.getJSONObject(i).getInt("num"), Config.url + response.getJSONObject(i).getString("image"));
                        list.add(o);
                        addItemMoney();
                    }
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                    Toast.makeText(getActivity(), R.string.getSuccess, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                    Toast.makeText(getActivity(), R.string.getFail, Toast.LENGTH_SHORT).show();
                }
            }
        }, new NetConnect.Failed() {
            @Override
            public void OnFail() {
                Toast.makeText(getActivity(), R.string.getFail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        } else {
            final ProgressDialog pd = ProgressDialog.show(getActivity(), "结算中", "正在结算");
            String url = Config.CLEAR_CAR + "userid=" + Config.getCachedUserid(getActivity()) + "&money=" + num;
            net.LoadData(url, new NetConnect.Success() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        if (response.getInt("status") == 1) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), R.string.clearSuccess, Toast.LENGTH_SHORT).show();
                            list.clear();
                            adapter.notifyDataSetChanged();
                            Intent i = new Intent(getActivity(), BuySuccessActivity.class);
                            i.putExtra("product_price", num);
                            startActivity(i);
                        } else {
                            pd.dismiss();
                            Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new NetConnect.Failed() {
                @Override
                public void OnFail() {
                    pd.dismiss();
                    Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh:
                update();
                break;
            case R.id.btnBuy:
                final String[] names = {"余额支付", "银行卡支付"};
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("选择方式");
                adb.setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final ProgressDialog pd = ProgressDialog.show(getActivity(), "结算中", "正在结算");
                            String url = Config.CLEAR_CAR + "userid=" + Config.getCachedUserid(getActivity()) + "&money=" + num;
                            net.LoadData(url, new NetConnect.Success() {
                                @Override
                                public void OnSuccess(JSONObject response) {
                                    try {
                                        if (response.getInt("status") == 1) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), R.string.clearSuccess, Toast.LENGTH_SHORT).show();
                                            list.clear();
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        pd.dismiss();
                                        Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            }, new NetConnect.Failed() {
                                @Override
                                public void OnFail() {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Intent i = new Intent(getActivity(), ChongzhiActivity.class);
                            i.putExtra("number", num);
                            startActivityForResult(i, 1);
                        }
                    }
                }).show();
//                final ProgressDialog pd = ProgressDialog.show(getActivity(), "结算中", "正在结算");
//                String url = Config.CLEAR_CAR + "userid=" + Config.getCachedUserid(getActivity()) + "&money=" + num;
//                net.LoadData(url, new NetConnect.Success() {
//                    @Override
//                    public void OnSuccess(JSONObject response) {
//                        try {
//                            if (response.getInt("status") == 1) {
//                                pd.dismiss();
//                                Toast.makeText(getActivity(), R.string.clearSuccess, Toast.LENGTH_SHORT).show();
//                                list.clear();
//                                adapter.notifyDataSetChanged();
//                            } else {
//                                pd.dismiss();
//                                Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            pd.dismiss();
//                            Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                }, new NetConnect.Failed() {
//                    @Override
//                    public void OnFail() {
//                        pd.dismiss();
//                        Toast.makeText(getActivity(), R.string.clearFail, Toast.LENGTH_SHORT).show();
//                    }
//                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), ProductActivity.class);
        i.putExtra("productid", list.get(position).getProductid());
        startActivity(i);
    }

    //计算一共多少钱然后数字显示在合计textview上
    private void addItemMoney() {
        num = 0;
        for (int i = 0; i < list.size(); i++) {
            num += list.get(i).getPrice() * list.get(i).getNum() * (10 - Config.getCachedVip(getActivity())) / 10;
        }
        Message msg = new Message();
        msg.what = UPDATE_UI;
        mHandler.sendMessage(msg);
    }
}
