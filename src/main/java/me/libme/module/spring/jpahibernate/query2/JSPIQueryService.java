package me.libme.module.spring.jpahibernate.query2;

import javax.persistence.Query;
import java.util.List;

public interface JSPIQueryService {

	<T> T getSingleResult(Query query, JQuery<?> _query);

	<T> List<T> getResultList(Query query, JQuery<?> _query);

	Query createJPQLQuery(String jpql, JQuery<?> _query);

	Query createNamedQuery(String namedSql, JQuery<?> _query);

	Query createNativeQuery(String nativeSql, JQuery<?> _query);
}
