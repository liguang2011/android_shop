package com.salehelper.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.salehelper.fragment.FoundFragment;
import com.salehelper.fragment.HomePageFragment;
import com.salehelper.fragment.ShoppingCarFragment;
import com.salehelper.fragment.UserFragment;
import com.salehelper.salehelper.R;

import java.util.ArrayList;


/**
 * 导航页面 Activity
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private long firsttime; // 监听两次返回
    private ArrayList<ImageView> mImgList = new ArrayList<ImageView>(); //点击更换颜色  把四个按钮的图片放进来
    private ArrayList<TextView> mTextList = new ArrayList<TextView>(); //点击更换颜色  把四个按钮的文本放进来
    private HomePageFragment mHomeFragment;
    private ShoppingCarFragment mShoppingCarFragment;
    private FoundFragment mFoundFragment;
    private UserFragment mUserFragment;
//    Fragment now_fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setCurrentTab(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initView() {
        ImageView mImgHome = (ImageView) findViewById(R.id.imgHome);
        ImageView mImgFound = (ImageView) findViewById(R.id.imgFound);
        ImageView mImgCar = (ImageView) findViewById(R.id.imgCar);
        ImageView mImgUser = (ImageView) findViewById(R.id.imgUser);
        mImgList.add(mImgHome);
        mImgList.add(mImgFound);
        mImgList.add(mImgCar);
        mImgList.add(mImgUser);

        TextView mTextHome = (TextView) findViewById(R.id.tvHome);
        TextView mTextFound = (TextView) findViewById(R.id.tvFound);
        TextView mTextCar = (TextView) findViewById(R.id.tvCar);
        TextView mTextUser = (TextView) findViewById(R.id.tvUser);

        mTextList.add(mTextHome);
        mTextList.add(mTextFound);
        mTextList.add(mTextCar);
        mTextList.add(mTextUser);

        LinearLayout mBtnHome = (LinearLayout) findViewById(R.id.btnHome);
        mBtnHome.setOnClickListener(this);
        LinearLayout mBtnFound = (LinearLayout) findViewById(R.id.btnFound);
        mBtnFound.setOnClickListener(this);
        LinearLayout mBtnCar = (LinearLayout) findViewById(R.id.btnCar);
        mBtnCar.setOnClickListener(this);
        LinearLayout mBtnUser = (LinearLayout) findViewById(R.id.btnUser);
        mBtnUser.setOnClickListener(this);

    }


    private void setCurrentTab(int c) {
        FragmentTransaction trasection = getSupportFragmentManager()
                .beginTransaction();
        for (int i = 0; i < 4; i++) {
            if (i == c) {
                mImgList.get(i).setSelected(true);
                mTextList.get(i).setSelected(true);
            } else {
                mImgList.get(i).setSelected(false);
                mTextList.get(i).setSelected(false);
            }
        }
        //依次对应4个fragment
        switch (c) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomePageFragment();
                }
                trasection.replace(R.id.fragmentContainer, mHomeFragment, "Home").commit();
//                if (now_fragment == null) {
//                    now_fragment = mHomeFragment;
//                    break;
//                } else {
//                    trasection.hide(now_fragment).show(mHomeFragment);
//                    now_fragment = mHomeFragment;
//                }
                break;
            case 1:
                if (mFoundFragment == null) {
                    mFoundFragment = new FoundFragment();
                }
                trasection.replace(R.id.fragmentContainer, mFoundFragment, "Found").commit();
//                trasection.hide(now_fragment).show(mFoundFragment);
//                now_fragment = mFoundFragment;
                break;
            case 2:
                if (mShoppingCarFragment == null) {
                    mShoppingCarFragment = new ShoppingCarFragment();
                }
                trasection.replace(R.id.fragmentContainer, mShoppingCarFragment, "Car").commit();
//                trasection.hide(now_fragment).show(mShoppingCarFragment);
//                now_fragment = mShoppingCarFragment;
                break;
            case 3:
                if (mUserFragment == null) {
                    mUserFragment = new UserFragment();
                }
                trasection.replace(R.id.fragmentContainer, mUserFragment, "User").commit();
//                trasection.hide(now_fragment).show(mUserFragment);
//                now_fragment = mUserFragment;
                break;
            default:
                break;
        }
    }

    //点击两次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - firsttime < 3000) {
                finish();
                return true;
            } else {
                firsttime = System.currentTimeMillis();
                Toast.makeText(this, "再点一次退出", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHome:
                setCurrentTab(0);
                break;
            case R.id.btnFound:
                setCurrentTab(1);
                break;
            case R.id.btnCar:
                setCurrentTab(2);
                break;
            case R.id.btnUser:
                setCurrentTab(3);
                break;
        }
    }
}
