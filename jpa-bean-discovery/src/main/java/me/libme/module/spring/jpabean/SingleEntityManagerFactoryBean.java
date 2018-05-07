package me.libme.module.spring.jpabean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;

/**
 * Created by J on 2017/10/14.
 */
public class SingleEntityManagerFactoryBean implements FactoryBean<Object>,ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Class entityClass;

    private Class<?> clazz;

    private EntityManagerDiscovery entityManagerDiscovery;

    @Autowired
    public void setEntityManagerDiscovery(EntityManagerDiscovery entityManagerDiscovery) {
        this.entityManagerDiscovery = entityManagerDiscovery;
    }

    private SessionUserFactory sessionUserFactory;

    @Autowired
    public void setSessionUserFactory(SessionUserFactory sessionUserFactory) {
        this.sessionUserFactory = sessionUserFactory;
    }

    private EntityOnSaveListener entityOnSaveListener;

    @Autowired
    public void setEntityOnSaveListener(EntityOnSaveListener entityOnSaveListener) {
        this.entityOnSaveListener = entityOnSaveListener;
    }


    private EntityOnUpdateListener entityOnUpdateListener;

    @Autowired
    public void setEntityOnUpdateListener(EntityOnUpdateListener entityOnUpdateListener) {
        this.entityOnUpdateListener = entityOnUpdateListener;
    }


    private EntityOnDeleteListener entityOnDeleteListener;

    @Autowired
    public void setEntityOnDeleteListener(EntityOnDeleteListener entityOnDeleteListener) {
        this.entityOnDeleteListener = entityOnDeleteListener;
    }

    private class SingleRepoProxy implements InvocationHandler {

        private final SingleEntityManager singleEntityManager;

        public SingleRepoProxy(SingleEntityManager singleEntityManager) {
            this.singleEntityManager = singleEntityManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Method targetMethod=singleEntityManager.getClass()
                    .getMethod(method.getName(),method.getParameterTypes());
            Object object=targetMethod.invoke(singleEntityManager,args);
            return object;
        }

    }

    @Override
    public Object getObject() throws Exception {

        SingleEntityManager singleEntityManager=SingleEntityManagerGetter.get()
                .getInstance(entityClass,entityManagerDiscovery);
        singleEntityManager.setSessionUserFactory(sessionUserFactory);
        singleEntityManager.setEntityOnSaveListener(entityOnSaveListener);
        singleEntityManager.setEntityOnUpdateListener(entityOnUpdateListener);
        singleEntityManager.setEntityOnDeleteListener(entityOnDeleteListener);

        SingleRepoProxy singleRepoProxy=new SingleRepoProxy(singleEntityManager);
        Object object=Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},singleRepoProxy);
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }
}
