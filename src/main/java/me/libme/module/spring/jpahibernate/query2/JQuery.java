package me.libme.module.spring.jpahibernate.query2;


import me.libme.kernel._c._m.JPage;
import me.libme.kernel._c._m.JPageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JQuery<T extends JQuery<T>> {
	
	protected static JSPIQueryService spiQueryService
	=JSPIQueryServiceUtil.getSPIQueryService();

	protected Map<?, Object> params=new HashMap<Object, Object>();
	
	/**
	 * Parameters for count computing
	 */
	protected Map<?, Object> countParams=new HashMap<Object, Object>();
	
	
	protected final EntityManager em;
	
    /**
     * If pageable
     */
	protected JPageable pageable;
	
    /**
     * Expected Result Type 
     */
	protected Class<?> result;
	
    /**
     * The result set is only one element or many.
     */
	protected boolean single;
	
    /**
     * Result Set Mapping Defined in the orm.xml or on the Entity Class
     */
	protected String resultSetMapping;
	
	/**
	 * set true if the sql is update of insert 
	 * like 'update table set ... ; insert table ...'
	 */
	protected boolean update=false;
	
	/**
	 * whether to use alias , the resultset is {@link Map}
	 */
	protected boolean useAlias;
	
//	/**
//	 * whether to user SPI(Hibernate , Eclipse Link etc.)
//	 */
//	protected boolean useSpi=true;

	public JQuery(EntityManager em) {
		super();
		this.em = em;
	}
	
	public JPageable getPageable() {
		return pageable;
	}
	
	public T setPageable(JPageable pageable) {
		this.pageable = pageable;
		return (T) this;
	}
	public boolean isPageable(){
		return pageable!=null;
	}
	
	public Map<?, Object> getParams() {
		return params;
	}
	
	public T setParams(Map<?, Object> params) {
		this.params = params;
		return (T) this;
	}
	
	public Map<?, Object> getCountParams() {
		return countParams;
	}

	public T setCountParams(Map<?, Object> countParams) {
		this.countParams = countParams;
		return (T) this;
	}

	abstract Query getQuery();
	
	public abstract String getQueryString();
	
	public abstract String getCountQueryString();
	
	abstract Query getCountQuery();

	public Class<?> getResult() {
		return result;
	}

	private T setResult(Class<?> result) {
		this.result = result;
		return (T) this;
	}

	public boolean isSingle() {
		return single;
	}

	public T setSingle(boolean single) {
		this.single = single;
		return (T) this;
	}
	
	public T setResultSetMapping(String resultSetMapping) {
		this.resultSetMapping = resultSetMapping;
		return (T) this;
	}
	
	public String getResultSetMapping() {
		return resultSetMapping;
	}

	/**
	 * set true if the sql is update of insert 
	 * like 'update table set ... ; insert table ...'
	 * @param update
	 */
	public T setUpdate(boolean update) {
		this.update = update;
		return (T) this;
	}
	
	public boolean isUpdate() {
		return update;
	}
	
	public boolean isUseAlias() {
		return useAlias;
	}
	
	/**
	 * alias as key, column value as value. 
	 * <p><strong>the select clause must contain alias for each column</strong>
	 * @return
	 */
	public List<Map<String, Object>> maps(){
		setSingle(false);
		return ready().executeMap();
	}
	
	/**
	 * alias as key, column value as value. 
	 * <p><strong>the select clause must contain alias for each column</strong>
	 * @return
	 */
	public Map<String, Object> map(){
		setSingle(true);
		return ready().executeMap();
	}
	
	/**
	 * pageable Map
	 * <p><strong>the select clause must contain alias for each column</strong>
	 * @param pageable
	 * @return
	 */
	public JPage<Map<String, Object>> mapPage(JPageable pageable){
		setPageable(pageable);
		setSingle(false);
		return execute();
	}
	
	/**
	 * convenience to {@link #mapPage(JPageable)}
	 * @return
	 */
	public JPage<Map<String, Object>> mapPage(){
		return mapPage(pageable);
	}
	
	/**
	 * the result is a POJO
	 * @param resultClass expected type
	 * @return a POJO
	 */
	public <M> M model(Class<M> resultClass){
		return setResult(resultClass)
					.setSingle(true)
						.execute();
	}
	
	public <M> M model(){
		return model(null);
	}
	
	/**
	 * the result is a set of POJOs.
	 * @param resultClass expected type
	 * @return POJOs 
	 */
	public <M> List<M> models(Class<M> resultClass){
		return setResult(resultClass)
					.setSingle(false)
						.execute();
	}
	
	public <M> List<M> models(){
		return models(null);
	}
	
	/**
	 * the pageable set of POJOs
	 * @param pageable 
	 * @param resultClass expected type
	 * @return
	 */
	public <M> JPage<M> modelPage(JPageable pageable,Class<M> resultClass){
		return setResult(resultClass)
					.setPageable(pageable)
						.setSingle(false)
						.execute();
	}
	
	public <M> JPage<M> modelPage(JPageable pageable){
		return modelPage(pageable, null);
	}
	
	/**
	 * convenience to {@link #modelPage(JPageable, Class)}
	 * @param resultClass
	 * @return
	 */
	public <M> JPage<M> modelPage(Class<M> resultClass){
		return modelPage(pageable, resultClass);
	}
	
	public <M> JPage<M> modelPage(){
		return modelPage(pageable, null);
	}
	
	/**
	 * use {@link #model()} ,{@link #models()} instead of this, strongly recommended never use this method.
	 * @see JQuery#model()
	 * @see #models()
	 */
	@Deprecated
	public <M> M execute(){
		return ready().execute();
	}
	
	private JQueryExecution ready(){
		JQueryExecution queryExecution=null;
		if(isPageable()){
			queryExecution=new JQueryExecution.PagedExecution(this);
		}
		else if(isUpdate()){
			queryExecution=new JQueryExecution.UpdateExecution(this);
		}
		else if(isSingle()){
			queryExecution=new JQueryExecution.SingleExecution(this);
		}
		else{
			queryExecution=new JQueryExecution.ListExecution(this);
		}
		return queryExecution;
	}

	public void detach(Object entity){

	}

}
