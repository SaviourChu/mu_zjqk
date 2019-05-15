package com.core.admin.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.common.tools.Digests;
import com.common.tools.EncodeUtils;
import com.core.admin.model.base.BaseSecUser;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class User extends BaseSecUser<User> {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	public static final User DAO = new User();

	public Page<User> paginate(int pageNumber, int pageSize, User user) {
		StringBuilder sqlExceptSelect = new StringBuilder();
		String select = "SELECT u.id,u.fullname,u.username,u.phone,u.plain_pwd password,o.id orgId,o.`name` orgName,"
				+ "r.id roleId,r.`name` roleName,u.enabled,u.create_date createDate ";
		sqlExceptSelect.append("FROM sec_user u LEFT JOIN sec_org o ON u.org_id=o.id LEFT JOIN sec_role r ON u.role_id=r.id WHERE 1=1 ");
		List<String> params = new ArrayList<String>();
		String username = user.getStr("username");
		String fullname = user.getStr("fullname");
		if (StringUtils.isNotEmpty(username)) {
			sqlExceptSelect.append(" AND u.username=? ");
			params.add(username);
		}
		if (StringUtils.isNotEmpty(fullname)) {
			sqlExceptSelect.append(" AND u.fullname=? ");
			params.add(fullname);
		}
		sqlExceptSelect.append(" ORDER BY id DESC");
		return paginate(pageNumber, pageSize, select, sqlExceptSelect.toString(), params.toArray());
	}

	public User getByName(String name) {
		String sql = "SELECT u.*,o.name as orgName FROM sec_user u LEFT JOIN sec_org o ON u.org_id=o.id WHERE u.username=? ";
		return User.DAO.findFirst(sql, name);
	}

	// 根据用户ID查询该用户所拥有的权限列表
	public List<String> getAuthoritiesName(Integer userId) {
		String sql = "SELECT m.`name` FROM sec_menu m "
				+ "LEFT JOIN sec_role_menu rm ON m.id=rm.menu_id "
				+ "LEFT JOIN sec_user u ON u.role_id=rm.role_id WHERE u.id=?";
		return Db.query(sql, userId);
	}

	// 根据用户ID查询该用户所拥有的角色列表
	public List<String> getRolesName(Integer userId) {
		String sql = "SELECT r.`name` FROM sec_user u LEFT JOIN sec_role r ON u.role_id=r.id WHERE u.id=?";
		return Db.query(sql, userId);
	}

	// 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
	public void entryptPassword(User user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.set("salt", EncodeUtils.hexEncode(salt));
		byte[] hashPassword = Digests.sha1(user.getStr("plain_pwd").getBytes(), salt, HASH_INTERATIONS);
		user.set("password", EncodeUtils.hexEncode(hashPassword));
	}
}
