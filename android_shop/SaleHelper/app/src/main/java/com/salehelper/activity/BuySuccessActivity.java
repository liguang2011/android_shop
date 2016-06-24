package com.salehelper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.salehelper.net.Config;
import com.salehelper.salehelper.R;

import org.w3c.dom.Text;


public class BuySuccessActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buysuccess);
        findViewById(R.id.btnBack).setOnClickListener(this);//绑定监听器
        double product_price = getIntent().getDoubleExtra("product_price", -1);
        TextView buyOne;
        TextView buyTwo;
        buyOne = (TextView) findViewById(R.id.buyOne);
        buyTwo = (TextView) findViewById(R.id.buyTwo);
        buyOne.setText("您花费了  " + product_price + "  购买了此物品");
        buyTwo.setText("正在火速送往 " + Config.getCachedAddress(this));
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
