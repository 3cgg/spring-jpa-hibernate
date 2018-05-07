package me.libme.module.spring.jpabean;


import me.libme.module.spring.jpahibernate._m.JBaseModel;

import javax.persistence.EntityManager;

public class SingleEntityManagerGetter {

	private static final SingleEntityManagerGetter INSTANCE=new SingleEntityManagerGetter();
	
	public static final SingleEntityManagerGetter get(){
		return INSTANCE;
	}

	public final <T extends JBaseModel> SingleEntityManager<T> getInstance(Class<T> clazz, EntityManagerDiscovery entityManagerDiscovery){
		return getInstance(clazz,entityManagerDiscovery.entityManager(clazz));
	}

	public final <T extends JBaseModel> SingleEntityManager<T> getInstance(Class<T> clazz, EntityManager em){
		SingleEntityManager<T> singleEntityManager=new SingleEntityManager<>(em, clazz);
		return singleEntityManager;
	}


	
}
