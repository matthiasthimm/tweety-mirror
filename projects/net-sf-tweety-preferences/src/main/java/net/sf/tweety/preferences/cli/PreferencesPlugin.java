package net.sf.tweety.preferences.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.tweety.cli.plugins.AbstractTweetyPlugin;
import net.sf.tweety.cli.plugins.PluginOutput;
import net.sf.tweety.cli.plugins.parameter.CommandParameter;
import net.sf.tweety.cli.plugins.parameter.SelectionCommandParameter;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.io.POParser;
import net.sf.tweety.preferences.io.ParseException;
import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * The CLI-Plugin for the Preferences-Package
 * 
 * @author Bastian Wolf
 * 
 */
@PluginImplementation
public class PreferencesPlugin extends AbstractTweetyPlugin {

	// the static identifier for this plugin
	private static final String PREF__CALL_PARAMETER = "preferences";

	// preference aggregation
	private static final String PREF__AGGR_IDENTIFIER = "-aggr";

	private static final String PREF__AGGR_DESCRIPTION = "-aggr <rule>, preference aggregation command with <rule>={plurality, veto, borda}";

	private static final String[] PREF__AGGR_RULES = new String[] {
			"plurality", "borda", "veto" };

	// dynamic preference aggregation
	private static final String PREF__DYN_IDENTIFIER = "-dynaggr";

	private static final String PREF__DYN_DESCRIPTION = "-dynaggr <rule>, dynamic preference aggregation command with <rule>={dynplurality, dynveto, dynborda}";

	private static final String[] PREF__DYN_RULES = new String[] {
			"dynplurality", "dynborda", "dynveto" };

	// update for dynamic preference aggregation
	private static final String PREF__UP_IDENTIFIER = "-up";

	private static final String PREF__UP_DESCRIPTION = "-up <rule>, update for dynamic preference aggregation command with <rule>={weaken, strengthen}";

	private static final String[] PREF__UP_RULES = new String[] { "weaken",
			"strengthen" };

	public String getCommand() {
		return PREF__CALL_PARAMETER;
	}

	// init command parameter
	public PreferencesPlugin() {
		this.addParameter(new SelectionCommandParameter(PREF__AGGR_IDENTIFIER,
				PREF__AGGR_DESCRIPTION, PREF__AGGR_RULES));
		this.addParameter(new SelectionCommandParameter(PREF__DYN_IDENTIFIER,
				PREF__DYN_DESCRIPTION, PREF__DYN_RULES));
		this.addParameter(new SelectionCommandParameter(PREF__UP_IDENTIFIER,
				PREF__UP_DESCRIPTION, PREF__UP_RULES));
	}

	@Override
	public PluginOutput execute(File[] input, CommandParameter[] params) {
		// File-Handler
		// Parsing,...
		Set<PreferenceOrder<String>> poset = new HashSet<PreferenceOrder<String>>();
		for (int i = 0; i < input.length; i++) {
			String filename = input[i].getAbsoluteFile().getAbsolutePath();
			if (filename.endsWith(".po")) {
				// PreferenceOrder<String> po = POParser.parse(filename);
				// poset.add(po);
			}
		}

		// parameter
		for (CommandParameter tempComParam : params) {
			// if command parameter is for aggregation
			if (tempComParam.getIdentifier().equals("-aggr")) {

			}

			// if command parameter is for dynamic aggregation
			if (tempComParam.getIdentifier().equals("-dynaggr")) {

			}

			// if command parameter is for updates of dynamic aggregation
			if (tempComParam.getIdentifier().equals("-up")) {

			}
		}

		return null; //
	}

	@Override
	public List<CommandParameter> getParameters() {
		return super.getParameters();
	}

	@Override
	protected void addParameter(CommandParameter cmdParameter) {
		super.addParameter(cmdParameter);
	}
}
