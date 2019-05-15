package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.CycItems;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class CycItemsController extends Controller {

	public void index() {
		List<CycItems> ciItems = CycItems.dao.list();
		this.setAttr("ciItems", ciItems);
		List<Store> stores = Store.dao.list();
		this.setAttr("stores", stores);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(CycItems.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(CycItems.dao.saveOrUpdate(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(CycItems.dao.deletes(this.getPara("ids")));
	}

	public void listCycItems(){
		renderJson(CycItems.dao.list());
	}
	
	@Before(Tx.class)
	public void importCycItems(){
		renderJson(CycItems.dao.importCycItems(this));;
	}
	
	public void getCiNames() {
		renderJson(CycItems.dao.getCiNames(this));
	}
	
}
