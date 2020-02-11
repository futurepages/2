package org.futurepages.core.migration;

public interface VersionedDataModel {

	String getVersion();
	void addVersion(String newVersion, String oldVersion, String log, int success, int fail);
	void registerNoChanges(String oldVersion, String logTxt, int fail);
	boolean installed();
}