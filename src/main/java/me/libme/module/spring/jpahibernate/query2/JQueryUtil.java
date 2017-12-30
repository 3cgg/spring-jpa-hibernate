package me.libme.module.spring.jpahibernate.query2;

import javax.persistence.EntityManager;

public class JQueryUtil {

	
	public static EntityManager getEntityManager(JQuery<?> query){
		return query.em;
	}
	
	
}
