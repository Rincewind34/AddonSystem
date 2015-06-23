package de.securebit.api.system.shell;

import java.util.List;

public interface ShellCommand {
	
	String getName();
	
	List<Class<?>> getParameters();
	
	void execute(Object[] parms);
	
}
