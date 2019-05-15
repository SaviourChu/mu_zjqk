package com.core.zjqk.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.common.config.JdbcUtils;
import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.base.BaseInvoice;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class Invoice extends BaseInvoice<Invoice> {
	
	public static final Invoice dao = new Invoice();
	
	public Page<Invoice> page(Controller c){
		String select = "SELECT id,bill_no billNo,invoice_no invoiceNo,money,invoice_title invoiceTitle,create_date createDate ";
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		sqlExceptSelect.append("FROM invoice WHERE flag=1 AND classify='"+ShiroUtils.getClassify()+"' "); 
		String billNo = c.getPara("billNo");
		if(StringUtils.isNotBlank(billNo)){
			sqlExceptSelect.append("AND INSTR(bill_no, ?) ");
			params.add(billNo);
		}
		sqlExceptSelect.append("ORDER BY id ");
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	/*
	 * 保存发票信息
	 */
	public ReturnMsg save(Controller c){
		Integer orgId = ShiroUtils.getOrgId();
		String classify = ShiroUtils.getClassify();
		if(orgId == 1){
			Integer type = c.getParaToInt("type");
			String tabName = type == 1 ? "disp_bills" : "cyc_bills";
			boolean result = false;
			try {
				Integer fkxId = c.getParaToInt("fkx");
				if(fkxId == null) {
					return new ReturnMsg("14000", "请您选择付款项！");
				}
				String invoiceNo = c.getPara("invoiceNo");
				String money = c.getPara("money");
				String invoiceTitle = c.getPara("invoiceTitle");
				String createDate = c.getPara("createDate");
				String billNo = c.getPara("billNo");
				if(StringUtils.isAnyBlank(invoiceNo, createDate, billNo, money, invoiceTitle)){
					return ReturnMsg.DATAERROR;
				}
				//实际支付金额
				Double actualPayMoney = findActualPayMoney(tabName, billNo, classify);
				//已开发票金额
				Double partMoney = totalAmount(billNo);
				//发票实际金额
				Double itemMoney = strToDouble(money);
				int status = 6;//部分发票
				Double totalMoney = itemMoney + partMoney;
				totalMoney = Double.valueOf(new DecimalFormat("0.00").format(totalMoney));
				int i = numsCompare(new BigDecimal(totalMoney), new BigDecimal(actualPayMoney));
				if(i == 1){
					return ReturnMsg.INVOICE_MONEY_ERROR;
				}else if(i == 0){
					status = 7;//发票已全
				}else if(totalMoney.equals(0.0)){
					status = 4;//无发票
				}
				Invoice invoice = new Invoice();
				invoice.set("type", type)
					.set("bill_no", billNo)
					.set("invoice_no", invoiceNo)
					.set("money", itemMoney)
					.set("classify", classify)
					.set("invoice_title", Tools.trimStr(invoiceTitle))
					.set("create_date", createDate);
				result = invoice.save() && updateStatus(tabName, billNo, status, classify);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	/*
	 * 修改发票信息
	 */
	public ReturnMsg update(Controller c){
		Integer orgId = ShiroUtils.getOrgId();
		String classify = ShiroUtils.getClassify();
		if(orgId == 1){
			boolean result = false;
			String id = c.getPara("id");
			//根据ID获取修改的单号，费用类型和金额
			Invoice tempInvoice = findInvoiceById(id);
			String billNo = tempInvoice.get("billNo");
			Integer type = tempInvoice.getInt("type");
			Double updateMoney = doubleDefaultValue(tempInvoice.getDouble("money"));
			String tabName = type == 1 ? "disp_bills" : "cyc_bills";
			try {
				String invoiceNo = c.getPara("invoiceNo");
				String money = c.getPara("money");
				String invoiceTitle = c.getPara("invoiceTitle");
				String createDate = c.getPara("createDate");
				if(StringUtils.isAnyBlank(invoiceNo, createDate, billNo, money, invoiceTitle)){
					return ReturnMsg.DATAERROR;
				}
				//实际支付金额
				Double actualPayMoney = findActualPayMoney(tabName, billNo, classify);
				//已开发票金额
				Double partMoney = totalAmount(billNo);
				//发票实际金额
				Double itemMoney = strToDouble(money);
				int status = 6;//部分发票
				Double totalMoney = itemMoney + partMoney - updateMoney;
				totalMoney = Double.valueOf(new DecimalFormat("0.00").format(totalMoney));
				int i = numsCompare(new BigDecimal(totalMoney), new BigDecimal(actualPayMoney));
				if(i == 1){
					return ReturnMsg.INVOICE_MONEY_ERROR;
				}else if(i == 0){
					status = 7;//发票已全
				}else if(totalMoney.equals(0.0)){
					status = 4;//无发票
				}
				Invoice invoice = new Invoice();
				invoice.set("invoice_no", invoiceNo)
					.set("money", itemMoney)
					.set("classify", classify)
					.set("invoice_title", invoiceTitle)
					.set("create_date", createDate);
				result = invoice.set("id", id).update() && updateStatus(tabName, billNo, status, classify);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	private int numsCompare(BigDecimal a, BigDecimal b){
		int i = 0;
		if(a.compareTo(b) < 0){
			i = -1;
		}else if(a.compareTo(b) == 0){
			i = 0;
		}else{
			i = 1;
		}
		return i;
	}
	
	/*
	 * 删除发票信息
	 */
	public ReturnMsg delete(String id){
		Integer orgId = ShiroUtils.getOrgId();
		if(orgId == 1){
			//根据ID获取修改的单号，费用类型和金额
			Invoice tempInvoice = findInvoiceById(id);
			String billNo = tempInvoice.get("billNo");
			Integer type = tempInvoice.getInt("type");
			Double deleteMoney = doubleDefaultValue(tempInvoice.getDouble("money"));
			String tabName = type == 1 ? "disp_bills" : "cyc_bills";
			//已开发票金额
			Double partMoney = totalAmount(billNo);
			int status = 6;//部分发票
			Double totalMoney = partMoney - deleteMoney;
			if(totalMoney.equals(0.0)){
				status = 4;//无发票
			}
			Invoice invoice = new Invoice();
			invoice.set("id", id).set("flag", 2);
			boolean result = updateStatus(tabName, billNo, status, ShiroUtils.getClassify());
			return invoice.update() && result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	/*
	 * 已开发票金额
	 */
	public Double totalAmount(String billNo){
		String sql = "SELECT SUM(money) totalAmount FROM invoice WHERE flag=1 AND bill_no=? AND classify=? ";
		Double result = this.findFirst(sql, billNo, ShiroUtils.getClassify()).getDouble("totalAmount");
		return result != null ? result : 0.0;
	}
	
	/*
	 * 根据ID获取修改的单号，费用类型和金额
	 */
	public Invoice findInvoiceById(String id){
		return this.findFirst("SELECT bill_no billNo,type,money FROM invoice WHERE id=? ", id);
	}
	
	/*
	 * 实际支付金额
	 */
	public Double findActualPayMoney(String tabName, String billNo, String lb){
		String sql = "SELECT actual_money actualMoney FROM " + tabName + " WHERE flag=1 AND bill_no=? AND lb=? ";
		return doubleDefaultValue(findFirst(sql, billNo, lb).getDouble("actualMoney"));
	}
	
	/*
	 * 修改单号状态
	 */
	public boolean updateStatus(String tabName, String billNo, int status, String classify){
		int i = 0;
		Connection conn = JdbcUtils.getConnection();
		PreparedStatement psmt = null;
		try {
			psmt = conn.prepareStatement("UPDATE " + tabName + " SET status=? WHERE bill_no=? AND lb=? ");
			psmt.setInt(1, status);
			psmt.setString(2, billNo);
			psmt.setString(3, classify);
			i = psmt.executeUpdate();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("修改单号状态失败！");
		} finally {
			JdbcUtils.closeAll(null, null, psmt, conn);
		}
		return i > 0;
	}
	
	public Double strToDouble(String strMoney){
		Double money = Double.valueOf(strMoney);
		if(money == null){
			money = 0.0;
		}
		return money;
	}
	
	public Double doubleDefaultValue(Double val){
		return val == null ? 0.0 : val;
	}
	
	public static void main(String[] args) {
		Double d1 = 2000.0;
		Double d2 = 2000.0;
		boolean equals = d1.equals(d2);
		System.out.println(equals);
		System.out.println(d1.compareTo(d2));
	}
}
