package me.libme.module.spring.jpahibernate._hibernate.sqlserver;

import org.hibernate.dialect.SQLServer2008Dialect;

import java.sql.Types;

public class CustomSQLServer2008Dialect extends SQLServer2008Dialect{

	
	public CustomSQLServer2008Dialect() {
		super();
		registerHibernateType(Types.NVARCHAR, "string");
		registerHibernateType(Types.DECIMAL, "big_decimal");
	}
	
}
