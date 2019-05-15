package com.common.kits;

public enum Response {

	SEND_ERROR("13000", "短信验证码发送失败"), 
	SEND_SUCCESS("12000", "短信验证码发送成功"), 
	SIGN_SUCCESS("12000", "注册成功"), 
	SUCCESS("12000", "请求成功"), 
	FAIL("13000", "请求失败"), 
	PARAM_VALIDATE("13000", "参数不合法"), 
	INVALID_AUTO("1300", "用户已经认证或正在审核中"), 
	FAIL_NULL("13000", "查询为空"), LOGIN_FAIL("13000", "用户名或密码错误"), 
	EMAIL_ISREGISTER("13000", "该邮箱已经被绑定或注册"), 
	EMAIL_ISNOT_REGISTER("14000","该邮箱可以注册"), 
	LOGIN_ERROR("14000", "已授权或绑定"), 
	LOGIN_TYPE("13000", "已认证过，正在审核中...");

	private String code;
	private String name;

	private Response(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
