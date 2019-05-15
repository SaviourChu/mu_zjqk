package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.RentBill;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class RentBillController extends Controller {

	public void index() {
		List<Store> stores = Store.dao.list();
		this.setAttr("stores", stores);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(RentBill.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(RentBill.dao.saveOrUpdate(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(RentBill.dao.deletes(this.getPara("ids")));
	}
	
	@Before(Tx.class)
	public void add() {
		renderJson(RentBill.dao.add(this));
	}

}
