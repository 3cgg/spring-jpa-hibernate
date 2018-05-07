package me.libme.module.spring.jpabean;

import me.libme.module.spring.jpahibernate._m.IEntityModel;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by J on 2018/5/7.
 */
@Component
public class _DefaultEntityOnUpdate implements EntityOnUpdateListener{

    @Override
    public void onUpdate(IEntityModel entityModel, SessionUser authorizer) {
        entityModel.setUpdateId(authorizer.getId());
        entityModel.setUpdateTime(new Date());
    }

    
}
