package me.libme.module.spring.jpabean;

import me.libme.module.spring.jpahibernate._m.IEntityModel;
import org.springframework.stereotype.Component;

/**
 * Created by J on 2018/5/7.
 */
@Component
public class _DefaultEntityOnDelete implements EntityOnDeleteListener{

    @Override
    public void onDelete(IEntityModel entityModel, SessionUser sessionUser) {
        entityModel.setDeleted("Y");
    }
}
