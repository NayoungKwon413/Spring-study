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

import logic.User;

@Repository
public class UserDao {
	private NamedParameterJdbcTemplate template;
	RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	Map<String, Object> param = new HashMap<String, Object>();
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}

	public void insert(User user) {
		param.clear();
		SqlParameterSource prop = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount "
				+ "(userid, username, password, birthday, phoneno, postcode, address, email)"
				+ "values(:userid, :username, :password, :birthday, :phoneno, :postcode, :address, :email)";
		template.update(sql, prop);
	}

	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.queryForObject("select * from useraccount where userid=:userid", param, mapper);
	}

	public void update(User user) {
		param.clear();
		SqlParameterSource prop = new BeanPropertySqlParameterSource(user);
		String sql = "update useraccount set "
				+ "username=:username, birthday=:birthday, phoneno=:phoneno, postcode=:postcode, address=:address, email=:email "
				+ "where userid=:userid";
		template.update(sql, prop);
	}

	public void delete(User user) {
		param.clear();
		SqlParameterSource prop = new BeanPropertySqlParameterSource(user);
		String sql = "delete from useraccount where userid=:userid";
		template.update(sql, prop);
	}
	
	public List<User> list() {
		return template.query("select * from useraccount where userid != 'admin'", mapper);
	}

	public List<User> list(String[] idchks) {
		String sql = "select * from useraccount where userid in (";
		for(int i=0; i<idchks.length; i++) {	
			if(i == idchks.length -1) {
				sql += "'" + idchks[i] + "'";
			}else {
				sql += "'" + idchks[i] + "',";
			}
		}
		sql += ")";

//		String ids = "";
//		for(int i=0; i<idchks.length; i++) {
//			ids += "'" + idchks[i] + ((i==idchks.length-1)?"'":"',");
//		}
//		String sql = "select * from useraccount where userid in (" + ids + ")";
		
		return template.query(sql, mapper);
	}

	
}
