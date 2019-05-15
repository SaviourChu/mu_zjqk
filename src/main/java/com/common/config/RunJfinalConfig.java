package com.common.config;

import com.common.interceptor.IPInterceptor;
import com.common.tools.Slf4jLogFactory;
import com.core.admin.model._AdminMappingKit;
import com.core.zjqk.model._MappingKit;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * @author: SaviourChu
 * @description: RunJfinalConfig
 * @date: 2017年8月25日 上午10:31:15
 */
public class RunJfinalConfig extends JFinalConfig {

	Routes routes;

	public void loadProp(String pro, String dev) {
		PropKit.use(pro);
		if (PropKit.getBoolean(ConstantInit.DEVMODE)) {
			PropKit.clear();
			PropKit.use(dev);
		}
	}

	@Override
	public void configConstant(Constants me) {
		loadProp("init.properties", "dev.properties");
		me.setLogFactory(new Slf4jLogFactory());
		me.setBaseViewPath("/WEB-INF/view/");
	}

	@Override
	public void configRoute(Routes me) {
		routes = me;
		// me.add(new AutoBindRoutes());
		me.add(new ApiRoutes());
	}

	@Override
	public void configPlugin(Plugins me) {
		C3p0Plugin zjqk = new C3p0Plugin(PropKit.get(ConstantInit.JDBCURL), PropKit.get(ConstantInit.USERNAME),
				PropKit.get(ConstantInit.PASSWORD), PropKit.get(ConstantInit.DRIVERCLASS));
		zjqk.setInitialPoolSize(5);
		me.add(zjqk);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(zjqk);
		// arp.setShowSql(true);
		_AdminMappingKit.mapping(arp);
		_MappingKit.mapping(arp);
		me.add(arp);

		/*
		 * AutoTableBindPlugin atbp = new AutoTableBindPlugin(PropKit.get(ConstantInit.DATASOURCE), zjqk);
		 * atbp.autoScan(false);
		 * atbp.addScanPackages(PropKit.get(ConstantInit.ADMIN_PACKAGE));
		 * atbp.addScanPackages(PropKit.get(ConstantInit.ZJQK_PACKAGE));
		 * atbp.setShowSql(true); me.add(atbp);
		 */

		ShiroPlugin shiroPlugin = new ShiroPlugin(routes);
		shiroPlugin.setLoginUrl("/login");
		shiroPlugin.setSuccessUrl("/");
		shiroPlugin.setUnauthorizedUrl("/login");
		me.add(shiroPlugin);
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		interceptors.addGlobalActionInterceptor(new IPInterceptor());
		interceptors.add(new SessionInViewInterceptor());
		interceptors.add(new ShiroInterceptor());
	}

	@Override
	public void configHandler(Handlers handlers) {
		handlers.add(new ContextPathHandler("base"));
	}

}
