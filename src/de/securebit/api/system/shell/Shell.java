package de.securebit.api.system.shell;

public interface Shell {

	public static final String LOG_ENDING = ".log";
	public static final String LOG_PATH = "plugins/Core/logs";

	public abstract void registerCommand(ShellCommand cmd);

	public abstract void execCmd(String cmdName, Object... params);

	public abstract void log(String sender, String clazz, String log);

	public abstract void createLog(String path, String name);

	public abstract String createLogName();

	public abstract void clearLog();

}