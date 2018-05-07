package me.libme.module.spring.jpabean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * Created by J on 2017/10/14.
 */
@Component
public class EntityManagerInSpringContext implements EntityManagerDiscovery ,ApplicationContextAware{

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public EntityManager entityManager(Class clazz) {
        EntityManager entityManager=applicationContext.getBean(EntityManager.class);
        return entityManager;

    }



}
