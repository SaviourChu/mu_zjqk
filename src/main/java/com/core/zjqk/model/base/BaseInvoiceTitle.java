package com.core.zjqk.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseInvoiceTitle<M extends BaseInvoiceTitle<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setFlag(java.lang.Integer flag) {
		set("flag", flag);
	}

	public java.lang.Integer getFlag() {
		return get("flag");
	}

	public void setClassify(java.lang.String classify) {
		set("classify", classify);
	}
	
	public java.lang.String getClassify() {
		return get("classify");
	}
}
