package com.common.interceptor;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 登陆校验如果用户没有登陆则跳转到首页
 */
public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
	    Controller controller = inv.getController();
	    HttpSession session = controller.getSession(false);
	    Optional<HttpSession> sessionOpt = Optional.ofNullable(session);
	    if(sessionOpt.isPresent()){
	    	Object user = sessionOpt.get().getAttribute("user");
	    	  if(Optional.ofNullable(user).isPresent()){
	    		  inv.invoke();
	    	  }else{
	    		  controller.redirect("/login");
	    	  }
	    }else{
	      controller.redirect("/login");
	    }
	}
}
