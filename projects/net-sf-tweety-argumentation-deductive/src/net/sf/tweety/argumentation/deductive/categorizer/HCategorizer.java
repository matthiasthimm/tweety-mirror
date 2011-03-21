package net.sf.tweety.argumentation.deductive.categorizer;

import java.util.Set;

import net.sf.tweety.argumentation.deductive.semantics.ArgumentTree;
import net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument;

/**
 * This class implements the h-categorizer from<br/>
 * <br/>
 * Philippe Besnard and Anthony Hunter. A logic-based theory of deductive arguments.
 * In Artificial Intelligence, 128(1-2):203-235, 2001.
 * 
 * @author Matthias Thimm
 */
public class HCategorizer implements Categorizer{

	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.deductive.categorizer.Categorizer#categorize(net.sf.tweety.argumentation.deductive.semantics.ArgumentTree)
	 */
	@Override
	public double categorize(ArgumentTree argumentTree) {
		return this.categorize(argumentTree,null,argumentTree.getRoot());
	}
	
	/**
	 * Categorizes the node in the given tree.
	 * @param argumentTree some argument tree.
	 * @param parent the parent of the current node.
	 * @param node some node.
	 * @return the categorization of the node.
	 */
	private double categorize(ArgumentTree argumentTree, DeductiveArgument parent, DeductiveArgument node){
		Set<DeductiveArgument> children = argumentTree.getNeighbors(node);
		if(parent != null) children.remove(parent);
		if(children.isEmpty())
			return 1;
		double sumChildren = 1;
		for(DeductiveArgument child: children)
			sumChildren += this.categorize(argumentTree, node, child);
		return 1d/sumChildren;
	}

}
