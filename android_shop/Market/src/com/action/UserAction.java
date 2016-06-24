package com.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.dao.impl.UserDaoImpl;
import com.models.Success;

public class UserAction {
	private String address;
	private String card;
	private int productid;
	private int num;
	private int userid;
	private String name;
	private String passWord;
	private int vipLevel;
	private double money;
	private String pickName;
	private UserDaoImpl userdao = new UserDaoImpl();

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getPickName() {
		return pickName;
	}

	public void setPickName(String pickName) {
		this.pickName = pickName;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getUserid() {
		return userid;
	}

	// 正式函数
	// 放进购物车
	public void putInCar() {
		setStatus(userdao.putInCar(userid, productid, num));
	}

	// 登陆
	public void login() {
		System.out.println(name + " " + passWord);
		Object o = userdao.login(name, passWord);
		String s = JSON.toJSONString(o, true);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 注册
	public void register() {
		System.out.println(name + " " + passWord);
		setStatus(userdao.register(name, passWord, address, card));
	}

	// 判断是否是vip
	public void isVip() {
		System.out.println(userid);
		setStatus(userdao.isVIP(userid));
	}

	// 设置vip
	public void setVip() {
		System.out.println(vipLevel);
		setStatus(userdao.setVIP(userid, money));
	}

	// 获取vip等级
	public void getVip() {
		int result = userdao.getVIP(userid);
		int status = (Integer) JSON.toJSON(result);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(status);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 更给地址
	public void changeAddress() {
		String new_name = null;
		try {
			new_name = URLDecoder.decode(address, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setStatus(userdao.changeAddress(userid, new_name));
	}

	// 更改银行卡
	public void changeBankCard() {
		setStatus(userdao.changeBankCard(userid, card));
	}

	// 更改用户名
	public void changeName() {
		setStatus(userdao.changeName(userid, name));
	}

	// 改密码
	public void changePassword() {
		setStatus(userdao.changePassword(userid, passWord));
	}

	// 消费减少钱数
	public void changeMoney() {
		setStatus(userdao.changeMoney(userid, productid, money, num));
	}

	// 更改昵称
	public void changePickName() {
		String new_name = null;
		try {
			new_name = URLDecoder.decode(pickName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setStatus(userdao.changePickName(userid, new_name));
	}

	// 获取用户信息
	public void getUser() {
		HttpServletResponse response = ServletActionContext.getResponse();
		Object o = userdao.getUser(userid);
		String s = JSON.toJSONString(o, true);
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//充值
	public void chongzhi() {
		setStatus(userdao.chongzhi(userid, passWord, money));
	}
	
	// 结算购物车
	public void clearCar() {
		setStatus(userdao.clearCar(userid, money));
	}

	// 删除购物车中的一个物品
	public void deleteOneCar() {
		setStatus(userdao.deleteOneCar(userid, productid));
	}

	// 返回状态（成功1 失败0）
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