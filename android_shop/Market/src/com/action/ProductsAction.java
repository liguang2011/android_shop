package com.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.dao.impl.ProductsDaoImpl;
import com.models.Success;

public class ProductsAction {

	private int userid; // 购物车列表获取需要的userid
	private int productid; // 商品id
	private String name; // 商品名
	private double price; // 商品价格
	private String describe; // 商品描述
	private int num; // 商品数量 （从库存获取的时候是商品的数量，其余的时候存的是购买的数量）
	private String image; // 商品图片的url
	private ProductsDaoImpl productdao = new ProductsDaoImpl(); // 实例化一个impl对象
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	// 正式函数
	//获取购物车列表
	public void getShoppingCarList() {
		String s = JSON.toJSONString(productdao.getShoppingCarList(userid),
				true);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//获取商品信息
	public void getProduct() {
		Object o = productdao.getProduct(productid);
		String s = JSON.toJSONString(o, true);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//更改商品数量
	public void changeNum() {
		setStatus(productdao.changeNum(productid, num));
	}
	//获取商品库存
	public void getProductNumber() {
		int result = productdao.getProductNumber();
		Success o = new Success(result);
		String s = JSON.toJSONString(o, true);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//获取商品列表
	public void getProductList() {
		String s = JSON.toJSONString(productdao.getProductList(), true);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//获取搜索到的商品列表
	public void getSearchProduct() {
		String new_name = null;
		try {
			new_name = URLDecoder.decode(name, "UTF-8");
			System.out.println(new_name);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String s = JSON.toJSONString(productdao.getSearchProduct(new_name),
				true);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//通用函数，上面是否成功返回类型的都调用此函数返回json串  成功返回1 失败返回0
	private void setStatus(boolean flag) {
		int result;
		if (flag) {
			result = 1;
		} else {
			result = 0;
		}
		Success o = new Success(result);
		String s = JSON.toJSONString(o, true);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
