package com.core.admin.model;

import java.util.List;

import com.core.admin.model.base.BaseSecRole;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class Role extends BaseSecRole<Role> {

	public static final Role dao = new Role();
	
	public List<Role> findList() {
		return find("SELECT id,`name` FROM sec_role");
	}
	
	public Page<Role> findPage(int page, int row) {
		return this.paginate(page, row, "SELECT id,description,name", "FROM sec_role ORDER BY id");
	}
	
	public List<Role> getAllMenusByRoleId(String roleId){
		return find("SELECT menu_id menuId FROM sec_role_menu WHERE role_id=?", roleId);
	}
	
	public Integer getRoleIdByName(String roleName){
		Role role = findFirst("SELECT id FROM sec_role WHERE name=? LIMIT 1", roleName);
		return role != null ? role.getId() : 0;
	}
	
}
