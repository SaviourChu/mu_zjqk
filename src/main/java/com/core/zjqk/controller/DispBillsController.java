package com.core.zjqk.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import com.common.kits.ExcelUtil;
import com.common.kits.PageUtils;
import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.admin.model.User;
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.DispBills;
import com.core.zjqk.model.DispDetails;
import com.core.zjqk.model.TVar;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class DispBillsController extends Controller {

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
		renderJson(PageUtils.page(DispBills.dao.pageOperator(this)));
	}
	public void listManager() {
		renderJson(PageUtils.page(DispBills.dao.pageManager(this)));
	}
	public void listFinancial() {
		renderJson(PageUtils.page(DispBills.dao.pageFinancial(this)));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(DispBills.dao.deletes(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void submits() {
		renderJson(DispBills.dao.submits(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void passs() {
		renderJson(DispBills.dao.passs(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void backs() {
		renderJson(DispBills.dao.backs(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void cancels() {
		renderJson(DispBills.dao.cancels(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void savePaymentInfo() {
		renderJson(DispBills.dao.savePaymentInfo(this));
	}
	
	public void getBillNo(){
		renderJson(new ReturnMsg("12000", TVar.dao.findBillNo(this)));
	}
	
	
	public void fundMgt() {
		renderJson(PageUtils.page(DispBills.dao.fundMgt(this)));
	}
	public void fund() {
		render("fundMgt.html");
	}
	
	public void dispInvoiceCount() {
		renderJson(PageUtils.page(DispBills.dao.dispInvoiceCount(this)));
	}
	
	public void dispInvoice() {
		render("dispInvoice.html");
	}
	
	public void dispFeesCount() {
		renderJson(PageUtils.page(DispBills.dao.dispFeesCount(this)));
	}
	
	public void dispFees() {
		render("dispFees.html");
	}
	
	public void dispDetailsData() {
		renderJson(PageUtils.page(DispBills.dao.dispDetailsData(this)));
	}
	
	public void dispDetails() {
		render("dispDetails.html");
	}
	
	/*
	 * 一次性费用统计表导出
	 */
	public void exDispFeesCount() throws IOException {
		String sheetName = "一次性费用统计表";
		String[] titles = { "制单日期","单号","店铺名称","请款金额","申请支付日期","实际支付金额","实际支付日期","付款账户","付款账号","开户行","实际未付金额","状态" };
		String[] columns = { "createTime","billNo","sName","totalMoney","payDate","actualMoney","actualDate","pName","account","bank","nopayMoney","status"  };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), DispBills.dao.exDispFeesCount(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("一次性费用统计表导出报错");
		}
		renderNull();
	}
	
	/*
	 * 一次性费用统计表导出
	 */
	public void exDispDetails() throws IOException {
		String sheetName = "一次性费用明细表";
		String[] titles = { "制单日期","请款单号","店铺名称","付款项目","请款金额","实际支付日期","状态" };
		String[] columns = { "createTime","billNo","sName","diName","money","actualDate","status"  };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), DispBills.dao.exDispDetailsData(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("一次性费用明细表导出报错");
		}
		renderNull();
	}
	
	/*
	 * 一次性费用发票统计表导出
	 */
	public void exDispInvoice() throws IOException {
		String sheetName = "一次性费用发票统计表";
		String[] titles = { "制单日期","单号","店铺名称","支付金额","付款账户","发票金额","已付款未开票金额","状态" };
		String[] columns = { "createTime","billNo","sName","actualMoney","pName","totalMoney","otherMoney","status" };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), DispBills.dao.exDispInvoice(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("一次性费用发票统计表导出报错");
		}
		renderNull();
	}
	
	/*
	 * 可回收资金管理表导出
	 */
	public void exFundMgt() throws IOException {
		String sheetName = "可回收资金管理表";
		String[] titles = { "店铺名称","开业日期","撤铺日期","租赁保证金","装修押金","公共事业押金","水电保证金","POS押金","出入证押金","商品质量保证金" };
		String[] columns = { "sName","openingDate","closeDate","zlbzj","zxyj","ggsyyj","sdbzj","posyj","crzyj","spzlbzj" };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), DispBills.dao.exFundMgt(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("可回收资金管理表导出报错");
		}
		renderNull();
	}
	
	@Before(Tx.class)
	public void exDispBill() throws IOException {
		String id = this.getPara("id");
		if(StringUtils.isNoneBlank(id)){
			DispBills db = DispBills.dao.dispBillById(this.getPara("id"));
			List<DispDetails> dd = DispDetails.dao.dispDetailsByBillNo(db.get("billNo"));
			Workbook workbook = ExcelUtil.dispBillInfo(db, dd, Tools.ymdhmsStr(), Tools.ymdStr());
			if(workbook != null){
				ExcelUtil.exportDatasExcel(Tools.ymdhmsStr(), workbook, this.getResponse());
			}
		}
		renderNull();
	}
	
	public void printPreview(){
		User user = ShiroUtils.getUser();
		String id = this.getPara("id");
		if(StringUtils.isNoneBlank(id)){
			String signDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String no = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			DispBills db = DispBills.dao.dispBillById(this.getPara("id"));
			List<DispDetails> dd = DispDetails.dao.dispDetailsByBillNo(db.get("billNo"));
			String username = user.get("fullname");
			setAttr("signDate", signDate);
			setAttr("no", no);
			setAttr("username", username);
			setAttr("db", db);
			setAttr("dd", dd);
			render("preview.html");
		}else{
			renderNull();
		}
	}
}
