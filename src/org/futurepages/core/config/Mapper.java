package org.futurepages.core.config;

import java.io.File;

public abstract class Mapper {
    
	/**
     * Verifica a existência de uma pasta chamada "database" dentro da pasta referente ao
     * módulo passado como entrada. Retorna verdadeiro caso exista. 
     * @param module módulo que se deseja verificar a existência do banco de dados.
     * @return true se existir a pasta "database" dentro do diretório do módulo
     */
    public static boolean moduleHasDB(File module) {
        File bdDir = new File(module.getAbsolutePath() + "/" + Params.get("DATABASE_DIR_NAME"));
        return bdDir.exists();
    }
}
