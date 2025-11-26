package com.register;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;

	public class DatabaseConfig {
	    public static DataSource getDataSource() {
	        HikariDataSource dataSource = new HikariDataSource();
	        dataSource.setJdbcUrl("jdbc:h2:./register;AUTO_SERVER=TRUE");
	        dataSource.setUsername("");
	        dataSource.setPassword("");
	        return dataSource;
	    }
}
