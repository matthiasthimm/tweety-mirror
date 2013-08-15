package net.sf.tweety.cli.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.cli.plugins.parameter.CommandParameter;
import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

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

	public static Object configCLI() throws ConfigurationException {

		XMLConfiguration tweetyXmlConfig = new XMLConfiguration(
				TWEETY_CLI_DEFAULT_CONFIG);

		Object path = tweetyXmlConfig.getProperty("plugins.plugin.path");

		return path;

	}

	public static void main(String[] args) {

		PluginManager pm = PluginManagerFactory.createPluginManager();
		PluginManagerUtil pmu = new PluginManagerUtil(pm);

		ArrayList<ArrayList<String>> collectedparams = new ArrayList<ArrayList<String>>();
		List<CommandParameter> inParams = new ArrayList<CommandParameter>();

		try {
			Object path = configCLI();
			if (path instanceof Collection) {
				ArrayList<String> pluginpathes = (ArrayList<String>) path;
				for (String s : pluginpathes) {
					pm.addPluginsFrom(new File(s).toURI());
				}
			}
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Collection<TweetyPlugin> plugins = new LinkedList<TweetyPlugin>(
				pmu.getPlugins(TweetyPlugin.class));

		// TODO implement the main CLI

		for (int i = 0; i < args.length; i++) {

			// The called plugin
			if (args[i].equals(ARG__CALLED_PLUGIN)
					|| args[i].equals(ARG__CALLED_PLUGIN_SHORT)) {
				String calledPlugin = "";
				while (!args[i + 1].startsWith("-"))
					calledPlugin += args[++i];
				plugin = calledPlugin;
			}

			// the input files
			else if (args[i].equals(ARG__INPUT_FILES)
					|| args[i].equals(ARG__INPUT_FILES_SHORT)) {
				String inFiles = new String();
				while (!args[i + 1].startsWith("-"))
					inFiles += args[i];
				String[] files = inFiles
						.split("[[A-Z,a-z,0-9]?([.][a-z,0-9]?]?){0,1}");

				File[] inf = new File[files.length];

				for (int k = 0; k < inf.length - 1; k++) {
					inf[k] = new File(files[k]).getAbsoluteFile();
				}

				inputFiles = inf;

				// deprecated
				// List<File> in = new ArrayList<File>();
				//
				// for(String s : files){
				// File f = new File(s).getAbsoluteFile();
				// in.add(f);
				// }
				// File[] inf = new File[in.size()];
				// for(int k = 0; k<in.size()-1;k++){
				// inf[k] = in.get(k);
				// }
			}

			// output file
			else if (args[i].equals(ARG__OUTPUT_FILE)
					|| args[i].equals(ARG__OUTPUT_FILE_SHORT)) {
				outputFile = args[++i];
			}

			// collecting given command parameters
			else {
				ArrayList<String> temp = new ArrayList<String>();
				while (!args[i + 1].startsWith("-")) {
					temp.add(args[++i]);
				}
				collectedparams.add(temp);
			}
		}
		// checks if the called plugin is among all plugins
		for (TweetyPlugin tp : pmu.getPlugins(TweetyPlugin.class)) {
			if (tp.getCommand().equalsIgnoreCase(plugin)) {

				// each input parameter is checked against the called plugin
				// whether it is valid
				for (ArrayList<String> inparam : collectedparams) {

					for (CommandParameter tpcp : tp.getParameters()) {

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
