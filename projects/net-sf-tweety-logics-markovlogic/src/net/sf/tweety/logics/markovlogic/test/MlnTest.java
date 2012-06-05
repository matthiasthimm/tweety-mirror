package net.sf.tweety.logics.markovlogic.test;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;
import net.sf.tweety.logics.markovlogic.NaiveMlnReasoner;
import net.sf.tweety.logics.markovlogic.analysis.*;
import net.sf.tweety.logics.markovlogic.syntax.MlnFormula;
import net.sf.tweety.util.Pair;

public class MlnTest {

	public static Pair<MarkovLogicNetwork,FolSignature> SmokersExample(int domain_size) throws ParserException, IOException{
		// see [Richardson:2006]
		Predicate friends = new Predicate("friends",2);
		Predicate smoker = new Predicate("smokes",1);
		Predicate cancer = new Predicate("cancer",1);
		
		FolSignature sig = new FolSignature();
		sig.add(friends);
		sig.add(smoker);
		sig.add(cancer);
		
		for(int i = 0; i< domain_size; i++)
			sig.add(new Constant("d"+i));
		
		FolParser parser = new FolParser();
		parser.setSignature(sig);
		
		MarkovLogicNetwork mln = new MarkovLogicNetwork();
		
		//friends of friends are friends
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!friends(X,Y) || !friends(Y,Z) || friends(X,Z)"), new Double(0.7)));
		//friendless people smoke
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("(exists Y: (friends(X,Y))) || smokes(X)"), new Double(2.3)));
		//smoking causes cancer
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!smokes(X) ||  cancer(X)"), new Double(1.5)));
		//smoking behavior of friends is the same
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!friends(X,Y) || ((smokes(X) && smokes(Y))||(!smokes(X) && !smokes(Y)))"), new Double(1.1)));
		
		return new Pair<MarkovLogicNetwork,FolSignature>(mln,sig);
	}
	
	public static Pair<MarkovLogicNetwork,FolSignature> ElephantZooExample(int domain_size) throws ParserException, IOException{
		// see [Finthammer:2012]
		Predicate likes = new Predicate("likes",2);
		Predicate elephant = new Predicate("elephant",1);
		Predicate keeper = new Predicate("keeper",1);
		
		Constant fred = new Constant("fred");
		Constant clyde = new Constant("clyde");
		
		FolSignature sig = new FolSignature();
		sig.add(likes);
		sig.add(elephant);
		sig.add(keeper);
		sig.add(fred);
		sig.add(clyde);
		
		for(int i = 0; i< domain_size; i++)
			sig.add(new Constant("d"+i));
		
		FolParser parser = new FolParser();
		parser.setSignature(sig);
		
		MarkovLogicNetwork mln = new MarkovLogicNetwork();
		
		// Clyde is an elephant, Fred is a keeper
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!elephant(clyde)"), Double.MIN_VALUE)); //p=1
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!keeper(fred)"), Double.MIN_VALUE)); //p=1
		// elephants are not keepers
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("keeper(X) && elephant(X)"), Double.MIN_VALUE)); //p=1
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!keeper(X) && !elephant(X)"), Double.MIN_VALUE)); //p=1
		//elephants like keepers
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!elephant(X) || !keeper(Y) || likes(X,Y)"), new Double(2.1972))); //p=0.9
		// elephants do not like Fred
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!elephant(X) || likes(X,fred)"), new Double(-0.8573))); //p=0.3
		// Clyde likes Fred
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!likes(clyde,fred)"), Double.MIN_VALUE)); //p=1
				
		return new Pair<MarkovLogicNetwork,FolSignature>(mln,sig);
	}
	
	public static Pair<MarkovLogicNetwork,FolSignature> CommonColdExample(int domain_size) throws ParserException, IOException{
		// see [Fisseler:2008]
		Predicate contact = new Predicate("contact",2);
		Predicate cold = new Predicate("cold",1);
		Predicate susceptible = new Predicate("susceptible",1);
		
		FolSignature sig = new FolSignature();
		sig.add(contact);
		sig.add(cold);
		sig.add(susceptible);
		
		for(int i = 0; i< domain_size; i++)
			sig.add(new Constant("d"+i));
		
		FolParser parser = new FolParser();
		parser.setSignature(sig);
		
		MarkovLogicNetwork mln = new MarkovLogicNetwork();
		
		// general probability of infection
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("cold(X)"), new Double(-4.5951198501))); //p=0.01
		// infection if susceptible
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!susceptible(X) || cold(X)"), new Double(-2.1972245773))); //p=0.1
		// infection by contact
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("!contact(X,Y) || !cold(Y) || cold(X)"), new Double(0.4054651081))); //p=0.6
		
		return new Pair<MarkovLogicNetwork,FolSignature>(mln,sig);
	}
	
	public static Pair<MarkovLogicNetwork,FolSignature> SimpleExample(int domain_size) throws ParserException, IOException{
		Predicate a = new Predicate("A",1);
		Constant c1 = new Constant("c1");
		
		FolSignature sig = new FolSignature();
		sig.add(a);
		sig.add(c1);
		for(int i = 0; i< domain_size; i++)
			sig.add(new Constant("d"+i));
		FolParser parser = new FolParser();
		parser.setSignature(sig);
		
		MarkovLogicNetwork mln = new MarkovLogicNetwork();
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("A(X)"), new Double(2)));
		mln.add(new MlnFormula((FolFormula)parser.parseFormula("A(c1)"), new Double(-5)));
		
		return new Pair<MarkovLogicNetwork,FolSignature>(mln,sig);
	}
	
	public static Pair<MarkovLogicNetwork,FolSignature> iterateExamples(int exNum, int domain_size) throws ParserException, IOException{
		if(exNum == 0)
			return SimpleExample(domain_size);
		if(exNum == 1)
			return SmokersExample(domain_size);
		if(exNum == 2)
			return CommonColdExample(domain_size);
		return ElephantZooExample(domain_size);		
	}
	
	public static void main(String[] args) throws ParserException, IOException{
		//MlnTest.createChart(null, "", "");
		String expPath = "/home/share/mln/";
					
		List<AggregationFunction> aggrFunctions = new ArrayList<AggregationFunction>();
		aggrFunctions.add(new MaxAggregator());
		aggrFunctions.add(new MinAggregator());
		aggrFunctions.add(new AverageAggregator());
		//aggrFunctions.add(new ProductAggregator());
		//aggrFunctions.add(new SumAggregator());
		
		List<DistanceFunction> distFunctions = new ArrayList<DistanceFunction>();
		for(AggregationFunction af: aggrFunctions)
			distFunctions.add(new AggregatingDistanceFunction(af));
		for(AggregationFunction af: aggrFunctions)
			distFunctions.add(new ProbabilisticAggregatingDistanceFunction(af,3));
		for(int i = 1; i< 2; i++){
			distFunctions.add(new PNormDistanceFunction(i,false));
			distFunctions.add(new PNormDistanceFunction(i,true));
		}
		for(int i = 1; i< 4; i++)
			distFunctions.add(new ProbabilisticPNormDistanceFunction(i,3));
		
		for(int i = 0; i < 4; i++){
			for(AggregationFunction af: aggrFunctions){
				for(DistanceFunction df: distFunctions){				
					AggregatingCoherenceMeasure measure = new AggregatingCoherenceMeasure(df,af);
					double[][] results = new double[2][12];
					MarkovLogicNetwork mln_g = null;
					for(int dsize = 3; dsize < 15; dsize++){						
						Pair<MarkovLogicNetwork,FolSignature> ex = MlnTest.iterateExamples(i, dsize);
						MarkovLogicNetwork mln = ex.getFirst();
						mln_g = mln;
						FolSignature sig = ex.getSecond();
						NaiveMlnReasoner reasoner = new NaiveMlnReasoner(mln, sig);
						reasoner.setTempDirectory(expPath+"tmp");
						results[0][dsize-3] = dsize;
						results[1][dsize-3] = measure.coherence(mln, reasoner, sig);
						System.out.println("Example " + i + ", domain size " + dsize + ", measure " + measure.toString() + ", coherence value " + results[1][dsize-3]);
					}
					List<double[][]> series = new ArrayList<double[][]>();
					series.add(results);
					MlnTest.createChart(series, i+"_"+measure.toString(), expPath+i+"_"+measure.toString()+".png");
					ExpResult expResult = new ExpResult();
					expResult.coherenceMeasure = measure;
					expResult.domain2Coherence = results;
					expResult.mln = mln_g;
					FileOutputStream fos = null;
					ObjectOutputStream out = null;
					fos = new FileOutputStream(expPath+i+"_"+measure.toString()+".obj");
					out = new ObjectOutputStream(fos);
					out.writeObject(expResult);
					out.close();					
				}
			}			
		}				
	}
	
	public static void createChart(List<double[][]> series, String title, String filename) throws IOException {
		DefaultXYDataset data = new DefaultXYDataset();
		
		double minValue = 0;
		int max_domain_size = 0;
		for(double[][] line: series){
			data.addSeries(line.hashCode(), line);
			int i = 0;
			for(; i < line[1].length; i++)				
				minValue = minValue > line[1][i] ? line[1][i] : minValue;
			max_domain_size = max_domain_size > i ? max_domain_size : i;
		}
				
		JFreeChart chart = ChartFactory.createXYLineChart(title, "domain size", "coherence", data, PlotOrientation.VERTICAL, false, false, false);
	    chart.setBackgroundPaint(Color.white);

	    XYPlot plot = (XYPlot) chart.getPlot();
	    plot.setBackgroundPaint(Color.white);
	    plot.setDomainCrosshairVisible(true);
	    plot.setRangeCrosshairVisible(true);

	    XYItemRenderer r = plot.getRenderer();
	    if (r instanceof XYLineAndShapeRenderer) {
	        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
	        renderer.setBaseShapesVisible(true);
	        renderer.setBaseShapesFilled(true);
	        renderer.setDrawSeriesLineAsPath(true);
	    }

	    plot.getDomainAxis().setLowerBound(3);
	    plot.getDomainAxis().setUpperBound(max_domain_size+3);	    
	    ((NumberAxis)plot.getDomainAxis()).setTickUnit(new NumberTickUnit(1));
	    
	    plot.getRangeAxis().setLowerBound(minValue);
	    plot.getRangeAxis().setUpperBound(1);
	    	        
	    ChartUtilities.saveChartAsPNG(new File(filename), chart, 1000, 1000);
		
	}
	
	
	
	
}
