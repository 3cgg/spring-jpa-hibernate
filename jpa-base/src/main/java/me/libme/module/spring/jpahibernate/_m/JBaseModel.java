package me.libme.module.spring.jpahibernate._m;

import me.libme.module.spring.jpahibernate.Delete;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * The super class that those all implementations can be serialized
 * @author J
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class JBaseModel implements IEntityModel {

	/**
	 * the primary key , uuid 
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	@Column(name = "id", length = 36)
	private String id;
	
	/**
	 * create user id
	 */
	@Column(name="create_id",length=36)
	private String createId;
	
	/**
	 * update user id 
	 */
	@Column(name="update_id",length=36)
	private String updateId;
	
	/**
	 * create time
	 */
	@Column(name = "create_time" , updatable = false)
	@Temporal(value= TemporalType.TIMESTAMP)
	private Date createTime;
	
	/**
	 * update time
	 */
	@Column(name = "update_time")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date updateTime;
	
	/**
	 * marks whether the record is deleted. Value is Y|N
	 */
	@Column(name="deleted",length=1)
	@Delete
	private String deleted;
	
	/**
	 * the property can limit the async operation effectively 
	 */
	@Version
	@Column(name = "version")
	private int version;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getCreateId() {
		return createId;
	}

	@Override
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	@Override
	public String getUpdateId() {
		return updateId;
	}

	@Override
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public Date getUpdateTime() {
		return updateTime;
	}

	@Override
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String getDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
	public JBaseModel clone() throws CloneNotSupportedException {
		return (JBaseModel) super.clone();
	}
	
}
