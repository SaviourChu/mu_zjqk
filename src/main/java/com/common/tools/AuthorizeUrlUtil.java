package com.common.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

public class AuthorizeUrlUtil {

	public static final String snsapi_base = "https://open.weixin.qq.com/connect/oauth2/authorize?";
	
	public static enum ScopeType {
		snsapi_base, snsapi_userinfo;
	}
	
	public static String createUrl(String url,ScopeType scopeType){
		String wxurl = "";
		try {
			String encodeurl = URLEncoder.encode(url, "UTF-8");
			String appId = ApiConfigKit.getApiConfig().getAppId();
			ImmutableMap<Object, Object> map = ImmutableMap.builder().put("appid", appId)
			.put("redirect_uri",encodeurl)
			.put("scope",scopeType.name())
			.put("response_type","code")
			.put("state","123#wechat_redirect").build();
			wxurl =snsapi_base+ Joiner.on("&").withKeyValueSeparator("=").join(map);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return wxurl;
	}
	
	public static String createUrl(String url){
	  return AuthorizeUrlUtil.createUrl(url, ScopeType.snsapi_base);
	}
}
