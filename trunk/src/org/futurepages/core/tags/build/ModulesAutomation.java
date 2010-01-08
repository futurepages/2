package org.futurepages.core.tags.build;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.futurepages.core.config.Params;
import org.futurepages.util.ClassesUtil;
import org.futurepages.util.FileUtil;

public abstract class ModulesAutomation {

	protected File[] modules;
	private String dirName;
	private List applicationClasses = null;

	public ModulesAutomation(File[] modules, String dirName) {
		super();
		this.modules = modules;
		this.dirName = dirName;
	}

	public File[] getModules() {
		return modules;
	}

	public String getDirName() {
		return dirName;
	}

	protected <S extends Object> List<Class<S>> getApplicationClasses(Class<S> superKlass, Class<? extends Annotation> annotation) {
		if (applicationClasses == null) {
			applicationClasses = new ArrayList<Class<S>>();
			File dirr = new File(Params.get("CLASSES_PATH") + this.getDirName());

			applicationClasses = new ArrayList<Class<S>>(ClassesUtil.getInstance().listClassesFromDirectory(
					dirr, Params.get("CLASSES_PATH"), superKlass, annotation, true));
		}
		return applicationClasses;
	}

	public <S extends Object> Map<String, List<Class<S>>> getModulesDirectoryClasses(Class<S> superKlass, Class<? extends Annotation> annotation) {

		Map<String, List<Class<S>>> modulesClasses = new HashMap<String, List<Class<S>>>();
		List<Class<S>> classes;
		if (this.modules != null) {
			for (File module : this.modules) {

				final File dir = getSubFile(module, getDirName());
				classes = new ArrayList<Class<S>>(ClassesUtil.getInstance().listClassesFromDirectory(
						dir, Params.get("CLASSES_PATH"), superKlass, annotation, true));

				sortClassList(classes);
				modulesClasses.put(module.getName(), classes);
			}
		}
		return modulesClasses;
	}

	private <S extends Object> void sortClassList(List<Class<S>> classes) {
		Collections.sort(classes, new Comparator<Class<S>>() {

			@Override
			public int compare(Class<S> o1, Class<S> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	protected File getSubFile(File file, String name) {
		return FileUtil.getInstance().getSubFile(file, name);
	}
}

