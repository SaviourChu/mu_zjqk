package com.common.tools;

import java.util.List;
import java.util.Objects;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class SqlHolder {

  private String sql;
  
  
  
  private SqlHolder(String sql) {
    super();
    this.sql = sql;
  }



  public static class SqlBuilder{
    private List<String> fields = Lists.newArrayList();
    private String table;
    private List<String> and;
    private List<String> order;
    
    public SqlBuilder select(String field){
      fields.add(field);
      return this;
    }
    
    public SqlBuilder from(String table){
      this.table = table;
      return this;
    }
    
    public SqlHolder build(){
      String sqlFields = Joiner.on(", ").join(fields);
      return new SqlHolder(sqlFields);
    }
    
    public SqlHolder buildSQL(){
      String sqlFields = Joiner.on(", ").join(fields);
      StringBuffer sqlBuff = new StringBuffer().append("SELECT ")//select
      .append(sqlFields)//field
      .append(" FROM ")//from
      .append(table);//table
      if(Objects.nonNull(and)){
        String andWhere = Joiner.on(" and ").join(and);
        sqlBuff.append(" WHERE ")//where
        .append(andWhere);
      }
      if(Objects.nonNull(order)){
        sqlBuff.append(" order by ");
        sqlBuff.append(Joiner.on(",").join(order));
      }
      String sql = sqlBuff.toString();
      return new SqlHolder(sql);
    }
    
    public SqlBuilder where(int columnSize){
      final int realSize = columnSize + 1;
      this.and = Lists.newArrayListWithCapacity(realSize);
      and.add(" 1=1 ");
      return this;
    }
    
    public SqlBuilder asc(String column){
      Objects.requireNonNull(order,"please invoke order method first");
      order.add(column+" asc ");
      return this;
    }
    
    public SqlBuilder desc(String column){
      Objects.requireNonNull(order,"please invoke order method first");
      order.add(column+" desc ");
      return this;
    }
    
    public SqlBuilder order(){
      this.order = Lists.newArrayList();
      return this;
    } 
    
    public SqlBuilder and(String field){
      Objects.requireNonNull(and,"please invoke and method first");
      and.add(field+"=?");
      return this;
    }
    
    public SqlBuilder greaterThan(String field){
      Objects.requireNonNull(and,"please invoke and method first");
      and.add(field+">=?");
      return this;
    }
    
    public SqlBuilder lessThan(String field){
      Objects.requireNonNull(and,"please invoke and method first");
      and.add(field+"<=?");
      return this;
    }
  }

  public String getSql() {
    return sql;
  }
  
  public String limitOne(){
    String sql = this.getSql();
    return sql + " limit 1";
  }
}
