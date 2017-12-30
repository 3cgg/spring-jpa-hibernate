package me.libme.module.spring.jpahibernate.query2;


import me.libme.kernel._c._m.JModel;
import me.libme.module.spring.jpahibernate.meta.JEntityUtilService;
import me.libme.kernel._c.util.JStringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCondition implements JModel {
	
	/**
	 * how to link this condition.
	 */
	private LinkType linkType;  
	
	private JSingleEntityQuery singleEntityQuery;
	
	private Class<?> entityClass;
	
	private List<ConditionSlice> conditionSliceClauses=new ArrayList<ConditionSlice>();
	
	private Map<String, Object> params=new HashMap<String, Object>();
	
	private boolean rootUsed=false;
	
	/**
	 * next condition
	 */
	private JCondition next;
	
	/**
	 * previous condition
	 */
	private JCondition pre;


	private int seq;
	
	private Restrict strict;
	
	private class Restrict{
		
		private boolean empty=true;
		
		private boolean nullable=true;
		
		public JCondition ready(){
			JCondition.this.strict=this;
			return JCondition.this;
		}
		
		public void empty(boolean empty) {
			this.empty = empty;
		}
		
		public void nullable(boolean nullable) {
			this.nullable = nullable;
		}
		
	}
	
	/**
	 * next comparing will throw {@link IllegalArgumentException} if the compared value is null or empty
	 * @return
	 */
	public Restrict nextOpeRestrict(){
		return new Restrict();
	}
	
	private void resetStrict(){
		strict=null;
	}
	
	private void throwIfNullOrEmpty(Object value){
		if(strict!=null){
			if(!strict.nullable){
				if(value==null){
					throw new IllegalArgumentException("null or empty argument passed");
				}
			}
			
			if(strict.empty){
				if(String.class.isInstance(value)
						&&JStringUtils.isNullOrEmpty((String) value)){
					throw new IllegalArgumentException("null or empty argument passed");
				}
			}
		}
	}
	
	
	private boolean isNullOrEmpty(Object value){
		return value==null||
				(String.class.isInstance(value)
						&& JStringUtils.isNullOrEmpty((String) value));
	}
	
	
	public JCondition(Class<?> entityClass) {
		this(entityClass, LinkType.ROOT);
	}
	
	private JCondition(Class<?> entityClass,LinkType linkType) {
		this(entityClass, linkType, null);
	}
	
	private JCondition(Class<?> entityClass,LinkType linkType,JCondition previousCondition) {
		this.entityClass=entityClass;
		this.linkType=linkType;
		this.pre=previousCondition;
	}
	
	void setSingleEntityQuery(JSingleEntityQuery singleEntityQuery) {
		this.singleEntityQuery = singleEntityQuery;
	}
	
	
	private boolean validate(String property) throws IllegalArgumentException{
		return true;
	}
	
	public Map<String, Object> getParams() {
		return params;
	}
	
	public Map<String, Object> toParams() {
		if(pre==null){
			return getParams();
		}
		Map<String, Object> preSliceParams=pre.toParams();
		Map<String, Object> thisSliceParams=getParams();
		Map<String, Object> allParams=new HashMap<String, Object>(preSliceParams.size()+thisSliceParams.size());
		allParams.putAll(preSliceParams);
		allParams.putAll(thisSliceParams);
		return allParams;
	}
	
	public String toSliceClause(JSingleEntityQueryMeta.SqlType sqlType){
		StringBuffer stringBuffer=new StringBuffer("");
		String prefix= LinkType.ROOT==linkType?"":linkType.name();
		for(ConditionSlice conditionSlice:conditionSliceClauses){
			String clause=_conditionSlice(conditionSlice,sqlType);
			stringBuffer.append(" "+clause+" ");
		}
		String inner=stringBuffer.toString().trim();
		return " "+prefix+" ("+inner+")";
	}
	
	/**
	 * include this condition
	 * @return
	 */
	private String toPreWholeClause(JSingleEntityQueryMeta.SqlType sqlType){
		if(pre==null){
			return toSliceClause(sqlType);
		}
		String preSliceClause=pre.toPreWholeClause(sqlType);
		String thisSliceClause=toSliceClause(sqlType);
		return preSliceClause+ thisSliceClause;
	}
	
	/**
	 * exclude this condition.
	 * @param thisCondition
	 * @return
	 */
	private String toNextWholeClause(JCondition thisCondition,JSingleEntityQueryMeta.SqlType sqlType){
		if(next==null){
			if(this!=thisCondition){
				return toSliceClause(sqlType);
			}
			else{
				return "";
			}
		}
		String nextSliceClause=next.toNextWholeClause(thisCondition,sqlType);
		String thisSliceClause="";
		if(this!=thisCondition){
			thisSliceClause=toSliceClause(sqlType);
		}
		return thisSliceClause+nextSliceClause;
	}
	
	public String toWhereClause(){
		return toWhereClause(JSingleEntityQueryMeta.SqlType.JPQL);
	}
	
	public String toWhereClause(JSingleEntityQueryMeta.SqlType sqlType){
		return " where "+toPreWholeClause(sqlType)+toNextWholeClause(this,sqlType);
	}
	
	public static enum LinkType{
		AND("AND"),OR("OR"),ROOT("ROOT");
		private LinkType(String type){
			
		}
		
	}
	
	private static enum Ope{
		
		EQUAL(" = "),
		NOT_EQUAL("!="),
		LARGER(">"),
		LARGER_EQUAL(">="),
		SMALLER("<"),
		SMALLER_EQUAL("<="),
		LIKE("like"),
		START_LIKE("like"),
		END_LIKE("like"),
		IN("in"),
		IS("is");
		
		
		private String instruct;
		
		private Ope(String instruct){
			this.instruct=instruct;
		}
		
		String getInstruct() {
			return instruct;
		}
		
		Object wrapValue(Object value){
			if(Ope.START_LIKE==this){
				return "%"+String.valueOf(value);
			}else if(Ope.END_LIKE==this){
				return "%"+String.valueOf(value);
			}else if(Ope.LIKE==this){
				return "%"+String.valueOf(value)+"%";
			}else{
				return value;
			}
		}
		
	}
	
	
	private JCondition append(String property,Object value,Ope opeType,LinkType... linkType){
		throwIfNullOrEmpty(value);
		validate(property);
		if(opeType!= Ope.IS){
			if(isNullOrEmpty(value)){
				return this;
			}
		}
		
		String linkTypeName=null;
		if(rootUsed){
			linkTypeName=linkType.length>0?linkType[0].name(): LinkType.AND.name();
		}else{
			linkTypeName="";
			rootUsed=true;
		}
		String paramString=property+(++seq);
		ConditionSlice conditionSlice=new ConditionSlice();
		conditionSlice.linkTypeName=linkTypeName;
		conditionSlice.propertyName=property;
		conditionSlice.columnName= JEntityUtilService.get()
				.getEntityColumnMeta(entityClass, property).getColumn();
		conditionSlice.opeType=opeType;
		conditionSlice.paramString=paramString;
		conditionSliceClauses.add(conditionSlice);
//		conditionSliceClauses.add(linkTypeName+" "+JSingleEntityQueryMeta.ALIAS+"."+property+opeType+" :"+paramString);
		
		if(opeType!= Ope.IS){
			params.put(paramString, opeType.wrapValue(value));
		}
		resetStrict();
		return this;
	}
	
	private String _conditionSlice(ConditionSlice conditionSlice,JSingleEntityQueryMeta.SqlType sqlType){
		return conditionSlice.linkTypeName
				+" "+JSingleEntityQueryMeta.ALIAS+"."
				+(JSingleEntityQueryMeta.SqlType.JPQL==sqlType? conditionSlice.propertyName :conditionSlice.columnName)
				+( conditionSlice.opeType== Ope.IS?
						( " is null " ) :(" "+conditionSlice.opeType.getInstruct()+" :"+conditionSlice.paramString)
						);
	}
	
	/**
	 * link to another condition, 
	 * such as <p> (1=1 and 1=2) <strong>[first condition]</strong> or (1=1 and 1=2)<strong>[second condition]</strong>           
	 */
	public JCondition link(LinkType linkType){
		next=new JCondition(entityClass, linkType,this);
		return next;
	}
	
	public JSingleEntityQuery ready(){
		return singleEntityQuery;
	}
	
	public JCondition startLikes(String property,String value,LinkType... linkType){
		return append(property, value, Ope.START_LIKE,linkType);
	}
	
	public JCondition endLikes(String property,String value,LinkType... linkType){
		return append(property, value, Ope.END_LIKE,linkType);
	}
	
	public JCondition likes(String property,String value,LinkType... linkType){
		return append(property, value, Ope.LIKE,linkType);
	}
	
	public JCondition equals(String property,Object value,LinkType... linkType){
		append(property, value, Ope.EQUAL,linkType);
		return this;
	}
	
	public JCondition notEquals(String property,Object value,LinkType... linkType){
		append(property, value, Ope.NOT_EQUAL,linkType);
		return this;
	}
	
	public JCondition larger(String property,Object value,LinkType... linkType){
		append(property, value, Ope.LARGER,linkType);
		return this;
	}

	public JCondition largerAndEquals(String property,Object value,LinkType... linkType){
		append(property, value, Ope.LARGER_EQUAL,linkType);
		return this;
	}
	
	public JCondition smaller(String property,Object value,LinkType... linkType){
		append(property, value, Ope.SMALLER,linkType);
		return this;
	}
	
	public JCondition smallerAndEqual(String property,Object value,LinkType... linkType){
		append(property, value, Ope.SMALLER_EQUAL,linkType);
		return this;
	}
	
	public JCondition isNull(String property,LinkType... linkType){
		append(property, null, Ope.IS,linkType);
		return this;
	}
	
	public JCondition primary(Object value,LinkType... linkType){
		append("id", value, Ope.EQUAL,linkType);
		return this;
	}
	
	public JCondition in(String property,List value,LinkType... linkType){
		append(property, value, Ope.IN,linkType);
		return this;
	}
	
	/**
	 * linkTypeName+" "+JSingleEntityQueryMeta.ALIAS+"."+property+opeType+" :"+paramString
	 * @author JIAZJ
	 *
	 */
	private class ConditionSlice{
		
		private String linkTypeName;
		
		private String propertyName;
		
		private String columnName;

		private Ope opeType;
		
		private String paramString;
		
	}



	public static class Condition{

		private String column="";

		private Ope ope;

		private Object value;

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getOpe() {
			return ope.getInstruct();
		}

		public void setOpe(Ope ope) {
			this.ope = ope;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public static Condition likes(String column,Object value){
			Condition condition=new Condition();
			condition.column=column;
			condition.ope=Ope.LIKE;
			condition.value="%"+value+"%";
			return condition;
		}

		public static Condition startLikes(String column,Object value){
			Condition condition=new Condition();
			condition.column=column;
			condition.ope=Ope.LIKE;
			condition.value=value+"%";
			return condition;
		}

		public static Condition endLikes(String column,Object value){
			Condition condition=new Condition();
			condition.column=column;
			condition.ope=Ope.LIKE;
			condition.value="%"+value;
			return condition;
		}

		public static Condition equals(String column,Object value){
			Condition condition=new Condition();
			condition.column=column;
			condition.ope=Ope.EQUAL;
			condition.value=value;
			return condition;
		}

		public static Condition notEquals(String column,Object value){
			Condition condition=new Condition();
			condition.column=column;
			condition.ope=Ope.NOT_EQUAL;
			condition.value=value;
			return condition;
		}

		public static Condition likes(Object value){
			Condition condition=new Condition();
			condition.ope=Ope.LIKE;
			condition.value="%"+value+"%";
			return condition;
		}

		public static Condition startLikes(Object value){
			Condition condition=new Condition();
			condition.ope=Ope.LIKE;
			condition.value=value+"%";
			return condition;
		}

		public static Condition endLikes(Object value){
			Condition condition=new Condition();
			condition.ope=Ope.LIKE;
			condition.value="%"+value;
			return condition;
		}

		public static Condition equal(Object value){
			Condition condition=new Condition();
			condition.ope=Ope.EQUAL;
			condition.value=value;
			return condition;
		}

		public static Condition notEquals(Object value){
			Condition condition=new Condition();
			condition.ope=Ope.NOT_EQUAL;
			condition.value=value;
			return condition;
		}


		public static Condition smaller(Object value){
			Condition condition=new Condition();
			condition.ope=Ope.SMALLER;
			condition.value=value;
			return condition;
		}

		public static Condition larger(Object value){
			Condition condition=new Condition();
			condition.ope=Ope.LARGER;
			condition.value=value;
			return condition;
		}


	}
	
	
	
}
