package com.salehelper.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.salehelper.activity.ChangeActivity;
import com.salehelper.activity.ChongzhiActivity;
import com.salehelper.activity.LoginActivity;
import com.salehelper.activity.ViperActivity;
import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户页面
 * Created by longlong on 2015/3/3.
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    private boolean IF_FINISH = false;
    private static final int SET_PASSWORD = 2;
    private static final int SET_PICKNAME = 3;
    private static final int SET_ADDRESS = 4;
    private static final int SET_CARD = 5;
    private static final int SET_MONEY = 6;
    private TextView mTextPickname;  //用户昵称
    private TextView mTextMoney;  //用户money
    private static final String TAG = "com.salehelper.UserFragment";
    private static final int UPDATE_UI = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    JSONObject response = (JSONObject) msg.obj;
                    try {
                        mTextPickname.setText(response.getString("pickName"));
                        mTextMoney.setText("余额：" + response.getDouble("money") + "元");
                        setVipImage(response.getInt("vip"));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        initView(v);
        update();
        return v;
    }

    public void initView(View v) {
        v.findViewById(R.id.userButton).setOnClickListener(this);
        v.findViewById(R.id.settingVIP).setOnClickListener(this);
        v.findViewById(R.id.settingPassword).setOnClickListener(this);
        v.findViewById(R.id.settingPickname).setOnClickListener(this);
        v.findViewById(R.id.btnRefresh).setOnClickListener(this);
        v.findViewById(R.id.settingAddress).setOnClickListener(this);
        v.findViewById(R.id.settingCard).setOnClickListener(this);
        v.findViewById(R.id.settingMoney).setOnClickListener(this);
        mTextPickname = (TextView) v.findViewById(R.id.userName);
        mTextMoney = (TextView) v.findViewById(R.id.moneyNum);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userButton:
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(loginIntent, 1);
                break;
            case R.id.settingVIP:
                if (Config.getCachedUserid(getActivity()) == -1) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent settingVipIntent = new Intent(getActivity(), ViperActivity.class);
                startActivityForResult(settingVipIntent, 1);
                break;
            case R.id.settingPickname:
                if (Config.getCachedUserid(getActivity()) == -1) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent settingPicknameIntent = new Intent(getActivity(), ChangeActivity.class);
                settingPicknameIntent.putExtra("type", SET_PICKNAME);
                startActivityForResult(settingPicknameIntent, 1);
                break;
            case R.id.settingPassword:
                if (Config.getCachedUserid(getActivity()) == -1) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent settingPasswordIntent = new Intent(getActivity(), ChangeActivity.class);
                settingPasswordIntent.putExtra("type", SET_PASSWORD);
                startActivityForResult(settingPasswordIntent, 1);
                break;
            case R.id.settingAddress:
                if (Config.getCachedUserid(getActivity()) == -1) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent settingAddressIntent = new Intent(getActivity(), ChangeActivity.class);
                settingAddressIntent.putExtra("type", SET_ADDRESS);
                startActivityForResult(settingAddressIntent, 1);
                break;
            case R.id.settingCard:
                if (Config.getCachedUserid(getActivity()) == -1) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent settingCardIntent = new Intent(getActivity(), ChangeActivity.class);
                settingCardIntent.putExtra("type", SET_CARD);
                startActivityForResult(settingCardIntent, 1);
                break;
            case R.id.settingMoney:
                if (Config.getCachedUserid(getActivity()) == -1) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent settingMoneyIntent = new Intent(getActivity(), ChongzhiActivity.class);
                settingMoneyIntent.putExtra("number", "");
                startActivityForResult(settingMoneyIntent, 1);
                break;
            case R.id.btnRefresh:
                update();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            return;  //取消指令控制
        }
        switch (requestCode) {
            case 1:  //登陆成功处理
                Log.d(TAG, "userid = " + Config.getCachedUserid(getActivity()));
                update();
                break;
            default:
                break;
        }
    }

    //设置VIP等级的图片
    private void setVipImage(int viplevel) {
        Drawable nav_up;
        switch (viplevel) {
            case 0:
                nav_up = getResources().getDrawable(R.drawable.ic_user_growth_0);
                break;
            case 1:
                nav_up = getResources().getDrawable(R.drawable.ic_user_growth_1);
                break;
            case 2:
                nav_up = getResources().getDrawable(R.drawable.ic_user_growth_2);
                break;
            case 3:
                nav_up = getResources().getDrawable(R.drawable.ic_user_growth_3);
                break;
            case 4:
                nav_up = getResources().getDrawable(R.drawable.ic_user_growth_4);
                break;
            case 5:
                nav_up = getResources().getDrawable(R.drawable.ic_user_growth_5);
                break;
            case 6:
                nav_up = getResources().getDrawable(R.drawable.ic_user_growth_6);
                break;
            default:
                nav_up = getResources().getDrawable(R.drawable.ic_user_growth_0);
                break;
        }
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        mTextPickname.setCompoundDrawables(null, null, nav_up, null);
    }

    //更新页面UI
    public void update() {
        if (Config.getCachedUserid(getActivity()) == -1) {
            return;
        }
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "获取用户信息", "正在连接服务器");
        NetConnect net = new NetConnect(getActivity());
        net.LoadData(Config.GET_USER + "userid=" + Config.getCachedUserid(getActivity()), new NetConnect.Success() {
            @Override
            public void OnSuccess(JSONObject response) {
                Message message = new Message();
                message.what = UPDATE_UI;
                message.obj = response;
                mHandler.sendMessage(message);
                pd.dismiss();
            }
        }, new NetConnect.Failed() {
            @Override
            public void OnFail() {
                pd.dismiss();
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
