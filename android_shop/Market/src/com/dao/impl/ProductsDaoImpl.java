package com.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dao.ProductsDao;
import com.models.Product;
import com.utils.SQLUtil;

public class ProductsDaoImpl implements ProductsDao {

	@Override
	public Product getProduct(int productid) {
		ResultSet rs = SQLUtil.executeQuery(
				"select * from products where id = ?",
				new Object[] { productid });
		try {
			rs.next();
			Product o = new Product(productid, rs.getString("name"),
					rs.getDouble("price"), rs.getString("describe"),
					rs.getInt("num"), rs.getString("image"));
			return o;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean changeNum(int productid, int num) {
		int result;
		int inventory;
		ResultSet rs = SQLUtil.executeQuery(
				"select * from products where id = ?",
				new Object[] { productid });
		try {
			rs.next();
			inventory = rs.getInt("num");
			if (num > inventory) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		result = SQLUtil.executeUpdate(
				"update products set num = ? where id = ?", new Object[] {
						inventory - num, productid });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getProductNumber() {
		ResultSet rs = SQLUtil.executeQuery("select count(id) from products",
				new Object[] {});
		try {
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public ArrayList<Product> getProductList() {
		int num = getProductNumber();
		if (num == -1) {
			return null;
		}
		ArrayList<Product> list = new ArrayList<Product>();
		ResultSet rs = SQLUtil.executeQuery("select * from products",
				new Object[] {});
		try {
			while (rs.next()) {
				list.add(new Product(rs.getInt("id"), rs.getString("name"), rs
						.getDouble("price"), rs.getString("describe"), rs
						.getInt("num"), rs.getString("image")));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<Product> getSearchProduct(String name) {
		int num = getProductNumber();
		if (num == -1) {
			return null;
		}
		ArrayList<Product> list = new ArrayList<Product>();
		ResultSet rs = SQLUtil.executeQuery(
				"select * from products where name like CONCAT('%', ?,'%')",
				new Object[] { name });
		try {
			while (rs.next()) {
				list.add(new Product(rs.getInt("id"), rs.getString("name"), rs
						.getDouble("price"), rs.getString("describe"), rs
						.getInt("num"), rs.getString("image")));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<Product> getShoppingCarList(int userid) {
		ArrayList<Product> list = new ArrayList<Product>();
		ResultSet rs = SQLUtil.executeQuery(
				"select * from car where userid = ?", new Object[] { userid });
		try {
			while (rs.next()) {
				ResultSet ts = SQLUtil.executeQuery(
						"select * from products where id = ?",
						new Object[] { rs.getInt("productid") });
				ts.next();
				list.add(new Product(ts.getInt("id"), ts.getString("name"), ts
						.getDouble("price"), ts.getString("describe"), rs.getInt("num"), ts.getString("image")));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
