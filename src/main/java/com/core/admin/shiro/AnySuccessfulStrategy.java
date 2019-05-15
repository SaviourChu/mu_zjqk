package com.core.admin.shiro;

import java.util.Collection;
import java.util.Objects;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.CollectionUtils;

public class AnySuccessfulStrategy extends AbstractAuthenticationStrategy {

	@Override
	public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, AuthenticationToken token)
			throws AuthenticationException {
		return null;
	}

	@Override
	protected AuthenticationInfo merge(AuthenticationInfo info, AuthenticationInfo aggregate) {
		if(Objects.nonNull(aggregate)&&!CollectionUtils.isEmpty(aggregate.getPrincipals())){
			return aggregate;
		}
		return info!=null?info:aggregate;
	}

	@Override
	public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo,
			AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
		if(singleRealmInfo == null){
			if(t.getClass().isAssignableFrom(LockedAccountException.class)){
				throw new LockedAccountException(t.getMessage());
			}else if(t.getClass().isAssignableFrom(UnknownAccountException.class)){
				throw new UnknownAccountException(t.getMessage());
			}else if(t.getClass().isAssignableFrom(IncorrectCaptchaException.class)){
				throw new IncorrectCaptchaException(t.getMessage());
			}else if(t.getClass().isAssignableFrom(ExcessiveAttemptsException.class)){
				throw new ExcessiveAttemptsException(t.getMessage());
			}
			throw new AuthenticationException(t.getMessage());
		}
		return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
	}
	
	

	
	
}
