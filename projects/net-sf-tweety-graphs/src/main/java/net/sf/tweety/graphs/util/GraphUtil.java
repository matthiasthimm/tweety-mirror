package net.sf.tweety.graphs.util;

import java.util.*;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import net.sf.tweety.graphs.Graph;
import net.sf.tweety.graphs.Node;
import net.sf.tweety.math.ComplexNumber;

/**
 * This abstract class contains some auxiliary methods for working
 * with graphs.
 * 
 * @author Matthias Thimm
 */
public abstract class GraphUtil {

	/** For archiving page rank values. */
	private static Map<Graph<? extends Node>,Map<Double,Map<Double,Map<Node,Double>>>> archivePageRank = new HashMap<Graph<? extends Node>,Map<Double,Map<Double,Map<Node,Double>>>>();
	
	/**
	 * Computes the PageRank of the given node in the given graph.
	 * @param g a graph
	 * @param n a node
	 * @param dampingFactor the damping factor for PageRank
	 * @param precision the precision (smaller values mean higher precision)
	 * @return the PageRank of the given node in the given graph.
	 */
	public static Double pageRank(Graph<? extends Node> g, Node n, double dampingFactor, double precision){
		if(GraphUtil.archivePageRank.containsKey(g) &&
				GraphUtil.archivePageRank.get(g).containsKey(dampingFactor) &&
				GraphUtil.archivePageRank.get(g).get(dampingFactor).containsKey(precision))
			return GraphUtil.archivePageRank.get(g).get(dampingFactor).get(precision).get(n);
		Map<Node,Double> pageRanks = new HashMap<Node,Double>();
		// init
		double m = g.getNodes().size();
		Set<Node> sinks = new HashSet<Node>();
		for(Node v: g){
			pageRanks.put(v,1/m);
			if(g.getChildren(v).isEmpty())
				sinks.add(v);
		}
		// iterate
		double maxDiff;
		double sum;
		Map<Node,Double> pageRanks_tmp;		
		do{			
			maxDiff = 0;
			pageRanks_tmp = new HashMap<Node,Double>();			
			for(Node v: g){
				sum = 0;
				for(Node w: g.getParents(v)){
					sum += pageRanks.get(w)/g.getChildren(w).size();
				}
				for(Node w: sinks)
					sum += pageRanks.get(w)/m;
				pageRanks_tmp.put(v, ((1-dampingFactor)/m) + (dampingFactor * sum));						
				maxDiff = Math.max(maxDiff, Math.abs(pageRanks.get(v)-pageRanks_tmp.get(v)));				
			}
			pageRanks = pageRanks_tmp;			
		}while(maxDiff > precision);		
		if(!GraphUtil.archivePageRank.containsKey(g))
			GraphUtil.archivePageRank.put(g, new HashMap<Double,Map<Double,Map<Node,Double>>>());
		if(!GraphUtil.archivePageRank.get(g).containsKey(dampingFactor))
			GraphUtil.archivePageRank.get(g).put(dampingFactor, new HashMap<Double,Map<Node,Double>>());
		GraphUtil.archivePageRank.get(g).get(dampingFactor).put(precision, pageRanks);		
		return pageRanks.get(n);
	}
	
	/**
	 * Computes the (real parts of the) Eigenvalues of the given graph.
	 * @param g some graph
	 * @return an array of double (the real parts of the Eigenvalues).
	 */
	public static ComplexNumber[] eigenvalues(Graph<? extends Node> g){
		Matrix m = g.getAdjancyMatrix();
		EigenvalueDecomposition ed = new EigenvalueDecomposition(m);		
		ComplexNumber[] result = new ComplexNumber[ed.getRealEigenvalues().length];
		for(int i = 0; i < ed.getImagEigenvalues().length; i++){
			result[i] = new ComplexNumber(ed.getRealEigenvalues()[i], ed.getImagEigenvalues()[i]);
		}			
		return result;
	}
	
}
