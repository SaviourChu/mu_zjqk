package com.core.admin.controller;

import java.util.List;

import com.common.kits.PageUtils;
import com.common.kits.ReturnMsg;
import com.core.admin.model.Menu;
import com.core.admin.validate.MenuValidator;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

public class MenuController extends Controller {
	/*
	 * 跳转页面并且查询父级菜单
	 */
	public void index() {
		List<Menu> menus = Menu.dao.findMenu();
		setAttr("menus", menus);
		render("menulist.html");
	}

	/*
	 * 分页查询所有的菜单
	 */
	public void findAll() {
		Integer pages = getParaToInt("page");
		Integer rows = getParaToInt("rows");
		String menuId = getPara("menuId");
		Page<Menu> page = Menu.dao.findAll(pages, rows, menuId);
		renderJson(PageUtils.page(page));
	}

	@Before({ Tx.class })
	public void insert() {
		renderJson(Menu.dao.saveOrUpdate(this));
	}

	@Before(MenuValidator.class)
	public void save() {
		getModel(Menu.class).save();
		redirect("/security/menu");
	}

	@Before(MenuValidator.class)
	public void update() {
		getModel(Menu.class).update();
		redirect("/security/menu");
	}

	public void delete() {
		try {
			Menu.dao.deleteById(getPara("id"));
			renderJson(ReturnMsg.SUCCESS);
		} catch (Exception e) {
			renderJson(ReturnMsg.ERROR);
		}
	}
	
	public void menuList() {
		renderJson(Menu.dao.findMenu());
	}
}
