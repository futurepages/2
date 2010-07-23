package org.futurepages.exceptions;

public class ModuleWithoutBeanDirException extends Exception {

	public ModuleWithoutBeanDirException(String moduleName) {
		super("M�dulo '"+moduleName+"' n�o possui diret�rio de beans");
	}
}
