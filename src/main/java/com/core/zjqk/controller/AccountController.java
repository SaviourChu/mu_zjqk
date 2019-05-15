package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.Account;
import com.core.zjqk.model.Store;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class AccountController extends Controller {

	public void index() {
		List<Account> accounts = Account.dao.list();
		this.setAttr("accounts", accounts);
		List<Store> stores = Store.dao.list();
		this.setAttr("stores", stores);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(Account.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(Account.dao.saveOrUpdate(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(Account.dao.deletes(this.getPara("ids")));
	}

	public void listAccounts(){
		renderJson(Account.dao.list());
	}
	
	public void listByStore(){
		renderJson(Account.dao.listByStore(this));
	}
	
	@Before(Tx.class)
	public void importAccounts(){
		renderJson(Account.dao.importAccounts(this));;
	}
}
