package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.common.kits.ReturnMsg;
import com.core.zjqk.model.Account;
import com.core.zjqk.model.DispDetails;
import com.core.zjqk.model.DispItems;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class DispDetailsController extends Controller {

	public void index() {
		List<Account> accounts = Account.dao.list();
		List<Store> stores = Store.dao.list();
		List<DispItems> dItems = DispItems.dao.list();
		setAttr("accounts", accounts);
		setAttr("stores", stores);
		setAttr("dItems", dItems);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(DispDetails.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(DispDetails.dao.saveOrUpdate(this));
	}
	
	@Before(Tx.class)
	public void editBillInfo() {
		renderJson(DispDetails.dao.editBillInfo(this));
	}

	@Before(Tx.class)
	public void delete() {
		renderJson(DispDetails.dao.delete(getPara("id")));
	}
	
	public void findInitFee(){
		renderJson(new ReturnMsg("12000", DispDetails.dao.findInitFee(this)));
	}
	
	public void totalAmount() {
		renderJson(DispDetails.dao.totalAmount(getPara("billNo")));
	}
	
	public void fundMgtmx(){
		renderJson(PageUtils.page(DispDetails.dao.fundMgtmx(this)));
	}
}
