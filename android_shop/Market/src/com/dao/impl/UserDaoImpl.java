package com.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dao.UserDao;
import com.models.User;
import com.utils.SQLUtil;

public class UserDaoImpl implements UserDao {

	@Override
	public User login(String name, String password) {
		ResultSet rs = SQLUtil.executeQuery(
				"select * from user where name = ? and password = ?",
				new Object[] { name, password });
		try {
			if (rs.next()) {
				int userid = (Integer) rs.getObject("id");
				String username = (String) rs.getObject("name");
				String passWord = (String) rs.getObject("password");
				int vip = (Integer) rs.getObject("vip");
				double money = (Double) rs.getObject("money");
				String pickName = (String) rs.getObject("pickname");
				String address = (String) rs.getObject("address");
				String bankcard = (String) rs.getObject("bankcard");
				User user = new User(userid, username, passWord, vip, money,
						pickName, address, bankcard);
				return user;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean register(String name, String password, String address,
			String bankcard) {
		ResultSet rs = SQLUtil.executeQuery(
				"select * from user where name = ?", new Object[] { name });
		try {
			if (rs.next() == true) {
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
		int result = SQLUtil
				.executeUpdate(
						"insert into user (name,password,vip,money,pickname,address,bankcard) values(?,?,?,?,?,?,?)",
						new Object[] { name, password, 0, 0, name, address,
								bankcard }); // 成功返回1
		// 失败返回0
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isVIP(int userid) {
		ResultSet rs = SQLUtil.executeQuery("select * from user where id = ?",
				new Object[] { userid });
		try {
			rs.next();
			int num = (Integer) rs.getObject("vip");
			if (num == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setVIP(int userid, double money) {
		int level = (int) money / 100;
		int result;
		ResultSet rs = SQLUtil.executeQuery("select * from user where id = ?",
				new Object[] { userid });
		try {
			rs.next();
			if (money < rs.getDouble("money")) {
				result = SQLUtil.executeUpdate(
						"update user set money = ?,vip = ? where id = ?",
						new Object[] { rs.getDouble("money") - money, level,
								userid });
			} else {
				result = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean update(int userid, String name, String new_password,
			int sale, double money, String pickName) {
		return false;
	}

	@Override
	public boolean changeName(int userid, String name) {
		int result = SQLUtil.executeUpdate(
				"update user set name = ? where id = ?", new Object[] { name,
						userid });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean changePassword(int userid, String new_password) {
		int result = SQLUtil.executeUpdate(
				"update user set password = ? where id = ?", new Object[] {
						new_password, userid });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean changeMoney(int userid, int productid, double money, int num) {
		int result;
		ResultSet rs = SQLUtil.executeQuery("select * from user where id = ?",
				new Object[] { userid });
		ResultSet rp = SQLUtil.executeQuery(
				"select * from products where id = ?",
				new Object[] { productid });
		try {
			rs.next();
			rp.next();
			if (money < rs.getDouble("money") && rp.getInt("num") - num > 0) {
				result = SQLUtil.executeUpdate(
						"update user set money = ? where id = ?", new Object[] {
								rs.getDouble("money") - money, userid });
				result = SQLUtil.executeUpdate(
						"update products set num = ? where id = ?",
						new Object[] { rp.getInt("num") - num, productid });
			} else {
				result = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean changePickName(int userid, String pickName) {
		int result = SQLUtil.executeUpdate(
				"update user set pickname = ? where id = ?", new Object[] {
						pickName, userid });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int getVIP(int userid) {
		ResultSet rs = SQLUtil.executeQuery("select * from user where id = ?",
				new Object[] { userid });
		try {
			rs.next();
			int num = (Integer) rs.getObject("vip");
			return num;
		} catch (SQLException e) {
			return -1;
		}
	}

	@Override
	public User getUser(int userid) {
		ResultSet rs = SQLUtil.executeQuery("select * from user where id = ?",
				new Object[] { userid });
		try {
			if (rs.next()) {
				int id = (Integer) rs.getObject("id");
				String username = (String) rs.getObject("name");
				String passWord = (String) rs.getObject("password");
				int vip = (Integer) rs.getObject("vip");
				double money = (Double) rs.getObject("money");
				String pickName = (String) rs.getObject("pickname");
				String address = (String) rs.getObject("address");
				String bankcard = (String) rs.getObject("bankcard");
				User user = new User(userid, username, passWord, vip, money,
						pickName, address, bankcard);
				return user;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean putInCar(int userid, int productid, int num) {
		if (num == 0) {
			deleteOneCar(userid, productid);
			return true;
		}
		int result;
		ResultSet rs = SQLUtil.executeQuery(
				"select * from car where productid = ? and userid = ?",
				new Object[] { productid, userid });
		try {
			if (rs.next() == false) {
				result = SQLUtil.executeUpdate(
						"insert into car (userid,productid,num) values(?,?,?)",
						new Object[] { userid, productid, num });
			} else {
				result = SQLUtil
						.executeUpdate(
								"update car set num = ? where userid = ? and productid = ?",
								new Object[] { num, userid, productid });
			}

			if (result == 1) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean clearCar(int userid, double money) {
		// 检查用户钱够不够，然后检查商品够不够
		// rs存储唯一用户信息，ts存储购物车中的都有什么商品，productRS存每一个商品的信息(现找现查)
		try {
			ResultSet rs = SQLUtil.executeQuery(
					"select * from user where id = ?", new Object[] { userid });
			rs.next();
			if (rs.getDouble("money") < money) {
				return false;
			}
			ResultSet ts = SQLUtil.executeQuery(
					"select * from car where userid = ?",
					new Object[] { userid });
			ResultSet productRS;
			// 获取了商品id和商品购买数量存在ts中，开始检查商品库存
			while (ts.next()) {
				productRS = SQLUtil.executeQuery(
						"select * from products where id = ?",
						new Object[] { ts.getInt("productid") });
				productRS.next();
				if (productRS.getInt("num") < ts.getInt("num")) {
					// 任意商品库存不够都要返回false
					return false;
				}
			}
			// 检查之后的开始扣钱
			ts = SQLUtil.executeQuery("select * from car where userid = ?",
					new Object[] { userid });
			while (ts.next()) {
				productRS = SQLUtil.executeQuery(
						"select * from products where id = ?",
						new Object[] { ts.getInt("productid") });
				productRS.next();
				SQLUtil.executeUpdate(
						"update products set num = ? where id = ?",
						new Object[] {
								productRS.getInt("num") - ts.getInt("num"),
								ts.getInt("productid") });
			}
			int result = SQLUtil.executeUpdate(
					"update user set money = ? where id = ?",
					new Object[] { rs.getDouble("money") - money,
							rs.getInt("id") });
			result = SQLUtil.executeUpdate("delete from car where userid = ?",
					new Object[] { userid });
			if (result == 0) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteOneCar(int userid, int productid) {
		ResultSet rs = SQLUtil.executeQuery(
				"select * from car where userid = ? and productid = ?",
				new Object[] { userid, productid });
		try {
			if (!rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		int result = SQLUtil.executeUpdate(
				"delete from car where userid = ? and productid = ?",
				new Object[] { userid, productid });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean changeAddress(int userid, String address) {
		int result = SQLUtil.executeUpdate(
				"update user set address = ? where id = ?", new Object[] {
						address, userid });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean changeBankCard(int userid, String card) {
		int result = SQLUtil.executeUpdate(
				"update user set bankcard = ? where id = ?", new Object[] {
						card, userid });
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean chongzhi(int userid, String passWord, double money) {
		ResultSet rs = SQLUtil.executeQuery(
				"select * from user where id = ? and password = ?",
				new Object[] { userid, passWord });
		try {
			if (!rs.next()) {
				return false;
			}
			int result = SQLUtil.executeUpdate(
					"update user set money = ? where id = ?", new Object[] {
							money + rs.getDouble("money"), rs.getInt("id") });
			if (result != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

}
