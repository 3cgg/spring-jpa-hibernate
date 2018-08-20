package me.libme.module.spring.jpabean;

import me.libme.module.spring.jpahibernate._m.IEntityModel;

import java.util.Date;

/**
 * Created by J on 2018/5/7.
 */
public class _DefaultEntityOnUpdate implements EntityOnUpdateListener{

    @Override
    public void onUpdate(IEntityModel entityModel, SessionUser authorizer) {
        entityModel.setUpdateId(authorizer.getId());
        entityModel.setUpdateTime(new Date());
    }

    
}
