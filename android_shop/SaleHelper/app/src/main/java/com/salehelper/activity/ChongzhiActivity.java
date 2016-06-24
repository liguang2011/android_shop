package com.salehelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ChongzhiActivity extends Activity implements View.OnClickListener {

    private TextView cardID;
    private EditText etMoney;
    private EditText etMima;
    private EditText etMimaAgain;
    private NetConnect net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chongzhi);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnFinish).setOnClickListener(this);
        cardID = (TextView) findViewById(R.id.cardID);
        cardID.setText(Config.getCachedCard(this));
        etMoney = (EditText) findViewById(R.id.etMoney);
        etMima = (EditText) findViewById(R.id.etMima);
        etMimaAgain = (EditText) findViewById(R.id.etMimaAgain);
        etMoney.setText(String.valueOf(getIntent().getDoubleExtra("number", 0.0d)));
        net = new NetConnect(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnFinish:
                if (TextUtils.isEmpty(etMoney.getText().toString()) || TextUtils.isEmpty(etMima.getText().toString()) || TextUtils.isEmpty(etMimaAgain.getText().toString())) {
                    Toast.makeText(this, "充值信息为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!etMima.getText().toString().equals(etMimaAgain.getText().toString())) {
                    Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    break;
                }
                String url = Config.CHONGZHI + "userid=" + Config.getCachedUserid(this) + "&passWord=" + etMima.getText().toString() + "&money=" + etMoney.getText().toString();
                final ProgressDialog pd = ProgressDialog.show(this, "充值中", "正在充值");
                net.LoadData(url, new NetConnect.Success() {
                    @Override
                    public void OnSuccess(JSONObject response) {
                        try {
                            if (response.getInt("status") == 1) {
                                Toast.makeText(getApplicationContext(), "充值成功", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "充值失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd.dismiss();

                    }
                }, new NetConnect.Failed() {
                    @Override
                    public void OnFail() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "充值失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
