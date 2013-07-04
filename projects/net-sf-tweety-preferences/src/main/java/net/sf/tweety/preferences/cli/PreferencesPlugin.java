package net.sf.tweety.preferences.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.cli.plugins.ComputeOperation;
import net.sf.tweety.cli.plugins.PluginModel;
import net.sf.tweety.cli.plugins.PluginOutput;
import net.sf.tweety.cli.plugins.PluginParameter;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.io.POParser;
import net.sf.tweety.preferences.io.ParseException;
import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * The CLI-Plugin for the Preferences-Package
 * 
 * @author Bastian Wolf
 * 
 */
@PluginImplementation
public class PreferencesPlugin extends PluginModel implements Plugin {
	
	// TODO:
	// Initiale PluginParameter (Parameter, Datenstruktur, initial geladen)

	@Override
	public String getCommand() {
		return "preferences";
	}

	@Override
	public PluginOutput execute(File[] input, ComputeOperation cop,
			PluginParameter[] params) {
		// File-Handler
		// Parsing,...
		Set<PreferenceOrder<String>> poset = new HashSet<PreferenceOrder<String>>();
		for (int i = 0; i < input.length; i++) {
			String filename = input[i].getAbsoluteFile().toString();
			if (filename.endsWith(".po")) {
				try {
					PreferenceOrder<String> po = POParser.parse(filename);
					poset.add(po);
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
			}
		}
		// Cmpute-Operation-Handler
		//
		
		

		// Parameter-Handler
		//

		return null; //
	}
	
	@Override
	public PluginParameter[] getParameters() {
		
		return null;
	}
}
