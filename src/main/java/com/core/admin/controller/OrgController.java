package com.core.admin.controller;

import java.util.Map;

import com.common.kits.PageUtils;
import com.common.kits.ReturnMsg;
import com.core.admin.model.Org;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class OrgController extends Controller {

	public void index() {
		render("orgList.html");
	}

	public void orgList() {
		Integer pages = getParaToInt("page");
		Integer rows = getParaToInt("rows");
		Page<Org> page = Org.dao.paginate(pages, rows);
		Map<String, Object> result = PageUtils.page(page);
		renderJson(result);
	}
	
	public void saveOrUpdate(){
		try {
			renderJson(Org.dao.saveOrUpdate(this));
		} catch (Exception e) {
			renderJson(ReturnMsg.ERROR);
		}
	}

	public void delete() {
		try {
			String id = getPara("id");
			Org.dao.deleteById(id);
			renderJson(ReturnMsg.SUCCESS);
		} catch (Exception e) {
			renderJson(ReturnMsg.ERROR);
		}
	}

}
