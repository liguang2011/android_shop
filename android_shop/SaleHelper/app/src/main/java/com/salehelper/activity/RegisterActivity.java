package com.salehelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by longlong on 2015/4/20.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText etUser;
    private EditText etPassword;
    private EditText etAddress;
    private EditText etBankCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        etUser = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etBankCard = (EditText) findViewById(R.id.etBankCard);
        findViewById(R.id.finishRegister).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        NetConnect net = new NetConnect(this);
        switch (v.getId()) {
            case R.id.finishRegister:
                if (TextUtils.isEmpty(etAddress.getText().toString()) || TextUtils.isEmpty(etBankCard.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString()) || TextUtils.isEmpty(etUser.getText().toString())) {
                    Toast.makeText(this, "注册信息不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                final ProgressDialog pdReg = ProgressDialog.show(this, "注册中", "正在连接服务器");
                String urlRegister = null;
                try {
                    urlRegister = Config.REGISTER + "name=" + etUser.getText().toString() + "&passWord=" + etPassword.getText().toString() + "&address=" + URLEncoder.encode(etAddress.getText().toString(), "UTF-8") + "&card=" + etBankCard.getText().toString();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                net.LoadData(urlRegister, new NetConnect.Success() {
                    @Override
                    public void OnSuccess(JSONObject response) {
                        try {
                            if (response.getInt("status") == 1) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerSuccess), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerFail), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pdReg.dismiss();
                        finish();
                    }
                }, new NetConnect.Failed() {
                    @Override
                    public void OnFail() {
                        pdReg.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerFail), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btnBack:
                finish();
                break;
            default:
                break;
        }
    }
}
