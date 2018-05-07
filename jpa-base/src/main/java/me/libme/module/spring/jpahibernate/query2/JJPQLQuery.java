package me.libme.module.spring.jpahibernate.query2;

import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JJPQLQuery extends JQuery<JJPQLQuery>{

	private String jpql;
	
	private String countSql;
	
	public JJPQLQuery(EntityManager em) {
		super(em);
	}
	
	public String getQueryString() {
		return this.jpql;
	}
	
	@Override
	public String getCountQueryString() {
		if(countSql==null){
			countSql=QueryUtils.createCountQueryFor(jpql);
		}
		return countSql;
	}

	@Override
	Query getCountQuery() {
		return em.createQuery(getCountQueryString());
	}

	@Override
	Query getQuery() {
		return spiQueryService.createJPQLQuery(jpql, this);
	}

	public String getJpql() {
		return jpql;
	}

	/**
	 * {@link #setSql(String)} instead of 
	 * @param jpql
	 * @return
	 */
	@Deprecated
	public JJPQLQuery setJpql(String jpql) {
		this.jpql = jpql;
		return this;
	}

	public JJPQLQuery setSql(String jpql) {
		return setJpql(jpql);
	}
	
	public JJPQLQuery setCountSql(String countSql) {
		this.countSql = countSql;
		return this;
	}

	public void detach(Object entity){
		if(entity!=null){
			if(entity.getClass().getDeclaredAnnotation(Entity.class)!=null){
				if(em.contains(entity)){
					em.detach(entity);
				}
			}
		}
	}
	
}
