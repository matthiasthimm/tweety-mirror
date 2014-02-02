package net.sf.tweety.logics.fol.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.fol.ClassicalInference;
import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.parser.FolParser;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.plugin.AbstractTweetyPlugin;
import net.sf.tweety.plugin.PluginOutput;
import net.sf.tweety.plugin.parameter.CommandParameter;
import net.sf.tweety.plugin.parameter.SelectionCommandParameter;
import net.sf.tweety.plugin.parameter.StringListCommandParameter;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * This class models the plugin for first order logics used in the tweety cli
 * Note: Very early state, not finished or debugged yet.
 * @author Bastian Wolf
 *
 */
@PluginImplementation
public class FirstOrderLogicPlugin extends AbstractTweetyPlugin {

	// <---- STATIC DELCARATION ----->	
	
	// the static identifier for this plugin
	private static final String FOLOGIC__CALL_PARAMETER = "firstorderlogic";
		
//	private static final String FOL__PLUGIN_DESCRIPTION = "";
	
	// reasoner enum command parameter
	private static final String FOLOGIC__REASONER_IDENTIFIER = "-reasoner";
	
	private static final String FOLOGIC__REASONER_DESCRIPTION = "-reasoner <solver>, use given solver with query";
	// TODO: check this!
	private static final String[] FOLOGIC__REASONER_SOLVERENUM = {"classic"};
	
	// query input parameter
	private static final String FOLOGIC__QUERY_IDENTIFIER = "-query";
	
	private static final String FOLOGIC__QUERY_DESCRIPTION = "-query <formula>, one or more queries to be checked against knowledge base";
	
	// <---- STATIC DELCARATION ----->	

	/**
	 * This class returns the parameter used to call this plugin
	 * @return the parameter used to call this plugin
	 */
	@Override
	public String getCommand() {
		return FOLOGIC__CALL_PARAMETER;
	}
	
	
	/**
	 * non-empty constructor in case of problems concerning jspf
	 * @param args never observed
	 */
	public FirstOrderLogicPlugin(String[] args) {
		this();
	}
	
	/**
	 * actually used constructor, initializes start parameters for this plugin
	 */
	public FirstOrderLogicPlugin() {
		super();
		this.addParameter(new SelectionCommandParameter(FOLOGIC__REASONER_IDENTIFIER, FOLOGIC__REASONER_DESCRIPTION,FOLOGIC__REASONER_SOLVERENUM));
		this.addParameter(new StringListCommandParameter(FOLOGIC__QUERY_IDENTIFIER, FOLOGIC__QUERY_DESCRIPTION));
	}

	/**
	 * Executes this plugin with given input files and other aggregated parameters
	 * @param input files to be parsed (e.g. knowledge base)
	 * @param params other parameter like queries, 
	 * @return the output calculated from input files and arguments
	 */
	@Override
	public PluginOutput execute(File[] input, CommandParameter[] params) {
		
		FolBeliefSet folbs = new FolBeliefSet();
		
		FolParser parser = new FolParser();
		
		ClassicalInference reasoner = null;
		
		FolFormula[] queries = new FolFormula[1];
		
		// read in all input files (knowledge base)
		for(int i = 0; i < input.length; i++){
			if(input[i].getAbsolutePath().endsWith(".fologic")) {
				try {
					FileReader fr = new FileReader(input[i].getAbsolutePath());
					folbs = parser.parseBeliefBase(fr);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for (CommandParameter tempComParam : params){
			// if parameter identifier is for a solver
			if(tempComParam.getIdentifier().equals("-reasoner")){
				SelectionCommandParameter tmp = (SelectionCommandParameter) tempComParam;
				if(tmp.getValue().equalsIgnoreCase("classic")){
					reasoner = new ClassicalInference(folbs);
				}
			} 
			// if parameter identifier is for a query
			if(tempComParam.getIdentifier().equals("-query")){
				// cast command parameter to correct subclass
				StringListCommandParameter tmp = (StringListCommandParameter) tempComParam;
				// re-initialize queries with correct length
				queries = new FolFormula[tmp.getValue().length];
				// parse in all queries
				for(int i = 0; i<tmp.getValue().length; i++){
					try {
						queries[i] = (FolFormula) parser.parseFormula(tmp.getValue()[i]);
						
					} catch (ParserException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
			}
			
		}
		
		// Test:
		// apply all queries and print out results
		for(FolFormula folf : queries){
			System.out.println(reasoner.query(folf));
		}
		
		// TODO: make up and return plugin output
		PluginOutput out = new PluginOutput();
		
		return out;
		
		
	}

}
