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

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.core.admin.model.User;

/**
 * shiro工具类
 */
public class ShiroUtils {
	/**
	 * 返回当前登录的认证实体ID
	 * 
	 * @return
	 */
	public static Integer getUserId() {
		ShiroPrincipal principal = getPrincipal();
		return (principal != null) ? principal.getId() : -1;
	}

	public static User getUser() {
		ShiroPrincipal principal = getPrincipal();
		return (principal != null) ? principal.getUser() : null;
	}

	/**
	 * 返回当前登录的认证实体部门ID
	 * 
	 * @return
	 */
	public static Integer getOrgId() {
		User user = getUser();
		Integer orgId = user.getOrgId();
		return (user != null && orgId != null) ? orgId : -1;
	}

	/**
	 * 获取当前登录的认证实体
	 * 
	 * @return
	 */
	public static ShiroPrincipal getPrincipal() {
		Subject subject = SecurityUtils.getSubject();
		return (ShiroPrincipal) subject.getPrincipal();
	}

	/**
	 * 获取所有组集合
	 * 
	 * @return
	 */
	public static List<String> getGroups() {
		List<String> groups = new ArrayList<String>();
		Integer orgId = getOrgId();
		ShiroPrincipal principal = getPrincipal();
		if (principal != null) {
			groups.addAll(principal.getRoles());
		}
		if (orgId != null) {
			groups.add(String.valueOf(orgId));
		}
		return groups;
	}

	/**
	 * 获取当前认证实体的中文名称
	 * 
	 * @return
	 */
	public static String getFullname() {
		ShiroPrincipal sp = getPrincipal();
		return sp != null ? sp.toString() : "";
	}

	/**
	 * 获取当前认证实体的登录名称
	 * 
	 * @return
	 */
	public static String getUsername() {
		ShiroPrincipal sp = getPrincipal();
		return sp != null ? sp.toString() : "";
	}

	/**
	 * 获取当前认证的实体部门名称
	 * 
	 * @return
	 */
	public static String getOrgName() {
		User user = getUser();
		return user != null ? user.get("orgName") : "";
	}

	/*
	 * 获取分类
	 */
	public static String getClassify() {
		ShiroPrincipal sp = getPrincipal();
		return sp != null ? sp.getClassify() : "";
	}

	public static Integer getRoleId() {
		ShiroPrincipal sp = getPrincipal();
		return sp != null ? sp.getRoleId() : null;
	}

	public static PrincipalCollection getPrincipals() {
		Subject subject = SecurityUtils.getSubject();
		return (PrincipalCollection) subject.getPrincipals();
	}
}
