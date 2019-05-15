package com.core.zjqk.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import com.common.kits.ExcelUtil;
import com.common.kits.PageUtils;
import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.zjqk.model.CycBills;
import com.core.zjqk.model.CycDetails;
import com.core.zjqk.model.TVar;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class CycBillsController extends Controller {

	public void index() {
		render("index.html");
	}
	public void recordManager() {
		render("record_manager.html");
	}
	public void recordFinancial() {
		render("record_financial.html");
	}

	public void listOperator() {
		renderJson(PageUtils.page(CycBills.dao.pageOperator(this)));
	}
	public void listManager() {
		renderJson(PageUtils.page(CycBills.dao.pageManager(this)));
	}
	public void listFinancial() {
		renderJson(PageUtils.page(CycBills.dao.pageFinancial(this)));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(CycBills.dao.deletes(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void submits() {
		renderJson(CycBills.dao.submits(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void passs() {
		renderJson(CycBills.dao.passs(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void backs() {
		renderJson(CycBills.dao.backs(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void cancels() {
		renderJson(CycBills.dao.cancels(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void savePaymentInfo() {
		renderJson(CycBills.dao.savePaymentInfo(this));
	}
	
	public void getBillNo(){
		renderJson(new ReturnMsg("12000", TVar.dao.findBillNo(this)));
	}
	
	public void cycInvoiceCount() {
		renderJson(PageUtils.page(CycBills.dao.cycInvoiceCount(this)));
	}
	
	public void cycFeesCount() {
		renderJson(PageUtils.page(CycBills.dao.cycFeesCount(this)));
	}
	
	public void cycDetailsData() {
		renderJson(PageUtils.page(CycBills.dao.cycDetailsData(this)));
	}
	
	public void cycInvoice() {
		render("cycInvoice.html");
	}
	
	public void cycFees() {
		render("cycFees.html");
	}
	
	public void cycDetails() {
		render("cycDetails.html");
	}
	
	/*
	 * 周期性费用统计表导出
	 */
	public void exCycFeesCount() throws IOException {
		String sheetName = "周期性费用统计表";
		String[] titles = { "定义年月","制单日期","单号","店铺名称","请款金额","申请支付日期","实际支付金额","实际支付日期","付款账户","付款账号","开户行","实际未付金额","状态" };
		String[] columns = { "ym","createTime","billNo","sName","totalMoney","payDate","actualMoney","actualDate","pName","account","bank","nopayMoney","status"  };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), CycBills.dao.exCycFeesCount(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderNull();
	}
	
	/*
	 * 周期性费用统计表导出
	 */
	public void exCycDetails() throws IOException {
		String sheetName = "周期性费用明细表";
		String[] titles = { "定义年月","制单日期","请款单号","店铺名称","开始时间","结束时间","付款项目","请款金额","实际支付日期","状态" };
		String[] columns = { "ym","createTime","billNo","sName","startTime","endTime","ciName","money","actualDate","status" };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), CycBills.dao.exCycDetailsData(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderNull();
	}
	
	/*
	 * 周期性费用发票统计表导出
	 */
	public void exCycInvoice() throws IOException {
		String sheetName = "周期性费用发票统计表";
		String[] titles = { "制单日期","单号","店铺名称","支付金额","付款账户","发票金额","已付款未开票金额","状态" };
		String[] columns = { "createTime","billNo","sName","actualMoney","pName","totalMoney","otherMoney","status" };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), CycBills.dao.exCycInvoice(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("周期性费用发票统计表导出报错");
		}
		renderNull();
	}
	
	@Before(Tx.class)
	public void exCycBill() throws IOException {
		String id = this.getPara("id");
		if(StringUtils.isNoneBlank(id)){
			CycBills cb = CycBills.dao.cycBillById(this.getPara("id"));
			List<CycDetails> cd = CycDetails.dao.cycDetailsByBillNo(cb.get("billNo"));
			Workbook workbook = ExcelUtil.cycBillInfo(cb, cd, Tools.ymdhmsStr(), Tools.ymdStr());
			if(workbook != null){
				ExcelUtil.exportDatasExcel(Tools.ymdhmsStr(), workbook, this.getResponse());
			}
		}
		renderNull();
	}
	
}
