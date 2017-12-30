package me.libme.module.spring.jpahibernate.query2;

import javax.persistence.EntityManager;

public class JQueryBuilder {
	
	private static JQueryBuilder INSTANCE=null;
	
	private JQueryBuilder(EntityManager em){
		this.em=em;
	}
	
	private EntityManager em;
	
	private static final Object sync=new Object();
	
	public static JQueryBuilder get(EntityManager em){
		if(INSTANCE==null){
			synchronized (sync) {
				if(INSTANCE==null){
					INSTANCE=new JQueryBuilder(em);
				}
			}
		}
		return INSTANCE;
	}
	
	public JNativeQuery nativeQuery(){
		return new JNativeQuery(em);
	}
	
	public JJPQLQuery jpqlQuery(){
		return new JJPQLQuery(em);
	}
	
	public JNamedQuery namedQuery(){
		return new JNamedQuery(em);
	}
	
}
