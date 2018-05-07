package me.libme.module.spring.jpahibernate._m;

import me.libme.kernel._c._m.JModel;

import java.util.Date;


/**
 *
 */
public interface IEntityModel extends JModel {

    String getId();

    void setId(String id);

    String getCreateId();

    void setCreateId(String createId);

    String getUpdateId();

    void setUpdateId(String updateId);

    Date getCreateTime();

    void setCreateTime(Date createTime);

    Date getUpdateTime();

    void setUpdateTime(Date updateTime);

    String getDeleted();

    void setDeleted(String deleted);

    int getVersion();

    void setVersion(Integer version);

}
