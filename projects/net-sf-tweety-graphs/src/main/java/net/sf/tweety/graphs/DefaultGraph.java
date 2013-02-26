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
public class DefaultGraph<T extends Node> implements Graph<T>{

	/** The set of nodes */
	private Set<T> nodes;
	
	/** The set of edges */
	private Set<Edge<T>> edges;
	
	/**
	 * Creates an empty graph.
	 */
	public DefaultGraph(){
		this.nodes = new HashSet<T>();
		this.edges = new HashSet<Edge<T>>();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#add(net.sf.tweety.graphs.Node)
	 */
	public void add(T node){
		this.nodes.add(node);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#add(net.sf.tweety.graphs.Edge)
	 */
	public void add(Edge<T> edge){
		if(!this.nodes.contains(edge.getNodeA()) || !this.nodes.contains(edge.getNodeB()))
			throw new IllegalArgumentException("The edge connects node that are not in this graph.");
		this.edges.add(edge);
	}
		
	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getNodes()
	 */
	public Collection<T> getNodes(){
		return this.nodes;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getEdges()
	 */
	public Collection<Edge<T>> getEdges(){
		return this.edges;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return this.nodes.iterator();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#contains(java.lang.Object)
	 */
	public boolean contains(Object obj){
		return this.nodes.contains(obj) || this.edges.contains(obj); 
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getNeighbors(net.sf.tweety.graphs.Node)
	 */
	public Collection<T> getNeighbors(Node node){
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
	 * @see net.sf.tweety.graphs.Graph#getChildren(net.sf.tweety.graphs.Node)
	 */
	public Collection<T> getChildren(Node node){
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
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#getParents(net.sf.tweety.graphs.Node)
	 */
	public Collection<T> getParents(Node node){
		if(!this.nodes.contains(node))
			throw new IllegalArgumentException("The node is not in this graph.");
		Set<T> parents = new HashSet<T>();
		for(Edge<T> edge: this.edges){
			if(edge.getNodeB() == node)
				parents.add(edge.getNodeA());
			else if(edge.getNodeA() == node && (edge instanceof UndirectedEdge))
				parents.add(edge.getNodeB());
		}
		return parents;
	}
	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.graphs.Graph#existsDirectedPath(net.sf.tweety.graphs.Node, net.sf.tweety.graphs.Node)
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
