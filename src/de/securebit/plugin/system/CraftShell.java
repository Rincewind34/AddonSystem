package de.securebit.plugin.system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.securebit.api.exceptions.RegisterShellCommandException;
import de.securebit.api.exceptions.ShellCommandProcessException;
import de.securebit.api.system.shell.Shell;
import de.securebit.api.system.shell.ShellCommand;
import de.securebit.plugin.CraftCore;

public class CraftShell implements Shell {
	
	private Map<String, ShellCommand> commands;
	
	private List<String> log;
	
	public CraftShell() {
		this.log = new ArrayList<String>();
		this.commands = new HashMap<String, ShellCommand>();
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.system.shell.Shell#registerCommand(de.rincewind.api.system.shell.ShellCommand)
	 */
	@Override
	public void registerCommand(ShellCommand cmd) {
		if(commands.containsKey(cmd.getName())) throw new RegisterShellCommandException();
		else this.commands.put(cmd.getName(), cmd);
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.system.shell.Shell#execCmd(java.lang.String, java.lang.Object)
	 */
	@Override
	public void execCmd(String cmdName, Object... params) {
		if(!this.commands.containsKey(cmdName)) throw new ShellCommandProcessException("The shellcommand " + cmdName + " does not exist!");
		else {
			ShellCommand cmd = this.commands.get(cmdName);
			
			if(cmd.getParameters().size() != params.length)
				throw new ShellCommandProcessException("The shellcommand " + cmdName + " does only perform a parameterslength of " +
						cmd.getParameters().size());
			
			for(int i = 0; i < params.length; i++) {
				if(params[i] != cmd.getParameters().get(i))
					throw new ShellCommandProcessException("The shellcommand " + cmdName + " does only perform the type '" +
							cmd.getParameters().get(i).getName() + "' as the '" + i + "' parameter!");
			}
			
			cmd.execute(params);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.system.shell.Shell#log(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void log(String sender, String clazz, String log) {
		this.log.add("[" + sender + "] [" + clazz + "]: " + log);
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.system.shell.Shell#createLog(java.lang.String, java.lang.String)
	 */
	@Override
	public void createLog(String path, String name) {
		File file = new File(path + File.separator + name + Shell.LOG_ENDING);
		
		try {
			if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
			if(!file.exists()) file.createNewFile();
			
			PrintWriter pWriter = null;
	        
	        pWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
	        
	        for(String line : this.log) {
	          	pWriter.println(line);
	        }
	        
	        if (pWriter != null){
	            pWriter.flush();
	            pWriter.close();
	        }
		} catch (IOException ex) {
			ex.printStackTrace();
			CraftCore.output("", "Could not create the log '" + path + File.separator + name + "'!");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.system.shell.Shell#createLogName()
	 */
	@Override
	@SuppressWarnings("deprecation")
	public String createLogName() {
		Time time = new Time(System.currentTimeMillis());
		String result = time.toLocaleString().split(" ")[0] + "|";
		result = result + time.toLocaleString().split(" ")[1];
		return result;
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.system.shell.Shell#clearLog()
	 */
	@Override
	public void clearLog() {
		this.log.clear();
	}
	
}
