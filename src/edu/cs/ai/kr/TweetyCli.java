package edu.cs.ai.kr;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.*;
import org.apache.log4j.*;

import edu.cs.ai.kr.fol.parser.*;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.rpcl.*;
import edu.cs.ai.kr.rpcl.parser.*;
import edu.cs.ai.kr.rpcl.parser.rpclcondensedprobabilitydistributionparser.*;
import edu.cs.ai.kr.rpcl.parser.rpclprobabilitydistributionparser.*;
import edu.cs.ai.kr.rpcl.semantics.*;
import edu.cs.ai.kr.rpcl.writers.*;
import edu.cs.ai.util.*;

/**
 * This class implements a simple command line interface for accessing
 * the functionalities provided by the Tweety libraries.
 * 
 * @author Matthias Thimm
 */
public class TweetyCli {

	/**
	 * Logger.
	 */
	private static Log log = LogFactory.getLog(TweetyCli.class);
	
	/** The argument name for the input file(s) */
	public static final String ARG__INPUT_FILES = "--input";
	/** The argument name for the input file(s) (short) */
	public static final String ARG__INPUT_FILES_SHORT = "-i";
	/** The argument name for the parser(s) used for reading the input file(s) */
	public static final String ARG__INPUT_PARSER = "--parser";
	/** The argument name for the parser(s) used for reading the input file(s) (short) */
	public static final String ARG__INPUT_PARSER_SHORT = "-p";
	/** The argument name for the output file */
	public static final String ARG__OUTPUT_FILE = "--output";
	/** The argument name for the output file (short) */
	public static final String ARG__OUTPUT_FILE_SHORT = "-o";
	/** The argument name for the writer used for writing the output file. */
	public static final String ARG__OUTPUT_WRITER = "--writer";
	/** The argument name for the writer used for writing the output file (short) */
	public static final String ARG__OUTPUT_WRITER_SHORT = "-w";
	/** The argument name for the log level (The possible values are described
	 *  by <code>TweetyConfiguration.LogLevel</code>, default
	 *  is <code>TweetyConfiguration.LogLevel.INFO</code>) */
	public static final String ARG__LOG_LEVEL = "--log";
	/** The argument name for the log level (The possible values are described
	 *  by <code>TweetyConfiguration.LogLevel</code>, default
	 *  is <code>TweetyConfiguration.LogLevel.INFO</code>) (short) */
	public static final String ARG__LOG_LEVEL_SHORT = "-l";
	/** The argument name for the file used for logging (if this parameter is not set,
	 *  logging is performed on the standard output) */
	public static final String ARG__LOG_FILE = "--logfile";
	/** The argument name for the file used for logging (if this parameter is not set,
	 *  logging is performed on the standard output) (short) */
	public static final String ARG__LOG_FILE_SHORT = "-lf";
	/** The argument name for advanced options */
	public static final String ARG__OPTIONS = "--options";
	/** The argument name for advanced options (short) */
	public static final String ARG__OPTIONS_SHORT = "-op";
	/** The argument name for a query */
	public static final String ARG__QUERY = "--query";
	/** The argument name for a query */
	public static final String ARG__QUERY_SHORT = "-q";
	
	
	/** The input file */
	private static String[] inputFiles = null;
	/** The parser used for reading the input file */
	private static Object[] inputParser = null;
	/** The output file */
	private static String outputFile = null;
	/** The writer used for writing the output file. */
	private static Writer outputWriter = null;
	/** The log level (The possible values are described
	 *  by <code>TweetyConfiguration.LogLevel</code>, default
	 *  is <code>TweetyConfiguration.LogLevel.INFO</code>) */
	private static TweetyConfiguration.LogLevel logLevel = TweetyConfiguration.LogLevel.INFO;
	/** The file used for logging (if this parameter is not set,
	 *  logging is performed on the standard output) */
	private static String logFile = null;
	/** Advanced options */
	private static String options = null;
	/** The query */
	private static String query = null;
	
	/**
	 * Program entry.<br>
	 * <br>
	 * Current program call:<br>
	 * - java -jar TweetyCLI.jar --input RPCLKBFILE --parser rpclme --output RPCLPROBFILE --writer X
	 * 		--options [rpcl.semantics=Y,rpcl.inference=Z]<br>	 * 
	 * - java  -jar TweetyCLI.jar --input RPCLKBFILE RPCLPROBFILE --parser rpclme X --query=SOMEQUERY
	 * 		--options [rpcl.semantics=Y,rpcl.inference=Z] 
	 * 		
	 * with X\in{rpclmeProb, rpclmeCondProb}, Y\in {averaging,aggregating}, Z\in{standard,lifted}<br>
	 * @param args command line arguments.
	 */
	public static void main(String[] args){
		// TODO the following has to be generalized
		// (at the moment this cli just supports reasoning with RPCL)
		
		// read arguments		
		for(int i = 0; i < args.length; i++){
			if(args[i].equals(ARG__INPUT_FILES) || args[i].equals(ARG__INPUT_FILES_SHORT)){
				List<String> files = new ArrayList<String>();
				while(!args[i+1].startsWith("-"))
					files.add(args[++i]);
				inputFiles = files.toArray(inputFiles);
			}else if(args[i].equals(ARG__INPUT_PARSER) || args[i].equals(ARG__INPUT_PARSER_SHORT)){
				//TODO generalize the following
				List<Object> parser = new ArrayList<Object>();
				while(!args[i+1].startsWith("-")){
					i++;
					if(args[i].equals("rpclme"))
						parser.add(new RpclParser());
					else if(args[i].equals("rpclmeProb"))
						parser.add(new RpclProbabilityDistributionParser());
					else if(args[i].equals("rpclmeCondProb"))
						parser.add(new RpclCondensedProbabilityDistributionParser());
					else{					
						System.err.println("At the moment TweetyCLI only supports reasoning with RPCL.");
						System.exit(1);
					}					
				}
				inputParser = parser.toArray(inputParser);
			}else if(args[i].equals(ARG__OUTPUT_FILE) || args[i].equals(ARG__OUTPUT_FILE_SHORT))
				outputFile = args[++i];
			else if(args[i].equals(ARG__OUTPUT_WRITER) || args[i].equals(ARG__OUTPUT_WRITER)){
				//TODO generalize the following
				i++;
				if(args[i].equals("rpclmeProb")){
					outputWriter = new DefaultProbabilityDistributionWriter();
				}else if(args[i].equals("rpclmeCondProb"))
					outputWriter = new DefaultCondensedProbabilityDistributionWriter();
				else{					
					System.err.println("At the moment TweetyCLI only supports reasoning with RPCL.");
					System.exit(1);
				}
			}else if(args[i].equals(ARG__LOG_LEVEL) || args[i].equals(ARG__LOG_LEVEL_SHORT))
				logLevel = TweetyConfiguration.LogLevel.getLogLevel(args[++i]);
			else if(args[i].equals(ARG__LOG_FILE) || args[i].equals(ARG__LOG_FILE_SHORT))
				logFile = args[++i];
			else if(args[i].equals(ARG__OPTIONS) || args[i].equals(ARG__OPTIONS_SHORT))
				options = args[++i];
			if(args[i].equals(ARG__QUERY) || args[i].equals(ARG__QUERY_SHORT))
				query = args[++i];
		}
		
		inputFiles = new String[2]; inputFiles[0] = "/Users/mthimm/Desktop/test"; inputFiles[1] = "/Users/mthimm/Desktop/output";
		options = "[rpcl.semantics=averaging,rpcl.inference=lifted]";
		inputParser = new Object[2]; inputParser[0] = new RpclParser(); inputParser[1] = new RpclCondensedProbabilityDistributionParser();
		query = "bird(a)";
		logLevel = TweetyConfiguration.LogLevel.INFO; 
		
		// TODO customize the following
		Properties properties = new Properties();
		properties.put("log4j.rootLogger", logLevel.toString() + ",mainlogger");
		properties.put("log4j.appender.mainlogger.layout","org.apache.log4j.PatternLayout");
		properties.put("log4j.appender.mainlogger.layout.ConversionPattern","%5p [%t] %C{1}.%M%n      %m%n");

		if(logFile != null){
			properties.put("log4j.appender.mainlogger","org.apache.log4j.RollingFileAppender");
			properties.put("log4j.appender.mainlogger.File",logFile);
		}else
			properties.put("log4j.appender.mainlogger","org.apache.log4j.ConsoleAppender");
		PropertyConfigurator.configure(properties);
		
		log.info("Start logging.");
		
		// parse options
		// TODO generalize this (at the moment, only "rpcl.semantics={averaging,aggregating}"
		// and "rpcl.inference={standard,lifted} are valid options)
		RpclSemantics semantics = null;
		if(options.toLowerCase().indexOf("averaging") != -1)
			semantics = new AveragingSemantics();
		else semantics = new AggregatingSemantics();
		int inferenceType = RpclMeReasoner.STANDARD_INFERENCE;
		if(options.toLowerCase().indexOf("lifted") != -1)
			inferenceType = RpclMeReasoner.LIFTED_INFERENCE;		
				
		// perform inference
		try{
			RpclBeliefSet kb = (RpclBeliefSet)((RpclParser) inputParser[0]).parseBeliefBaseFromFile(inputFiles[0]);
			if(inputFiles.length == 1){
				RpclMeReasoner reasoner = new RpclMeReasoner(kb,semantics,(FolSignature)((RpclParser) inputParser[0]).getSignature(),inferenceType);
				ProbabilityDistribution p = reasoner.getMeDistribution();
				outputWriter.setObject(p);
				outputWriter.writeToFile(outputFile);			
			}else if(inputParser[1] instanceof RpclProbabilityDistributionParser) {
				((RpclProbabilityDistributionParser)inputParser[1]).setSemantics(semantics);
				((RpclProbabilityDistributionParser)inputParser[1]).setSignature((FolSignature)((RpclParser) inputParser[0]).getSignature());
				ProbabilityDistribution p = ((RpclProbabilityDistributionParser)inputParser[1]).parseProbabilityDistribution(new InputStreamReader(new java.io.FileInputStream(inputFiles[1])));
				FolParser folParser = new FolParser();
				folParser.setSignature((FolSignature)((RpclParser) inputParser[0]).getSignature());
				Probability result = p.probability((FolFormula)folParser.parseFormula(query));
				log.info("Probability of " + query + " is: " + result.getValue());
			}else if(inputParser[1] instanceof RpclCondensedProbabilityDistributionParser) {
				((RpclCondensedProbabilityDistributionParser)inputParser[1]).setSemantics(semantics);
				((RpclCondensedProbabilityDistributionParser)inputParser[1]).setSignature((FolSignature)((RpclParser) inputParser[0]).getSignature());
				CondensedProbabilityDistribution p = ((RpclCondensedProbabilityDistributionParser)inputParser[1]).parseCondensedProbabilityDistribution(new InputStreamReader(new java.io.FileInputStream(inputFiles[1])));
				FolParser folParser = new FolParser();
				folParser.setSignature((FolSignature)((RpclParser) inputParser[0]).getSignature());
				Probability result = p.probability((FolFormula)folParser.parseFormula(query));
				log.info("Probability of " + query + " is: " + result.getValue());
			}else log.error("Wrong parser");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Application terminated.");		
	}
}
