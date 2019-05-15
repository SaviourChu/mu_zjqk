package com.core.admin.model;

import com.core.admin.model.base.BaseSecRoleMenu;
import com.jfinal.plugin.activerecord.Db;

@SuppressWarnings("serial")
public class RoleMenu extends BaseSecRoleMenu<RoleMenu> {

	public static final RoleMenu dao = new RoleMenu();
	
	public void saveRoleMenu(String roleId, String menuId) {
		Db.update("INSERT INTO sec_role_menu(role_id, menu_id) VALUES(?, ?)", roleId, menuId);
	}
	
	public void delRoleMenu(String roleId) {
		Db.update("DELETE FROM sec_role_menu WHERE role_id=?", roleId);
	}
	
	public void delRoleMenu(String roleId, String menuId) {
		Db.update("DELETE FROM sec_role_menu WHERE role_id=? AND menu_id=?", roleId, menuId);
	}
	
}
