package me.libme.module.spring.jpabean;

import me.libme.module.spring.jpahibernate._m.IEntityModel;

/**
 * Created by J on 2018/5/7.
 */
public class _DefaultEntityOnDelete implements EntityOnDeleteListener{

    @Override
    public void onDelete(IEntityModel entityModel, SessionUser sessionUser) {
        entityModel.setDeleted("Y");
    }
}
