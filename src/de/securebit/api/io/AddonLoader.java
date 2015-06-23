package de.securebit.api.io;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import de.securebit.api.Addon;
import de.securebit.api.AddonType;
import de.securebit.api.Core;
import de.securebit.api.exceptions.InvalidAddonYMLException;
import de.securebit.api.exceptions.InvalidConfigYMLException;
import de.securebit.plugin.CraftCore;

public final class AddonLoader {
	
	public final static File ADDON_DIR;

	static {
		ADDON_DIR = new File("plugins" + File.separator + "Core" + File.separator + "addons");
		if (!ADDON_DIR.exists()) {
			ADDON_DIR.mkdirs();
		}
	}
	
	private List<Addon> addons;
	
	private List<String> addonsToLoad;
	private List<String> loaded;
	private Map<String, String> configs;
	
	public AddonLoader(List<String> addonsToLoad, Map<String, String> configs) {
		this.addons = new ArrayList<Addon>();
		this.loaded = new ArrayList<String>();
		this.addonsToLoad = addonsToLoad;
		this.configs = configs;
	}
	
	public void loadAddons() {
		Core.getShell().log("Core", "AddonLoader", "Addon are loaded!");
		
		for (File addonFile : AddonLoader.getFiles()) {
			if (!this.addonsToLoad.contains(addonFile.getName())) continue;
			try {
				AddonYMLExtractor ex = new AddonYMLExtractor(addonFile);
				
				String name = ex.getAddonName();
				if (this.loaded.contains(name)) throw new InvalidAddonYMLException("The name '" + name + "' is already used by an addon!");
				else this.loaded.add(name);
				
				System.out.println("[Core] Loading addon " + name + "!");
				Core.getShell().log("Core", "AddonLoader", "Loading addon '" + name + "'!");
				
				URL url = addonFile.toURI().toURL();
				URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { url }, Addon.class.getClassLoader());
				classLoader.setDefaultAssertionStatus(true);
				
				for (String targetClazz : this.loadJar(addonFile.getPath())) classLoader.loadClass(targetClazz);
				
				Class<?> clazz = classLoader.loadClass(ex.getMain());
				
				if (clazz.getSuperclass() == Addon.class) {
					Addon addon = (Addon) clazz.newInstance();
					
					String description = ex.getDescription();
					String version = ex.getAddonVersion();
					List<String> authors = ex.getAuthors();
					AddonType type = ex.getAddonType();
					
					AddonConfig config = new AddonConfig(name, this.configs.get(addonFile.getName()));
					
					if (!config.check()) throw new InvalidConfigYMLException("The config '" + config + "' is for the addon '" + name + "' invalid!");

					config.load();
					
					this.initAddon(addon, name, description, version, authors, type, config, addonFile);
					
					this.addons.add(addon);
					Core.getShell().log("Core", "AddonLoader", "Addon '" + name + "' was loaded!");
				} else {
					throw new InvalidAddonYMLException("The class " + ex.getMain() + " does not extend from 'Addon'!");
				}
					
				
				classLoader.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				Core.getShell().log("Core", "SetupLoader", "Exception: " + ex.getMessage());
				CraftCore.output("[AddonLoader]", "Skiping addon '" + addonFile.getPath() + "'");
				continue;
			}
		}
		
		Core.getShell().log("Core", "AddonLoader", "Addons were loaded!");
	}
	
	public List<String> getAddonsToLoad() {
		return addonsToLoad;
	}
	
	public Map<String, String> getConfigs() {
		return configs;
	}
	
	public List<Addon> getAddons() {
		return this.addons;
	}
	
	@SuppressWarnings({ "resource" })
	private List<String> loadJar(String path) {
		List<String> classes = new ArrayList<String>();
		
		try {
			JarInputStream crunchifyJarFile = new JarInputStream(new FileInputStream(path));
			JarEntry crunchifyJar;
 
			while (true) {
				crunchifyJar = crunchifyJarFile.getNextJarEntry();
				if (crunchifyJar == null) {
					break;
				}
				
				if ((crunchifyJar.getName().endsWith(".class"))) {
					String className = crunchifyJar.getName().replaceAll("/", "\\.");
					String clazz = className.substring(0, className.lastIndexOf('.'));
					classes.add(clazz);
				}
			}
			
		} catch (Exception e) {
			return null;
		}
		
		return classes;
	}
	
	private void initAddon(Object object, String name, String description, String version, List<String> authors, AddonType type, AddonConfig config, File file) {
		try {
			Method method = Addon.class.getDeclaredMethod("init", new Class[] { String.class, String.class, String.class, AddonType.class,
					List.class, AddonConfig.class, File.class });
			method.setAccessible(true);
			method.invoke(object, new Object[] {name, description, version, type, authors, config, file});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static List<File> getFiles() {
		List<File> files = new ArrayList<File>();
		
		for (File file : ADDON_DIR.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".jar")) {
				try {
					FileInputStream fis = new FileInputStream(file);
					String s = new String(new byte[] { (byte) fis.read(), (byte) fis.read() });
					fis.close();
					if (!s.equals("PK")) {
						continue;
					}
					
					if (!new AddonYMLExtractor(file).isExtracted()) throw new Exception("The file '" + file.getPath() + "' does not contains an 'addon.yml'!");
					if (!new ConfigYMLExtractor(file).isExtracted()) throw new Exception("The file '" + file.getPath() + "' does not contains an 'config.yml'!");
				} catch (Exception ex) {
					ex.printStackTrace();
					continue;
				}
				
				files.add(file);
			}
		}
		
		return files;
	}
	
}
