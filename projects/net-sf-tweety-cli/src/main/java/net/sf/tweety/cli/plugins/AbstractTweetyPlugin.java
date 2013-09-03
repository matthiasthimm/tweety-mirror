package net.sf.tweety.cli.plugins;

import java.io.File;

import java.util.List;

import net.sf.tweety.cli.plugins.parameter.CommandParameter;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * This abstract class implements the TweetyPlugin interface and provides a base
 * for plugin implementations in each project
 * 
 * @author Bastian Wolf
 * 
 */
//@PluginImplementation
public abstract class AbstractTweetyPlugin implements TweetyPlugin {
	/**
	 * valid parameters for this plugin
	 */
	protected List<CommandParameter> parameters;

	/**
	 * returns the command this plugin is called with
	 */
	@Override
	public abstract String getCommand();

	/**
	 * executes the given input
	 */
	@Override
	public abstract PluginOutput execute(File[] input, CommandParameter[] params);

	/**
	 * adds new command parameter to this plugin
	 * @param cmdParameter the command parameter to be added
	 */
	protected void addParameter(CommandParameter cmdParameter) {
		parameters.add(cmdParameter);
	}

	/**
	 * returns all possible parameters
	 */
	public List<CommandParameter> getParameters() {
		return parameters;
	}

	/**
	 * checks, whether each command parameter given with the plugin call is valid within is this plugin
	 * @param s
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public CommandParameter validateParameter(String s)
			throws CloneNotSupportedException {

		String commandpart = "";
		String valuepart = "";

		String[] split = s.split(" ");
		// only one value parameter is currently estimated within each string
		for (int j = 0; j < split.length; j++) {
			if (split[j].contains("-")) {
				commandpart = split[j];
			} else {
				valuepart = split[j];
			}
		}

		for (CommandParameter cp : parameters) {
			if (commandpart.equalsIgnoreCase(cp.getIdentifier())
					&& cp.isValid(valuepart)) {
				CommandParameter cparam = cp.instantiate(valuepart);
				return cparam;
			} else if (!cp.isValid(s)) {
				throw new IllegalArgumentException();
			}
		}
		return null;
	}

}
