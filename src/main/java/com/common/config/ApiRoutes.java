package com.common.config;

import com.core.admin.controller.IndexController;
import com.core.admin.controller.MenuController;
import com.core.admin.controller.OrgController;
import com.core.admin.controller.RoleController;
import com.core.admin.controller.UserController;
import com.core.zjqk.controller.AccountController;
import com.core.zjqk.controller.CycBillsController;
import com.core.zjqk.controller.CycDetailsController;
import com.core.zjqk.controller.CycInitfeesController;
import com.core.zjqk.controller.CycItemsController;
import com.core.zjqk.controller.DispBillsController;
import com.core.zjqk.controller.DispDetailsController;
import com.core.zjqk.controller.DispInitfeesController;
import com.core.zjqk.controller.DispItemsController;
import com.core.zjqk.controller.InvoiceController;
import com.core.zjqk.controller.InvoiceTitleController;
import com.core.zjqk.controller.PayAccountController;
import com.core.zjqk.controller.RentBillController;
import com.core.zjqk.controller.RentOptController;
import com.core.zjqk.controller.StoreController;
import com.jfinal.config.Routes;

public class ApiRoutes extends Routes {

	@Override
	public void config() {
		add("/", IndexController.class, "/");
		add("/security/menu", MenuController.class, "/sec/menu");
		add("/security/user", UserController.class, "/sec/user");
		add("/security/org", OrgController.class, "/sec/org");
		add("/security/role", RoleController.class, "/sec/role");

		add("/invoice", InvoiceController.class, "/html/invoice");
		add("/invoiceTitle", InvoiceTitleController.class, "/html/invoiceTitle");
		add("/account", AccountController.class, "/html/account");
		add("/payAccount", PayAccountController.class, "/html/payAccount");
		add("/store", StoreController.class, "/html/store");
		add("/rentOpt", RentOptController.class, "/html/rentOpt");
		add("/rentBill", RentBillController.class, "/html/rentBill");

		add("/dispItems", DispItemsController.class, "/html/dispItems");
		add("/dispInitfees", DispInitfeesController.class, "/html/dispInitfees");
		add("/dispDetails", DispDetailsController.class, "/html/dispDetails");
		add("/dispBills", DispBillsController.class, "/html/dispBills");

		add("/cycItems", CycItemsController.class, "/html/cycItems");
		add("/cycInitfees", CycInitfeesController.class, "/html/cycInitfees");
		add("/cycDetails", CycDetailsController.class, "/html/cycDetails");
		add("/cycBills", CycBillsController.class, "/html/cycBills");
	}

}
