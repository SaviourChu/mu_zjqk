package com.core.admin.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.common.kits.ReturnMsg;
import com.core.admin.model.base.BaseSecOrg;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class Org extends BaseSecOrg<Org> {

	public static final Org dao = new Org();

	public Page<Org> paginate(int pages, int rows) {
		return this.paginate(pages, rows, "SELECT id,name,description ", "FROM sec_org");
	}
	
	public List<Org> findOrgList() {
		return this.find("SELECT id, name FROM sec_org");
	}
	
	public String getOrgName(String userName){
		String sql = "SELECT o.`name` orgName FROM sec_org o LEFT JOIN sec_user u ON u.org_id=o.id WHERE u.username=? OR u.fullname=?";
		Org org = this.findFirst(sql, userName, userName);
		return org != null ? org.get("orgName") : "";
	}
	
	public ReturnMsg saveOrUpdate(Controller c){
		boolean result = false;
		String id = c.getPara("id");
		String name = c.getPara("name");
		String description = c.getPara("description");
		if(StringUtils.isBlank(name)){
			return ReturnMsg.DATAERROR;
		}
		Org org = new Org();
		org.set("name", name.trim()).set("description", description);
		result = StringUtils.isNoneBlank(id) ? org.set("id", id).update() : org.save();
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
	
}

