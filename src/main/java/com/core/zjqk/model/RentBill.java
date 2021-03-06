package com.core.zjqk.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.common.config.JdbcUtils;
import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.base.BaseRentBill;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class RentBill extends BaseRentBill<RentBill> {
	public static final RentBill dao = new RentBill();
	
	public Page<RentBill> page(Controller c){
		String select = "SELECT r.id,s.`name` sName,opening_date openingDate,close_date closeDate,"
				+ "r.ym_date ymDate,r.actual_income actualIncome,r.shop_income shopIncome ";
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		String classify = ShiroUtils.getClassify();
		sqlExceptSelect.append("FROM rent_bill r LEFT JOIN store s ON r.s_id = s.id WHERE r.flag = 1 AND s.classify='"+classify+"' ");
		String sName = c.getPara("sName");
		if(StringUtils.isNotBlank(sName)){
			sqlExceptSelect.append("AND INSTR(s.`name`, ?) ");
			params.add(sName);
	    }
		String ymData = c.getPara("ymData");
		if(StringUtils.isNotBlank(ymData)){
			sqlExceptSelect.append(" AND r.ym_date=? ");
			params.add(ymData);
		}
		String s = c.getParaToInt("sort",1) == 2 ? "ym_date" : "opening_date";
		sqlExceptSelect.append("ORDER BY " + s);
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	public ReturnMsg add(Controller c){
		Integer orgId = ShiroUtils.getOrgId();
		if(orgId == 1){
			boolean update = false;
			Connection conn = JdbcUtils.getConnection();
			PreparedStatement psmt = null;
			String ymData = c.getPara("ymData");
			if(StringUtils.isNotBlank(ymData)){
				String sql1 = "SELECT COUNT(1) count FROM rent_bill WHERE flag=1 AND ym_date=? AND s_id=?";
				String sql2 = "INSERT INTO rent_bill (s_id,ym_date) VALUES (?,?)";
				String sName = c.getPara("sName");
				if(StringUtils.isNotBlank(sName)){
					Integer sId = Store.dao.findStoreId(sName);
					Long count = this.findFirst(sql1, ymData, sId).getLong("count");
					if(count <= 0){
						try {
							psmt = conn.prepareStatement(sql2);
							psmt.setInt(1, sId);
							psmt.setString(2, ymData);
							psmt.executeUpdate();
						} catch (SQLException e) {
							//e.printStackTrace();
							System.out.println("添加营业额数据失败！");
						} finally {
							JdbcUtils.closeAll(null, null, psmt, conn);
						}
						update = true;
					}else{
						return ReturnMsg.REPEATERROR;
					}
				}else{
					List<Store> stores = Store.dao.list();
					for (Store store : stores) {
						Integer sId = store.get("sId");
						Long count = this.findFirst(sql1, ymData, sId).getLong("count");
						if(count <= 0){
							try {
								psmt = conn.prepareStatement(sql2);
								psmt.setInt(1, sId);
								psmt.setString(2, ymData);
								psmt.executeUpdate();
							} catch (SQLException e) {
								//e.printStackTrace();
								System.out.println("添加营业额数据失败！");
							} finally {
								JdbcUtils.closeAll(null, null, psmt, conn);
							}
						}
					}
					update = true;
				}
			}else{
				return ReturnMsg.YMEMPTY;
			}
			return update ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	public ReturnMsg saveOrUpdate(Controller c){
		boolean result = false;
		Double payableFee = 0.0;
		Double bdzj = 0.0;
		String tcbl = "";
		String id = c.getPara("id");
		String sName = c.getPara("sName");
		String ymDate = c.getPara("ymDate");
		//实际收入
		String actualIncome = c.getPara("actualIncome","0");
		//上报商场收入
		String shopIncome = c.getPara("shopIncome","0");
		String startTime = ymDate + "-01";
		
		// 获取指定年月的天数
		Integer year = Integer.valueOf(ymDate.substring(0, 4));
		Integer month = Integer.valueOf(ymDate.substring(5, 7));
		int days = Tools.getDayOfMonth(year, month);
		//String endTime = ymData + "-" + String.valueOf(days);
		
		//根据店铺名称和年月从 租金和物管费用配置表 查询对应店铺的 保底租金 和 提成租金 比例
		Long count = this.findFirst("SELECT COUNT(1) count FROM rent_opt ro LEFT JOIN store s ON ro.s_id=s.id "
				+ "WHERE SUBSTR(start_time,1,7)<=? AND SUBSTR(end_time,1,7)>=? AND s.`name`=? ", ymDate, ymDate, sName).getLong("count");
		String sql = "SELECT start_time startTime,end_time endTime,init_rent initRent,rent_point rentPoint "
				+ "FROM rent_opt ro LEFT JOIN store s ON ro.s_id=s.id "
				+ "WHERE SUBSTR(start_time,1,7)<=? AND SUBSTR(end_time,1,7)>=? AND s.`name`=? ORDER BY end_time";
		
		/*
		 * 1.没有对应记录，计算应补金额为0
		 * 2.查询到一条记录，计算应补金额=上报商场收入*提成租金比例-保底租金
		 * 3.查询到两条记录，保底租金和计算应补金额按两个时间段分开计算（整月的统计）
		 */
		if(count == 0){
			payableFee = 0.0;
		}else if(count == 1){
			RentBill rb = this.findFirst(sql, ymDate, ymDate, sName);
			bdzj = rb.getDouble("initRent");
			Double rentPoint = rb.getDouble("rentPoint");
			payableFee = Double.valueOf(shopIncome) * rentPoint - bdzj;
			tcbl = rentPoint.toString();
			payableFee = Double.valueOf(new java.text.DecimalFormat("#.00").format(payableFee));
			if(payableFee < 0.0){
				payableFee = 0.0;
			}
		}else{
			List<RentBill> list = this.find(sql, ymDate, ymDate, sName);
			Double initRent1 = 0.0;
			Double rentPoint1 = 0.0;
			String endTime1 = "";
			
			Double initRent2 = 0.0;
			Double rentPoint2 = 0.0;
			
			RentBill rb1 = list.get(0);
			RentBill rb2 = list.get(1);
			
			endTime1 = rb1.get("endTime");
			long l = Tools.getDaysByStrDateDiff(startTime, endTime1) + 1;
			
			initRent1 = rb1.getDouble("initRent");
			rentPoint1 = rb1.getDouble("rentPoint");
			
			initRent2 = rb2.getDouble("initRent");
			rentPoint2 = rb2.getDouble("rentPoint");
			bdzj = initRent1 / days * l + initRent2 / days * (days - l);
			tcbl = rentPoint1.toString() + " + " + rentPoint2.toString();
			payableFee = Double.valueOf(shopIncome) * rentPoint1 - initRent1 / days * l;
			payableFee = payableFee + Double.valueOf(shopIncome) * rentPoint2 - initRent2 / days * (days - l);
			payableFee = Double.valueOf(String.format("%.2f", payableFee));
			if(payableFee < 0.0){
				payableFee = 0.0;
			}
		}
		RentBill rent = new RentBill();
		rent.set("actual_income", actualIncome)
			.set("shop_income", shopIncome)
			.set("payable_fee", payableFee);
		if(StringUtils.isNoneBlank(id)){
			result = rent.set("id", id).update();
		}else{
			result = rent.set("bdzj", bdzj).set("tcbl", tcbl).save();
		}
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
	
	
	public ReturnMsg saveOrUpdate2(Controller c){
		boolean result = false;
		Double payableFee = 0.0;
		Double bdzj = 0.0;
		String tcbl = "";
		String id = c.getPara("id");
		String sName = c.getPara("sName");
		String ymDate = c.getPara("ymDate");
		//实际收入
		String actualIncome = c.getPara("actualIncome","0");
		//上报商场收入
		String shopIncome = c.getPara("shopIncome","0");
		String startTime = ymDate + "-01";
		
		// 获取指定年月的天数
		Integer year = Integer.valueOf(ymDate.substring(0, 4));
		Integer month = Integer.valueOf(ymDate.substring(5, 7));
		int days = Tools.getDayOfMonth(year, month);
		//String endTime = ymData + "-" + String.valueOf(days);
		
		//根据店铺名称和年月从 租金和物管费用配置表 查询对应店铺的 保底租金 和 提成租金 比例
		Long count = this.findFirst("SELECT COUNT(1) count FROM rent_opt ro LEFT JOIN store s ON ro.s_id=s.id "
				+ "WHERE SUBSTR(start_time,1,7)<=? AND SUBSTR(end_time,1,7)>=? AND s.`name`=? ", ymDate, ymDate, sName).getLong("count");
		String sql = "SELECT start_time startTime,end_time endTime,init_rent initRent,rent_point rentPoint "
				+ "FROM rent_opt ro LEFT JOIN store s ON ro.s_id=s.id "
				+ "WHERE SUBSTR(start_time,1,7)<=? AND SUBSTR(end_time,1,7)>=? AND s.`name`=? ORDER BY end_time";
		
		/*
		 * 1.没有对应记录，计算应补金额为0
		 * 2.查询到一条记录，计算应补金额=上报商场收入*提成租金比例-保底租金
		 * 3.查询到两条记录，保底租金和计算应补金额按两个时间段分开计算（整月的统计）
		 */
		if(count == 0){
			payableFee = 0.0;
		}else if(count == 1){
			RentBill rb = this.findFirst(sql, ymDate, ymDate, sName);
			bdzj = rb.getDouble("initRent");
			Double rentPoint = rb.getDouble("rentPoint");
			payableFee = Double.valueOf(shopIncome) * rentPoint - bdzj;
			tcbl = rentPoint.toString();
			payableFee = Double.valueOf(new java.text.DecimalFormat("#.00").format(payableFee));
			if(payableFee < 0.0){
				payableFee = 0.0;
			}
		}else{
			List<RentBill> list = this.find(sql, ymDate, ymDate, sName);
			Double initRent1 = 0.0;
			Double rentPoint1 = 0.0;
			String endTime1 = "";
			
			Double initRent2 = 0.0;
			Double rentPoint2 = 0.0;
			
			RentBill rb1 = list.get(0);
			RentBill rb2 = list.get(1);
			
			endTime1 = rb1.get("endTime");
			long l = Tools.getDaysByStrDateDiff(startTime, endTime1) + 1;
			
			initRent1 = rb1.getDouble("initRent");
			rentPoint1 = rb1.getDouble("rentPoint");
			
			initRent2 = rb2.getDouble("initRent");
			rentPoint2 = rb2.getDouble("rentPoint");
			bdzj = initRent1 / days * l + initRent2 / days * (days - l);
			tcbl = rentPoint1.toString() + " + " + rentPoint2.toString();
			payableFee = Double.valueOf(shopIncome) * rentPoint1 - initRent1 / days * l;
			payableFee = payableFee + Double.valueOf(shopIncome) * rentPoint2 - initRent2 / days * (days - l);
			payableFee = Double.valueOf(String.format("%.2f", payableFee));
			if(payableFee < 0.0){
				payableFee = 0.0;
			}
		}
		RentBill rent = new RentBill();
		rent.set("id", id)
		.set("actual_income", actualIncome)
		.set("shop_income", shopIncome)
		.set("bdzj", bdzj)
		.set("tcbl", tcbl)
		.set("payable_fee", payableFee);
		result = StringUtils.isNoneBlank(id) ? rent.set("id", id).update() : rent.save();
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
	
	public ReturnMsg deletes(String ids){
		Integer orgId = ShiroUtils.getOrgId();
		if(orgId == 1){
			boolean update = false;
			for (String id : ids.split(";")) {
				update = new RentBill().set("id", id).set("flag", 2).update();
				if(update == false){
					break;
				}
			}
			return update ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	/*public static void main(String[] args) {
		Connection conn = JdbcUtils.getConnection();
		CallableStatement cs = null;
        try {
            cs = conn.prepareCall("{CALL computeRent2(?,?,?,?,?)}");
            cs.setString(1, "深圳华润万象天地");
            cs.setString(2, "2018-08");
            cs.setDouble(3, 100.0);
            cs.setString(4, "@aa");
            cs.setString(5, "@bb");
            ResultSet rs = cs.executeQuery();
            int count = rs.getMetaData().getColumnCount();
            while(rs.next()){  
                StringBuffer sb = new StringBuffer();
                for(int i=0;i<count;i++){  
                    sb.append(rs.getString(i+1)+"  ");  
                }  
                System.out.println(sb);  
            }  
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	JdbcUtils.closeAll(null, cs, null, conn);
        }
    }*/
}

