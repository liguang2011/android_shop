package com.salehelper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.salehelper.models.Product;
import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends BaseAdapter {
    private NetConnect net;
    private Context context;
    private ArrayList<Product> mList;
    private static final String TAG = "com.salehelper.productlistadapter";

    public ProductListAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        mList = list;
        net = new NetConnect(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getProductid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_item, null);
            viewHolder.productIcon = (ImageView) convertView.findViewById(R.id.productImage);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productInventory = (TextView) convertView.findViewById(R.id.productInventory);
            viewHolder.productNumBefore = (TextView) convertView.findViewById(R.id.productNumBefore);
            viewHolder.productNumAfter = (TextView) convertView.findViewById(R.id.productNumAfter);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        net.LoadImage(viewHolder.productIcon, mList.get(position).getImage()); //加载图片
        Log.i("TAG", mList.get(position).getImage());
        viewHolder.productName.setText(mList.get(position).getName());
        viewHolder.productInventory.setText("库存：" + mList.get(position).getNum());
        viewHolder.productNumBefore.setText("原价：" + mList.get(position).getPrice());
        viewHolder.productNumAfter.setText("会员：" + mList.get(position).getPrice() * ((10 - Config.getCachedVip(context))) / 10);
        return convertView;
    }

    static class ViewHolder {
        public ImageView productIcon;
        public TextView productName;
        public TextView productInventory;
        public TextView productNumBefore;
        public TextView productNumAfter;
    }
}
