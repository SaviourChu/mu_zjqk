package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.DispItems;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class DispItemsController extends Controller {

	public void index() {
		List<DispItems> diItems = DispItems.dao.list();
		this.setAttr("diItems", diItems);
		List<Store> stores = Store.dao.list();
		this.setAttr("stores", stores);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(DispItems.dao.page(this)));
	}
	
	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(DispItems.dao.saveOrUpdate(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(DispItems.dao.deletes(this.getPara("ids")));
	}

	public void listDispItems(){
		renderJson(DispItems.dao.list());
	}
	
	@Before(Tx.class)
	public void importDispItems(){
		renderJson(DispItems.dao.importDispItems(this));;
	}
}
