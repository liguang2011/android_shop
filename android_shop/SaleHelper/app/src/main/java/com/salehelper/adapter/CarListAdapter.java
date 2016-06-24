package com.salehelper.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.tv.TvContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.salehelper.models.Product;
import com.salehelper.net.Config;
import com.salehelper.net.NetConnect;
import com.salehelper.salehelper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 */
public class CarListAdapter extends BaseAdapter {

    private NetConnect net;
    private Context context;
    private ArrayList<Product> mList;

    public CarListAdapter(Context context, ArrayList<Product> list) {
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.car_item, null);

            viewHolder.productIcon = (ImageView) convertView.findViewById(R.id.productImage);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
            viewHolder.productInventory = (TextView) convertView.findViewById(R.id.productInventory);
            viewHolder.productNumBefore = (TextView) convertView.findViewById(R.id.productNumBefore);
            viewHolder.productNumAfter = (TextView) convertView.findViewById(R.id.productNumAfter);
            viewHolder.btnDelete = (TextView) convertView.findViewById(R.id.btnDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        net.LoadImage(viewHolder.productIcon, mList.get(position).getImage()); //加载图片
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = ProgressDialog.show(context,"删除中","正在删除...");
                String url = Config.DELETE_CAR + "userid=" + Config.getCachedUserid(context) + "&productid=" + mList.get(position).getProductid();
                net.LoadData(url, new NetConnect.Success() {
                    @Override
                    public void OnSuccess(JSONObject response) {
                        try {
                            if (response.getInt("status") == 1) {
                                mList.remove(position);
                                change();
                                pd.dismiss();
                                Toast.makeText(context,R.string.deleteSuccess,Toast.LENGTH_SHORT).show();
                            } else {
                                pd.dismiss();
                                Toast.makeText(context,R.string.deleteFail,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new NetConnect.Failed() {
                    @Override
                    public void OnFail() {
                        pd.dismiss();
                        Toast.makeText(context,R.string.deleteFail,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        viewHolder.productName.setText(mList.get(position).getName());
        viewHolder.productInventory.setText("购买数：" + mList.get(position).getNum());
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
        public TextView btnDelete;
    }

    private void change() {
        this.notifyDataSetChanged();
    }
}
