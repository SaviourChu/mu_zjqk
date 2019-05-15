package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.CycInitfees;
import com.core.zjqk.model.CycItems;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class CycInitfeesController extends Controller {

	public void index() {
		List<CycItems> ciItems = CycItems.dao.list();
		this.setAttr("ciItems", ciItems);
		List<Store> stores = Store.dao.list();
		this.setAttr("stores", stores);
		render("index.html");
	}
	
	public void list() {
		renderJson(PageUtils.page(CycInitfees.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(CycInitfees.dao.saveOrUpdate(this));
	}
	
	@Before(Tx.class)
	public void saveOrUpdateCIF() {
		renderJson(CycInitfees.dao.saveOrUpdateCIF(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(CycInitfees.dao.deletes(this.getPara("ids")));
	}

	@Before(Tx.class)
	public void importCycInitfees(){
		renderJson(CycInitfees.dao.importCycInitfees(this));;
	}
	
}
