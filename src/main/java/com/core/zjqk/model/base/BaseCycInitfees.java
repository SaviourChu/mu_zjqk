package com.core.zjqk.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseCycInitfees<M extends BaseCycInitfees<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setSId(java.lang.Integer sId) {
		set("s_id", sId);
	}

	public java.lang.Integer getSId() {
		return get("s_id");
	}

	public void setCId(java.lang.Integer cId) {
		set("c_id", cId);
	}

	public java.lang.Integer getCId() {
		return get("c_id");
	}

	public void setInitFee(java.lang.String initFee) {
		set("init_fee", initFee);
	}

	public java.lang.String getInitFee() {
		return get("init_fee");
	}

	public void setDelFlag(java.lang.Integer delFlag) {
		set("del_flag", delFlag);
	}

	public java.lang.Integer getDelFlag() {
		return get("del_flag");
	}

}
