package me.libme.module.spring.jpabean;


import me.libme.kernel._c._m.JPageable;
import me.libme.kernel._c._m.SimplePageRequest;
import me.libme.module.spring.jpahibernate.SingleEntityRepo;
import me.libme.module.spring.jpahibernate._m.IEntityModel;
import me.libme.module.spring.jpahibernate.meta.JEntityUtilService;
import me.libme.module.spring.jpahibernate.query2.JSingleEntityQuery;
import me.libme.module.spring.jpahibernate.query2.SqlType;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * delegate service operation of a certain table, 
 * <p>include insert, update, delete(default set "DELETE" as "Y" ), get(one record according)
 * <p>sub-class should implements method of {@code getRepo()} .
 * @author J
 *
 * @param <T>
 */
public class SingleEntityManager<T extends IEntityModel> implements ISingleEntityAccess<T,String> {
	
	private EntityManager em;
	
	EntityManager getEm() {
		if(em==null){
			throw new IllegalStateException("missing EntityManager, invalid state.");
		}
		return em;
	}
	
	
	
	protected Class<T> entityClass;

	private SessionUserFactory sessionUserFactory;

	private EntityOnSaveListener entityOnSaveListener;

	private EntityOnUpdateListener entityOnUpdateListener;

	private EntityOnDeleteListener entityOnDeleteListener;

	SingleEntityManager(EntityManager em,Class<T> entityClass) {
		this.entityClass=entityClass;
		this.em=em;
	}


	void setEntityOnSaveListener(EntityOnSaveListener entityOnSaveListener) {
		this.entityOnSaveListener = entityOnSaveListener;
	}

	void setSessionUserFactory(SessionUserFactory sessionUserFactory) {
		this.sessionUserFactory = sessionUserFactory;
	}

	void setEntityOnUpdateListener(EntityOnUpdateListener entityOnUpdateListener) {
		this.entityOnUpdateListener = entityOnUpdateListener;
	}

	void setEntityOnDeleteListener(EntityOnDeleteListener entityOnDeleteListener) {
		this.entityOnDeleteListener = entityOnDeleteListener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveOnly( T object) {
		proxyOnSave(getRepo(), sessionUserFactory.sessionUser(), object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateOnly( T object){
		proxyOnUpdate(getRepo(), sessionUserFactory.sessionUser(), object);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete( String id,Class<?>... entryClass) {
		T abstractEntity= getRepo().getModel(id, entryClass);
		abstractEntity.setDeleted("Y");
		updateOnly(abstractEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getById( String id) {
		return getRepo().getModel(id, this.entityClass);
	}


	@Override
	public <M> M getById(String id, Class<M> targetClass) {
		if(entityClass==targetClass){
			return (M) getById(id);
		}
		return singleEntityQuery2().id(id).ready().model();
	}

	/**
	 * fill in common info.  to execute 
	 * @param authorizer    generally its login user
	 */
	private T proxyOnSave(SingleEntityRepo<T , ?> repo, SessionUser authorizer, T baseModel){
		entityOnSaveListener.onSave(baseModel,authorizer);
		repo.saveModel(baseModel);
		return baseModel;
	}

	/*
	private T proxyOnSave(SingleEntityRepo<T , ?> repo, T baseModel){
		baseModel.setVersion(0);
		baseModel.setCreatorId("SYS-JOB");
		baseModel.setCreateDate(new Date());
		baseModel.setModifierId("SYS-JOB");
		baseModel.setModifyDate(new Date());
		baseModel.setIsAvailable(Availability.available);
		repo.saveModel(baseModel);
		return baseModel;
	}
	*/

	/**
	 * fill in common info.
	 * also validate whether the version changes, then to execute 
	 */
	private T proxyOnUpdate(SingleEntityRepo<T, ?> repo, SessionUser authorizer, T baseModel){
		entityOnUpdateListener.onUpdate(baseModel,authorizer);
		repo.updateModel(baseModel);
		return baseModel;
	}

	/*
	private T proxyOnUpdate(SingleEntityRepo<T, ?> repo, T baseModel){
		baseModel.setModifierId("SYS-JOB");
		baseModel.setModifyDate(new Date());
		repo.updateModel(baseModel);
		return baseModel;
	}
	*/

	protected SimplePageRequest toPageRequest(JPageable pageable){
		return new SimplePageRequest(pageable.getPageNumber(), pageable.getPageSize());
	}
	
	public void deleteAllByIds( List<String> ids,Class<?>... entryClass) {
		for(String id : ids){
			delete(id,entryClass);
		}
	}
	
	@Override
	public void deleteAllByModels( List<T> models,Class<?>... entryClass) {
		for(T model:models){
			delete(model.getId());
		}
	}
	
	@Override
	public void delete( T model) {
		delete(model.getId());
	}
	
	@Override
	public List<T> getAllModes(){
		return getRepo().getAllModels(this.entityClass);
	}

	@Override
	public <M> List<M> getAllModes(Class<M> targetClass) {
		return singleEntityQuery2()
				.conditionDefault().ready().models(targetClass);
	}

	/**
	 * 物理删除
	 * @param model
	 */
	@Override
	public void deletePermanently(T model){
		getRepo().deleteModel(model);
	}
	
	/**
	 * 批量物理删
	 * @param models
	 */
	@Override
	public void deletePermanentlyByModels(List<T> models,Class<?>... entryClass){
		for(T model : models){
			deletePermanently(model);
		}
	}
	
	@Override
	public void saveAllOnly( Iterable<T> objects,Class<?>... entryClass) {
		getRepo().saveAllModels(objects, entryClass);
	}
	
	/**
	 * override the method to provide the real repository.
	 */
	@Override
	public SingleEntityRepo<T, String> getRepo() {
		return new InternalRepo();
	}
	
	public class InternalRepo implements SingleEntityRepo<T, String> {
		@Override
		public void saveModel(T baseModel) {
			getEm().persist(baseModel);
		}

		@Override
		public int updateModel(T baseModel) {
			getEm().merge(baseModel);
			return 1;
		}

		@Override
		public T getModel(String id, Class<?>... entryClass) {
			return getEm().getReference(entityClass, id);
		}

		@Override
		public void deleteModel(T baseModel) {
			getEm().remove(getEm().contains(baseModel) ? baseModel : getEm().merge(baseModel));
		}

		@Override
		public void markModelDeleted(T baseModel) {
			throw new UnsupportedOperationException("the method is not supported.");
		}

		@Override
		public void markModelDeleted(String id, Class<?>... entryClass) {
			throw new UnsupportedOperationException("the method is not supported.");
		}

		@Override
		public List<T> getAllModels(Class<?>... entryClass) {
			throw new UnsupportedOperationException("the method is not supported.");
		}

		@Override
		public void saveAllModels(Iterable<T> objects, Class<?>... entryClass) {
			for (T entity : objects) {
				saveModel(entity);
			}
		}
		
	}
	
	public JSingleEntityQuery singleEntityQuery2(){
		return new JSingleEntityQuery(entityClass, getEm());
	}
	
	public String selectCause(String... alias){
		return JEntityUtilService.get().selectCause(entityClass, alias);
	}
	
	public String selectCause(SqlType sqlType, String... alias){
		return JEntityUtilService.get().selectCause(sqlType,entityClass, alias);
	}

	@Override
	public <M> M active(String id, Class<M> targetClass) {
		return singleEntityQuery2().active(id).ready().model(targetClass);
	}

	@Override
	public T active(String id) {
		return active(id,entityClass);
	}
}
