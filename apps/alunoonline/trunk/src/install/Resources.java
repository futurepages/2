package install;

import org.futurepages.core.config.Params;
import org.futurepages.core.install.Installer;
import org.futurepages.enums.PathTypeEnum;
import org.futurepages.util.StringUtils;

import java.io.File;

/**
 * Instalador de Recursos Iniciais
 * @author leandro
 */
public class Resources extends Installer {

    @Override
    public void execute() {
        makeUploadsPaths();
    }

    private void makeUploadsPaths() {
        File uploadsDir = new File(getUploadsPath(PathTypeEnum.REAL));
        if(!uploadsDir.exists()){
            uploadsDir.mkdirs();
        }
    }

	public static String getUploadsPath(PathTypeEnum pathType){
		return Params.get(StringUtils.concat("UPLOADS_", pathType.name(), "_PATH"));
	}
}