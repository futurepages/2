package org.futurepages.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.futurepages.util.Is;
import org.futurepages.util.SVNUtils;

import java.io.IOException;

public class DeployAntTask extends Task {

	private String source;
	private String target;
	
	@Override
	public void execute() throws BuildException {
		System.out.println("DeployAntTask.execute()");
		System.out.println("source: "+source );
		System.out.println("target: "+target);
	
		try {
			String baseDir = this.getProject().getBaseDir().getAbsolutePath();
			if(Is.empty(source)){
				source = baseDir+"/web";
				System.out.println("default source: "+source);
			}
			if(Is.empty(target)){
				System.out.println("default target: "+target);
				target = baseDir+"/_deployed";
			}
			SVNUtils.cleanCopy(source, target);
		} catch (IOException e) {
			System.out.println("Falha ao copiar.");
			e.printStackTrace();
		}
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
}
