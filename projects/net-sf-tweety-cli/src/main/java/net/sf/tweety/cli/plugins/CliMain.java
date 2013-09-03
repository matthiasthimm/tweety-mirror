package net.sf.tweety.cli.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.cli.TweetyCli;
import net.sf.tweety.cli.plugins.parameter.CommandParameter;
import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.impl.AvalonLogger;

/**
 * skeleton of the new main method of this CLI using plugins.
 * 
 * @author Bastian Wolf
 * 
 */

public class CliMain {

	public static final String TWEETY_CLI_DEFAULT_CONFIG = "tweety_config.xml";

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

	/** the called plugin */
	private static String plugin;
	/** the list of input files */
	private static File[] inputFiles = new File[1];
	/** the output file */
	private static String outputFile = null;
	/** the optional plugin parameters */
	private static CommandParameter[] pluginParams = null;

	/**
	 * This method is meant to load the tweety plugin pathes on startup
	 * 
	 * @returns an object with one or more pluginpathes
	 * @throws ConfigurationException
	 */
	public static Map<String, String> configCLI()
			throws ConfigurationException, FileNotFoundException {
		// TODO : fehlermeldung bei falscher/leerer config-datei und null-werten
		Map<String, String> loadablePlugins = new HashMap<String, String>();

		XMLConfiguration tweetyXmlConfig = new XMLConfiguration();
		File in = new File(TWEETY_CLI_DEFAULT_CONFIG);
		String inPath = in.getAbsolutePath();
		tweetyXmlConfig.setBasePath(inPath.substring(0, inPath.length()
				- TWEETY_CLI_DEFAULT_CONFIG.length() - 1));
		tweetyXmlConfig.load(in);

		// map über "plugins.plugin" mit keys ()
		Iterator<String> it = tweetyXmlConfig.getKeys("plugin");

		ArrayList<String> tmp = (ArrayList<String>) tweetyXmlConfig
				.getProperty(it.next());
		ArrayList<String> tmp2 = (ArrayList<String>) tweetyXmlConfig
				.getProperty(it.next());

		for (int i = 0; i < tmp.size(); i++) {
			loadablePlugins.put(tmp2.get(i), tmp.get(i));
		}

		return loadablePlugins;

	}

	public static void loadPlugin(String plugin) {

	}

	public static void main(String[] args) {

		PluginManager pm = PluginManagerFactory.createPluginManager();
		PluginManagerUtil pmu = new PluginManagerUtil(pm);

		ArrayList<ArrayList<String>> collectedparams = new ArrayList<ArrayList<String>>();
		List<CommandParameter> inParams = new ArrayList<CommandParameter>();

		Map<String, String> availablePlugins = new HashMap<String, String>();
		
		try {
			availablePlugins = configCLI();
		} catch (ConfigurationException e) {
			System.out.println("Something went wrong with your Configuration: ");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("No such file: ");
			e.printStackTrace();
		}

		// TODO implement the main CLI
		// FIXME: while-loops
		for (int i = 0; i < args.length; i++) {

			// The called plugin
			if (args[i].equals(ARG__CALLED_PLUGIN)
					|| args[i].equals(ARG__CALLED_PLUGIN_SHORT)) {
				String calledPlugin = "";
				while ((i+1) < args.length && !args[i+1].startsWith("-")){
					calledPlugin += args[++i];
				}
				plugin = calledPlugin;
			}

			// the input files
			else if (args[i].equals(ARG__INPUT_FILES)
					|| args[i].equals(ARG__INPUT_FILES_SHORT)) {
				ArrayList<String> inFiles = new ArrayList<String>();
				while ((i+1) < args.length && !args[i + 1].startsWith("-")) {
					inFiles.add(args[++i]);
				}
				
				String[] files = new String[inFiles.size()];
				inFiles.toArray(files);

				File[] inf = new File[inFiles.size()];

				for (int k = 0; k < inf.length - 1; k++) {
					inf[k] = new File(files[k]).getAbsoluteFile();
				}

				inputFiles = inf;
			}

			// output file
			else if (args[i].equals(ARG__OUTPUT_FILE)
					|| args[i].equals(ARG__OUTPUT_FILE_SHORT)) {
				outputFile = args[++i];
			}

			// collecting given command parameters
			else {
				ArrayList<String> temp = new ArrayList<String>();
				while ((i+1) < args.length && !args[i + 1].startsWith("-")) {
					temp.add(args[++i]);
				}
				collectedparams.add(temp);
			}
		}
		
		boolean pluginPresent = false;
		for(TweetyPlugin tp : pmu.getPlugins(TweetyPlugin.class)){
			if (tp.getCommand().equalsIgnoreCase(plugin)){
				pluginPresent = true;
			}
		}
		
		if(!pluginPresent){
			if(availablePlugins.containsKey(plugin)) {
			pmu.addPluginsFrom(new File(availablePlugins.get(plugin)).toURI());
			}
		}
		// kein plugin geladen...
		System.out.println(pmu.getPlugins(TweetyPlugin.class));
		
		for(TweetyPlugin tp : pmu.getPlugins(TweetyPlugin.class)){
				if(tp.getCommand().equalsIgnoreCase(plugin)){
				// each input parameter is checked against the called plugin
				// whether it is valid
				for (ArrayList<String> inparam : collectedparams) {

					for (CommandParameter tpcp : tp.getParameters() ) {

						if (inparam.get(0).startsWith("-")) {

							if (inparam.get(0).equalsIgnoreCase(
									tpcp.getIdentifier())) {
								try {
									CommandParameter temp = tpcp
											.instantiate(inparam.remove(0)
													.toString());
									if (!temp.equals(null)) {
										inParams.add(temp);
									} else {
										
									}
								} catch (CloneNotSupportedException e) {

									e.printStackTrace();
								} catch (NullPointerException e) {
									System.out
											.println("Your input parameter arguments are somehow wrong:");
									e.printStackTrace();
								}
							}
						} else {
							String s = "-";
							s += inparam.get(0);
							if (s.equalsIgnoreCase(tpcp.getIdentifier())) {
								try {
									CommandParameter temp = tpcp
											.instantiate(inparam.remove(0)
													.toString());
									if (!temp.equals(null)) {
										inParams.add(temp);
									} else {
										// parameter error
									}
								} catch (CloneNotSupportedException e) {

									e.printStackTrace();
								} catch (NullPointerException e) {
									System.out
											.println("Your input parameter arguments are somehow wrong:");
									e.printStackTrace();
								}
							}
						}

					}
				}
			}

		}

	}
}
