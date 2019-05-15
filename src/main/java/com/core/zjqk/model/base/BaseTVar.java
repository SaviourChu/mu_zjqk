package com.core.zjqk.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

@SuppressWarnings("serial")
public abstract class BaseTVar<M extends BaseTVar<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setBillNo(java.lang.String billNo) {
		set("bill_no", billNo);
	}

	public java.lang.String getBillNo() {
		return get("bill_no");
	}

	public void setType(java.lang.Integer type) {
		set("type", type);
	}

	public java.lang.Integer getType() {
		return get("type");
	}
	
	public void setClassify(java.lang.String classify) {
		set("classify", classify);
	}
	
	public java.lang.String getClassify() {
		return get("classify");
	}
}
