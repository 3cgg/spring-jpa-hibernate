package me.libme.module.spring.jpahibernate.meta;


import me.libme.kernel._c._ref.JDefaultFieldMeta;

public class JEntityColumnMeta extends JDefaultFieldMeta {
	
	private String property;
	
	private String column;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	

	
}
