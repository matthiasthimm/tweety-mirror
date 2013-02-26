package net.sf.tweety.argumentation.deductive.semantics;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import net.sf.tweety.argumentation.deductive.DeductiveKnowledgeBase;
import net.sf.tweety.graphs.*;
import net.sf.tweety.logics.propositionallogic.PlBeliefSet;
import net.sf.tweety.logics.propositionallogic.syntax.Conjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;

/**
 * Instances of this class are compilations in the sense of
 * Definition 8 in<br/>
 * <br/>
 * Philippe Besnard and Anthony Hunter. Knowledgebase Compilation for Efficient Logical Argumentation.
 * In Proceedings of the 10th International Conference on Knowledge Representation (KR'06), pages
 * 123-133, AAAI Press, 2006.<br/>
 * <br/>
 * A compilation of a knowledge base is a graph where the nodes
 * are the minimal inconsistent subsets of the knowledge base
 * and the edges connect sets that have a non-empty intersection.
 * 
 * @author Matthias Thimm
 *
 */
public class Compilation extends DefaultGraph<CompilationNode>{

	/** Creates the compilation of the given knowledge base.
	 * @param kb some deductive knowledge base.
	 */
	public Compilation(DeductiveKnowledgeBase kb){
		Set<PlBeliefSet> minInconSets = kb.getMinimalInconsistentSubsets();
		for(PlBeliefSet set: minInconSets)
			this.add(new CompilationNode(set));
		Stack<CompilationNode> stackNodes = new Stack<CompilationNode>();
		stackNodes.addAll(this.getNodes());
		while(!stackNodes.isEmpty()){
			CompilationNode nodeA = stackNodes.pop();
			if(stackNodes.isEmpty())
				break;
			for(CompilationNode nodeB: stackNodes){
				PlBeliefSet temp = new PlBeliefSet(nodeA);
				temp.retainAll(nodeB);
				if(!temp.isEmpty())
					this.add(new UndirectedEdge<CompilationNode>(nodeA,nodeB));
			}
		}
	}
	
	/**
	 * Returns the argument tree for the given argument.
	 * @param arg some deductive argument.
	 * @return the argument tree for the given argument.
	 */
	public ArgumentTree getArgumentTree(DeductiveArgument arg){
		ArgumentTree argTree = new ArgumentTree(arg);
		argTree.add(arg);
		Set<CompilationNode> firstLevelNodes = this.firstLevel(arg);
		for(CompilationNode node: firstLevelNodes){
			Set<PropositionalFormula> support = new HashSet<PropositionalFormula>(node);
			support.removeAll(arg.getSupport());
			DeductiveArgument undercut = new DeductiveArgument(support,new Negation(new Conjunction(arg.getSupport())));
			argTree.add(undercut);
			argTree.add(new DirectedEdge<DeductiveArgument>(undercut,arg));
			Set<CompilationNode> remainingNodes = new HashSet<CompilationNode>(this.getNodes());
			remainingNodes.remove(node);
			this.subcuts(undercut, remainingNodes, node, new HashSet<PropositionalFormula>(arg.getSupport()), argTree);
		}
		return argTree;
	}
	
	/**
	 * This method returns the compilation nodes that can be used
	 * to construct undercuts to the given argument.
	 * @param arg some argument.
	 * @return a set of compilation nodes.
	 */
	private Set<CompilationNode> firstLevel(DeductiveArgument arg){
		Stack<CompilationNode> candidates = new Stack<CompilationNode>();
		for(CompilationNode node: this){
			Set<PropositionalFormula> set = new HashSet<PropositionalFormula>(node);
			set.retainAll(arg.getSupport());
			if(!set.isEmpty())
				candidates.add(node);
		}
		Set<CompilationNode> result = new HashSet<CompilationNode>();
		while(!candidates.isEmpty()){
			CompilationNode node = candidates.pop();
			boolean addToResult = true;
			for(CompilationNode node2: candidates){
				Set<PropositionalFormula> set1 = new HashSet<PropositionalFormula>(node);
				Set<PropositionalFormula> set2 = new HashSet<PropositionalFormula>(node2);
				set1.removeAll(arg.getSupport());
				set2.removeAll(arg.getSupport());
				if(set2.containsAll(set1)){
					addToResult = false;
					break;
				}
			}
			if(addToResult)
				for(CompilationNode node2: result){
					Set<PropositionalFormula> set1 = new HashSet<PropositionalFormula>(node);
					Set<PropositionalFormula> set2 = new HashSet<PropositionalFormula>(node2);
					set1.removeAll(arg.getSupport());
					set2.removeAll(arg.getSupport());
					if(set2.containsAll(set1)){
						addToResult = false;
						break;
					}
				}
			if(addToResult)
				result.add(node);
		}
		return result;
	}
	
	/**
	 * This method recursively builds up the argument tree from
	 * the given argument.
	 * @param arg an argument.
	 * @param remainingNodes the non-visited nodes in the compilation.
	 * @param current the current node.
	 * @param currentSupport the union of the supports of the current path.
	 * @param argTree the argument tree.
	 */
	private void subcuts(DeductiveArgument arg, Set<CompilationNode> remainingNodes, CompilationNode current, Set<PropositionalFormula> currentSupport, ArgumentTree argTree){
		for(CompilationNode node: remainingNodes){
			UndirectedEdge<CompilationNode> edge = new UndirectedEdge<CompilationNode>(current,node);
			if(this.contains(edge)){
				if(!currentSupport.containsAll(node)){
					Set<PropositionalFormula> set = new HashSet<PropositionalFormula>(arg.getSupport());
					set.retainAll(node);
					if(!set.isEmpty()){
						boolean properUndercut = true;
						for(Edge<CompilationNode> edge2: this.getEdges()){
							if(!edge2.equals(edge) && (edge2.getNodeA() == current || edge2.getNodeB() == current)){
								Set<PropositionalFormula> set1 = new HashSet<PropositionalFormula>(node);
								Set<PropositionalFormula> set2;
								if(edge2.getNodeA() == current)
									set2 = new  HashSet<PropositionalFormula>(edge2.getNodeB());
								else set2 = new  HashSet<PropositionalFormula>(edge2.getNodeA());
								set1.retainAll(arg.getSupport());
								set2.retainAll(arg.getSupport());
								if(set1.containsAll(set2)){
									properUndercut = false;
									break;
								}
							}
						}
						if(properUndercut){
							Set<PropositionalFormula> support = new HashSet<PropositionalFormula>(node);
							support.removeAll(arg.getSupport());
							DeductiveArgument undercut = new DeductiveArgument(support,new Negation(new Conjunction(arg.getSupport())));
							argTree.add(undercut);
							argTree.add(new DirectedEdge<DeductiveArgument>(undercut,arg));
							Set<CompilationNode> newRemainingNodes = new HashSet<CompilationNode>(remainingNodes);
							newRemainingNodes.remove(node);
							Set<PropositionalFormula> newSupport = new HashSet<PropositionalFormula>(support);
							newSupport.addAll(undercut.getSupport());
							this.subcuts(undercut, newRemainingNodes, node, newSupport, argTree);
						}
					}
				}
			}
		}
	}
}
