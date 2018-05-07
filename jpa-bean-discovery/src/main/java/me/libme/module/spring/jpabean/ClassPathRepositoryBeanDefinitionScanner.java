package me.libme.module.spring.jpabean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.ClassMetadata;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by J on 2017/10/14.
 */
public class ClassPathRepositoryBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {


    private final Logger LOGGER= LoggerFactory.getLogger(ClassPathRepositoryBeanDefinitionScanner.class);


    public ClassPathRepositoryBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry,true);
        super.addIncludeFilter(
                (metadataReader,metadataReaderFactory)->{
//                    boolean included=false;
//                    ClassMetadata classMetadata= metadataReader.getClassMetadata();
//                    if(classMetadata.isInterface()){
//                        String indicator=ISingleEntityAccess.class.getName();
//                        String[] interfaceNames=classMetadata.getInterfaceNames();
//                        for(String name : interfaceNames){
//                            if(indicator.equals(name)){
//                                included=true;
//                                break;
//                            }
//                        }
//                    }
//                    return included;
                    return true;
                });

        super.addExcludeFilter((metadataReader,metadataReaderFactory)->{
            boolean excluded=false;
            ClassMetadata classMetadata= metadataReader.getClassMetadata();

            try {
                Class clazz=Class.forName(classMetadata.getClassName());
                if(!ISingleEntityAccess.class.isAssignableFrom(clazz)
                        ||clazz.getAnnotation(Repository.class)==null){
                    //我们在这里仅仅找自己的Repo接口
                    excluded=true;
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            return excluded;
        });
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }


    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders= super.doScan(basePackages);
        beanDefinitionHolders.forEach(beanDefinitionHolder -> {
            BeanDefinition beanDefinition=beanDefinitionHolder.getBeanDefinition();

            ScannedGenericBeanDefinition scannedGenericBeanDefinition=(ScannedGenericBeanDefinition) beanDefinition;
            String className=scannedGenericBeanDefinition.getMetadata().getClassName();
            ClassLoader classLoader=scannedGenericBeanDefinition.getClass().getClassLoader();

            Class entityClass=null;
            Class clazz=null;
            try{
                clazz=classLoader.loadClass(className);

                Type[] types=clazz.getGenericInterfaces();
                for(Type type:types){
                    if(ParameterizedType.class.isInstance(type)){
                        ParameterizedType parameterizedType=(ParameterizedType)type;
                        Type rawType= parameterizedType.getRawType();
                        if(ISingleEntityAccess.class.isAssignableFrom((Class<?>) rawType)){
                            entityClass=((Class)parameterizedType.getActualTypeArguments()[0]);
                        }
                    }

                }
            }catch (Exception e){}

            if(entityClass==null){
                LOGGER.info("can not find any entity , check whether the repo is abstract or not? : "+className);
            }else {

                ((ScannedGenericBeanDefinition) beanDefinition).setBeanClass(SingleEntityManagerFactoryBean.class);
                beanDefinition.setLazyInit(true);
                beanDefinition.getPropertyValues().add("clazz", clazz);
                beanDefinition.getPropertyValues().add("entityClass", entityClass);


                LOGGER.info("----append new bean definition : " + className);
            }

        });
        return  beanDefinitionHolders;
    }
}
