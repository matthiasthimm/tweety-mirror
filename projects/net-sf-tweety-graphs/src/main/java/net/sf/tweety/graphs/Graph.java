package net.sf.tweety.graphs;

import java.util.Collection;
import java.util.Iterator;

import Jama.Matrix;

/**
 * Common interface for graphs with
 * nodes of type T
 * 
 * @author Matthias Thimm
 * 
 * @param <T> The type of the node.
 */
public interface Graph<T extends Node> extends Iterable<T>{

	/**
	 * Adds the given node to this graph.
	 * @param node some node.
	 */
	public void add(T node);
	
	/**
	 * Adds the given edge to this graph. If at least one
	 * of the nodes the given edge connects is not in the
	 * graph, an illegal argument exception is thrown.
	 * @param node some edge.
	 */
	public void add(Edge<T> edge);
	
	/**
	 * Returns the nodes of this graph.
	 * @return the nodes of this graph.
	 */
	public Collection<T> getNodes();
	
	/**
	 * Returns the number of nodes in this graph.
	 * @return the number of nodes in this graph.
	 */
	public int getNumberOfNodes();
	
	/**
	 * Returns "true" iff the two nodes are connected by a directed edge
	 * from a to b or an undirected edge.
	 * @param a some node
	 * @param b some node
	 * @return "true" iff the two nodes are connected by a directed edge
	 * from a to b or an undirected edge.
	 */
	public boolean areAdjacent(T a, T b);
	
	/**
	 * Returns the edges of this graph.
	 * @return the edges of this graph.
	 */
	public Collection<Edge<T>> getEdges();

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator();

	/** 
	 * Returns "true" when this graph contains the given
	 * node or edge.
	 * @param some object
	 * @return "true" if this graph contains the given
	 * node or edge.
	 */
	public boolean contains(Object obj);
	
	/**
	 * Returns the set of children (node connected via an undirected edge or a directed edge
	 * where the given node is the parent) of the given node.
	 * @param node some node (must be in the graph).
	 * @return the set of children of the given node.
	 */
	public Collection<T> getChildren(Node node);
	
	/**
	 * Returns the set of parents (node connected via an undirected edge or a directed edge
	 * where the given node is the child) of the given node.
	 * @param node some node (must be in the graph).
	 * @return the set of parents of the given node.
	 */
	public Collection<T> getParents(Node node);
	
	/**
	 * Checks whether there is a (directed) path from node1 to node2.
	 * @param node1 some node.
	 * @param node2 some node.
	 * @return "true" if there is a directed path from node1 to node2.
	 */
	public boolean existsDirectedPath(T node1, T node2);
	
	/**
	 * Returns the set of neighbors of the given node.
	 * @param node some node (must be in the graph).
	 * @return the set of neighbors of the given node.
	 */
	public Collection<T> getNeighbors(Node node);
	
	/**
	 * Returns the adjacency matrix of this graph (the order
	 * of the nodes is the same as returned by "iterator()").
	 * @return the adjacency matrix of this graph
	 */
	public Matrix getAdjancyMatrix();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString();
}
