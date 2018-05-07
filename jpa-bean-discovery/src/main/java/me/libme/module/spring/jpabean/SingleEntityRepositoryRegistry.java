package me.libme.module.spring.jpabean;

import me.libme.kernel._c.json.JJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 2017/10/14.
 */
@Component
public class SingleEntityRepositoryRegistry implements BeanDefinitionRegistryPostProcessor ,EnvironmentAware{

    private final Logger LOGGER= LoggerFactory.getLogger(SingleEntityRepositoryRegistry.class);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        String[] names=registry.getBeanDefinitionNames();
        for(String name : names){
            LOGGER.info("--bean definition name[predefined]-- : "+name);
        }

        List<String> repoPackages=new ArrayList<>();
        for(int i=0;i< Integer.MAX_VALUE;i++){
            String key="cpp.jpa.repoPackages["+i+"]";
            if(environment.containsProperty(key)){
                repoPackages.add(environment.getProperty(key));
            }else{
                break;
            }
        }


        LOGGER.info("--begin loading repository from base package : "+ JJSON.get().format(repoPackages));

        ClassPathRepositoryBeanDefinitionScanner scanner=new ClassPathRepositoryBeanDefinitionScanner(registry);
        int count=scanner.scan(repoPackages.toArray(new String[]{}));
        LOGGER.info("--append bean definition count : "+count);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
