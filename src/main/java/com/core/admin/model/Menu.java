package com.core.admin.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.common.kits.ReturnMsg;
import com.core.admin.model.base.BaseSecMenu;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class Menu extends BaseSecMenu<Menu> {

	public static final Menu dao = new Menu();
	
	private List<Menu> subMenus = Lists.newArrayList();

	
	// 菜单资源的根菜单标识为0
	public static final Integer ROOT_MENU = 0;
	
	public ReturnMsg saveOrUpdate(Controller c){
		boolean result = false;
		String id = c.getPara("id");
		String name = c.getPara("name");
		String parentMenu = c.getPara("parentMenu", null);
		String url = c.getPara("url");
		String description = c.getPara("description");
		int sort = findSort(parentMenu);
		Menu menu = new Menu();
		if(StringUtils.isBlank(name)){
			return ReturnMsg.DATAERROR;
		}
		menu.set("name", name.trim());
		menu.set("parent_menu", parentMenu);
		menu.set("url", url);
		menu.set("description", description);
		menu.set("sort", sort);
		result = StringUtils.isNoneBlank(id) ? menu.set("id", id).update() : menu.save();
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}

	/*
	 * 查询排序
	 */
	private int findSort(String parentMenu) {
		Integer index = 0;
		String sql = "SELECT sort FROM sec_menu WHERE parent_menu=? ";
		List<Menu> list = this.find(sql, parentMenu);
		for (Menu record : list) {
			Integer sum = record.get("sort");
			if (index < sum) {
				index = sum;
			}
		}
		return index;
	}
	
	public List<Menu> allMenuList(){
		return find("SELECT id,`name`,parent_menu FROM sec_menu ORDER BY id,sort");
	}
	
	public List<Menu> allMenuListByRoleId(Integer roleId){
		return find("SELECT m.id,`name`,parent_menu,url FROM sec_menu m "
				+ "LEFT JOIN sec_role_menu rm ON m.id=rm.menu_id WHERE rm.role_id=?", roleId);
	}
	
	/*
	 * 分页查询菜单
	 */
	public Page<Menu> findAll(Integer pages, Integer rows, String menuId) {
		String select = "SELECT m1.id,m1.`name`,m2.`name` parentMenu,m1.url,m1.description ";
		String from = "FROM sec_menu m1 LEFT JOIN sec_menu m2 ON m1.parent_menu=m2.id ";
		List<Object> params = Lists.newArrayListWithExpectedSize(1);
		if (StringUtils.isNoneBlank(menuId)) {
			from += "WHERE m1.parent_menu=? ";
			params.add(menuId);
		}
		return this.paginate(pages, rows, select, from, params.toArray());
	}
	
	/*
	 * 查询所有父级菜单
	 */
	public List<Menu> findMenu() {
		return find("SELECT id,`name` FROM sec_menu WHERE parent_menu IS NULL OR parent_menu=0");
	}

	public List<Menu> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<Menu> subMenus) {
		this.subMenus = subMenus;
	}

	public void add(Menu menu) {
		subMenus.add(menu);
	}
	
}
