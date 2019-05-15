package com.core.zjqk.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.core.zjqk.model.InvoiceTitle;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class InvoiceTitleController extends Controller {

	public void index() {
		List<InvoiceTitle> its = InvoiceTitle.dao.list();
		this.setAttr("its", its);
		render("index.html");
	}

	public void list() {
		renderJson(PageUtils.page(InvoiceTitle.dao.page(this)));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		renderJson(InvoiceTitle.dao.saveOrUpdate(this));
	}

	@Before(Tx.class)
	public void deletes() {
		renderJson(InvoiceTitle.dao.deletes(this.getPara("ids")));
	}
	
	public void InvoiceTitlesInfo(){
		renderJson(InvoiceTitle.dao.list());
	}
	
	@Before(Tx.class)
	public void importInvoiceTitles(){
		renderJson(InvoiceTitle.dao.importInvoiceTitles(this));;
	}
}
