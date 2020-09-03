package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import logic.Item;

public class ItemDao {
	//spring 의 jdbc 템플릿
	private NamedParameterJdbcTemplate template;
	// mapper : 컬럼명과 Item의 프로퍼티를 이용하여 데이터 저장
	private RowMapper<Item> mapper = 
			new BeanPropertyRowMapper<Item>(Item.class);
	
	// set 프로퍼티를 통해 주입
	public void setDataSource(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}
	// template 에  의해 해당하는 레코드들을 mapper가 아이템으로 만들어 List로 전달
	public List<Item> list() {
		return template.query("select * from item", mapper);
	}
	public Item selectOne(Integer id) {
		Map<String, Integer> param = new HashMap<>();
		param.put("id", id);
		// queryForObject : 결과가 1개의 레코드인 경우만 가능함.
		// :id : 파라미터 처리
		return template.queryForObject("select * from item where id=:id", param, mapper);
	}
}
