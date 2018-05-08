package me.libme.module.spring.jpahibernate.meta;


import me.libme.kernel._c._ref.JDefaultFieldMeta;
import me.libme.module.spring.jpahibernate.Delete;

public class JEntityColumnMeta extends JDefaultFieldMeta {
	
	private String property;
	
	private String column;

	private boolean primary;

	private boolean delete;

	private Delete deleteAnnotation;

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

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}


	public Delete getDeleteAnnotation() {
		return deleteAnnotation;
	}

	void setDeleteAnnotation(Delete deleteAnnotation) {
		this.deleteAnnotation = deleteAnnotation;
	}
}
