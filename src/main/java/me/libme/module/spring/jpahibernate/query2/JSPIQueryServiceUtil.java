package me.libme.module.spring.jpahibernate.query2;

import me.libme.module.spring.jpahibernate.spi.JGenericQueryService;
import me.libme.module.spring.jpahibernate.spi.JHibernateQueryService;

public class JSPIQueryServiceUtil {

	protected static JSPIFoundService spiFoundService=new JSPIFoundService();
	
	protected static JSPIQueryService spiQueryService;
	static {
		JJPASPI jpaSpi=spiFoundService.getSpi();
		switch (jpaSpi) {
		case HIBERNATE:
			spiQueryService=new JHibernateQueryService();
			break;
		case EMPTY:
			spiQueryService=new JGenericQueryService();
			break;
		default:
			spiQueryService=new JGenericQueryService();
		}
	}
	
	public static JSPIQueryService getSPIQueryService(){
		return spiQueryService;
	}
	
}
