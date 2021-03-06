package com.core.zjqk.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.base.BaseStore;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class Store extends BaseStore<Store> {
	public static final Store dao = new Store();
	
	/*
	 * 提成租金计算表
	 */
	public Page<Store> rentCount(Controller c){
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		String select = comSQL(c, sqlExceptSelect, params);
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	/*
	 * 提成租金计算表导出
	 */
	public List<Store> exRentCount(Controller c){
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		String select = comSQL(c, sqlExceptSelect, params);
		return this.find(select + sqlExceptSelect.toString(), params.toArray());
	}
	
	private String comSQL(Controller c, StringBuilder sqlExceptSelect, List<Object> params) {
		String classify = ShiroUtils.getClassify();
		String select = "SELECT s.id,rb.ym_date ymDate,s.`name` sName,opening_date openingDate,close_date closeDate,bdzj,tcbl,"
				+ "actual_income actualIncome,shop_income shopIncome,payable_fee payableFee,actual_payment actualPayment,"
				+ "ROUND((payable_fee-actual_payment),2) differenceFee ";
		sqlExceptSelect.append("FROM rent_bill rb LEFT JOIN store s ON s.id=rb.s_id "
				+ "WHERE s.flag=1 AND rb.flag=1 AND classify='"+classify+"' ");
		String sName = c.getPara("sName");
		if(StringUtils.isNotBlank(sName)){
			sqlExceptSelect.append("AND INSTR(s.`name`, ?) ");
			params.add(sName);
		}
		String ymDate = c.getPara("ymDate");
		if(StringUtils.isNotBlank(ymDate)){
			sqlExceptSelect.append("AND rb.ym_date = ? ");
			params.add(ymDate);
		}
		return select;
	}
	
	public Page<Store> rentCount2(Controller c){
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		String select = comSQL2(c, sqlExceptSelect, params);
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	/*
	 * 提成租金计算表导出
	 */
	public List<Store> exRentCount2(Controller c){
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		String select = comSQL2(c, sqlExceptSelect, params);
		return this.find(select + sqlExceptSelect.toString(), params.toArray());
	}
	
	private String comSQL2(Controller c, StringBuilder sqlExceptSelect, List<Object> params) {
		String classify = ShiroUtils.getClassify();
		String select = "SELECT s.id,rb.ym_date ymDate,s.`name` sName,opening_date openingDate,close_date closeDate,bdzj,tcbl,"
				+ "actual_income actualIncome,shop_income shopIncome,payable_fee payableFee ";
		sqlExceptSelect.append("FROM rent_bill rb LEFT JOIN store s ON s.id=rb.s_id "
				+ "WHERE shop_income <>0 AND s.flag=1 AND rb.flag=1 AND classify='"+classify+"' ");
		String sName = c.getPara("sName");
		if(StringUtils.isNotBlank(sName)){
			sqlExceptSelect.append("AND INSTR(s.`name`, ?) ");
			params.add(sName);
		}
		String ymDate = c.getPara("ymDate");
		if(StringUtils.isNotBlank(ymDate)){
			sqlExceptSelect.append("AND rb.ym_date = ? ");
			params.add(ymDate);
		}
		return select;
	}
	
	public Page<Store> page(Controller c){
		String classify = ShiroUtils.getClassify();
		String name = c.getPara("sName");
		String select = "SELECT id,name,areas,brand,berth_no berthNo,opening_date openingDate,close_date closeDate,address,"
				+ "payment_day paymentDay,free_rent_stime freeRentStime,free_rent_etime freeRentEtime,contract_subject contractSubject,"
				+ "contract_stime contractStime,contract_etime contractEtime,operation_contact operationContact,finance_contact financeContact ";
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		sqlExceptSelect.append("FROM store WHERE flag=1 AND classify='"+classify+"' ");
		if(StringUtils.isNotBlank(name)){
			sqlExceptSelect.append("AND INSTR(name, ?) ");
			params.add(name);
	    }
		sqlExceptSelect.append("ORDER BY opening_date ");
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	public List<Store> list(){
		String classify = ShiroUtils.getClassify();
		return this.find("SELECT name sName, id sId FROM store WHERE flag=1 AND classify=? ORDER BY id ", classify);
	}
	
	public ReturnMsg saveOrUpdate(Controller c){
		Integer orgId = ShiroUtils.getOrgId();
		String classify = ShiroUtils.getClassify();
		if(orgId == 1){
			boolean result = false;
			String id = c.getPara("id");
			String name = c.getPara("name");
			if(StringUtils.isBlank(name)){
				return ReturnMsg.DATAERROR;
			}
			name = Tools.trimStr(name);
			Long count = this.findFirst("SELECT COUNT(1) count FROM store WHERE name=? AND flag=1 ", name).getLong("count");
			if(StringUtils.isBlank(id) && count > 0){
				return ReturnMsg.REPEATERROR;
			}else{
				Store store = new Store();
				store.set("name", name)
					.set("classify", classify)
					.set("areas", c.getPara("areas"))
					.set("brand", c.getPara("brand"))
					.set("payment_day", c.getParaToInt("paymentDay"))
					.set("berth_no", c.getPara("berthNo"))
					.set("address", c.getPara("address"))
					.set("opening_date", c.getPara("openingDate"))
					.set("close_date", c.getPara("closeDate"))
					.set("contract_subject", c.getPara("contractSubject"))
					.set("contract_stime", c.getPara("contractStime"))
					.set("contract_etime", c.getPara("contractEtime"))
					.set("free_rent_stime", c.getPara("freeRentStime"))
					.set("free_rent_etime", c.getPara("freeRentEtime"))
					.set("operation_contact", c.getPara("operationContact"))
					.set("finance_contact", c.getPara("financeContact"));
				result = StringUtils.isNoneBlank(id) ? store.set("id", id).update() : store.save();
			}
			return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	
	public ReturnMsg deletes(String ids){
		Integer orgId = ShiroUtils.getOrgId();
		if(orgId == 1){
			boolean update = false;
			for (String id : ids.split(";")) {
				update = new Store().set("id", id).set("flag", 2).update();
				if(update == false){
					break;
				}
			}
			return update ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
		}else{
			return ReturnMsg.NORIGHT;
		}
	}
	/**
	 * 根据店铺名称查询店铺ID
	 */
	public Integer findStoreId(String name){
		String sql = "SELECT id FROM store WHERE INSTR(name, ?) AND flag=1 AND classify=? ";
		Store store = this.findFirst(sql, name, ShiroUtils.getClassify());
		return store != null ? store.get("id") : null;
	}
	
	public String findStoreNames(Integer id){
		String classify = ShiroUtils.getClassify();
		String sql = "SELECT name FROM store WHERE id=? AND flag=1 AND classify=? ";
		Store store = this.findFirst(sql, id, classify);
		return store != null ? store.get("name") : "";
	}
	
	public List<Store> paymentDays(){
		String classify = ShiroUtils.getClassify();
		String sql = "SELECT DISTINCT(payment_day) dayNums FROM store WHERE flag=1 AND classify=? ORDER BY payment_day ";
		return this.find(sql, classify);
	}
	
	public List<Store> findStoresByDay(int payDay){
		String classify = ShiroUtils.getClassify();
		String sql = "SELECT `name` sName FROM store WHERE flag=1 AND payment_day=? AND classify=? ORDER BY `name` ";
		return this.find(sql, payDay, classify);
	}
	
	public ReturnMsg importStores(Controller c) {
		boolean result = false;
		Workbook workbook = null;
		InputStream is = null;
		Object value = null;
		try {
			is = new FileInputStream(c.getFile().getFile());
			workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);
			Row row = null;
			String[] fields = {"name","areas","brand","payment_day","opening_date","close_date","berth_no","contract_subject","contract_stime",
					"contract_etime","free_rent_stime","free_rent_etime","operation_contact","finance_contact","address"};
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if(row != null){
					Store store = new Store();
					for (int j = 0; j < fields.length; j++) {
						Cell cell = row.getCell(j);
						if(cell != null){
							switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING:
									value = cell.getRichStringCellValue().toString().trim();
									break;
								case Cell.CELL_TYPE_NUMERIC:
									short format = cell.getCellStyle().getDataFormat();
									SimpleDateFormat sdf = null;
									if (format == 14 || format == 31 || format == 57 || format == 58) {
										sdf = new SimpleDateFormat("yyyy-MM-dd");
										value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
									} else{
										value = String.valueOf(cell.getNumericCellValue());
									}
									break;
								default:
									value = "";
									break;
							}
						}
						store.set(fields[j], value);
					}
					Cell cell3 = row.getCell(3);
					if(Objects.isNull(cell3) || StringUtils.isBlank(cell3.toString())){
						store.set(fields[3], 1);
					}else{
						store.set(fields[3], cell3.getNumericCellValue());
					}
					Cell cell0 = row.getCell(0);
					if(cell0 != null){
						Long count = this.findFirst("SELECT COUNT(1) count FROM store WHERE flag=1 AND name=? AND classify=? ",
								cell0.toString(), ShiroUtils.getClassify()).getLong("count");
						if (count > 0) {
							continue;
						} else {
							result = store.save();
						}
					}
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
