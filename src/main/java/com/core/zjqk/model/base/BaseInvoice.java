package com.core.zjqk.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

@SuppressWarnings("serial")
public abstract class BaseInvoice<M extends BaseInvoice<M>> extends Model<M> implements IBean {

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

	public void setInvoiceNo(java.lang.String invoiceNo) {
		set("invoice_no", invoiceNo);
	}

	public java.lang.String getInvoiceNo() {
		return get("invoice_no");
	}

	public void setMoney(java.lang.Double money) {
		set("money", money);
	}

	public java.lang.Double getMoney() {
		return get("money");
	}

	public void setInvoiceTitle(java.lang.String invoiceTitle) {
		set("invoice_title", invoiceTitle);
	}

	public java.lang.String getInvoiceTitle() {
		return get("invoice_title");
	}

	public void setCreateDate(java.lang.String createDate) {
		set("create_date", createDate);
	}

	public java.lang.String getCreateDate() {
		return get("create_date");
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

	public void setCId(java.lang.Integer cId) {
		set("c_id", cId);
	}

	public java.lang.Integer getCId() {
		return get("c_id");
	}
	
	public void setFl(java.lang.String fl) {
		set("fl", fl);
	}
	
	public java.lang.String getFl() {
		return get("fl");
	}
}
