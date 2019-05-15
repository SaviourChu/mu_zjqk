package com.generator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;

public class MyMetaBuilder extends MetaBuilder {

	private String[] tables;

	/**
	 * @param dataSource
	 */
	public MyMetaBuilder(DataSource dataSource, String... tables) {
		super(dataSource);
		this.tables = tables;
	}

	/**
	 * 不同数据库 dbMeta.getTables(...) 的 schemaPattern 参数意义不同 1：oracle 数据库这个参数代表
	 * dbMeta.getUserName() 2：postgresql 数据库中需要在 jdbcUrl中配置 schemaPatter，例如：
	 * jdbc:postgresql://localhost:15432/djpt?currentSchema=public,sys,app
	 * 最后的参数就是搜索schema的顺序，DruidPlugin 下测试成功 3：开发者若在其它库中发现工作不正常，可通过继承
	 * MetaBuilder并覆盖此方法来实现功能
	 */
	protected ResultSet getTablesResultSet() throws SQLException {
		String schemaPattern = dialect instanceof OracleDialect ? dbMeta.getUserName() : null;
		return dbMeta.getTables(conn.getCatalog(), schemaPattern, null, new String[] { "TABLE", "VIEW" });
	}

	@Override
	public void buildTableNames(List<TableMeta> ret) throws SQLException {
		ResultSet rs = getTablesResultSet();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			for (String table : tables) {
				if (StringUtils.equalsIgnoreCase(tableName, table)) {
					TableMeta tableMeta = new TableMeta();
					tableMeta.name = tableName;
					tableMeta.remarks = rs.getString("REMARKS");
					tableMeta.modelName = buildModelName(tableName);
					tableMeta.baseModelName = buildBaseModelName(tableMeta.modelName);
					ret.add(tableMeta);
				}
			}
		}
		rs.close();
	}

}
