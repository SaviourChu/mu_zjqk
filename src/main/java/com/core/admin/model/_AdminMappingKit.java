package com.core.admin.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

public class _AdminMappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("sec_menu", "id", Menu.class);
		arp.addMapping("sec_org", "id", Org.class);
		arp.addMapping("sec_role", "id", Role.class);
		arp.addMapping("sec_role_menu", "id", RoleMenu.class);
		arp.addMapping("sec_user", "id", User.class);
	}
}

