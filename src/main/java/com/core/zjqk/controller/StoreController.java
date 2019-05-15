package com.core.zjqk.controller;

import java.io.IOException;
import java.util.Arrays;

import org.apache.poi.ss.usermodel.Workbook;

import com.common.kits.ExcelUtil;
import com.common.kits.PageUtils;
import com.core.zjqk.model.Store;
import com.core.zjqk.model.TVar;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class StoreController extends Controller {

	public void index() {
		render("index.html");
	}
	
	public void rentIndex1(){
		render("rentCount1.html");
	}
	
	public void rentIndex2(){
		render("rentCount2.html");
	}

	public void list() {
		renderJson(PageUtils.page(Store.dao.page(this)));
	}
	
	public void rentStatistics() {
		renderJson(PageUtils.page(Store.dao.rentCount(this)));
	}
	
	public void rentStatistics2() {
		renderJson(PageUtils.page(Store.dao.rentCount2(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(Store.dao.saveOrUpdate(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(Store.dao.deletes(this.getPara("ids")));
	}
	
	public void storeName() {
		renderJson(Store.dao.findStoreNames(this.getParaToInt("sid")));
	}
	
	public void findStoresByDay() {
		renderJson(Store.dao.findStoresByDay(this.getParaToInt("payDay")));
	}
	
	public void paymentDays(){
		renderJson(Store.dao.paymentDays());
	}
	
	public void listStores(){
		renderJson(Store.dao.list());
	}
	
	public void classifys(){
		renderJson(TVar.dao.classifys());
	}
	
	@Before(Tx.class)
	public void importStores(){
		renderJson(Store.dao.importStores(this));;
	}
	
	/*
	 * 提成租金计算表导出
	 */
	public void exRentCount() throws IOException {
		String sheetName = "提成租金计算表";
		String[] titles = { "所属年月","店铺名称","开业日期","撤铺日期","保底租金","提成租金比例","实际收入","上报商场收入","计算应补金额","实际支付金额","差额" };
		String[] columns = { "ymDate","sName","openingDate","closeDate","bdzj","tcbl","actualIncome","shopIncome","payableFee","actualPayment","differenceFee" };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), Store.dao.exRentCount(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderNull();
	}
	
	/*
	 * 提成租金计算导出
	 */
	public void exRentCount2() throws IOException {
		String sheetName = "提成租金计算";
		String[] titles = { "所属年月","店铺名称","开业日期","撤铺日期","保底租金","提成租金比例","实际收入","上报商场收入","计算应补金额" };
		String[] columns = { "ymDate","sName","openingDate","closeDate","bdzj","tcbl","actualIncome","shopIncome","payableFee" };
		Workbook workbook = ExcelUtil.buildWorkbookCustomizable(sheetName, Arrays.asList(titles),
				Arrays.asList(columns), Store.dao.exRentCount2(this));
		try {
			ExcelUtil.exportDatasExcel(sheetName, workbook, getResponse());
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderNull();
	}

}
