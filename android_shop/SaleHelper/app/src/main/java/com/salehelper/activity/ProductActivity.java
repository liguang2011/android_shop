package com.salehelper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商品详情页面 Activity
 */
public class ProductActivity extends Activity implements View.OnClickListener {

    private boolean found = false;
    private static final int UPDATE_UI = 1;
    private NetConnect net;
    private int productid;
    private ImageView productImage;
    private TextView productNumBefore;
    private TextView productNumAfter;
    private TextView productNumber;
    private TextView productIntroduction;
    private EditText buyNum;
    private double trueMoney;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    JSONObject response = (JSONObject) msg.obj;
                    try {
                        net.LoadImage(productImage, Config.url + response.getString("image"));
                        if (found) {
                            productNumBefore.setText("原价：" + response.getDouble("price") * 0.8);
                            trueMoney = response.getDouble("price") * ((10 - Config.getCachedVip(getApplicationContext()))) / 10 * 0.8;
                            productNumAfter.setText("会员：" + String.valueOf(trueMoney));
                            productNumber.setText("库存：" + response.getInt("num"));
                            productIntroduction.setText(response.getString("describe"));
                        } else {
                            productNumBefore.setText("原价：" + response.getDouble("price"));
                            trueMoney = response.getDouble("price") * ((10 - Config.getCachedVip(getApplicationContext()))) / 10;
                            productNumAfter.setText("会员：" + trueMoney);
                            productNumber.setText("库存：" + response.getInt("num"));
                            productIntroduction.setText(response.getString("describe"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initView();
        update();
    }

    private void initView() {
        productid = getIntent().getIntExtra("productid", -1);
        findViewById(R.id.btnPutInCar).setOnClickListener(this);
        findViewById(R.id.btnBuy).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        productImage = (ImageView) findViewById(R.id.productImage);
        productNumBefore = (TextView) findViewById(R.id.productNumBefore);
        productNumAfter = (TextView) findViewById(R.id.productNumAfter);
        productNumber = (TextView) findViewById(R.id.productNumber);
        productIntroduction = (TextView) findViewById(R.id.productIntroduction);
        buyNum = (EditText) findViewById(R.id.etNum);
        net = new NetConnect(this);
        found = getIntent().getBooleanExtra("zhekou", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        } else {
            String url = Config.CHANGE_MONEY + "userid=" + Config.getCachedUserid(getApplicationContext()) + "&productid=" + productid + "&money=" + trueMoney + "&num=1";
            net.LoadData(url, new NetConnect.Success() {
                @Override
                public void OnSuccess(JSONObject response) {
                    try {
                        if (response.getInt("status") == 1) {
                            Toast.makeText(getApplicationContext(), R.string.buySuccess, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ProductActivity.this, BuySuccessActivity.class);
                            i.putExtra("product_price", trueMoney);
                            startActivity(i);
                            update();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.buyFail, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), R.string.buyFail, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new NetConnect.Failed() {
                @Override
                public void OnFail() {
                    Toast.makeText(getApplicationContext(), R.string.buyFail, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnPutInCar:
                String url = Config.PUT_IN_CAR + "userid=" + Config.getCachedUserid(getApplicationContext()) + "&productid=" + productid + "&num=" + buyNum.getText().toString();
                final ProgressDialog pd = ProgressDialog.show(this, "联网中", "正在放入购物车");
                net.LoadData(url, new NetConnect.Success() {
                    @Override
                    public void OnSuccess(JSONObject response) {
                        try {
                            if (response.getInt("status") == 1) {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.putSuccess, Toast.LENGTH_SHORT).show();
                            } else {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.putFail, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), R.string.putFail, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new NetConnect.Failed() {
                    @Override
                    public void OnFail() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.putFail, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btnBuy:
                final String[] names = {"余额支付", "银行卡支付"};
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("选择方式");
                adb.setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            String url = Config.CHANGE_MONEY + "userid=" + Config.getCachedUserid(getApplicationContext()) + "&productid=" + productid + "&money=" + trueMoney + "&num=1";
                            net.LoadData(url, new NetConnect.Success() {
                                @Override
                                public void OnSuccess(JSONObject response) {
                                    try {
                                        if (response.getInt("status") == 1) {
                                            Toast.makeText(getApplicationContext(), R.string.buySuccess, Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(ProductActivity.this, BuySuccessActivity.class);
                                            i.putExtra("product_price", trueMoney);
                                            startActivity(i);
                                            update();
                                        } else {
                                            Toast.makeText(getApplicationContext(), R.string.buyFail, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), R.string.buyFail, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new NetConnect.Failed() {
                                @Override
                                public void OnFail() {
                                    Toast.makeText(getApplicationContext(), R.string.buyFail, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Intent i = new Intent(getApplicationContext(), ChongzhiActivity.class);
                            i.putExtra("number", trueMoney);
                            startActivityForResult(i, 1);
                        }
                    }
                }).show();
                break;
            default:
                break;
        }
    }


    private void update() {
        String url = Config.GET_PRODUCT + "productid=" + productid;
        final ProgressDialog pd = ProgressDialog.show(this, "获取中", "正在连接...");
        net.LoadData(url, new NetConnect.Success() {
            @Override
            public void OnSuccess(JSONObject response) {
                Message message = new Message();
                message.what = UPDATE_UI;
                message.obj = response;
                mHandler.sendMessage(message);
                pd.dismiss();
                Toast.makeText(getApplicationContext(), R.string.getSuccess, Toast.LENGTH_SHORT).show();
            }
        }, new NetConnect.Failed() {
            @Override
            public void OnFail() {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), R.string.getFail, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
