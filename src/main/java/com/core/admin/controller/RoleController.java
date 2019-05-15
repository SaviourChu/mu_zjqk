package com.core.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.common.kits.PageUtils;
import com.common.kits.ReturnMsg;
import com.core.admin.model.Menu;
import com.core.admin.model.Role;
import com.core.admin.model.RoleMenu;
import com.core.admin.model.TempMenu;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

public class RoleController extends Controller {

	public void index() {
		render("roleList.html");
	}

	public void roleList() {
		Integer pages = this.getParaToInt("page");
		Integer rows = this.getParaToInt("rows");
		Page<Role> findpage = Role.dao.findPage(pages, rows);
		Map<String, Object> page = PageUtils.page(findpage);
		renderJson(page);
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		try {
			String id = getPara("id");
			String name = getPara("name");
			if(StringUtils.isNoneBlank(name)){
				name = name.trim();
				String description = getPara("description");
				Role role = new Role();
				role.set("name", name);
				role.set("description", description);
				if(StringUtils.isBlank(id)){
					role.save();
					id = Role.dao.getRoleIdByName(name).toString();
				}else{
					role.set("id", id).update();
				}
				String roles = getPara("roles");
				if(StringUtils.isNoneBlank(roles)){
					roles = roles.substring(0, roles.length()-1);
					for(String roleId : roles.split(",")){
						String[] str = roleId.split("-");
						if("1".equals(str[1])){
							RoleMenu.dao.saveRoleMenu(id, str[0]);
						}else{
							RoleMenu.dao.delRoleMenu(id, str[0]);
						}
					}
				}
				renderJson(ReturnMsg.SUCCESS);
			}else{
				renderJson(ReturnMsg.ERROR);
			}
		} catch (Exception e) {
			renderJson(ReturnMsg.ERROR);
		}
	}
	
	@Before(Tx.class)
	public void delete() {
		try {
			String id = this.getPara("id");
			Role.dao.deleteById(id);
			RoleMenu.dao.delRoleMenu(id);
			renderJson(ReturnMsg.SUCCESS);
		} catch (Exception e) {
			renderJson(ReturnMsg.ERROR);
		}
	}
	
	public void loadMenus(){
		List<Menu> allMenus = Menu.dao.allMenuList();
		String roleId = getPara("roleId");
		if(StringUtils.isNotBlank(roleId)){
			List<TempMenu> allMenusTemp = new ArrayList<>();
			List<Role> selectedMenus = Role.dao.getAllMenusByRoleId(roleId);
			for(int i=0; i<allMenus.size(); i++){
				Menu menu = allMenus.get(i);
				TempMenu tempMenu = new TempMenu();
				tempMenu.setId(menu.getId());
				tempMenu.setParentMenu(menu.getParentMenu());
				tempMenu.setName(menu.getName());
				tempMenu.setChecked(false);
				for(int j=0; j<selectedMenus.size(); j++){
					if(menu.getId() == selectedMenus.get(j).getInt("menuId")){
						tempMenu.setChecked(true);
					}
				}
				allMenusTemp.add(tempMenu);
			}
			renderJson(allMenusTemp);
		}else{
			renderJson(allMenus);
		}
	}
	
}
