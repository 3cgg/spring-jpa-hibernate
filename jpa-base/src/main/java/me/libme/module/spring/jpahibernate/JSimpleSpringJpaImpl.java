package me.libme.module.spring.jpahibernate;

import me.libme.module.spring.jpahibernate._m.IEntityModel;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

public class JSimpleSpringJpaImpl<T extends IEntityModel, ID extends Serializable>
	extends SimpleJpaRepository<T, ID> implements JSpringJpaRepository<T, ID> {

    private EntityManager em;
	
	// There are two constructors to choose from, either can be used.
	  public JSimpleSpringJpaImpl(Class<T> domainClass, EntityManager entityManager) {
	    super(domainClass, entityManager);
	    // This is the recommended method for accessing inherited class dependencies.
	    this.em = entityManager;
	  }

	@Override
	public void saveModel(T baseModel) {
		save(baseModel);
	}

	@Override
	public int updateModel(T baseModel) {
		save(baseModel);
		return 1;
	}

	@Override
	public T getModel(ID id, Class<?>... entryClass) {
		return getOne(id);
	}

	@Override
	public void deleteModel(T baseModel) {
		delete(baseModel);
	}

	@Override
	public void markModelDeleted(T baseModel) {
		updateModel(baseModel);
	}

	@Override
	public void markModelDeleted(ID id, Class<?>... entryClass) {
		throw new RuntimeException("cannot delete only via primary .");
	}

	@Override
	public List<T> getAllModels(Class<?>... entryClass) {
		return findAll();
	}

	@Override
	public void saveAllModels(Iterable<T> objects, Class<?>... entryClass) {
		save(objects);
	}


}
