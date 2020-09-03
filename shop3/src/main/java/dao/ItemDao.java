package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Item;

@Repository
public class ItemDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	private Map<String, Object> param = new HashMap<String, Object>();
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public List<Item> list() {
		return template.query("select * from item", mapper);
	}
	public Item selectOne(Integer id) {
		param.clear();
		param.put("id", id);
		return template.queryForObject("select * from item where id=:id", param, mapper);
	}
	public void insert(Item item) {
		param.clear();
		int id = template.queryForObject("select ifnull(max(id), 0) from item", param, Integer.class);   // 파라미터는 비어있음. Integer.class 원래는 mapper 자리. 
		item.setId(++id + "");
		String sql = "insert into item (id, name, price, description, pictureUrl) values(:id, :name, :price, :description, :pictureUrl)";
		SqlParameterSource prop = new BeanPropertySqlParameterSource(item);   // sql에 전달되는 파라미터 값 -> 프로퍼티로 파라미터를 저장. 
		template.update(sql, prop);
	}
	public void update(Item item) {
		param.clear();
		String sql = "update item set name=:name, price=:price, description=:description, pictureUrl=:pictureUrl "
				+ "where id=:id";
		SqlParameterSource prop = new BeanPropertySqlParameterSource(item);
		template.update(sql, prop);
	}
	public void delete(String id) {
		param.clear();
		param.put("id", id);
		String sql = "delete from item where id=:id";
		template.update(sql, param);
	}
}
