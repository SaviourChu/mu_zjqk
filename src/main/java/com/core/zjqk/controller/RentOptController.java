package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.RentOpt;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class RentOptController extends Controller {

	public void index() {
		List<Store> stores = Store.dao.list();
		this.setAttr("stores", stores);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(RentOpt.dao.page(this)));
	}

	//@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(RentOpt.dao.saveOrUpdate(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(RentOpt.dao.deletes(this.getPara("ids")));
	}

	@Before(Tx.class)
	public void importRentOpts(){
		renderJson(RentOpt.dao.importRentOpts(this));;
	}
	
}
