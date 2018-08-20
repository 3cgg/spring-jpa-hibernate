package me.libme.module.spring.jpabean;

import me.libme.module.spring.jpahibernate._m.IEntityModel;

import java.util.Date;

/**
 * Created by J on 2018/5/7.
 */
public class _DefaultEntityOnSave implements EntityOnSaveListener {

    @Override
    public void onSave(IEntityModel entityModel, SessionUser authorizer) {

        entityModel.setVersion(0);
        entityModel.setCreateId(authorizer.getId());
        entityModel.setCreateTime(new Date());
        entityModel.setUpdateId(authorizer.getId());
        entityModel.setUpdateTime(new Date());
        entityModel.setDeleted("N");
        
    }

    
}
