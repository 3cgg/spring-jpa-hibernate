package me.libme.module.spring.jpabean;

import me.libme.kernel._c._m.JPageable;
import me.libme.kernel._c._m.SimplePageRequest;
import me.libme.module.spring.jpahibernate.query2.JCondition;
import me.libme.module.spring.jpahibernate.query2.JJPQLQuery;
import me.libme.module.spring.jpahibernate.query2.JNamedQuery;
import me.libme.module.spring.jpahibernate.query2.JNativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 * access to the database / any data source
 * @author J
 *
 */
public abstract class DataAccessSupport {
	
	protected final Logger LOGGER= LoggerFactory.getLogger(getClass());

	private EntityManager entityManager;

	@Autowired
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected SimplePageRequest toPageRequest(JPageable pageable){
		LOGGER.info("pageable",pageable);
		return new SimplePageRequest(pageable.getPageNumber(), pageable.getPageSize());
	}
	
	protected JNativeQuery nativeQuery(){
		return new JNativeQuery(entityManager);
	}
	
	protected JJPQLQuery jpqlQuery(){
		return new JJPQLQuery(entityManager);
	}
	
	protected JNamedQuery namedQuery(){
		return new JNamedQuery(entityManager);
	}

	protected JNativeQuery nativeQuery(EntityManager entityManager){
		return new JNativeQuery(entityManager);
	}

	protected JJPQLQuery jpqlQuery(EntityManager entityManager){
		return new JJPQLQuery(entityManager);
	}

	protected JNamedQuery namedQuery(EntityManager entityManager){
		return new JNamedQuery(entityManager);
	}

	protected Map<String, Object> params(Map<String, JCondition.Condition> params){
		Map<String, Object> realParams=new HashMap<>();
		for(Map.Entry<String, JCondition.Condition> entry:params.entrySet() ){
			realParams.put(entry.getKey(), entry.getValue().getValue());
		}
		return realParams;
	}
}
