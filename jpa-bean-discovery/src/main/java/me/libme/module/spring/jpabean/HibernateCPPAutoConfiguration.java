/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.libme.module.spring.jpabean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

@ConditionalOnClass({ HibernateJpaAutoConfiguration.class })
@ConditionalOnProperty(prefix = "cpp.jpa",name = "bean-discovery",havingValue ="true")
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class HibernateCppAutoConfiguration {

    public static final String BEAN_NAME="HibernateCppAutoConfiguration";

    private final Logger LOGGER= LoggerFactory.getLogger(HibernateCppAutoConfiguration.class);


    public HibernateCppAutoConfiguration() {
        LOGGER.info("----autoconfig----"+HibernateJpaAutoConfiguration.class);
    }


    @Bean
    public EntityOnSaveListener entityOnSaveListener(){
        return new _DefaultEntityOnSave();
    }

    @Bean
    public EntityOnUpdateListener entityOnUpdateListener(){
        return new _DefaultEntityOnUpdate();
    }

    @Bean
    public EntityOnDeleteListener entityOnDeleteListener(){
        return new _DefaultEntityOnDelete();
    }

    @Bean
    public EntityManagerDiscovery entityManagerDiscovery(){
        return new EntityManagerInSpringContext();
    }










}
