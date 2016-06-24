package com.dao;

import com.models.User;

public interface UserDao {
	User getUser(int userid); // 获取user信息

	User login(String name, String password); // 登陆

	boolean register(String name, String password, String address,
			String bankcard); // 注册 1 成功 0

	boolean isVIP(int userid); // 是否是VIP

	int getVIP(int userid); // 获取vip等级

	boolean setVIP(int userid, double money); // 设置VIP （VIP用百分比的折扣表示）

	boolean update(int userid, String name, String new_password, int sale,
			double money, String pickName); // 更新用户数据(改名，改密码，改会员，改多少钱)

	boolean changeName(int userid, String name); // 改名

	boolean changePassword(int userid, String new_password); // 改密码

	boolean changeMoney(int userid, int productid, double money, int num); // 改钱数

	boolean changePickName(int userid, String pickName);// 改昵称

	boolean changeAddress(int userid, String address); // 改地址

	boolean changeBankCard(int userid, String card); // 改银行卡

	boolean putInCar(int userid, int productid, int num); // 放进购物车

	boolean clearCar(int userid, double money); // 结算购物车

	boolean deleteOneCar(int userid, int productid); // 删除购物车中的一项

	boolean chongzhi(int userid, String passWord, double money);
}
