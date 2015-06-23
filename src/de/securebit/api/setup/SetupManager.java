
package de.securebit.api.setup;

import java.util.HashMap;
import java.util.Map;

import de.securebit.api.exceptions.SetupException;

public class SetupManager {
	
	private static Map<String, Setup> setups = new HashMap<String, Setup>();
	
	public static Setup getSetup(String name) {
		return SetupManager.setups.get(name);
	}
	
	public static Map<String, Setup> getSetups() {
		return SetupManager.setups;
	}
	
	public static void initSetup(Setup setup) {
		if (SetupManager.containsSetup(setup.getName())) throw new SetupException("The setup '" + setup.getName() + "' already exists!");
		else SetupManager.setups.put(setup.getName(), setup);
	}
	
	public static void createSetup(String name, String path) {
		if (!SetupManager.containsSetup(name)) throw new SetupException("The setup '" + name + "' does not exists!");
		else {
			SetupManager.setups.get(name).create(path);
			SetupManager.setups.remove(name);
		}
	}
	
	public static void deleteSetup(String name) {
		if (!SetupManager.containsSetup(name)) throw new SetupException("The setup '" + name + "' does not exists!");
		else SetupManager.setups.remove(name);
	}
	
	public static boolean containsSetup(String name) {
		return SetupManager.setups.containsKey(name);
	}
	
}
