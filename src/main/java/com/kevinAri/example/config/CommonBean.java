package com.kevinAri.example.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.TimeZone;

@Component
public class CommonBean {
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// setting untuk ignore field yang tidak ada di object tujuan
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// setting agar object mapper tidak mengambil nilai null
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// setting pakai local timezone
		objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        // oracle.TIMESTAMP failed to serialize
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// case insensitive
//        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

		return objectMapper;
	}

	@Bean
    public JdbcTemplate jdbcTemplate() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost:3306");
        dataSource.setDatabaseName("db_temp");
        dataSource.setUser("root");
        dataSource.setPassword("root1234");
//        url: jdbc:mysql://localhost:3306/db_temp?useSSL=false&serverTimezone=Asia/Jakarta&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true
//        username: root
//        password: root1234

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }
}
