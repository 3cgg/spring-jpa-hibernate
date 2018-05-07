package me.libme.module.spring.jpahibernate.query2;

import javax.persistence.TemporalType;
import java.util.Date;

public class JJpaDateParam {

	private Date date;
	
	private TemporalType temporalType;

	public JJpaDateParam(Date date, TemporalType temporalType) {
		this.date = date;
		this.temporalType = temporalType;
	}

	public JJpaDateParam() {

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TemporalType getTemporalType() {
		return temporalType;
	}

	public void setTemporalType(TemporalType temporalType) {
		this.temporalType = temporalType;
	}
}
