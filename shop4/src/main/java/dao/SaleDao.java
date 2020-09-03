package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.SaleMapper;
import logic.Sale;

@Repository   //@Component 의 하위 클래스 + 영속객체(?) : db 관련 클래스
public class SaleDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<String, Object>();
	
	public int getMaxSaleid() {
		return template.getMapper(SaleMapper.class).maxSaleId();
	}
	
	public void insert(Sale sale) {
		param.clear();
		template.getMapper(SaleMapper.class).insert(sale);
	}

	public List<Sale> list(String id) {
		param.clear();
		param.put("userid", id);
		return template.getMapper(SaleMapper.class).select(param);
	}
}
