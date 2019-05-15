package com.core.admin.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

@SuppressWarnings("serial")
public abstract class BaseSecOrg<M extends BaseSecOrg<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	public java.lang.Integer getId() {
		return get("id");
	}
	public void setDescription(java.lang.String description) {
		set("description", description);
	}
	public java.lang.String getDescription() {
		return get("description");
	}
	public void setName(java.lang.String name) {
		set("name", name);
	}
	public java.lang.String getName() {
		return get("name");
	}
}
