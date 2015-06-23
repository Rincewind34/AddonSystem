package de.securebit.api.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileExtractor {
	
	protected File file;
	protected FileConfiguration cfg;
	protected String fileName;
	
	public FileExtractor(File file, String fileName) {
		this.file = file;
		this.fileName = fileName;
		this.extract();
	}
	
	public void extract() {
		FileConfiguration result = null;
		try {
			ZipFile zip = new ZipFile(this.file);
			ZipInputStream zis = new ZipInputStream(new FileInputStream(this.file));
			ZipEntry currentEntry = null;
			while ((currentEntry = zis.getNextEntry()) != null) {
				if (currentEntry.getName().equalsIgnoreCase(this.fileName)) {
					result = YamlConfiguration.loadConfiguration(new InputStreamReader(zip.getInputStream(currentEntry)));
					zis.close();
					zip.close();
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		this.cfg = result;
	}
	
	public File getJarFile() {
		return this.file;
	}
	
	public boolean isExtracted() {
		return this.cfg != null;
	}

	
}
