package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.PayAccount;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class PayAccountController extends Controller {

	public void index() {
		List<PayAccount> pAccounts = PayAccount.dao.list();
		this.setAttr("pAccounts", pAccounts);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(PayAccount.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(PayAccount.dao.saveOrUpdate(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(PayAccount.dao.deletes(this.getPara("ids")));
	}
	
	public void listPayAccounts(){
		renderJson(PayAccount.dao.list());
	}
	
	@Before(Tx.class)
	public void importPayAccounts(){
		renderJson(PayAccount.dao.importPayAccounts(this));;
	}

}
