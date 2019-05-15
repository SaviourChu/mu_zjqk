package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.DispInitfees;
import com.core.zjqk.model.DispItems;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class DispInitfeesController extends Controller {

	public void index() {
		List<DispItems> diItems = DispItems.dao.list();
		this.setAttr("diItems", diItems);
		List<Store> stores = Store.dao.list();
		this.setAttr("stores", stores);
		render("index.html");
	}
	
	public void list() {
		renderJson(PageUtils.page(DispInitfees.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(DispInitfees.dao.saveOrUpdate(this));
	}
	
	@Before(Tx.class)
	public void saveOrUpdateDIF() {
		renderJson(DispInitfees.dao.saveOrUpdateDIF(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(DispInitfees.dao.deletes(this.getPara("ids")));
	}

	@Before(Tx.class)
	public void importDispInitfees(){
		renderJson(DispInitfees.dao.importDispInitfees(this));;
	}
}
