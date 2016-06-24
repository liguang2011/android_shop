package com.salehelper.net;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * 配置类  其中各种配置字符串
 * 配置说明：约定配置status=1为成功 status=0位失败
 * 返回对象的请求 失败返回null volley异常会处理 不用特殊处理
 */
public class Config {
    // public static final String url = "http://192.168.1.108:8080/Market/";   //后台url
    public static final String url = "http://172.20.10.2:8080/Market/";
    //有关user操作的所有url
    public static final String LOGIN = url + "user_login?";  //   name=XXX&passWord=XXX
    public static final String GET_USER = url + "user_getUser?";   //userid=XXXr
    public static final String REGISTER = url + "user_register?";  //name=XXX&passWord=XXXX&address=XXXXX&bankcard=XXXX
    public static final String CHANGE_PASSWORD = url + "user_changePassword?";//userid=XXX&passWord=XXX
    public static final String CHANGE_PICKNAME = url + "user_changePickName?"; //userid=XXX&pickName=XXX
    public static final String CHANGE_MONEY = url + "user_changeMoney?"; //userid=XXX&poductid=xxxx&money=XXX&num=XXX
    public static final String CHANGE_VIP = url + "user_setVip?"; //userid=XXXX&money=XXXX
    public static final String CHANGE_ADDRESS = url + "user_changeAddress?"; //userid=XXXX&address=XXXX
    public static final String CHANGE_CARD = url + "user_changeBankCard?";//userid=XXXX&card=XXXX
    public static final String PUT_IN_CAR = url + "user_putInCar?"; //userid=XXX&productid=XXXX&num=XXXX
    public static final String CLEAR_CAR = url + "user_clearCar?";//userid=XXX&money=XXXX
    public static final String DELETE_CAR = url + "user_deleteOneCar?"; //userid=XXXX&productid=XXXX
    public static final String CHONGZHI = url + "user_chongzhi?"; //userid=XXX&passWord=XXXX&money=XXXX
    //Android 端的配置
    public static final String APP_ID = "com.salehelper.salehelper";
    public static final String KEY_USERID = "userid";
    public static final String KEY_VIP = "vip";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CARD = "card";
    public static final String KEY_MONEY = "money";
    //有关products所有操作
    public static final String GET_PRODUCT = url + "products_getProduct?"; //参数是productid=XXX
    public static final String GET_PRODUCT_LIST = url + "products_getProductList?"; //没有参数
    public static final String GET_SEARCH = url + "products_getSearchProduct?"; //name=XXX
    public static final String GET_CAR = url + "products_getShoppingCarList?"; //userid=XXX

    //获取缓存的userid
    public static int getCachedUserid(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getInt(KEY_USERID, -1);  //获取token
    }

    //缓存userid
    public static void cacheUserid(Context context, int userid) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putInt(KEY_USERID, userid);
        e.commit();
    }

    //获取缓存的address
    public static String getCachedAddress(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_ADDRESS, "");  //获取token
    }

    //缓存address
    public static void cacheAddress(Context context, String address) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_ADDRESS, address);
        e.commit();
    }

    //获取银行卡号
    public static String getCachedCard(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_CARD, "");
    }

    //缓存银行卡号
    public static void cacheCard(Context context, String card) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_CARD, card);
        e.commit();
    }

    //获取钱数
    public static String getCachedMoney(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_MONEY, "");
    }

    //缓存钱数
    public static void cacheMoney(Context context, String money) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putString(KEY_MONEY, money);
        e.commit();
    }


    //获取缓存的vip等级
    public static int getCachedVip(Context context) {
        return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getInt(KEY_VIP, 0);  //获取token
    }

    //缓存vip
    public static void cacheVip(Context context, int userVipLevel) {
        SharedPreferences.Editor e = context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit();
        e.putInt(KEY_VIP, userVipLevel);
        e.commit();
    }

}
