package com.dao;

import java.util.ArrayList;

import com.models.Product;

public interface ProductsDao {
	ArrayList<Product> getProductList();
	
	Product getProduct(int productid); // 获取商品信息
	
	ArrayList<Product> getSearchProduct(String name); //搜索商品信息

	boolean changeNum(int productid, int num); // 更改商品库存
	
	int getProductNumber(); //获取商品总数
	
	ArrayList<Product> getShoppingCarList(int userid);
}
