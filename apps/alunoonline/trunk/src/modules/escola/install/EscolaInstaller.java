package modules.escola.install;

import install.Resources;
import org.futurepages.core.install.Installer;
import org.futurepages.enums.PathTypeEnum;

import java.io.File;

/*
 * Instala as turmas e depois os alunos.
 */
public class EscolaInstaller extends Installer{

    @Override
    public void execute() throws Exception {

	    File uploadsDir = new File(Resources.getUploadsPath(PathTypeEnum.REAL)+"/alunos");
	    if(!uploadsDir.exists()){
		    uploadsDir.mkdirs();
	    }
    }
}