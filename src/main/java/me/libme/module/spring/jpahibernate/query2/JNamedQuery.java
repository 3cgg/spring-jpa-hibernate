package me.libme.module.spring.jpahibernate.query2;


import me.libme.kernel._c.util.JStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JNamedQuery extends JQuery<JNamedQuery> {

	private static final Logger LOGGER=LoggerFactory.getLogger(JNamedQuery.class);
	
	private String namedSql;
	
	private String countNamedSql;
	
	private String sql;
	
	private String countSql;
	
	public JNamedQuery(EntityManager em) {
		super(em);
	}
	
	@Override
	public String getQueryString() {
		if(sql==null){
			PersistenceProvider persistenceProvider= PersistenceProvider.fromEntityManager(em);
			if(persistenceProvider.canExtractQuery()){
				sql = persistenceProvider.extractQueryString(em.createNamedQuery(namedSql));
			}
		}
		return sql;
	}
	
	@Override
	public String getCountQueryString() {
		if(countSql==null){
			boolean namedCountQueryIsPresent=false;
			if(JStringUtils.isNotNullOrEmpty(countNamedSql)){
				namedCountQueryIsPresent=hasNamedQuery(em, countNamedSql);
				if(!namedCountQueryIsPresent)
					throw new RuntimeException(" count named SQL canot fround as persistence context : " +countNamedSql);
			}
			PersistenceProvider persistenceProvider= PersistenceProvider.fromEntityManager(em);
			if(namedCountQueryIsPresent){
				if(persistenceProvider.canExtractQuery()){
					countSql=persistenceProvider.extractQueryString(em.createNamedQuery(countNamedSql));
				}
			}
			else{
				if(persistenceProvider.canExtractQuery()){
					String queryString = persistenceProvider.extractQueryString(em.createNamedQuery(namedSql));
					countSql = QueryUtils.createCountQueryFor(queryString);
				}
			}
		}
		return countSql;
	}

	
	private static boolean hasNamedQuery(EntityManager em, String queryName) {
		/*
		 * @see DATAJPA-617
		 * we have to use a dedicated em for the lookups to avoid a potential rollback of the running tx.
		 */
		EntityManager lookupEm = em.getEntityManagerFactory().createEntityManager();

		try {
			lookupEm.createNamedQuery(queryName);
			return true;
		} catch (IllegalArgumentException e) {
			LOGGER.debug("Did not find named query {}"+queryName);
			return false;
		} finally {
			lookupEm.close();
		}
	}

	@Override
	Query getCountQuery() {
		boolean namedCountQueryIsPresent=false;
		if(JStringUtils.isNotNullOrEmpty(countNamedSql)){
			namedCountQueryIsPresent=hasNamedQuery(em, countNamedSql);
			if(!namedCountQueryIsPresent)
				throw new RuntimeException(" count named SQL canot fround as persistence context : " +countNamedSql);
		}
		PersistenceProvider persistenceProvider= PersistenceProvider.fromEntityManager(em);
		if(namedCountQueryIsPresent){
			if(persistenceProvider.canExtractQuery()){
				return em.createNamedQuery(countNamedSql);
			}
		}
		else{
			if(persistenceProvider.canExtractQuery()){
				String queryString = persistenceProvider.extractQueryString(em.createNamedQuery(namedSql));
				countSql = QueryUtils.createCountQueryFor(queryString);
				return em.createNamedQuery(countSql,Long.class);
			}
		}
		return null;
	}
	
	@Override
	Query getQuery() {
		return spiQueryService.createNamedQuery(namedSql, this);
	}

	public JNamedQuery setNamedSql(String namedSql) {
		this.namedSql = namedSql;
		return this;
	}

	public JNamedQuery setCountNamedSql(String countNamedSql) {
		this.countNamedSql = countNamedSql;
		return this;
	}

	public String getNamedSql() {
		return namedSql;
	}

	public String getCountNamedSql() {
		return countNamedSql;
	}
	
}
