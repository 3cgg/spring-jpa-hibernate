package me.libme.module.spring.jpabean;

import me.libme.kernel._c.util.JStringUtils;
import me.libme.kernel._c.util.JUniqueUtils;
import me.libme.module.spring.jpahibernate._m.IEntityModel;

import java.util.Date;

/**
 * Created by J on 2018/5/7.
 */
public class _DefaultEntityOnSave implements EntityOnSaveListener {

    @Override
    public void onSave(IEntityModel entityModel, SessionUser authorizer) {

        String id=entityModel.getId();
        if(JStringUtils.isNullOrEmpty(id)){
            entityModel.setId(JUniqueUtils.unique());
        }
        entityModel.setVersion(0);
        entityModel.setCreateId(authorizer.getId());
        entityModel.setCreateTime(new Date());
        entityModel.setUpdateId(authorizer.getId());
        entityModel.setUpdateTime(new Date());
        entityModel.setDeleted("N");
        
    }

    
}
