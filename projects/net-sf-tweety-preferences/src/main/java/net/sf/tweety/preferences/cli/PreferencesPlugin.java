package net.sf.tweety.preferences.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.sf.tweety.cli.plugins.AbstractTweetyPlugin;
import net.sf.tweety.cli.plugins.PluginOutput;
import net.sf.tweety.cli.plugins.TweetyPlugin;
import net.sf.tweety.cli.plugins.parameter.CommandParameter;
import net.sf.tweety.cli.plugins.parameter.SelectionCommandParameter;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.aggregation.BordaScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.DynamicBordaScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.DynamicPluralityScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.DynamicVetoScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.PluralityScoringPreferenceAggregator;
import net.sf.tweety.preferences.aggregation.VetoScoringPreferenceAggregator;
import net.sf.tweety.preferences.io.POParser;
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
	
	/**
	 * non-empty constructur in case of errors in JSPF
	 * @param args
	 */
	public PreferencesPlugin(String[] args){
		super();
		this.addParameter(new SelectionCommandParameter(PREF__AGGR_IDENTIFIER,
				PREF__AGGR_DESCRIPTION, PREF__AGGR_RULES));
		this.addParameter(new SelectionCommandParameter(PREF__DYN_IDENTIFIER,
				PREF__DYN_DESCRIPTION, PREF__DYN_RULES));
		this.addParameter(new SelectionCommandParameter(PREF__UP_IDENTIFIER,
				PREF__UP_DESCRIPTION, PREF__UP_RULES));
	}
	
	// init command parameter
	/**
	 * Default empty constructor initializing this plugins parameter
	 */
	public PreferencesPlugin() {
		super();
		this.addParameter(new SelectionCommandParameter(PREF__AGGR_IDENTIFIER,
				PREF__AGGR_DESCRIPTION, PREF__AGGR_RULES));
		this.addParameter(new SelectionCommandParameter(PREF__DYN_IDENTIFIER,
				PREF__DYN_DESCRIPTION, PREF__DYN_RULES));
		this.addParameter(new SelectionCommandParameter(PREF__UP_IDENTIFIER,
				PREF__UP_DESCRIPTION, PREF__UP_RULES));
	}
	

	@Override
	/**
	 * executes the given parameters and files in this plugin
	 */
	public PluginOutput execute(File[] input, CommandParameter[] params) {
		PreferenceOrder<String> result = new PreferenceOrder<String>();
		// File-Handler
		// Parsing,...
		List<PreferenceOrder<String>> poset = new ArrayList<PreferenceOrder<String>>();
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
				SelectionCommandParameter tmp = (SelectionCommandParameter) tempComParam;
				// plurality scoring
				if (tmp.getValue().equalsIgnoreCase("plurality")) {
					PluralityScoringPreferenceAggregator<String> pluraggr = new PluralityScoringPreferenceAggregator<String>();
					result = pluraggr.aggregate(poset);

				// borda scoring
				} else if (tmp.getValue().equalsIgnoreCase("borda")) {
					BordaScoringPreferenceAggregator<String> brdaggr = new BordaScoringPreferenceAggregator<String>(
							poset.iterator().next().size());
					result = brdaggr.aggregate(poset);

				// veto scoring
				} else if (tmp.getValue().equalsIgnoreCase("veto")) {
					VetoScoringPreferenceAggregator<String> vetoaggr = new VetoScoringPreferenceAggregator<String>(
							0);
					result = vetoaggr.aggregate(poset);
				}

			}

			// if command parameter is for dynamic aggregation
			if (tempComParam.getIdentifier().equals("-dynaggr")) {
				SelectionCommandParameter tmp = (SelectionCommandParameter) tempComParam;

				// dynamic plurality scoring
				if (tmp.getValue().equalsIgnoreCase("dynplurality")) {
					DynamicPluralityScoringPreferenceAggregator<String> dynpluraggr = new DynamicPluralityScoringPreferenceAggregator<String>();
					result = dynpluraggr.aggregate(poset);
				// dynamic borda scoring
				} else if (tmp.getValue().equalsIgnoreCase("dynborda")) {
					DynamicBordaScoringPreferenceAggregator<String> dynbrdaggr = new DynamicBordaScoringPreferenceAggregator<String>(
							poset.iterator().next().size());
					result = dynbrdaggr.aggregate(poset);
				// dynamic veto scoring	
				} else if (tmp.getValue().equalsIgnoreCase("dynveto")) {
					DynamicVetoScoringPreferenceAggregator<String> dynvetoaggr = new DynamicVetoScoringPreferenceAggregator<String>(
							0);
					result = dynvetoaggr.aggregate(poset);
				}
			}

			// if command parameter is for updates of dynamic aggregation
			if (tempComParam.getIdentifier().equals("-up")) {

			}
		}
		PluginOutput out = new PluginOutput(result.toString());

		return out;
		
	}

	@Override
	public List<CommandParameter> getParameters() {
		return super.getParameters();
	}

	@Override
	public void addParameter(CommandParameter cmdParameter) {
		super.addParameter(cmdParameter);
	}

}
