package net.sf.tweety.cli.plugins;

import java.io.File;

import java.util.ArrayList;
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
// @PluginImplementation
public abstract class AbstractTweetyPlugin implements TweetyPlugin {
	/**
	 * valid parameters for this plugin
	 */
	protected List<CommandParameter> parameters = new ArrayList<CommandParameter>();

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
	 * 
	 * @param cmdParameter
	 *            the command parameter to be added
	 */
	public void addParameter(CommandParameter cmdParameter) {
		parameters.add(cmdParameter);
	}

	/**
	 * returns all possible parameters
	 */
	public List<CommandParameter> getParameters() {
		return parameters;
	}

	/**
	 * checks, whether each command parameter given with the plugin call is
	 * valid within is this plugin
	 * 
	 * @param s
	 * @return
	 * @throws CloneNotSupportedException
	 */
	public CommandParameter validateParameter(String s)
			throws CloneNotSupportedException {
		for (CommandParameter cmp : parameters) {
			if (cmp.isValid(s)) {
				CommandParameter cpa = cmp.instantiate(s);
				return cpa;
			}
		}
		return null;
	}

	public ArrayList<CommandParameter> validateParameter(ArrayList<String> s)
			throws CloneNotSupportedException {
		ArrayList<CommandParameter> alcp = new ArrayList<CommandParameter>(
				s.size());
		for (String value : s) {
			CommandParameter cp = validateParameter(value);
			if(!(cp==null)){
			alcp.add(cp);
			}
		}
		return alcp;
	}

}
