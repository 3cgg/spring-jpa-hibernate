/**
 * 
 */
package me.libme.module.spring.jpabean;

import me.libme.kernel._c._m.JModel;

/**
 * @author J
 *
 */
public interface SessionUser extends JModel {

	String getId();

	void setId(String id);

	String getUserName();

	void setUserName(String userName);

	String getNatureName();

	void setNatureName(String natureName);
	
}
