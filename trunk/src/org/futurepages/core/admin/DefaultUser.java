package org.futurepages.core.admin;

import java.util.Collection;

/**
 * Interface de usu�rio padr�o de uma aplica��o futurepages.
 *
 * Os usu�rios de uma aplica��o futurepages devem implement�-la para aproveitarem-se
 * dos recursos que o futurepages prov�.
 * 
 * @author leandro
 */
public interface DefaultUser {
	
	public String getLogin();
	
	public String getFullName();

	public String getPassword();
	
	public Collection getRoles();

	public Collection getModules();
	
	public boolean hasModule(String moduleId);

	public boolean hasModules();
	
	public boolean hasRole(String roleId);

	public boolean hasRole(DefaultRole role);
	
	public String getInfo();

	public void setEmail(String email);

	public String getEmail();

}