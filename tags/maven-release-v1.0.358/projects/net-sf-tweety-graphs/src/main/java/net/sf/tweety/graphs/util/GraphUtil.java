package net.sf.tweety.graphs.util;

import java.util.*;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import net.sf.tweety.graphs.Graph;
import net.sf.tweety.graphs.Node;
import net.sf.tweety.math.ComplexNumber;
import net.sf.tweety.util.MapTools;

/**
 * This abstract class contains some auxiliary methods for working
 * with graphs.
 * 
 * @author Matthias Thimm
 */
public abstract class GraphUtil {

	/** For archiving page rank values. */
	private static Map<Graph<? extends Node>,Map<Double,Map<Double,Map<Node,Double>>>> archivePageRank = new HashMap<Graph<? extends Node>,Map<Double,Map<Double,Map<Node,Double>>>>();
	
	/** For archiving HITS rank values. */
	private static Map<Graph<? extends Node>,Map<Double,Map<Node,Double>>> archiveHITSAuthRank = new HashMap<Graph<? extends Node>,Map<Double,Map<Node,Double>>>();
	private static Map<Graph<? extends Node>,Map<Double,Map<Node,Double>>> archiveHITSHubRank = new HashMap<Graph<? extends Node>,Map<Double,Map<Node,Double>>>();
	
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
	 * Computes the HITS rank of the given node in the given graph.
	 * @param g a graph
	 * @param n a node
	 * @param precision the precision (smaller values mean higher precision)
	 * @return the HITS rank of the given node in the given graph.
	 */
	public static Double hitsRank(Graph<? extends Node> g, Node n, double precision, boolean getAuth){
		if(getAuth){
			if(GraphUtil.archiveHITSAuthRank.containsKey(g) &&
					GraphUtil.archiveHITSAuthRank.get(g).containsKey(precision))
				return GraphUtil.archiveHITSAuthRank.get(g).get(precision).get(n);
		}else{
			if(GraphUtil.archiveHITSHubRank.containsKey(g) &&
					GraphUtil.archiveHITSHubRank.get(g).containsKey(precision))
				return GraphUtil.archiveHITSHubRank.get(g).get(precision).get(n);
		}
		Map<Node,Double> auth = new HashMap<Node,Double>();
		Map<Node,Double> hub = new HashMap<Node,Double>();
		// init
		for(Node v: g){
			auth.put(v,1d);
			hub.put(v,1d);			
		}
		// iterate
		double maxDiff;
		double sum;
		double norm;
		Map<Node,Double> auth_tmp, hub_tmp;		
		do{						
			maxDiff = 0;
			norm = 0;
			auth_tmp = new HashMap<Node,Double>();
			for(Node v: g){
				sum = 0;
				for(Node w: g.getParents(v))
					sum += hub.get(w);
				auth_tmp.put(v, sum);
				norm += Math.pow(sum, 2);
			}
			norm = Math.sqrt(norm);
			for(Node v: g){
				auth_tmp.put(v, auth_tmp.get(v) / norm);
				maxDiff = Math.max(maxDiff, Math.abs(auth.get(v)-auth_tmp.get(v)));
			}
			norm = 0;
			hub_tmp = new HashMap<Node,Double>();
			for(Node v: g){
				sum = 0;
				for(Node w: g.getChildren(v))
					sum += auth.get(w);
				hub_tmp.put(v, sum);
				norm += Math.pow(sum, 2);
			}
			norm = Math.sqrt(norm);
			for(Node v: g){
				hub_tmp.put(v, hub_tmp.get(v) / norm);
				maxDiff = Math.max(maxDiff, Math.abs(hub.get(v)-hub_tmp.get(v)));
			}
			auth = auth_tmp;
			hub = hub_tmp;			
		}while(maxDiff > precision);	
		if(!GraphUtil.archiveHITSHubRank.containsKey(g))
			GraphUtil.archiveHITSHubRank.put(g, new HashMap<Double,Map<Node,Double>>());		
		GraphUtil.archiveHITSHubRank.get(g).put(precision,hub);		
		if(!GraphUtil.archiveHITSAuthRank.containsKey(g))
			GraphUtil.archiveHITSAuthRank.put(g, new HashMap<Double,Map<Node,Double>>());		
		GraphUtil.archiveHITSAuthRank.get(g).put(precision,auth);
		return getAuth ? auth.get(n) : hub.get(n);		
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
	
	/**
	 * Checks whether the two graphs are isomorphic.
	 * @param g1 some graph.
	 * @param g2 some graph.
	 * @return "true" iff the two graphs are isomorphic.
	 */
	public static boolean isIsomorphic(Graph<? extends Node> g1, Graph<? extends Node> g2){
		// NOTE: we simply try out every possible permutation (note that this is an NP-hard problem anyway)
		MapTools<Node, Node> mapTools = new MapTools<Node,Node>();
		for(Map<Node,Node> isomorphism: mapTools.allBijections(new HashSet<Node>(g1.getNodes()), new HashSet<Node>(g2.getNodes()))){
			boolean isomorphic = true;
			for(Node a: g1){
				for(Node b: g1.getChildren(a)){
					if(!g2.getChildren(isomorphism.get(a)).contains(isomorphism.get(b))){
						isomorphic = false;
						break;
					}
				}
				if(!isomorphic)
					break;
			}
			if(isomorphic)
				return true;
		}		
		return false;
	}
	
}