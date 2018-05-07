package me.libme.module.spring.jpahibernate;

import me.libme.module.spring.jpahibernate._m.IEntityModel;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface JSpringJpaRepository<T extends IEntityModel,ID extends Serializable>
	extends PagingAndSortingRepository<T, ID>,SingleEntityRepo<T, ID>{
	
//	@Override
//	@Query(value="select t from #{#entityName} t")
//	public List<T> getModelsByPage(JPagination pagination);
	
}
