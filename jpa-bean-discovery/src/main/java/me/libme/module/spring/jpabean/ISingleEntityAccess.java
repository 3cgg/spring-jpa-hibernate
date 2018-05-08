package me.libme.module.spring.jpabean;

import me.libme.module.spring.jpahibernate.SingleEntityRepo;
import me.libme.module.spring.jpahibernate._m.IEntityModel;
import me.libme.module.spring.jpahibernate.query2.JSingleEntityQuery;
import me.libme.module.spring.jpahibernate.query2.SqlType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J on 2016/3/9.
 */
public interface ISingleEntityAccess<T extends IEntityModel,ID extends Serializable> {

	JSingleEntityQuery singleEntityQuery2();

	String selectCause(String... alias);

	String selectCause(SqlType sqlType, String... alias);

	void saveOnly(T object);

	void updateOnly(T object);

	void delete(String id, Class<?>... entryClass);

	void delete(T model);

	/**
	 * return entity model , include entity with deleted state
	 * @param id
	 * @return
	 */
	T getById(String id);

	/**
	 * return model with the expected type , include entity with deleted state
	 * @param id
	 * @param targetClass
	 * @param <M>
	 * @return
	 */
	<M> M getById(String id, Class<M> targetClass);

	/**
	 * return entity model , exclude entity with deleted state
	 * @param id
	 * @return
	 */
	T active(String id);

	/**
	 * return model with the expected type , include entity with deleted state
	 * @param id
	 * @param targetClass
	 * @param <M>
	 * @return
	 */
	<M> M active(String id, Class<M> targetClass);

	public void deleteAllByIds(List<String> ids, Class<?>... entryClass);

	public void deleteAllByModels(List<T> models, Class<?>... entryClass);

	public void deletePermanentlyByModels(List<T> models, Class<?>... entryClass);

	public List<T> getAllModes();

	public <M> List<M> getAllModes(Class<M> targetClass);

	public void deletePermanently(T model);

	/**
	 * {@inheritDoc}
	 */
//	@Override
//	public JPage<T> getsByPage(JPageable pagination) {
//		JPageImpl<T> page=new JPageImpl<T>();
//		List<T> records=getRepo().getModelsByPage(pagination);
//		page.setPageable(pagination);
//		page.setTotalRecordNumber(records.size());
//		page.setContent(records);
//		page.setTotalPageNumber(JPageImpl.caculateTotalPageNumber(records.size(), pagination.getPageSize()));
//		return page;
//	}

	SingleEntityRepo<T,ID> getRepo();

	void saveAllOnly(Iterable<T> objects, Class<?>... entryClass);

}
