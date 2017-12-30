package me.libme.module.spring.jpahibernate._hibernate.mysql;


import org.hibernate.dialect.MySQL5Dialect;

import java.sql.Types;

public class MySQLServerDialect extends MySQL5Dialect {
	
	public MySQLServerDialect() {
		super();
		registerHibernateType(Types.BIGINT, long.class.getName());
	}

}
