package net.sf.tweety.logics.pl.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sf.tweety.BeliefBaseSampler;
import net.sf.tweety.logics.commons.analysis.BeliefSetConsistencyTester;
import net.sf.tweety.logics.commons.analysis.ConsistencyWitnessProvider;
//import net.sf.tweety.logics.commons.analysis.MiInconsistencyMeasure;
//import net.sf.tweety.logics.commons.analysis.MicInconsistencyMeasure;
import net.sf.tweety.logics.commons.analysis.MusEnumerator;
import net.sf.tweety.logics.commons.analysis.streams.DefaultInconsistencyListener;
import net.sf.tweety.logics.commons.analysis.streams.DefaultStreamBasedInconsistencyMeasure;
import net.sf.tweety.logics.commons.analysis.streams.EvaluationInconsistencyListener;
import net.sf.tweety.logics.commons.analysis.streams.InconsistencyMeasurementProcess;
import net.sf.tweety.logics.commons.analysis.streams.StreamBasedInconsistencyMeasure;
//import net.sf.tweety.logics.commons.analysis.streams.WindowInconsistencyMeasurementProcess;
import net.sf.tweety.logics.pl.DefaultConsistencyTester;
import net.sf.tweety.logics.pl.LingelingEntailment;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.SatSolverEntailment;
//import net.sf.tweety.logics.pl.analysis.ContensionInconsistencyMeasurementProcess;
import net.sf.tweety.logics.pl.analysis.HsInconsistencyMeasurementProcess;
import net.sf.tweety.logics.pl.analysis.MarcoMusEnumerator;
//import net.sf.tweety.logics.pl.analysis.PlWindowInconsistencyMeasurementProcess;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.logics.pl.util.CnfSampler;
//import net.sf.tweety.logics.pl.util.ContensionSampler;
import net.sf.tweety.logics.pl.util.HsSampler;
//import net.sf.tweety.logics.pl.util.MiSampler;
import net.sf.tweety.math.opt.solver.LpSolve;
import net.sf.tweety.streams.DefaultFormulaStream;

public class StreamInconsistencyEvaluation {
	
	public static final int 												SIGNATURE_SIZE				= 60;//30;
	public static final double 												CNF_RATIO					= 1/8d;
	public static final int 												NUMBER_OF_ITERATIONS 		= 100;
	public static final int 												SIZE_OF_KNOWLEDGEBASES 		= 5000;
	public static final double 												STANDARD_SMOOTHING_FACTOR  	= 0.75;
	public static final int													STANDARD_EVENTS				= 1000000;//40000;
	public static final BeliefSetConsistencyTester<PropositionalFormula>	STANDARD_CONSISTENCY_TESTER = new DefaultConsistencyTester(new LingelingEntailment("/home/mthimm/strinc/lingeling/lingeling"));//new DefaultConsistencyTester(new LingelingEntailment("/Users/mthimm/Projects/misc_bins/lingeling"));
	public static final ConsistencyWitnessProvider<PropositionalFormula> 	STANDARD_WITNESS_PROVIDER	= new DefaultConsistencyTester(new LingelingEntailment("/home/mthimm/strinc/lingeling/lingeling"));//new DefaultConsistencyTester(new LingelingEntailment("/Users/mthimm/Projects/misc_bins/lingeling"));
	public static final MusEnumerator<PropositionalFormula> 				STANDARD_MUS_ENUMERATOR     = new MarcoMusEnumerator("/home/mthimm/strinc/marco/marco.py");// new MarcoMusEnumerator("/Users/mthimm/Projects/misc_bins/marco_py-1.0/marco.py");
	public static final String 												RESULT_PATH					= "/home/mthimm/strinc";//"/Users/mthimm/Desktop";
	public static final String												BELIEFSET_PATH				= "/home/mthimm/strinc/beliefsets.txt";//"/Users/mthimm/Desktop/beliefsets.txt";
	public static final String												TMP_FILE_FOLDER				= "/home/mthimm/strinc/tmp";//"/Users/mthimm/Desktop/tmp";
	public static final long												TIMEOUT						= -1;//120; //2 minutes
	
	public static void main(String[] args) throws InterruptedException{
		LpSolve.setBinary("/home/mthimm/strinc/lpsolve/lp_solve");
		LpSolve.setTmpFolder(new File(TMP_FILE_FOLDER));
		SatSolverEntailment.tempFolder = new File(TMP_FILE_FOLDER);
		PropositionalSignature signature = new PropositionalSignature(SIGNATURE_SIZE);
		BeliefBaseSampler<PlBeliefSet> sampler = new CnfSampler(signature,CNF_RATIO);
		// -----------------------------------------
		// the inconsistency measures to be compared
		// -----------------------------------------
		Collection<StreamBasedInconsistencyMeasure<PropositionalFormula>> measures = new HashSet<StreamBasedInconsistencyMeasure<PropositionalFormula>>(); 
//		// Stream contension measure (population size = 10)
//		Map<String,Object>  config = new HashMap<String,Object>();
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_WITNESSPROVIDER, STANDARD_WITNESS_PROVIDER);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_NUMBEROFPOPULATIONS, 10);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_SIGNATURE, signature);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);
//		StreamBasedInconsistencyMeasure<PropositionalFormula> cont_stream_1 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(ContensionInconsistencyMeasurementProcess.class,config);
//		cont_stream_1.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/stream-cont-1.txt",STANDARD_EVENTS));
//		cont_stream_1.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(cont_stream_1);
//		// Stream contension measure (population size = 100)
//		config = new HashMap<String,Object>();
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_WITNESSPROVIDER, STANDARD_WITNESS_PROVIDER);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_NUMBEROFPOPULATIONS, 100);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_SIGNATURE, signature);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);
//		StreamBasedInconsistencyMeasure<PropositionalFormula> cont_stream_2 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(ContensionInconsistencyMeasurementProcess.class,config);
//		cont_stream_2.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/stream-cont-2.txt",STANDARD_EVENTS));
//		cont_stream_2.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(cont_stream_2);
//		// Stream contension measure (population size = 500)
//		config = new HashMap<String,Object>();
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_WITNESSPROVIDER, STANDARD_WITNESS_PROVIDER);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_NUMBEROFPOPULATIONS, 500);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_KEY_SIGNATURE, signature);
//		config.put(ContensionInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);
//		StreamBasedInconsistencyMeasure<PropositionalFormula> cont_stream_3 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(ContensionInconsistencyMeasurementProcess.class,config);
//		cont_stream_3.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/stream-cont-3.txt",STANDARD_EVENTS));
//		cont_stream_3.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(cont_stream_3);
//		// Window-based MI measure (window size = 500)
//		Map<String,Object> config = new HashMap<String,Object>();
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_MEASURE, new MiInconsistencyMeasure<PropositionalFormula>(STANDARD_MUS_ENUMERATOR));
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);		
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_WINDOWSIZE, 500);
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_NAME, "-mi-1");
//		StreamBasedInconsistencyMeasure<PropositionalFormula> mi_1 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(PlWindowInconsistencyMeasurementProcess.class,config);
//		mi_1.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/naive-mi-1.txt",STANDARD_EVENTS));
//		mi_1.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(mi_1);
//		// Window-based MI measure (window size = 1000)
//		config = new HashMap<String,Object>();
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_MEASURE, new MiInconsistencyMeasure<PropositionalFormula>(STANDARD_MUS_ENUMERATOR));
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);		
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_WINDOWSIZE, 1000);
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_NAME, "-mi-2");
//		StreamBasedInconsistencyMeasure<PropositionalFormula> mi_2 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(PlWindowInconsistencyMeasurementProcess.class,config);
//		mi_2.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/naive-mi-2.txt",STANDARD_EVENTS));
//		mi_2.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(mi_2);
//		// Window-based MI measure (window size = 2000)
//		config = new HashMap<String,Object>();
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_MEASURE, new MiInconsistencyMeasure<PropositionalFormula>(STANDARD_MUS_ENUMERATOR));
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);		
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_WINDOWSIZE, 2000);
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_NAME, "-mi-3");
//		StreamBasedInconsistencyMeasure<PropositionalFormula> mi_3 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(PlWindowInconsistencyMeasurementProcess.class,config);
//		mi_3.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/naive-mi-3.txt",STANDARD_EVENTS));
//		mi_3.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(mi_3);
//		// Window-based MI^C measure (window size = 500)
//		config = new HashMap<String,Object>();
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_MEASURE, new MicInconsistencyMeasure<PropositionalFormula>(STANDARD_MUS_ENUMERATOR));
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);		
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_WINDOWSIZE, 500);
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_NAME, "-mic-1");
//		StreamBasedInconsistencyMeasure<PropositionalFormula> mic_1 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(PlWindowInconsistencyMeasurementProcess.class,config);
//		mic_1.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/naive-mic-1.txt",STANDARD_EVENTS));
//		mic_1.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(mic_1);
//		// Window-based MI^C measure (window size = 1000)
//		config = new HashMap<String,Object>();
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_MEASURE, new MicInconsistencyMeasure<PropositionalFormula>(STANDARD_MUS_ENUMERATOR));
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);		
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_WINDOWSIZE, 1000);
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_NAME, "-mic-2");
//		StreamBasedInconsistencyMeasure<PropositionalFormula> mic_2 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(PlWindowInconsistencyMeasurementProcess.class,config);
//		mic_2.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/naive-mic-2.txt",STANDARD_EVENTS));
//		mic_2.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(mic_2);
//		// Window-based MI^C measure (window size = 2000)
//		config = new HashMap<String,Object>();
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_MEASURE, new MicInconsistencyMeasure<PropositionalFormula>(STANDARD_MUS_ENUMERATOR));
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);		
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_WINDOWSIZE, 2000);
//		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
//		config.put(WindowInconsistencyMeasurementProcess.CONFIG_NAME, "-mic-3");
//		StreamBasedInconsistencyMeasure<PropositionalFormula> mic_3 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(PlWindowInconsistencyMeasurementProcess.class,config);
//		mic_3.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/naive-mic-3.txt",STANDARD_EVENTS));
//		mic_3.addInconsistencyListener(new DefaultInconsistencyListener());
//		measures.add(mic_3);
		// Stream hs measure (population size = 10)
		Map<String,Object> config = new HashMap<String,Object>();
		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_WITNESSPROVIDER, STANDARD_WITNESS_PROVIDER);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_NUMBEROFPOPULATIONS, 10);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_SIGNATURE, signature);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);
		StreamBasedInconsistencyMeasure<PropositionalFormula> hs_stream_1 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(HsInconsistencyMeasurementProcess.class,config);
		//hs_stream_1.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/stream-hs-1.txt",STANDARD_EVENTS));
		hs_stream_1.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/stream-hs-1.txt",STANDARD_EVENTS));
		hs_stream_1.addInconsistencyListener(new DefaultInconsistencyListener());
		measures.add(hs_stream_1);
		// Stream hs measure (population size = 100)
		config = new HashMap<String,Object>();
		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_WITNESSPROVIDER, STANDARD_WITNESS_PROVIDER);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_NUMBEROFPOPULATIONS, 100);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_SIGNATURE, signature);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);
		StreamBasedInconsistencyMeasure<PropositionalFormula> hs_stream_2 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(HsInconsistencyMeasurementProcess.class,config);
		hs_stream_2.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/stream-hs-2.txt",STANDARD_EVENTS));
		hs_stream_2.addInconsistencyListener(new DefaultInconsistencyListener());
		measures.add(hs_stream_2);
		// Stream hs measure (population size = 500)
		config = new HashMap<String,Object>();
		config.put(InconsistencyMeasurementProcess.CONFIG_TIMEOUT, TIMEOUT);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_WITNESSPROVIDER, STANDARD_WITNESS_PROVIDER);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_NUMBEROFPOPULATIONS, 500);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_KEY_SIGNATURE, signature);
		config.put(HsInconsistencyMeasurementProcess.CONFIG_SMOOTHINGFACTOR, STANDARD_SMOOTHING_FACTOR);
		StreamBasedInconsistencyMeasure<PropositionalFormula> hs_stream_3 = new DefaultStreamBasedInconsistencyMeasure<PropositionalFormula>(HsInconsistencyMeasurementProcess.class,config);
		hs_stream_3.addInconsistencyListener(new EvaluationInconsistencyListener(RESULT_PATH+"/stream-hs-3.txt",STANDARD_EVENTS));
		hs_stream_3.addInconsistencyListener(new DefaultInconsistencyListener());
		measures.add(hs_stream_3);
		
		// -----------------------------------------
		// iterate
		// -----------------------------------------
		int incvalue = 200;
		int[] kbsizes = {5000,10000,15000,20000,25000,30000,35000,40000,45000,50000};
		int kbsize = -1;
		EvaluationInconsistencyListener.TOLERANCE = 1;
		EvaluationInconsistencyListener.INCDEFAULTVALUE = 200;
		for(int iteration = 0; iteration < NUMBER_OF_ITERATIONS; iteration++){
			if(iteration % 10 == 0){
				kbsize = kbsizes[iteration / 10];				
				sampler = new HsSampler(signature,incvalue);
			}
			PlBeliefSet bs = sampler.randomSample(kbsize, kbsize);
			System.out.println(kbsize + "," + incvalue + " - " + bs);
			try {
				FileWriter writer = new FileWriter(new File(BELIEFSET_PATH), true);
				//writer.append(incvalue + "\tX" + millis + ";" + ival + "X\t" + bs.toString() + "\n");
				writer.append(incvalue + " - " + bs.toString() + "\n");
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}	
			for(StreamBasedInconsistencyMeasure<PropositionalFormula> sbim: measures){
				InconsistencyMeasurementProcess<PropositionalFormula> imp = sbim.getInconsistencyMeasureProcess(new DefaultFormulaStream<PropositionalFormula>(bs,true));
				imp.start();
				Thread.sleep(2000);
				while(imp.isAlive()){
					Thread.sleep(2000);
				}
			}
		}
	}
}
