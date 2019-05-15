package com.common.kits;

public class ReturnMsg {

	public static final String MESSAGE = "msg";
	private String code;
	private String msg;
	private Object rows;

	public ReturnMsg() {
	}

	public ReturnMsg(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public ReturnMsg(Response resonse) {
		this.code = resonse.getCode();
		this.msg = resonse.getName();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	public static final String VAIIDATE_PWD = "密码格式不正确";
	public static final String VAIIDATE_NICKNAMEE = "用户名长度必须为2-12";
	public static final String VAIIDATE_NITRO = "用户简介长度必须为6-50";
	public static final String VAIIDATE_WANT = "请填写想整形项目";
	public static final String VAIIDATE_FINSH = "请选择已整形的项目";

	public static final String VAIIDATE_CODE = "短信验证码校验失败";

	public static final String DOUBLE_REGISTER = "该手机号已被注册或绑定";
	public static final String PHONE_NORES = "该手机号未注册";

	public static final String TOKEN_ERROR = "token参数有误";
	public static final String TYPE_ERROR = "type参数有误";
	public static final String CODE_NULL = "验证码不能为空";
	public static final String CODE_EXPIRED = "验证码已过期";

	public static final String PHONE_EXISTENT = "手机号不存在";

	public static final String PARAMETER_ERROR = "参数错误";

	public static final ReturnMsg TOKENERROR = new ReturnMsg("11000", "token校验失败");

	public static final ReturnMsg SUCCESS = new ReturnMsg("12000", "操作成功！");
	
	public static final ReturnMsg NORIGHT = new ReturnMsg("12005", "对不起，您没有该操作权限！");
	public static final ReturnMsg INVOICE_MONEY_ERROR = new ReturnMsg("12006", "您的发票记录金额超过实际支付金额！");
	
	public static final ReturnMsg EXPORTSUCCESS = new ReturnMsg("12001", "导出成功！");
	public static final ReturnMsg EXPORTERROR = new ReturnMsg("12002", "导出失败！");
	public static final ReturnMsg ERROR = new ReturnMsg("14000", "您的数据有误，操作失败！");
	public static final ReturnMsg REPEATERROR = new ReturnMsg("14001", "您填写的信息不能重复！");
	public static final ReturnMsg DATAERROR = new ReturnMsg("14002", "请您将数据填写完整！");
	public static final ReturnMsg PROMPTINFO = new ReturnMsg("14003", "您的请款单头部信息有修改，<br>请点击<span style='color:red;'>[确认编辑]</span>按钮！");
	public static final ReturnMsg YMEMPTY = new ReturnMsg("14004", "请选择新增记录的年月！");
	public static final ReturnMsg SAVE_SUCCESS = new ReturnMsg("12000", "保存成功");
	public static final ReturnMsg SAVE_ERROR = new ReturnMsg("14000", "保存失败");

	public static final String SUCCESS_CODE = "12000";
	public static final String SUCCESS_MSG = "请求成功";

	public static final String ERROR_CODE = "13000";
	public static final String ERROR_MSG = "请求失败";

	public static final String MSG_CODE = "14000";

}
