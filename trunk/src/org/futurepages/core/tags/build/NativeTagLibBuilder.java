package org.futurepages.core.tags.build;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.futurepages.annotations.Tag;
import org.futurepages.util.ClassesUtil;
import org.futurepages.util.FileUtil;
//import org.junit.Test;

public class NativeTagLibBuilder {
	
	private final String NATIVE_TAGS_REPLACE_CONSTANT = "<!-- ${NATIVE_TAGS_REPLACE} -->";
	
//	@Test
	public void generateTagLib() throws Exception{
		
		String absolutePath = FileUtil.classRealPath(this.getClass()).substring(1);
		String packagePath = this.getClass().getPackage().getName().replace(".","\\").replace("\\","/");
		String pathname = packagePath.replaceAll("/core/tags/build", "/tags");
		String srcPath = absolutePath.replace(packagePath, "");
		File tagsDir = new File(srcPath+pathname);
		String destiny = (srcPath+TagLibBuilder.BASE_TAGLIB_URL).replace("///", "/");
		destiny = destiny.replace("bin", "src");
		String templatePath = templatePath();
		String content = getDeclaration(tagsDir, srcPath);
		Map<String, String> map = new HashMap<String, String>();
		map.put(NATIVE_TAGS_REPLACE_CONSTANT, content);
		FileUtil.putKeyValue(map, templatePath,  destiny);
		System.out.println(FileUtil.getStringContent(destiny));
	}

	private String getDeclaration(File tagsDir, String srcPath) {
		Collection<Class<Object>> classes = ClassesUtil.getInstance().listClassesFromDirectory(tagsDir, srcPath, null, Tag.class, true);
		String dec  = TagLibBuilder.tagsDeclaration(classes).toString();
		return dec;
	}

	private String templatePath() throws UnsupportedEncodingException {
		return FileUtil.classRealPath(this.getClass())+"res/tagLibTemplate.tld".replace("//","/");
	}
}
