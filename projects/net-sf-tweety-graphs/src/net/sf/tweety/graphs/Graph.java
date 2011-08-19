package net.sf.tweety.graphs;

import java.util.*;

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
	
	/**
	 * Returns the set of children (node connected via an undirected edge or a directed edge
	 * where the given node is the parent) of the given node.
	 * @param node some node (must be in the graph).
	 * @return the set of children of the given node.
	 */
	public Set<T> getChildren(T node){
		if(!this.nodes.contains(node))
			throw new IllegalArgumentException("The node is not in this graph.");
		Set<T> children = new HashSet<T>();
		for(Edge<T> edge: this.edges){
			if(edge.getNodeA() == node)
				children.add(edge.getNodeB());
			else if(edge.getNodeB() == node && (edge instanceof UndirectedEdge))
				children.add(edge.getNodeA());
		}
		return children;
	}
	
	/**
	 * Checks whether there is a (directed) path from node1 to node2.
	 * @param node1 some node.
	 * @param node2 some node.
	 * @return "true" if there is a directed path from node1 to node2.
	 */
	public boolean existsDirectedPath(T node1, T node2){
		if(!this.nodes.contains(node1) || !this.nodes.contains(node2))
			throw new IllegalArgumentException("The nodes are not in this graph.");
		if(node1 == node2)
			return true;
		// we perform a DFS.
		Stack<T> stack = new Stack<T>();
		stack.addAll(this.getChildren(node1));
		while(!stack.isEmpty()){
			T node = stack.pop();
			if(node == node2)
				return true;
			stack.addAll(this.getChildren(node));
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "<" + this.nodes + "," + this.edges + ">";
	}
}
