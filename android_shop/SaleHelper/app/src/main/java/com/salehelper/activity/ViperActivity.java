package com.salehelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 */
public class ViperActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viper);
        initView();
    }

    private void initView() {
        findViewById(R.id.btnBack).setOnClickListener(this);
        ListView list = (ListView) findViewById(R.id.viperList);
        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.viper_item, new String[]{"vipLevel", "vipMoney"}, new int[]{R.id.vipLevel, R.id.vipMoney});
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getData() {
        String[] levelStrings = getResources().getStringArray(R.array.vipLevelList);
        String[] moneyStrings = getResources().getStringArray(R.array.vipMoneyList);
        for (int i = 0; i < levelStrings.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("vipLevel", levelStrings[i]);
            map.put("vipMoney", moneyStrings[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final ProgressDialog pd = ProgressDialog.show(this, "办理中", "正在办理...");
        NetConnect net = new NetConnect(this);
        double money = (position + 1) * 100;
        String url = Config.CHANGE_VIP + "userid=" + Config.getCachedUserid(getApplicationContext()) + "&money=" + money;
        net.LoadData(url, new NetConnect.Success() {
            @Override
            public void OnSuccess(JSONObject response) {
                try {
                    if (response.getInt("status") == 1) {
                        pd.dismiss();
                        setResult(RESULT_OK);
                        if (Config.getCachedVip(getApplicationContext()) == position + 1) {
                            Toast.makeText(getApplicationContext(), "续费成功", Toast.LENGTH_SHORT).show(); //成功
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.vipSetSuccess, Toast.LENGTH_SHORT).show(); //成功
                            Config.cacheVip(getApplicationContext(), position + 1);
                        }
                        finish();
                    } else {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), R.string.vipSetFailed, Toast.LENGTH_SHORT).show();  //失败
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.vipSetFailed, Toast.LENGTH_SHORT).show();  //失败
                    e.printStackTrace();
                }
            }
        }, new NetConnect.Failed() {
            @Override
            public void OnFail() {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), R.string.vipSetFailed, Toast.LENGTH_SHORT).show();  //失败
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}
