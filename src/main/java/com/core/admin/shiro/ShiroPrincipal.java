/*
 *  Copyright 2014-2015 snakerflow.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.core.admin.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.core.admin.model.User;

/**
 * 自定义认证主体
 * @author yuqs
 * @since 0.1
 */
public class ShiroPrincipal implements Serializable {

	private static final long serialVersionUID = 1428196040744555722L;
	//用户对象
	private User user;
	//用户权限列表
	private List<String> authorities = new ArrayList<String>();
	//用户角色列表
	private List<String> roles = new ArrayList<String>();
	//是否已授权。如果已授权，则不需要再从数据库中获取权限信息，减少数据库访问
	//这里会导致修改权限时，需要重新登录方可有效
	private boolean isAuthorized = false;
	
	/**
	 * 构造函数，参数为User对象
	 * 根据User对象属性，赋值给Principal相应的属性上
	 * @param user
	 */
	public ShiroPrincipal(User user) {
		this.user = user;
	}
	public List<String> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public boolean isAuthorized() {
		return isAuthorized;
	}
	public void setAuthorized(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUsername() {
		return this.user.get("username");
	}
	public Integer getId() {
		return this.user.get("id");
	}
	public Integer getRoleId() {
		return this.user.get("roleId");
	}
	public String getClassify(){
		return this.user.get("classify");
	}
	/**
	 * <shiro:principal/>标签显示中文名称
	 */
	@Override
	public String toString() {
	    return this.user.get("fullname");
	}
}
