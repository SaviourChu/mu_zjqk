package com.core.admin.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName = "sec_user")
public class UserModel extends Model<UserModel> {

  private static final long serialVersionUID = 1806990704667440158L;

  public static final String table = "sec_user";
  public static final String id = "id";
  public static final String enabled = "enabled";
  public static final String fullname = "fullname";
  public static final String password = "password";
  public static final String plainPwd = "plain_pwd";
  public static final String salt = "salt";
  public static final String username = "username";
  public static final String orgId = "org_id";
  public static final String roleId = "role_id";

  public static final UserModel DAO = new UserModel();

}
