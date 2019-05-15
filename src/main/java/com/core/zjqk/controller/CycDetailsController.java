package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.common.kits.ReturnMsg;
import com.core.zjqk.model.Account;
import com.core.zjqk.model.CycDetails;
import com.core.zjqk.model.CycItems;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class CycDetailsController extends Controller {

	public void index() {
		List<Account> accounts = Account.dao.list();
		List<Store> stores = Store.dao.list();
		List<CycItems> cItems = CycItems.dao.list();
		this.setAttr("accounts", accounts);
		this.setAttr("stores", stores);
		this.setAttr("cItems", cItems);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(CycDetails.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(CycDetails.dao.saveOrUpdate(this));
	}
	
	@Before(Tx.class)
	public void editBillInfo() {
		renderJson(CycDetails.dao.editBillInfo(this));
	}

	@Before(Tx.class)
	public void delete() {
		renderJson(CycDetails.dao.delete(this.getPara("id")));
	}
	
	public void findInitFee(){
		renderJson(new ReturnMsg("12000", CycDetails.dao.findInitFee(this)));
	}
	
	public void totalAmount() {
		renderJson(CycDetails.dao.totalAmount(this.getPara("billNo")));
	}
	
	public void tczjmx(){
		renderJson(PageUtils.page(CycDetails.dao.tczjmx(this)));
	}
	
	public void getFkxMoney(){
		renderJson(CycDetails.dao.getFkxMoney(this));
	}
	
}
