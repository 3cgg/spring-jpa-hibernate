package me.libme.module.spring.jpahibernate.query2;

import me.libme.kernel._c._m.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import javax.persistence.Parameter;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class JQueryExecution {
	
	protected Logger logger= LoggerFactory.getLogger(getClass());
	
	protected JQuery<?> _query;
	
	protected static JSPIQueryService spiQueryService
	=JSPIQueryServiceUtil.getSPIQueryService();
	
	public JQueryExecution(JQuery<?> _query) {
		this._query = _query;
	}

	public <T> T executeMap(){
		this._query.useAlias=true;
		return execute();
	}
	

	public <T> T execute(){
		T result=null;
		try {
			result = doExecute();
		} catch (NoResultException e) {
			return null;
		}
		return result;
	}
	
	
	protected abstract <T> T doExecute();
	
	protected void setQueryParameters(Query query, Map<?, Object> params){
		boolean set=setSpecifiedQueryParameterAsPossible(query, params);
		if(!set){
			set=setUnspecifiedQueryParameterAsPossible(query, params);
		}
	}
	
	/**
	 * call {@link Query#getParameters()} to inspect how many parameters.
	 * @param query
	 * @param params
	 * @return if already set the parameters successfully.
	 * @see Query
	 */
	private boolean setSpecifiedQueryParameterAsPossible(Query query, Map<?, Object> params) {
		
		Set<Parameter<?>> sqlParams=null;
		try{
			sqlParams=query.getParameters();
		}catch(IllegalStateException e ){
			if(logger.isDebugEnabled()){
				logger.debug("cannot get parameters of the sql before setting parameters.");
			}
			return false;
		}
		
		for (Parameter<?> param : sqlParams){
			String paramName=param.getName();
			Object value=params.get(paramName);
			
			if(JJpaDateParam.class.isInstance(value)){
				JJpaDateParam jpaDateParam= (JJpaDateParam) value;
//				if(String.class.isInstance(paramName)){
				query.setParameter(paramName, jpaDateParam.getDate(), jpaDateParam.getTemporalType());
//				}
//				else if(Integer.class.isInstance(paramName)){
//					query.setParameter((Integer)paramName, jpaDateParam.getDate(), jpaDateParam.getTemporalType());
//				}
			}
			else if(JJpaCalendarParam.class.isInstance(value)){
				JJpaCalendarParam jpaCalendarParam= (JJpaCalendarParam) value;
//				if(String.class.isInstance(paramName)){
				query.setParameter(paramName, jpaCalendarParam.getCalendar(), jpaCalendarParam.getTemporalType());
//				}
//				else if(Integer.class.isInstance(paramName)){
//					query.setParameter((Integer)paramName, jpaCalendarParam.getCalendar(), jpaCalendarParam.getTemporalType());
//				}
			}
			else{
//				if(String.class.isInstance(paramName)){
				query.setParameter(paramName, value);
//				}
//				else if(Integer.class.isInstance(paramName)){
//					query.setParameter((Integer)paramName, value);
//				}
			}
		}
		return true;
	}
	
	
	/**
	 * directly iterate the parameters map to set parameters.
	 * @param query
	 * @param params
	 * @return  if already set the parameters successfully.
	 */
	private boolean setUnspecifiedQueryParameterAsPossible(Query query, Map<?, Object> params) {
		for (Map.Entry<?, Object> entry : params.entrySet()){
			if(JJpaDateParam.class.isInstance(entry.getValue())){
				JJpaDateParam jpaDateParam= (JJpaDateParam) entry.getValue();
				if(String.class.isInstance(entry.getKey())){
					query.setParameter((String)entry.getKey(), jpaDateParam.getDate(), jpaDateParam.getTemporalType());
				}
				else if(Integer.class.isInstance(entry.getKey())){
					query.setParameter((Integer)entry.getKey(), jpaDateParam.getDate(), jpaDateParam.getTemporalType());
				}
			}
			else if(JJpaCalendarParam.class.isInstance(entry.getValue())){
				JJpaCalendarParam jpaCalendarParam= (JJpaCalendarParam) entry.getValue();
				if(String.class.isInstance(entry.getKey())){
					query.setParameter((String)entry.getKey(), jpaCalendarParam.getCalendar(), jpaCalendarParam.getTemporalType());
					
				}
				else if(Integer.class.isInstance(entry.getKey())){
					query.setParameter((Integer)entry.getKey(), jpaCalendarParam.getCalendar(), jpaCalendarParam.getTemporalType());
				}
			}
			else{
				if(String.class.isInstance(entry.getKey())){
					query.setParameter((String)entry.getKey(), entry.getValue());
					
				}
				else if(Integer.class.isInstance(entry.getKey())){
					query.setParameter((Integer)entry.getKey(), entry.getValue());
				}
			}
		}
		return true;
	}
	
	static class UpdateExecution extends JQueryExecution{
		public UpdateExecution(JQuery<?> _query) {
			super(_query);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object doExecute() {
			Query query=_query.getQuery();
			setQueryParameters(query, _query.getParams());
			return query.executeUpdate();
		}
	}
	
	static class SingleExecution extends JQueryExecution{
		public SingleExecution(JQuery<?> _query) {
			super(_query);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object doExecute() {
			Query query=_query.getQuery();
			setQueryParameters(query, _query.getParams());
//			Object object= query.getSingleResult();
//			Class<?> result=_query.getResult();
//			return result==null?object:result.cast(object);
			return spiQueryService.getSingleResult(query, _query);
		}
	}
	
	static class ListExecution extends JQueryExecution{
		public ListExecution(JQuery<?> _query) {
			super(_query);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected List<?> doExecute() {
			Query query=_query.getQuery();
			setQueryParameters(query, _query.getParams());
//			return query.getResultList();
			return spiQueryService.getResultList(query, _query);
		}
	}
	
	
	static class PagedExecution extends JQueryExecution {
		
		public PagedExecution(JQuery<?> _query) {
			super(_query);
		}

		@Override
		protected JPage doExecute() {
			JPageable pageable=_query.getPageable();
			Query countQuery=_query.getCountQuery();
			setQueryParameters(countQuery, _query.getParams());
			long count=0;
			Object obj=countQuery.getSingleResult();
			if(BigInteger.class.isInstance(obj)){
				count=((BigInteger)obj).longValue();
			}
			else{
				count=Long.valueOf(obj.toString());
			}
			int pageNumber=pageable.getPageNumber();
			int pageSize=pageable.getPageSize();
			int tempTotalPageNumber= JImpl.caculateTotalPageNumber(count, pageSize);
			pageNumber=pageNumber>tempTotalPageNumber?tempTotalPageNumber:pageNumber;
			
			Query query=_query.getQuery();
			setQueryParameters(query, _query.getParams());
			query.setFirstResult(pageNumber*pageSize);
			query.setMaxResults(pageSize);
//			List list= query.getResultList();
			List list= spiQueryService.getResultList(query, _query);
			JImpl page=new JImpl();
			JPageUtil.replaceConent(page, list);
			page.setTotalRecordNumber(count);
			page.setTotalPageNumber(tempTotalPageNumber);
			SimplePageRequest pageRequest=new SimplePageRequest(pageNumber, pageable.getPageSize());
			page.setPageable(pageRequest);
			
			return page;
		}
	} 
	
	
	
}
