package me.libme.module.spring.jpahibernate;

import me.libme.module.spring.jpahibernate._m.JBaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * the basic entry to access the DB
 * @author JIAZJ
 *
 * @param <M>
 * @param <ID>
 */
public interface SingleEntityRepo<M extends JBaseModel,ID extends Serializable> {
	
	public void saveModel(M baseModel); 
	
	public int updateModel(M baseModel);
	
	public M getModel(ID id, Class<?>... entryClass);

	/**
	 * delete physically
	 * @param baseModel
	 */
	public void deleteModel(M baseModel);

	public void markModelDeleted(M baseModel);

	public void markModelDeleted(ID id, Class<?>... entryClass);

	public List<M> getAllModels(Class<?>... entryClass);

	public void saveAllModels(Iterable<M> objects, Class<?>... entryClass);
	
	
}
