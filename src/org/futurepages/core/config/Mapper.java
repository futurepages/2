package org.futurepages.core.config;

import java.io.File;

public abstract class Mapper {
    
	protected static final String BASE_HIBERANTE_XML_FILE = "hibernate.cfg.xml";
	protected static final String BASE_HIBERNATE_PROPERTIES_FILE = "hibernate.properties";
	
	/**
     * Verifica a exist�ncia de uma pasta chamada "database" dentro da pasta referente ao
     * m�dulo passado como entrada. Retorna verdadeiro caso exista. 
     * @param module m�dulo que se deseja verificar a exist�ncia do banco de dados.
     * @return true se existir a pasta "database" dentro do diret�rio do m�dulo
     */
    public static boolean moduleHasDB(File module) {
        File bdDir = new File(module.getAbsolutePath() + "/" + Params.get("DATABASE_DIR_NAME"));
        return bdDir.exists();
    }
}
