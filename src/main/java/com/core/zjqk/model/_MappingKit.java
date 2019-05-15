package com.core.zjqk.model;

import com.core.zjqk.model.Account;
import com.core.zjqk.model.CycInitfees;
import com.core.zjqk.model.CycItems;
import com.core.zjqk.model.DispInitfees;
import com.core.zjqk.model.DispItems;
import com.core.zjqk.model.PayAccount;
import com.core.zjqk.model.RentBill;
import com.core.zjqk.model.RentOpt;
import com.core.zjqk.model.Store;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("account", "id", Account.class);
		arp.addMapping("cyc_bills", "id", CycBills.class);
		arp.addMapping("cyc_details", "id", CycDetails.class);
		arp.addMapping("cyc_initfees", "id", CycInitfees.class);
		arp.addMapping("cyc_items", "id", CycItems.class);
		arp.addMapping("disp_bills", "id", DispBills.class);
		arp.addMapping("disp_details", "id", DispDetails.class);
		arp.addMapping("disp_initfees", "id", DispInitfees.class);
		arp.addMapping("disp_items", "id", DispItems.class);
		arp.addMapping("invoice", "id", Invoice.class);
		arp.addMapping("invoice_title", "id", InvoiceTitle.class);
		arp.addMapping("pay_account", "id", PayAccount.class);
		arp.addMapping("rent_bill", "id", RentBill.class);
		arp.addMapping("rent_opt", "id", RentOpt.class);
		arp.addMapping("store", "id", Store.class);
		arp.addMapping("t_var", "id", TVar.class);
	}
}

