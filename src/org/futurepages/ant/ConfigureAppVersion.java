package org.futurepages.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.futurepages.util.DateUtil;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

public class ConfigureAppVersion extends Task {

	private String baseDir;
	private String env;
	private String build;

	@Override
	public void execute() throws BuildException {
		try {
			if(!Is.empty(env)){
				File filesEnvFrom = new File(baseDir+"/test/env/"+env);
				File filesEnvTo = new File(baseDir+"/web");
				FileUtil.copy(filesEnvFrom.getAbsolutePath() , filesEnvTo.getAbsolutePath());
			}

			HashMap<String,String> contentMap = new HashMap();
			String releaseContent = "<param name=\"RELEASE\" value=\""+DateUtil.format(Calendar.getInstance(), "yyyy-MM-dd_HH_mm_ss")+"\" />";
			if(!Is.empty(build)){
				contentMap.put("<!--RELEASE-->","<param name=\"APP_BUILD_ID\" value=\""+ build+"\" />\n    "+releaseContent);
			}else{
				contentMap.put("<!--RELEASE-->",releaseContent);
			}
			String filePath = baseDir +"/web/WEB-INF/classes/conf/app-params.xml";
			FileUtil.putKeyValue(contentMap, filePath, filePath);


		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public void setBuild(String build) {
		this.build = build;
	}
}