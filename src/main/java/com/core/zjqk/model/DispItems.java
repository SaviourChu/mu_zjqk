package com.core.zjqk.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.common.config.JdbcUtils;
import com.common.kits.ReturnMsg;
import com.common.kits.Tools;
import com.core.admin.shiro.ShiroUtils;
import com.core.zjqk.model.base.BaseDispItems;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

@SuppressWarnings("serial")
public class DispItems extends BaseDispItems<DispItems> {
	public static final DispItems dao = new DispItems();
	
	public Page<DispItems> page(Controller c){
		String diName = c.getPara("diName");
		String select = "SELECT id,`name` AS diName ";
		StringBuilder sqlExceptSelect = new StringBuilder();
		List<Object> params = Lists.newArrayList();
		sqlExceptSelect.append("FROM disp_items WHERE del_flag=1 ");
		if(StringUtils.isNotBlank(diName)){
			sqlExceptSelect.append("AND INSTR(`name`, ?) ");
			params.add(diName);
	    }
		sqlExceptSelect.append("ORDER BY id ");
		return this.paginate(c.getParaToInt("page"), c.getParaToInt("rows"), select, sqlExceptSelect.toString(), params.toArray());
	}
	
	public List<DispItems> list(){
		return this.find("SELECT `name` AS diName,id AS diId FROM disp_items WHERE del_flag=1 ");
	}
	
	public List<DispItems> listByStore(){
		return this.find("SELECT `name` AS diName,id AS diId FROM disp_items WHERE id>0 GROUP BY `name` ");
	}
	
	public ReturnMsg saveOrUpdate(Controller c){
		Connection conn = JdbcUtils.getConnection();
		PreparedStatement psmt = null;
		boolean result = false;
		try {
			String id = c.getPara("id");
			String diName = c.getPara("diName");
			if(StringUtils.isAnyBlank(diName)){
				return ReturnMsg.DATAERROR;
			}
			diName = Tools.trimStr(diName);
			Long count = this.findFirst("SELECT COUNT(1) AS count FROM disp_items WHERE name=? AND del_flag=1 ", diName).getLong("count");
			if(StringUtils.isBlank(id) && count > 0){
				return ReturnMsg.REPEATERROR;
			}else{
				DispItems item = new DispItems();
				item.set("name", diName);
				if(StringUtils.isNoneBlank(id)){
					result = item.set("id", id).update();
				}else{
					result = item.save();
					List<Store> list = Store.dao.list();
					for (Store store : list) {
						Integer sId = (Integer)store.get("sId");
						Integer diId = this.findDispItemsId(diName);
						psmt = conn.prepareStatement("INSERT INTO disp_initfees (s_id,d_id) VALUES(?,?) ");
						psmt.setInt(1, sId);
						psmt.setInt(2, diId);
						psmt.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JdbcUtils.closeAll(null, null, psmt, conn);
		}
		return result ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
	
	public ReturnMsg deletes(String ids){
		Integer orgId = ShiroUtils.getOrgId();
		if(orgId == 1){
			boolean update = false;
			for (String id : ids.split(";")) {
				update = new DispItems().set("id", id).set("del_flag", 2).update();
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
	 * 根据费用项目名称查询对应的ID
	 */
	public Integer findDispItemsId(String name){
		DispItems di = this.findFirst("SELECT id FROM disp_items WHERE `name`=? AND del_flag=1", name);
		return di != null ? di.get("id") : null;
	}
	
	public ReturnMsg importDispItems(Controller c) {
		Workbook workbook = null;
		InputStream is = null;
		boolean save = false;
		try {
			is = new FileInputStream(c.getFile().getFile());
			workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheetAt(0);
			Row row = null;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if(row != null){
					DispItems di = new DispItems();
					Cell cell0 = row.getCell(0);
					if(Objects.nonNull(cell0.toString())){
						di.set("name", cell0.toString().trim());
					}else{
						continue;
					}
					if(cell0 != null){
						Long count = this.findFirst("SELECT COUNT(1) AS count FROM disp_items WHERE del_flag=1 AND name=? ", cell0.toString()).getLong("count");
						if (count > 0) {
							continue;
						} else {
							save = di.save();
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
		return save ? ReturnMsg.SUCCESS : ReturnMsg.ERROR;
	}
}
