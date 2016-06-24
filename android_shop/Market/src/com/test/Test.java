package com.test;

import com.dao.ProductsDao;
import com.dao.impl.ProductsDaoImpl;


public class Test {
	public static void main(String[] args) {
		ProductsDaoImpl dao = new ProductsDaoImpl();
		dao.getProductNumber();
	}
}
