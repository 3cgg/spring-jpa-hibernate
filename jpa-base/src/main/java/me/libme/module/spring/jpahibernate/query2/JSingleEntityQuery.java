package me.libme.module.spring.jpahibernate.query2;


import me.libme.kernel._c._m.JModel;
import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.JPageable;
import me.libme.module.spring.jpahibernate.meta.JEntityModelMeta;
import me.libme.module.spring.jpahibernate.meta.JEntityUtilService;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public class JSingleEntityQuery {

	private final JSingleEntityQueryMeta singleEntityQueryMeta;
	
	private final EntityManager entityManager;

	private final JEntityModelMeta entityModelMeta;


	public JSingleEntityQuery(Class<?> entityClass,
			EntityManager entityManager) {
		this.singleEntityQueryMeta = new JSingleEntityQueryMeta(entityClass,this);
		this.entityManager = entityManager;
		this.entityModelMeta=JEntityUtilService.get().getEntityModelMeta(entityClass);
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
				.equals(entityModelMeta.getPrimaryColumn().getProperty(),id, JCondition.LinkType.AND);
	}

	/**
	 * @param id  primary id
	 * @return
	 */
	public JCondition id(String id){
		return singleEntityQueryMeta.condition()
				.equals(entityModelMeta.getPrimaryColumn().getProperty(),id, JCondition.LinkType.AND);
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
				.setSql("select count("+_Cons.ALIAS+") "+singleEntityQueryMeta.toJPQL())
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


	class JSingleEntityQueryMeta implements JModel {

		private final Class<?> entityClass;

		private JCondition condition;

		private JOrder order;

		private JSingleEntityQuery singleEntityQuery;

		public JSingleEntityQueryMeta(Class<?> entityClass) {
			this.entityClass=entityClass;
		}

		public JSingleEntityQueryMeta(Class<?> entityClass,JSingleEntityQuery singleEntityQuery) {
			this.entityClass=entityClass;
			this.singleEntityQuery=singleEntityQuery;
		}

		void setSingleEntityQuery(JSingleEntityQuery singleEntityQuery) {
			this.singleEntityQuery = singleEntityQuery;
		}

		public JCondition condition(){
			condition= new JCondition(entityClass);
			condition.setSingleEntityQuery(singleEntityQuery);
			return condition;
		}

		public JCondition conditionDefault(){
			condition=condition().equals(entityModelMeta.getDeleteColumn().getProperty(),
					entityModelMeta.getDeleteColumn().getDeleteAnnotation().no());
			return condition;
		}

		public JOrder order() {
			order=new JOrder(entityClass);
			order.setSingleEntityQuery(singleEntityQuery);
			return order;
		}

		public String toJPQL(){
			String clause="from "+entityClass.getSimpleName()+" "+_Cons.ALIAS;
			if(condition!=null){
				clause=clause+" "+condition.toWhereClause();
			}
			if(order!=null){
				clause=clause+" "+order.toOrderClause();
			}
			return clause;
		}

		public String toNative(){
			String clause= toSelectCause(SqlType.NATIVE)
					+ " from "
					+ JEntityUtilService.get().getEntityModelMeta(entityClass).getTableName()
					+" "+_Cons.ALIAS;
			if(condition!=null){
				clause=clause+" "+condition.toWhereClause(SqlType.NATIVE);
			}
			if(order!=null){
				clause=clause+" "+order.toOrderClause(SqlType.NATIVE);
			}
			return clause;
		}

		public Map<String, Object> toParams(){
			return condition.toParams();
		}

		public String toSelectCause(){
			return JEntityUtilService.get().selectCause(entityClass, _Cons.ALIAS);
		}

		public String toSelectCause(SqlType sqlType){
			return JEntityUtilService.get().selectCause(sqlType,entityClass, _Cons.ALIAS);
		}

		public Class<?> entityClass() {
			return entityClass;
		}

	}

}
