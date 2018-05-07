package me.libme.module.spring.jpahibernate.spi;


import me.libme.kernel._c.util.JStringUtils;
import me.libme.module.spring.jpahibernate._hibernate.CaseInsensitiveAliasToBeanResultTransformer;
import me.libme.module.spring.jpahibernate.query2.JQuery;
import me.libme.module.spring.jpahibernate.query2.JQueryUtil;
import me.libme.module.spring.jpahibernate.query2.JSPIQueryService;
import org.hibernate.jpa.HibernateQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class JHibernateQueryService
implements JSPIQueryService
{
	
	private JGenericQueryService genericQueryService=new JGenericQueryService();

	private ResultTransformer getResultTransformer(JQuery<?> _query){
		Class<?> result=_query.getResult();
		if(result!=null){
			return CaseInsensitiveAliasToBeanResultTransformer.get(result);
		}
		if(_query.isUseAlias()){
			return Transformers.ALIAS_TO_ENTITY_MAP;
		}
		return null;
	}
	
	@Override
	public <T> T getSingleResult(Query query, JQuery<?> _query) {
		if(JStringUtils.isNotNullOrEmpty(_query.getResultSetMapping())){
			return genericQueryService.getSingleResult(query, _query);
		}
		HibernateQuery hibernateQuery=(HibernateQuery) query;
		org.hibernate.Query hQuery=hibernateQuery.getHibernateQuery();
		hQuery.setResultTransformer(getResultTransformer(_query));
		List<?> list=hQuery.list();
		if(!list.isEmpty()&&list.size()>1){
			throw new RuntimeException("more than one record is searched");
		}
		T result= list.isEmpty()?null:(T)(list.get(0));
		_query.detach(result);
		return result;
	}

	@Override
	public <T> List<T> getResultList(Query query, JQuery<?> _query) {
		if(JStringUtils.isNotNullOrEmpty(_query.getResultSetMapping())){
			return genericQueryService.getResultList(query, _query);
		}
		HibernateQuery hibernateQuery=(HibernateQuery) query;
		org.hibernate.Query hQuery=hibernateQuery.getHibernateQuery();
		hQuery.setResultTransformer(getResultTransformer(_query));
		return hQuery.list();
	}
	
	@Override
	public Query createJPQLQuery(String jpql, JQuery<?> _query) {
//		Class<?> result=_query.getResult();
		EntityManager em= JQueryUtil.getEntityManager(_query);
//		if(result!=null){
//			return em.createQuery(jpql,result);
//		}
		return em.createQuery(jpql);
	}

	@Override
	public Query createNamedQuery(String namedSql, JQuery<?> _query) {
//		Class<?> result=_query.getResult();
		EntityManager em=JQueryUtil.getEntityManager(_query);
//		if(result!=null){
//			return em.createNamedQuery(namedSql,result);
//		}
		return em.createNamedQuery(namedSql);
	}

	@Override
	public Query createNativeQuery(String nativeSql, JQuery<?> _query) {
		String resultSetMapping=_query.getResultSetMapping();
		EntityManager em=JQueryUtil.getEntityManager(_query);
//		if(result!=null){
//			return em.createNativeQuery(nativeSql,result);
//		}
		if(resultSetMapping!=null){
			return em.createNativeQuery(nativeSql,resultSetMapping);
		}
		return em.createNativeQuery(nativeSql);
	}
	
	
}
