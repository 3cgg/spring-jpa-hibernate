package me.libme.module.spring.jpahibernate.meta;

import me.libme.kernel._c._m.JModel;
import me.libme.kernel._c.util.JStringUtils;
import me.libme.module.spring.jpahibernate.query2.JSingleEntityQueryMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JEntityUtilService
{
	
	private static Map<Class<?>, _EntityMeta> entityMetas=new HashMap();

	private Object sync=new Object();
	
	@SuppressWarnings("serial")
	private class _EntityMeta implements JModel {
		JEntityModelMeta entityModelMeta;
		String selectCause;
	}
	
	private JEntityModelMeta genEntityModelMeta(Class<?> entityClass){
		JEntityMetaFinder entityMetaFinder=new JEntityMetaFinder(entityClass);
		JEntityModelMeta entityModelMeta= entityMetaFinder.find();
		return entityModelMeta;
	}
	
	
	private _EntityMeta _getE(Class<?> entityClass){
		if(entityMetas.containsKey(entityClass)){
			return entityMetas.get(entityClass);
		}
		synchronized (sync) {
			if(entityMetas.containsKey(entityClass)){
				return entityMetas.get(entityClass);
			}
			_EntityMeta entityMeta=new _EntityMeta();
			entityMeta.entityModelMeta= genEntityModelMeta(entityClass);
			entityMeta.selectCause=_selectCause(JSingleEntityQueryMeta.SqlType.JPQL,entityMeta.entityModelMeta, "");
			entityMetas.put(entityClass, entityMeta);
			return entityMeta;
		}
	 
	}
	
	public JEntityModelMeta getEntityModelMeta(Class<?> entityClass){
		return _getE(entityClass).entityModelMeta;
	}
	
	public JEntityColumnMeta getEntityColumnMeta(Class<?> entityClass , String property){
		Collection<JEntityColumnMeta> entityColumnMetas= getEntityModelMeta(entityClass).columnMetas();
		for (Iterator<JEntityColumnMeta> iterator = entityColumnMetas.iterator(); iterator.hasNext();) {
			JEntityColumnMeta entityColumnMeta =  iterator.next();
			if(entityColumnMeta.getProperty().equals(property)){
				return entityColumnMeta;
			}
		}
		return null;
	}
	
	
	private String _selectCause(JSingleEntityQueryMeta.SqlType sqlType, Class<?> entityClass, String alias){
		JEntityModelMeta entityModelMeta=getEntityModelMeta(entityClass);
		return _selectCause(sqlType,entityModelMeta, alias);
	}

	private String _selectCause(JSingleEntityQueryMeta.SqlType sqlType, JEntityModelMeta entityModelMeta, String alias) {
		Collection<JEntityColumnMeta> entityColumnMetas=entityModelMeta.columnMetas();
		String selectCause="select ";
		for(JEntityColumnMeta columnMeta:entityColumnMetas){
			selectCause=selectCause
					+(JStringUtils.isNullOrEmpty(alias)?"":(alias+"."))
					+(JSingleEntityQueryMeta.SqlType.JPQL==sqlType?
							(columnMeta.getProperty() +" as "+ columnMeta.getProperty())
							:(columnMeta.getColumn()+" as "+"'"+columnMeta.getProperty()+"'" ))
					+" , ";
		}
		selectCause=selectCause.substring(0, selectCause.lastIndexOf(","));
		return selectCause;
	}
	
	public String selectCause(JSingleEntityQueryMeta.SqlType sqlType, Class<?> entityClass, String... alias){
		return alias.length==0?(_getE(entityClass).selectCause):(_selectCause(sqlType,entityClass, alias[0]));
	}
	
	public String selectCause(Class<?> entityClass,String... alias){
		return selectCause(JSingleEntityQueryMeta.SqlType.JPQL, entityClass, alias);
	}

	private static final JEntityUtilService INSTANCE=new JEntityUtilService();
	
	public static JEntityUtilService get(){
		return INSTANCE;
	}
	
	
	

}
