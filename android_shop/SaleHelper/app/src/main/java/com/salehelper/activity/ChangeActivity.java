package com.salehelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

/*
 * 修改页面  activity
 */
public class ChangeActivity extends Activity implements View.OnClickListener {

    private EditText etChangeData;
    private int actType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changedata);
        intitView();
    }

    private void intitView() {
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnChangeData).setOnClickListener(this);
        etChangeData = (EditText) findViewById(R.id.etChangeData);
        actType = (int) getIntent().getSerializableExtra("type");
        switch (actType) {
            case 2:
                etChangeData.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                break;
            case 5:
                etChangeData.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btnChangeData:
                choose(actType, etChangeData.getText().toString());
                break;
        }
    }

    private void choose(int type, String str) {
        NetConnect net = new NetConnect(this);
        final ProgressDialog pd = ProgressDialog.show(this, "修改中", "正在修改");
        String url = null;
        switch (type) {
            case 2:
                url = Config.CHANGE_PASSWORD + "userid=" + Config.getCachedUserid(getApplicationContext()) + "&passWord=" + str;
                break;
            case 3:
                try {
                    url = Config.CHANGE_PICKNAME + "userid=" + Config.getCachedUserid(getApplicationContext()) + "&pickName=" + URLEncoder.encode(str, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    url = Config.CHANGE_ADDRESS + "userid=" + Config.getCachedUserid(getApplicationContext()) + "&address=" + URLEncoder.encode(str, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                url = Config.CHANGE_CARD + "userid=" + Config.getCachedUserid(getApplicationContext()) + "&card=" + str;
                break;
            default:
                url = null;
                break;
        }
        net.LoadData(url, new NetConnect.Success() {
            @Override
            public void OnSuccess(JSONObject response) {
                try {
                    if (response.getInt("status") == 1) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.changeSuccess), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.changeFailed), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        }, new NetConnect.Failed() {
            @Override
            public void OnFail() {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.changeFailed), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
