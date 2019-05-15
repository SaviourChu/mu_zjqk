package com.core.admin.shiro;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.tools.EncodeUtils;
import com.core.admin.model.User;


public class ShiroAuthorizingRealm extends AuthorizingRealm {

	private static final Logger LOG = LoggerFactory.getLogger(ShiroAuthorizingRealm.class);
	
	public ShiroAuthorizingRealm() {
		setAuthenticationTokenClass(UsernamePasswordToken.class);
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(User.HASH_ALGORITHM);
		matcher.setHashIterations(User.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}
	
	/**
	 * 根据认证方式（如表单）获取用户名称、密码
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken)token;
		String username = upToken.getUsername();
		if(StringUtils.isBlank(username)){
			throw new AuthenticationException("用户名不能为空");
		}
		User user = null;
		try {
			user = User.DAO.getByName(username);
		} catch (Exception ex) {
			throw new UnknownAccountException("获取用户失败\n" + ex.getMessage());
		}
		if(Objects.isNull(user)){
			throw new UnknownAccountException("用户名或者密码错误");
		}
		if(!StringUtils.equals("1", user.get("enabled").toString())){
			throw new LockedAccountException("该用户已被锁定");
		}
		LOG.info("用户【" + username + "】登录成功");
		byte[] salt = EncodeUtils.hexDecode(user.getStr("salt"));
		ShiroPrincipal subject = new ShiroPrincipal(user);
		List<String> authorities = User.DAO.getAuthoritiesName(user.getInt("id"));
		List<String> rolelist = User.DAO.getRolesName(user.getInt("id"));
		subject.setAuthorities(authorities);
		subject.setRoles(rolelist);
		subject.setAuthorized(true);
		return new SimpleAuthenticationInfo(subject, user.get("password"), ByteSource.Util.bytes(salt), getName());
	}
	
	
	/**
	 * 获取当前认证实体的授权信息（授权包括：角色、权限）
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//获取当前登录的用户名
		ShiroPrincipal subject = (ShiroPrincipal)super.getAvailablePrincipal(principals);
		String username = subject.getUsername();
		Integer userId = subject.getId();
		try {
			if(!subject.isAuthorized()) {
				//根据用户名称，获取该用户所有的权限列表
				List<String> authorities = User.DAO.getAuthoritiesName(userId);
				List<String> rolelist = User.DAO.getRolesName(userId);
				subject.setAuthorities(authorities);
				subject.setRoles(rolelist);
				subject.setAuthorized(true);
			}
		} catch(RuntimeException e) {
			throw new AuthorizationException("用户【" + username + "】授权失败");
		}
		//给当前用户设置权限
		info.addStringPermissions(subject.getAuthorities());
		info.addRoles(subject.getRoles());
		return info;
	}
	
}
