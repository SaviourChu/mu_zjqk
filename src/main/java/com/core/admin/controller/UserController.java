package com.core.admin.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;

import com.common.config.JdbcUtils;
import com.common.kits.PageUtils;
import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.admin.model.Org;
import com.core.admin.model.Role;
import com.core.admin.model.User;
import com.core.zjqk.model.TVar;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.ext.plugin.shiro.ClearShiro;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;

@RequiresUser
public class UserController extends Controller {
	
	public void index() {
		List<Role> roles = Role.dao.findList();
		List<Org> orgs = Org.dao.findOrgList();
		setAttr("roles", roles);
		setAttr("orgs", orgs);
		render("userList.html");
	}

	public void userlist() {
		Integer pages = this.getParaToInt("page");
		Integer rows = this.getParaToInt("rows");
		Page<User> page = User.DAO.paginate(pages, rows, new User());
		renderJson(PageUtils.page(page));
	}

	@Before(Tx.class)
	public void saveOrUpdate() {
		try {
			User user = new User();
			String password = getPara("password");
			String userName = getPara("username");
			String orgId = getPara("orgId");
			String roleId = getPara("roleId");
			if(StringUtils.isAnyBlank(userName, orgId, roleId)){
				renderJson(new ReturnMsg("400", "您的登录名、所属部门或角色数据有误！"));
			}else{
				Matcher match=Pattern.compile("^[a-zA-Z0-9]+$").matcher(userName);
				if(!match.matches()){
					renderJson(new ReturnMsg("400", "抱歉，登录名只支持英文和数字！"));
				}else{
					String fullname = getPara("fullname");
					String phone = getPara("phone");
					user.set("username", userName.trim())
						.set("fullname", Tools.trimStr(fullname))
						.set("phone", Tools.trimStr(phone))
						.set("org_id", orgId)
						.set("role_id", roleId)
						.set("plain_pwd", password)
						.set("enabled", getPara("enabled"));
					if (StringUtils.isNotBlank(password)) {
						user.entryptPassword(user);
					}
					String id = getPara("id");
					if(StringUtils.isNoneBlank(id)){
						String enabled = getPara("enabled");
						user.set("enabled", enabled).set("id", id).update();
					}else{
						user.set("create_date", Tools.ymdStr()).save();
					}
				}
			}
			renderJson(ReturnMsg.SUCCESS);
		} catch (Exception e) {
			renderJson(ReturnMsg.ERROR);
		}
	}

	@Before(Tx.class)
	public void delete() {
		try {
			User.DAO.deleteById(getParaToInt("id"));
			renderJson(ReturnMsg.SUCCESS);
		} catch (Exception e) {
			renderJson(ReturnMsg.ERROR);
		}
	}

	@ClearShiro
	@ActionKey("/login")
	public void login() {
		keepModel(User.class);
		render("/WEB-INF/view/login.html");
	}

	@ClearShiro
	public void dologin() {
		ReturnMsg result = new ReturnMsg();
		String username = getPara("user.username").trim();
		String password = getPara("user.password");
		String classify = getPara("classify", "奶茶").trim();
		Connection conn = JdbcUtils.getConnection();
		PreparedStatement psmt = null;
		UsernamePasswordToken token = null;
		try {
			psmt = conn.prepareStatement("UPDATE sec_user SET classify=? WHERE username=? AND plain_pwd=? ");
			psmt.setString(1, classify);
			psmt.setString(2, username);
			psmt.setString(3, password);
			psmt.executeUpdate();
			Long count = TVar.dao.findFirst("SELECT COUNT(1) count FROM t_var WHERE classify=? ", classify).getLong("count");
			if (count == 0) {
				new TVar().set("bill_no", "PD20180200000").set("type", 1).set("classify", classify).save();
				new TVar().set("bill_no", "PE20180200000").set("type", 2).set("classify", classify).save();
			}
			token = new UsernamePasswordToken(username, password);
			SecurityUtils.getSubject().login(token);
			result.setCode(ReturnMsg.SUCCESS_CODE);
			result.setMsg(ReturnMsg.SUCCESS_MSG);
		} catch (Exception e) {
			token.clear();
			result.setMsg("您输入的登录名或密码有误！");
		} finally {
			JdbcUtils.closeAll(null, null, psmt, conn);
		}
		renderJson(result);
	}

	public void dologout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		redirect("/");
	}
}
