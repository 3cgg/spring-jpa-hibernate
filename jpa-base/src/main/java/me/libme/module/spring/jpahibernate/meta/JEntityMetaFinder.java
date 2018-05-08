package me.libme.module.spring.jpahibernate.meta;

import me.libme.kernel._c._i.JFinder;
import me.libme.kernel._c.util.JClassUtils;
import me.libme.module.spring.jpahibernate.Delete;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JEntityMetaFinder implements JFinder<JEntityModelMeta> {

	private Class<?> entityClass;
	
	private JEntityModelMeta entityModelMeta;
	
	public JEntityMetaFinder(Class<?> entityClass) {
		this.entityClass=entityClass;
		entityModelMeta=new JEntityModelMeta(entityClass);
	}
	
	@Override
	public JEntityModelMeta find() {
		
		List<JEntityColumnMeta> columnMetas=findColumns();
		entityModelMeta.setColumnMetas(columnMetas);
		Table table=entityClass.getAnnotation(Table.class);
		entityModelMeta.setTableName(table.name());
		entityModelMeta.setSchema(table.schema());
		return entityModelMeta;
	}
	
	private List<JEntityColumnMeta> findColumns(){
		List<JEntityColumnMeta> columnMetas=new ArrayList<JEntityColumnMeta>(); 
		List<Field> fields= JClassUtils.getFields(entityClass, true);
		Set<String> calc=new HashSet<String>();
		for(Field  field:fields){
			String fieldName=field.getName();
			if(calc.contains(fieldName)){
				break;
			}
			calc.add(fieldName);
			Method getter=null;
			try{
				getter=JClassUtils.findGetterMethod(entityClass, fieldName);
			}catch(RuntimeException e){
			}

			JEntityColumnMeta columnMeta=new JEntityColumnMeta();

			boolean isPrimary=false;
			boolean isDelete=false;

			String columnName=null;
			//try getter
			if(getter!=null){
				Column column=getter.getAnnotation(Column.class);
				if(column!=null){
					columnName=column.name();
				}

				Id id=getter.getAnnotation(Id.class);
				isPrimary=id!=null;

				Delete delete=getter.getAnnotation(Delete.class);
				isDelete=delete!=null;
				if(delete!=null){
					columnMeta.setDeleteAnnotation(delete);
				}
			}
			
			//try field
			if(columnName==null){
				Column column=field.getAnnotation(Column.class);
				if(column!=null){
					columnName=column.name();
				}

				Id id=field.getAnnotation(Id.class);
				isPrimary=isPrimary||id!=null;

				Delete delete=field.getAnnotation(Delete.class);
				isDelete=delete!=null;
				if(delete!=null){
					columnMeta.setDeleteAnnotation(delete);
				}
			}
			
			if(columnName==null){
				continue;
			}
			

			columnMeta.setAnnotations(field.getAnnotations());
			columnMeta.setClazz(entityClass);
			columnMeta.setColumn(columnName);
			columnMeta.setField(field);
			columnMeta.setFieldName(field.getName());
			columnMeta.setProperty(field.getName());
			columnMeta.setPrimary(isPrimary);
			columnMeta.setDelete(isDelete);
			columnMeta.setSetterMethodName(JClassUtils.getSetterMethodName(field));
			columnMeta.setGetterMethodName(JClassUtils.getGetterMethodName(field));
			columnMeta.setAnnotations(field.getAnnotations());
			columnMetas.add(columnMeta);

			if(isPrimary){
				entityModelMeta.setPrimaryColumn(columnMeta);
			}

			if(isDelete){
				entityModelMeta.setDeleteColumn(columnMeta);
			}
		}
		return columnMetas;
	}
	
}
