package me.libme.module.spring.jpahibernate.query2;


import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.JPageable;

import javax.persistence.EntityManager;
import java.util.List;

public class JSingleEntityQuery {
	
	private JSingleEntityQueryMeta singleEntityQueryMeta;
	
	private EntityManager entityManager;

	public JSingleEntityQuery(Class<?> entityClass,
			EntityManager entityManager) {
		this.singleEntityQueryMeta = new JSingleEntityQueryMeta(entityClass,this);
		this.entityManager = entityManager;
	}
	
	public JCondition condition(){
		return singleEntityQueryMeta.condition();
	}
	
	public JCondition conditionDefault(){
		return singleEntityQueryMeta.conditionDefault();
	}
	
	/**
	 * @param id  primary id 
	 * @return
	 */
	public JCondition active(String id){
		return singleEntityQueryMeta.conditionDefault()
				.primary(id, JCondition.LinkType.AND);
	}
	
	public JOrder order() {
		return singleEntityQueryMeta.order();
	}
	
	public <T> List<T> models(){
		return models(null);
	}
	
	public <T> List<T> models(Class<T> clazz){
		if(clazz==null||singleEntityQueryMeta.entityClass()==clazz){
			return JQueryBuilder.get(entityManager).jpqlQuery()
					.setSql(singleEntityQueryMeta.toJPQL())
					.setParams(singleEntityQueryMeta.toParams())
					.models();
		}else{
			return JQueryBuilder.get(entityManager).nativeQuery()
					.setSql(singleEntityQueryMeta.toNative())
					.setParams(singleEntityQueryMeta.toParams())
					.models(clazz);
		}
	}
	
	public <T> T model(Class<T> clazz){
		if(clazz==null||singleEntityQueryMeta.entityClass()==clazz){
			return JQueryBuilder.get(entityManager).jpqlQuery()
					.setSql(singleEntityQueryMeta.toJPQL())
					.setParams(singleEntityQueryMeta.toParams())
					.model();
		}else{
			return JQueryBuilder.get(entityManager).nativeQuery()
					.setSql(singleEntityQueryMeta.toNative())
					.setParams(singleEntityQueryMeta.toParams())
					.model(clazz);
		}
	}
	public <T> T model(){
		return model(null);
	}
	
	public long count(){
		return JQueryBuilder.get(entityManager).jpqlQuery()
				.setSql("select count("+JSingleEntityQueryMeta.ALIAS+") "+singleEntityQueryMeta.toJPQL())
				.setParams(singleEntityQueryMeta.toParams())
				.model();
	}
	
	public <T> JPage<T> modelPage(JPageable pageable, Class<T> clazz){
		if(clazz==null||singleEntityQueryMeta.entityClass()==clazz){
			return JQueryBuilder.get(entityManager).jpqlQuery()
					.setSql(singleEntityQueryMeta.toJPQL())
					.setParams(singleEntityQueryMeta.toParams())
					.setPageable(pageable)
					.modelPage();
		}else{
			return JQueryBuilder.get(entityManager).nativeQuery()
					.setSql(singleEntityQueryMeta.toNative())
					.setParams(singleEntityQueryMeta.toParams())
					.setPageable(pageable)
					.modelPage(clazz);
		}
	}
	
	public <T> JPage<T> modelPage(JPageable pageable){
		return modelPage(pageable, null);
	}
	
}
