package com.core.zjqk.controller;

import com.common.kits.PageUtils;
import com.core.zjqk.model.Invoice;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;

public class InvoiceController extends Controller {


	@Before(Tx.class)
	public void saveInvoice() {
		renderJson(Invoice.dao.save(this));
	}
	
	public void listInvoice() {
		renderJson(PageUtils.page(Invoice.dao.page(this)));
	}
	
	@Before(Tx.class)
	public void deleteInvoice() {
		renderJson(Invoice.dao.delete(this.getPara("id")));
	}
	
	@Before(Tx.class)
	public void updateInvoice() {
		renderJson(Invoice.dao.update(this));
	}

	public void totalAmount() {
		renderJson(Invoice.dao.totalAmount(this.getPara("billNo")));
	}
}
