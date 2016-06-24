package com.salehelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * 登陆Activity
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "com.salehelper.activity.LoginActivity";
    private EditText etUser;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnRegister).setOnClickListener(this);
        etUser = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    @Override
    public void onClick(View v) {
        NetConnect net = new NetConnect(this);
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnLogin:
                if (TextUtils.isEmpty(etUser.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
                    break;
                }
                final ProgressDialog pdLogin = ProgressDialog.show(this, "登陆中", "正在连接服务器");
                String urlLogin = Config.LOGIN + "name=" + etUser.getText().toString() + "&passWord=" + etPassword.getText().toString();
                Log.d(TAG, urlLogin);
                net.LoadData(urlLogin, new NetConnect.Success() {
                    @Override
                    public void OnSuccess(JSONObject response) {
                        try {
                            Config.cacheUserid(getApplicationContext(), response.getInt("userid"));
                            Config.cacheVip(getApplicationContext(), response.getInt("vip"));
                            Config.cacheAddress(getApplicationContext(), response.getString("address"));
                            Config.cacheCard(getApplicationContext(), response.getString("card"));
                            Config.cacheMoney(getApplicationContext(), response.getString("money"));
                            Log.i("波波波波", response.getString("money"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pdLogin.dismiss();
                        setResult(RESULT_OK);
                        finish();
                    }
                }, new NetConnect.Failed() {
                    @Override
                    public void OnFail() {
                        pdLogin.dismiss();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.loginFail), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btnRegister:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
