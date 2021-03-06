package config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class DBConfig {
	@Bean(destroyMethod="close")
	public DataSource dataSource() {
		ComboPooledDataSource ds = new ComboPooledDataSource();   //Connection Pool 객체 이용
		try {
			ds.setDriverClass("org.mariadb.jdbc.Driver");
			ds.setJdbcUrl("jdbc:mariadb://localhost:3306/classdb");
			ds.setUser("scott");
			ds.setPassword("1234");
			ds.setMaxPoolSize(20);   //최대 연결 객체의 수
			ds.setMinPoolSize(3);    //최소 연결 객체의 수
			ds.setInitialPoolSize(5);   //초기 연결 객체의 갯수
		}catch(PropertyVetoException e) {
			e.printStackTrace();
		}
		return ds;
	}
	//MyBatis 설정
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource());
		bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		return bean.getObject();
	}
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() throws Exception{
		return new SqlSessionTemplate(sqlSessionFactory());
	}
}
