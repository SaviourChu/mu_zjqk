package com.core.zjqk.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.base.BaseAccount;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class Account extends BaseAccount<Account> {
	
	public static final Account dao = new Account();
	
	public Page<Account> page(Controller c){
		String classify = ShiroUtils.getClassify();
		String select = "SELECT id,name,account,bank,snames ";
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		sqlExceptSelect.append("FROM account WHERE flag=1 AND classify='"+classify+"' ");
		String aName = c.getPara("aName");
		if(StringUtils.isNotBlank(aName)){
			sqlExceptSelect.append("AND INSTR(name, ?) ");
			params.add(aName);
	    }
		sqlExceptSelect.append("ORDER BY name ");
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	public List<Account> list(){
		String classify = ShiroUtils.getClassify();
		return this.find("SELECT name AS aName, id AS aId FROM account WHERE flag=1 AND classify='"+classify+"' ORDER BY name ");
	}
	
	public List<Account> listByStore(Controller c){
		String sName = c.getPara("sName");
		return this.find("SELECT `name` AS aName FROM account WHERE INSTR(snames,?) AND flag=1 AND classify=?", sName, ShiroUtils.getClassify());
	}
	
	public ReturnMsg saveOrUpdate(Controller c){
		String classify = ShiroUtils.getClassify();
		boolean result = false;
		String id = c.getPara("id");
		String name = c.getPara("name");
		String sNames = c.getPara("sNames");
		if(StringUtils.isBlank(name)){
			return ReturnMsg.DATAERROR;
		}
		name = Tools.trimStr(name);
		
		/*Long count = this.findFirst("SELECT COUNT(1) AS count FROM account WHERE name=? AND flag=1 AND classify='"+classify+"' ", name).getLong("count");
		if(StringUtils.isBlank(id) && count > 0){
			return ReturnMsg.REPEATERROR;
		}else{
			Account account = new Account();
			account.set("name", name)
				.set("account", c.getPara("account"))
				.set("snames", sNames)
				.set("classify", classify)
				.set("bank", c.getPara("bank"));
			result = StringUtils.isNoneBlank(id) ? account.set("id", id).update() : account.save();
		}*/
		
		Account account = new Account();
		account.set("name", name)
			.set("account", c.getPara("account"))
			.set("snames", sNames)
			.set("classify", classify)
			.set("bank", c.getPara("bank"));
		result = StringUtils.isNoneBlank(id) ? account.set("id", id).update() : account.save();
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
	
	public ReturnMsg deletes(String ids){
		Integer orgId = ShiroUtils.getOrgId();
		if(orgId == 1){
			boolean update = false;
			for (String id : ids.split(";")) {
				update = new Account().set("id", id).set("flag", 2).update();
				if(update == false){
					break;
				}
			}
			return update ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	public Integer findAccountId(String name){
		String sql = "SELECT id FROM account WHERE `name`=? AND flag=1 ";
		return this.findFirst(sql, name).get("id");
	}
	
	/**
	 * 根据收款账户名称和店铺名称查询收款账户的ID
	 */
	public Integer findAccountId(String name, String sName){
		String sql = "SELECT id FROM account WHERE `name`=? AND INSTR(snames,?) AND flag=1 ";
		return this.findFirst(sql, name, sName).get("id");
	}
	
	public ReturnMsg importAccounts(Controller c) {
		Workbook workbook = null;
		InputStream is = null;
		boolean result = false;
		try {
			is = new FileInputStream(c.getFile().getFile());
			workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);
			Row row = null;
			String[] fields = {"name","account","bank","classify"};
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if(row != null){
					Account account = new Account();
					for (int j = 0; j < fields.length; j++) {
						Cell cell = row.getCell(j);
						if(cell != null){
							account.set(fields[j], cell.toString().trim());
						}else{
							continue;
						}
					}
					result = account.save();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
}
