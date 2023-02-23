package com.gengproject.util.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义类型转换器
 * 作用：
 * 1、数据存储时，自动将List集合，转为字符串(格式自定义)
 * 2、数据查询时，将查到的字符串再转为List集合
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class List2VarcharTypeHandler implements TypeHandler<List<Integer>> {

    public void setParameter(PreparedStatement ps, int i, List<Integer> strings, JdbcType jdbcType) throws SQLException {
        StringBuffer sb = new StringBuffer();
        for (Integer s : strings) {
            sb.append(s).append(",");
        }
        ps.setString(i, sb.toString());
    }

    public List<Integer> getResult(ResultSet rs, String s) throws SQLException {
        String favs = rs.getString(s);
        if (favs != null && !favs.equals("")) {
            String[] strs = favs.split(",");
            List<Integer> list = new ArrayList<>();
            for (String str : strs){
                list.add(Integer.parseInt(str));
            }
            return list;
        }
        return null;
    }

    public List<Integer> getResult(ResultSet rs, int i) throws SQLException {
        String favs = rs.getString(i);
        if (favs != null) {
            String[] strs = favs.split(",");
            List<Integer> list = new ArrayList<>();
            for (String str : strs){
                list.add(Integer.parseInt(str));
            }
            return list;
        }
        return null;
    }

    public List<Integer> getResult(CallableStatement cs, int i) throws SQLException {
        String favs = cs.getString(i);
        if (favs != null) {
            String[] strs = favs.split(",");
            List<Integer> list = new ArrayList<>();
            for (String str : strs){
                list.add(Integer.parseInt(str));
            }
            return list;
        }
        return null;
    }
}
