package com.core.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.admin.model.Menu;
import com.core.admin.model.User;
import com.core.admin.shiro.ShiroUtils;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

@RequiresUser
@SuppressWarnings("unused")
public class IndexController extends Controller {
	
	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

	public void index() {
		User user = ShiroUtils.getUser();
		setAttr("user", user);
		List<Menu> menus = Menu.dao.allMenuListByRoleId(user.getRoleId());
		ArrayList<Menu> mainMenus = Lists.newArrayList();
		ArrayList<Menu> subMenus = Lists.newArrayList();
		menus.forEach(new Consumer<Menu>() {
			@Override
			public void accept(Menu menu) {
				Integer pId = menu.getParentMenu();
				if (pId == null || pId == 0) {
					mainMenus.add(menu);
				} else {
					subMenus.add(menu);
				}
			}
		});
		for (Menu mainMenu : mainMenus) {
			for (Menu subMenu : subMenus) {
				Integer pId = subMenu.getParentMenu();
				if (pId != null && pId != 0 && mainMenu.getInt("id") == pId) {
					mainMenu.add(subMenu);
				}
			}
		}
		setAttr("menus", mainMenus);
		render("index.html");
	}
	
}
