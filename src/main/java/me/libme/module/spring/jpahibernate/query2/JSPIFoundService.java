package me.libme.module.spring.jpahibernate.query2;


import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;
import java.util.List;

public class JSPIFoundService
{
	
	public JJPASPI getSpi(){
		PersistenceProviderResolver persistenceProviderResolver=PersistenceProviderResolverHolder.getPersistenceProviderResolver();
		List<PersistenceProvider> persistenceProviders=persistenceProviderResolver.getPersistenceProviders();
		if(persistenceProviders!=null&&!persistenceProviders.isEmpty()){
			for(PersistenceProvider persistenceProvider:persistenceProviders){
				if(persistenceProvider instanceof HibernatePersistenceProvider){
					return JJPASPI.HIBERNATE;
				}
			}
		}
		return JJPASPI.EMPTY;
	} 
	
}
