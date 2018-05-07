package me.libme.module.spring.jpabean;

import javax.persistence.EntityManager;

/**
 * Created by J on 2017/10/14.
 */
@FunctionalInterface
public interface EntityManagerDiscovery {

    EntityManager entityManager(Class clazz);

}
