package de.securebit.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

public class InfoLayout {
	
	private List<String> lines;
	
	private String clPri;
	private String clSec;
	private String clPos;
	private String clNeg;
	private String clHiLi;
	
	private String prefix;
	
	public InfoLayout(String name) {
		this.lines = new ArrayList<String>();
		this.clPri = "§8";
		this.clSec = "§3";
		this.clPos = "§f";
		this.clNeg = "§c";
		this.clHiLi = "§b";
		this.prefix = this.clPri + "[" + this.clSec + name + this.clPri + "] §r";
	}
	
	public List<String> getLines() {
		return lines;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void newCategory(String name) {
		this.newBarrier();
		this.lines.add(this.prefix + this.clHiLi + name);
	}
	
	public void newBarrier() {
		this.lines.add(this.prefix + this.clPri + "=======================================================");
	}
	
	public void addInfo(String name, String info, boolean positiv) {
		this.lines.add(this.prefix + this.clSec + name + ": " + ((positiv) ? this.clPos : this.clNeg) + info);
	}
	
	public void addInfo(String name, boolean element) {
		this.lines.add(this.prefix + this.clSec + name + ": " + ((element) ? this.clPos : this.clNeg) + element);
	}
	
	public void addElement(String element, boolean positiv) {
		this.lines.add(this.prefix + ((positiv) ? this.clPos : this.clNeg) + element);
	}
	
	public void addComent(String element, boolean highlight) {
		this.lines.add(this.prefix + ((highlight) ? this.clHiLi : this.clSec) + element);
	}
	
	public void send(CommandSender sender) {
		for (String line : this.lines) {
			sender.sendMessage(line);
		}
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
}
