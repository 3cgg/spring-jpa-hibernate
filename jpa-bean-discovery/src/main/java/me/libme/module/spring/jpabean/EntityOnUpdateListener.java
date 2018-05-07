package me.libme.module.spring.jpabean;

import me.libme.module.spring.jpahibernate._m.IEntityModel;

/**
 * Created by J on 2018/5/7.
 */
public interface EntityOnUpdateListener {


    void onUpdate(IEntityModel entityModel, SessionUser sessionUser);


}
