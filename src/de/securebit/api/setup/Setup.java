package de.securebit.api.setup;

public interface Setup {
	
	String ENDING = ".setup";
	
	
	void linkConfig(String fileName, String cfgName);
	
	void linkConnection(String conName, String fileName, String fileSender);
	
	void closeConnection(String conName, String fileName);
	
	void registerAddon(String fileName);
	
	void create(String path);
	
	boolean isCreatable();
	
	boolean checkConnections();
	
	boolean checkConfigs();
	
	String getName();
	
}
