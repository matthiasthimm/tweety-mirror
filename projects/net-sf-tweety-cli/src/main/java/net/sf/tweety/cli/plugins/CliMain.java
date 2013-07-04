package net.sf.tweety.cli.plugins;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

/**
 * skeleton of the new main method of this CLI using plugins.
 * 
 * @author Bastian Wolf
 *
 */

public class CliMain {
	
	
	/** The argument name for the called plugin */
	public static final String ARG__CALLED_PLUGIN = "--plugin";
	/** The argument name for the called plugin (short) */
	public static final String ARG__CALLED_PLUGIN_SHORT = "-p";
	/** The argument name for the input file(s) */
	public static final String ARG__INPUT_FILES = "--input";
	/** The argument name for the input file(s) (short) */
	public static final String ARG__INPUT_FILES_SHORT = "-i";
	/** The argument name for the output file */
	public static final String ARG__OUTPUT_FILE = "--output";
	/** The argument name for the output file (short) */
	public static final String ARG__OUTPUT_FILE_SHORT = "-o";
	/** the argument name for optional plugin parameter */
	public static final String ARG__PLUGIN_PARAMETER = "--parameter";
	/** the argument name for optional plugin parameter (short) */
	public static final String ARG__PLUGIN_PARAMETER_SHORT = "-pa";
	/** The argument name for advanced options */
	public static final String ARG__OPTION = "--option";
	/** The argument name for advanced options (short) */
	public static final String ARG__OPTION_SHORT = "-op";
	
	
	/** the called plugin */
	private static String plugin;
	/** the list of input files */
	private static String[] inputFiles = new String[1];
	/** the output file */
	private static String outputFile = null;
	/** the operation to be executed */
	private static ComputeOperation operation = null;
	/** the optional plugin parameters */
	private static PluginParameter[] pluginParams = null;
	
	
	
	public static void CLImain(String[] args) {
		
		PluginManager pm = PluginManagerFactory.createPluginManager();
		PluginManagerUtil pmu = new PluginManagerUtil(pm);
		
		//TODO implement the main CLI
		
		for (int i = 0; i< args.length ; i++){
			
			// The called plugin
			if(args[i].equals(ARG__CALLED_PLUGIN) || args[i].equals(ARG__CALLED_PLUGIN_SHORT)){
				String calledPlugin = "";
				while(!args[i+1].startsWith("-"))
					calledPlugin += args[++i];
				plugin = calledPlugin;
			}
			
			// the input files
			else if(args[i].equals(ARG__INPUT_FILES) || args[i].equals(ARG__INPUT_FILES_SHORT))
			{
				List<String> inFiles = new ArrayList<String>();
				while (!args[i+1].startsWith("-"))
					inFiles.add(args[++i]);
				inputFiles = inFiles.toArray(inputFiles);
			}
			
			// the compute operation
			else if(args[i].equals(ARG__OPTION) || args[i].equals(ARG__OPTION_SHORT))
			{
				// TODO implement compute operation first
			}
			
			// output file
			else if(args[i].equals(ARG__OUTPUT_FILE) || args[i].equals(ARG__OUTPUT_FILE_SHORT))
			{
				outputFile = args[++i];
			}
			
			// optional plugin parameters
			else if(args[i].equals(ARG__PLUGIN_PARAMETER) || args[i].equals(ARG__PLUGIN_PARAMETER_SHORT))
			{
				List<String> params = new ArrayList<String>();
				while(!args[i+1].startsWith("-")){
					params.add(args[++i]);
				}
			}
		}
		// TODO: passing now separated input arguments to the called plugin
		
		// add Plugins
		pmu.addPluginsFrom(new File("plugins/").toURI());
		// get Plugin referenced via the string given as parameter
		
		// passing input files, compute operation and optional parameters to this plugin
		
		// handle returned output if necessary (CLI-Output)
	}	
}
