package me.zero.clarinet.command;

import me.zero.clarinet.util.Helper;

public abstract class AbstractCommand implements Helper {

    private String[] names;
    private String description;
    private CommandUsage[] usage;

    public AbstractCommand() {
        names = names();
        description = description();
        usage = usage();
    }

    public final String[] getNames() {
        return this.names;
    }

    public final String getDescription() {
        return this.description;
    }

    public final CommandUsage[] getUsage() {
        return this.usage;
    }

	/**
	 *
	 * @param message
	 * @param args
	 * @return true if executed properly, false if an error occured
	 */
	public abstract boolean run(String message, String[] args);
	
	/**
	 * @return Array of possible command headers
	 */
	protected abstract String[] names();
	
	/**
	 * @return Description of command
	 */
    protected abstract String description();
	
	/**
	 * @return Array of possible usages for command
	 */
    protected abstract CommandUsage[] usage();

    /**
     * Object used to store usage data
     */
    public static class CommandUsage {

        public final String usage, description;

        public CommandUsage(String usage, String description) {
            this.usage = usage;
            this.description = description;
        }
    }
}
