package com.core.admin.model;

public class TempMenu {

	private Integer id;
	private Integer parentMenu;
	private String name;
	private boolean checked;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(Integer parentMenu) {
		this.parentMenu = parentMenu;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
}
