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
import com.core.zjqk.model.base.BaseCycDetails;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class CycDetails extends BaseCycDetails<CycDetails> {

	public static final CycDetails dao = new CycDetails();

	public Page<CycDetails> page(Controller c) {
		String select = "SELECT cd.id,ci.`name` ciName,cd.init_fee initFee,cd.start_time startTime,cd.end_time endTime,cd.money,cd.remarks ";
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		sqlExceptSelect.append("FROM cyc_details cd LEFT JOIN cyc_items ci ON ci.id=cd.c_id WHERE cd.flag=1 AND type='"+ShiroUtils.getClassify()+"' ");
		String billNo = c.getPara("billNo");
		if (StringUtils.isNotBlank(billNo)) {
			sqlExceptSelect.append("AND INSTR(cd.bill_no, ?) ");
			params.add(billNo);
		}
		sqlExceptSelect.append("ORDER BY cd.id ");
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}

	/**
	 * 保存请款单明细数据
	 */
	public ReturnMsg saveOrUpdate(Controller c) {
		try {
			boolean result = false;
			Integer orgId = ShiroUtils.getOrgId();
			String classify = ShiroUtils.getClassify();
			String rowId = c.getPara("rowId");
			if (orgId == 1) {
				String billNo = c.getPara("billNo");//单号
				String cbym = c.getPara("cbym");//定义年月
				String sName = c.getPara("sName").trim();//店铺名称
				String aName = c.getPara("aName").trim();//收款账户名称
				String payDate = c.getPara("payDate").trim();//支付日期
				String sTime = c.getPara("sTime").trim();//开始时间
				String eTime = c.getPara("eTime").trim();//结束时间
				String ciName = c.getPara("ciName").trim();//付款项目
				String money = c.getPara("money").trim();//手动输入的费用
				String remarks = c.getPara("remarks").trim();//备注
				
				if (StringUtils.isAnyBlank(sName, aName, ciName, payDate)) {
					return ReturnMsg.DATAERROR;
				}
				String initFee = c.getPara("initFee");
				if (StringUtils.isBlank(initFee) || "暂无".equals(initFee) || "计算有误".equals(initFee)) {
					initFee = "0";
				}
				//未手动输入费用默认为初始化费用值
				if (StringUtils.isBlank(money)) {
					money = initFee;
				}
				
				Integer sId = Store.dao.findStoreId(sName);
				Integer aId = Account.dao.findAccountId(aName, sName);
				Integer ciId = CycItems.dao.findCycItemsId(ciName);
				
				String checkRepeatSql = "SELECT GROUP_CONCAT(t.bill_no) billNos FROM"
						+ "(SELECT bill_no FROM cyc_details WHERE flag=1 AND start_time=? AND end_time=? AND s_id=? AND c_id=? AND money=?) t";
				String billNos = this.findFirst(checkRepeatSql, sTime, eTime, sId, ciId, money).get("billNos");
				if(StringUtils.isNoneBlank(billNos)){
					return new ReturnMsg("14002", "请您核对【" + billNos + "】的请款项");
				}
				
				String sql1 = "SELECT COUNT(1) count FROM cyc_bills WHERE flag=1 AND lb=? AND s_id=? AND a_id=? AND bill_no=? AND pay_date=? ";
				Long count = this.findFirst(sql1, classify, sId, aId, billNo, payDate).getLong("count");
				//count>0 表示查询到了记录，说明请款单头部没有修改，那么，只会向明细表中添加记录，否则请款单和明细表都要保存数据；count<=0 表示有修改
				if(count > 0){
					CycDetails cd = new CycDetails();
					cd.set("bill_no", billNo)
						.set("s_id", sId)
						.set("c_id", ciId)
						.set("init_fee", initFee)
						.set("start_time", sTime)
						.set("end_time", eTime)
						.set("money", money)
						.set("type", classify)
						.set("remarks", remarks);
					result = cd.save();
					
					countActualPayment(classify, sTime, ciName, sId);
				}else{
					if(StringUtils.isBlank(rowId)){
						CycDetails cd = new CycDetails();
						cd.set("bill_no", billNo)
							.set("s_id", sId)
							.set("c_id", ciId)
							.set("init_fee", initFee)
							.set("start_time", sTime)
							.set("end_time", eTime)
							.set("money", money)
							.set("type", classify)
							.set("remarks", remarks);
						CycBills cb = new CycBills();
						cb.set("s_id", sId)
							.set("ym", cbym)
							.set("a_id", aId)
							.set("pay_date", payDate)
							.set("zdr", ShiroUtils.getUsername())
							.set("create_time", Tools.ymdStr())
							.set("lb", classify)
							.set("bill_no", billNo);
						TVar.dao.saveBillNo(2, billNo);
						result = cb.save() && cd.save();
						
						countActualPayment(classify, sTime, ciName, sId);
					}else{
						return ReturnMsg.PROMPTINFO;
					}
				}
				return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
			} else {
				return ReturnMsg.NORIGHT;
			}
		} catch (Exception e) {
			return ReturnMsg.ERROR;
		}
	}

	/**
	 * 提成租金计算表计算需要 【实际支付金额】
	 */
	private void countActualPayment(String classify, String sTime, String ciName, Integer sId) {
		if("提成租金".equals(ciName)){
			Connection conn = JdbcUtils.getConnection();
			PreparedStatement psmt = null;
			String d = sTime.substring(0,7);
			String sql2 = "SELECT SUM(money) sjje FROM cyc_details WHERE flag=1 AND c_id=2 AND type=? AND s_id=? AND SUBSTR(start_time,1,7)=? ";
			CycDetails cds = this.findFirst(sql2, classify, sId, d);
			Double actualPayment = 0.0;
			if(cds != null && cds.getDouble("sjje") != null){
				actualPayment = cds.getDouble("sjje");
			}
			try {
				psmt = conn.prepareStatement("UPDATE rent_bill SET actual_payment=? WHERE s_id=? AND ym_date=? AND flag=1");
				psmt.setDouble(1, actualPayment);
				psmt.setInt(2, sId);
				psmt.setString(3, d);
				psmt.executeUpdate();
			} catch (SQLException e) {
				//e.printStackTrace();
				System.out.println("更新实际支付金额（actualPayment） 失败！");
			} finally {
				JdbcUtils.closeAll(null, null, psmt, conn);
			}
		}
	}
	
	/**
	 * 修改请款单信息
	 */
	public ReturnMsg editBillInfo(Controller c) {
		try {
			Integer orgId = ShiroUtils.getOrgId();
			String classify = ShiroUtils.getClassify();
			if (orgId == 1) {
				String rowId = c.getPara("rowId");//选中ID标识
				String danhao = c.getPara("danhao");//单号
				String dyym = c.getPara("dyym").trim();//定义年月
				String dpmc = c.getPara("dpmc").trim();//店铺名称
				String skzh = c.getPara("skzh").trim();//收款账户名称
				String zfrq = c.getPara("zfrq").trim();//支付日期
				
				Integer sId = Store.dao.findStoreId(dpmc);
				Integer aId = Account.dao.findAccountId(skzh, dpmc);
				
				String sql = "SELECT COUNT(1) count FROM cyc_bills WHERE flag=1 AND lb=? AND s_id=? AND a_id=? AND bill_no=? AND pay_date=? ";
				Long count = this.findFirst(sql, classify, sId, aId, danhao, zfrq).getLong("count");
				//count>0 表示查询到了记录，说明请款单头部没有修改，count<=0 表示有修改
				if(count <= 0){
					//根据 ID对 cyc_bills表执行更新操作
					CycBills cb = new CycBills();
					cb.set("id", rowId)
						.set("ym", dyym)
						.set("s_id", sId)
						.set("a_id", aId)
						.set("pay_date", zfrq);
					Connection conn = JdbcUtils.getConnection();
					PreparedStatement psmt = null;
					try {
						//根据单号和模块标识更新cyc_details表的店铺ID
						psmt = conn.prepareStatement("UPDATE cyc_details SET s_id=? WHERE bill_no=? AND type=? AND flag=1");
						psmt.setInt(1, sId);
						psmt.setString(2, danhao);
						psmt.setString(3, classify);
						int i = psmt.executeUpdate();
						if(i == 0){
							return ReturnMsg.DATAERROR;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						JdbcUtils.closeAll(null, null, psmt, conn);
					}
					return cb.update() ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
				}else{
					return ReturnMsg.REPEATERROR;
				}
			} else {
				return ReturnMsg.NORIGHT;
			}
		} catch (Exception e) {
			return ReturnMsg.ERROR;
		}
	}

	public ReturnMsg delete(String id) {
		Integer orgId = ShiroUtils.getOrgId();
		if (orgId == 1) {
			CycDetails cd = new CycDetails();
			cd.set("id", id).set("flag", 2);
			return cd.update() ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		} else {
			return ReturnMsg.NORIGHT;
		}
	}

	public String findInitFee(Controller c) {
		String initFee = "暂无";
		String sName = c.getPara("sName").trim();
		String ciName = c.getPara("ciName").trim();
		try {
			if ("租金".equals(ciName) || "物业管理费".equals(ciName) || "提成租金".equals(ciName)) {
				String startTime = c.getPara("stime");
				String endTime = c.getPara("etime");
				// 获取两个日期字符串的相差天数
				long l = Tools.getDaysByStrDateDiff(startTime, endTime) + 1;
				// 获取匹配的配置费用
				CycDetails cycDetails = this.findFirst("SELECT pm_fee pmFee,init_rent initRent,rent_point rentPoint "
						+ "FROM rent_opt ro LEFT JOIN store s ON s.id = ro.s_id "
						+ "WHERE start_time<=? AND s.`name`=? AND ro.flag=1 ORDER BY start_time DESC LIMIT 1 ", startTime, sName);
				if(cycDetails != null){
					Double pmFee = cycDetails.getDouble("pmFee");
					Double initRent = cycDetails.getDouble("initRent");
					Double rentPoint = cycDetails.getDouble("rentPoint");
					// 获取指定年月的天数
					Integer year = Integer.valueOf(startTime.substring(0, 4));
					Integer month = Integer.valueOf(startTime.substring(5, 7));
					int days = Tools.getDayOfMonth(year, month);

					initRent = initRent / days * (double) l;
					if ("租金".equals(ciName)) {
						initFee = String.format("%.2f", initRent);
					} else if ("物业管理费".equals(ciName)) {
						pmFee = pmFee / days * (double) l;
						initFee = String.format("%.2f", pmFee);
					} else {
						// 查询指定店铺和年月对应的上报商场收入
						startTime = startTime.substring(0, 7);
						CycDetails cd2 = this.findFirst("SELECT r.shop_income shopIncome FROM rent_bill r "
										+ "LEFT JOIN store s ON s.id=r.s_id WHERE r.ym_date=? AND s.`name`=? ", startTime, sName);
						if (cd2 != null) {
							Double shopIncome = cd2.getDouble("shopIncome");
							Double result = rentPoint * shopIncome - initRent;
							initFee = result > 0.0 ? String.format("%.2f", result) : "0";
						}
					}
				}
			} else {
				String sql = "SELECT cf.init_fee initFee FROM cyc_initfees cf LEFT JOIN store s ON cf.s_id=s.id "
						+ "LEFT JOIN cyc_items ci ON cf.c_id=ci.id WHERE cf.del_flag=1 AND s.`name`=? AND ci.`name`=? ";
				CycDetails cd = this.findFirst(sql, sName, ciName);
				if (cd != null) {
					initFee = cd.get("initFee");
				}
			}
		} catch (NumberFormatException e) {
			initFee = "计算有误";
		}
		return initFee;
	}
	
	public Double totalAmount(String billNo){
		String sql = "SELECT SUM(money) totalAmount FROM cyc_details WHERE flag=1 AND type='"+ShiroUtils.getClassify()+"' AND bill_no=? ";
		Double result = this.findFirst(sql, billNo).getDouble("totalAmount");
		return result != null ? result : 0.0;
	}
	
	public List<CycDetails> cycDetailsByBillNo(String id) {
		String sql = "SELECT cd.id,ci.`name` ciName,cd.start_time startTime,cd.end_time endTime,cd.money,cd.remarks,"
				+ "(SELECT SUM(money) FROM cyc_details WHERE flag=1 AND bill_no=? AND type='"+ShiroUtils.getClassify()+"') totalMoney "
				+ "FROM cyc_details cd LEFT JOIN cyc_items ci ON ci.id=cd.c_id WHERE cd.flag=1 AND cd.bill_no=? AND type='"+ShiroUtils.getClassify()+"'";
		return this.find(sql, id, id);
	}
	
	public Page<CycDetails> tczjmx(Controller c){
		String select = "SELECT cd.bill_no billNo,ci.`name` ciName,cd.start_time startTime,cd.end_time endTime,cd.money,cd.remarks ";
		String sqlExceptSelect = "FROM cyc_details cd LEFT JOIN store s ON cd.s_id=s.id LEFT JOIN cyc_items ci ON ci.id=cd.c_id WHERE s.`name`=? AND cd.flag=1 AND cd.c_id=2 ORDER BY ci.id ";
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect, c.getPara("sName"));
	}
	
	public Double getFkxMoney(Controller c) {
		Integer fkxId = c.getParaToInt("fkxId");
		String billNo = c.getPara("billNo");
		String sql = "SELECT money FROM cyc_details WHERE type='"+ShiroUtils.getClassify()+"' AND c_id=? AND bill_no=? ";
		Double result = this.findFirst(sql, fkxId, billNo).getDouble("money");
		return result != null ? result : 0.0;
	}
	
}
