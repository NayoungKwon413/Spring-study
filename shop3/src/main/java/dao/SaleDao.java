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

import logic.Sale;

@Repository   //@Component 의 하위 클래스 + 영속객체(?) : db 관련 클래스
public class SaleDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<Sale> mapper = new BeanPropertyRowMapper<Sale>(Sale.class);
	private Map<String, Object> param = new HashMap<String, Object>();
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public int getMaxSaleid() {
		return template.queryForObject("select ifnull(max(saleid),0) from sale", param, Integer.class);
	}
	public void insert(Sale sale) {
		param.clear();
		String sql = "insert into Sale(saleid, userid, saledate) values(:saleid, :userid, now())";
		SqlParameterSource prop = new BeanPropertySqlParameterSource(sale);
		template.update(sql, prop);
	}

	public List<Sale> list(String id) {
		param.clear();
		param.put("userid", id);
		String sql = "select * from sale where userid=:userid";
		return template.query(sql, param, mapper);
	}
}
