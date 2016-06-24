package com.models;

public class User {
	private int userid;
	private String name;
	private String passWord;
	private int vip;
	private double money;
	private String pickName;
	private String address;
	private String card;

	public User(int userid, String name, String passWord, int vip,
			double money, String pickName, String address, String card) {
		this.userid = userid;
		this.name = name;
		this.passWord = passWord;
		this.vip = vip;
		this.money = money;
		this.pickName = pickName;
		this.address = address;
		this.card = card;
	}


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



	public String getPickName() {
		return pickName;
	}

	public void setPickName(String pickName) {
		this.pickName = pickName;
	}


	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
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

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

}
