package net.sf.tweety.cli.plugins;

import java.io.File;

/**
 * This class provides the base for each plugin's functionality
 * 
 * @author Bastian Wolf
 *
 */

public abstract class PluginModel {
	/**
	 * returns the keyword used in the cli to call this plugin
	 * @return the keyword used in the cli to call this plugin
	 */
	public abstract String getCommand();	
	
	/**
	 * passes by the arguments given with the call to the called plugin
	 * 
	 * @param input files to be used within the plugin
	 * @param cop compute operations performed on the given input files
	 * @param params parameter handled in the plugin (e.g. desired output file, iterations...)
	 * @return the output resulted after the execution
	 */
	public abstract PluginOutput execute(File[] input, ComputeOperation cop, PluginParameter[] params); 
	
	/**
	 * returns parameters allowed with plugin calls
	 * @return parameters allowed with plugin calls
	 */
	public abstract PluginParameter[] getParameters(); 

}
