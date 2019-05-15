package com.core.admin.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

@SuppressWarnings("serial")
public abstract class BaseSecUser<M extends BaseSecUser<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}
	public java.lang.Integer getId() {
		return get("id");
	}
	public void setEnabled(java.lang.Integer enabled) {
		set("enabled", enabled);
	}
	public java.lang.Integer getEnabled() {
		return get("enabled");
	}
	public void setFullname(java.lang.String fullname) {
		set("fullname", fullname);
	}
	public java.lang.String getFullname() {
		return get("fullname");
	}
	public void setPassword(java.lang.String password) {
		set("password", password);
	}
	public java.lang.String getPassword() {
		return get("password");
	}
	public void setPlainPwd(java.lang.String plainPwd) {
		set("plain_pwd", plainPwd);
	}
	public java.lang.String getPlainPwd() {
		return get("plain_pwd");
	}
	public void setSalt(java.lang.String salt) {
		set("salt", salt);
	}
	public java.lang.String getSalt() {
		return get("salt");
	}
	public void setUsername(java.lang.String username) {
		set("username", username);
	}
	public java.lang.String getUsername() {
		return get("username");
	}
	public void setClassify(java.lang.String classify) {
		set("classify", classify);
	}
	public java.lang.String getClassify() {
		return get("classify");
	}
	public void setOrgId(java.lang.Integer orgId) {
		set("org_id", orgId);
	}
	public java.lang.Integer getOrgId() {
		return get("org_id");
	}
	public void setRoleId(java.lang.Integer roleId) {
		set("role_id", roleId);
	}
	public java.lang.Integer getRoleId() {
		return get("role_id");
	}
	public void setPhone(java.lang.String phone) {
		set("phone", phone);
	}
	public java.lang.String getPhone() {
		return get("phone");
	}
	public void setCreateDate(java.lang.String createDate) {
		set("create_date", createDate);
	}
	public java.lang.String getCreateDate() {
		return get("create_date");
	}

}
