package net.sf.tweety.graphs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Instance of this class represent graphs with
 * nodes of type T
 * 
 * @author Matthias Thimm
 * 
 * @param <T> The type of the node.
 */
public class Graph<T extends Node> implements Iterable<T>{

	/** The set of nodes */
	private Set<T> nodes;
	
	/** The set of edges */
	private Set<Edge<T>> edges;
	
	/**
	 * Creates an empty graph.
	 */
	public Graph(){
		this.nodes = new HashSet<T>();
		this.edges = new HashSet<Edge<T>>();
	}
	
	/**
	 * Adds the given node to this graph.
	 * @param node some node.
	 */
	public void add(T node){
		this.nodes.add(node);
	}
	
	/**
	 * Adds the given edge to this graph. If at least one
	 * of the nodes the given edge connects is not in the
	 * graph, an illegal argument exception is thrown.
	 * @param node some edge.
	 */
	public void add(Edge<T> edge){
		if(!this.nodes.contains(edge.getNodeA()) || !this.nodes.contains(edge.getNodeB()))
			throw new IllegalArgumentException("The edge connects node that are not in this graph.");
		this.edges.add(edge);
	}
	
	/**
	 * Returns the nodes of this graph.
	 * @return the nodes of this graph.
	 */
	public Set<T> getNodes(){
		return this.nodes;
	}
	
	/**
	 * Returns the edges of this graph.
	 * @return the edges of this graph.
	 */
	public Set<Edge<T>> getEdges(){
		return this.edges;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return this.nodes.iterator();
	}

	/** 
	 * Returns "true" when this graph contains the given
	 * node or edge.
	 * @param some object
	 * @return "true" if this graph contains the given
	 * node or edge.
	 */
	public boolean contains(Object obj){
		return this.nodes.contains(obj) || this.edges.contains(obj); 
	}

	/**
	 * Returns the set of neighbors of the given node.
	 * @param node some node (must be in the graph).
	 * @return the set of neighbors of the given node.
	 */
	public Set<T> getNeighbors(T node){
		if(!this.nodes.contains(node))
			throw new IllegalArgumentException("The node is not in this graph.");
		Set<T> neighbors = new HashSet<T>();
		for(Edge<T> edge: this.edges){
			if(edge.getNodeA() == node)
				neighbors.add(edge.getNodeB());
			else if(edge.getNodeB() == node)
				neighbors.add(edge.getNodeA());
		}
		return neighbors;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "<" + this.nodes + "," + this.edges + ">";
	}
}
