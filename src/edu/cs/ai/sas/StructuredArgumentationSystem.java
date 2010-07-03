package edu.cs.ai.sas;

import java.util.*;

import edu.cs.ai.agents.*;
import edu.cs.ai.kr.dung.semantics.*;
import edu.cs.ai.kr.dung.syntax.*;
import edu.cs.ai.kr.saf.*;

/**
 * This class represents a structured argumentation system, i.e. a set of agents
 * that argue within some given protocol based on structured argumentation frameworks.
 * 
 * @author Matthias Thimm
 */
public class StructuredArgumentationSystem extends MultiAgentSystem<SasAgent> {

	/**
	 * This class models an environment for structured argumentation systems.
	 * @author Matthias Thimm
	 */
	private static class SasEnvironment implements Environment {

		/**
		 * The common view of all agents.
		 */
		private StructuredArgumentationFramework commonView;
		
		/**
		 * The underlying structured argumentation framework.
		 */
		private StructuredArgumentationFramework saf;
		
		/**
		 * Creates a new SasEnvironment based on the given SAF.
		 * @param saf a structured argumentation framework.
		 */
		public SasEnvironment(StructuredArgumentationFramework saf){
			this.saf = saf;
			this.commonView = new StructuredArgumentationFramework();
		}
		
		/* (non-Javadoc)
		 * @see edu.cs.ai.agents.Environment#execute(edu.cs.ai.agents.Executable)
		 */
		@Override
		public Set<Perceivable> execute(Executable action) {
			if(!(action instanceof Extension))
				throw new IllegalArgumentException("SasEnvironment expects action of type 'Extension'.");
			Extension e = (Extension) action;
			if(!(this.saf.containsAll(e)))
				throw new IllegalArgumentException("Action contains unknown arguments.");
			this.commonView.addAll(e);
			for(Argument a: this.commonView){
				for(Argument b: this.commonView)
					if(saf.isAttackedBy(a, b))
						this.commonView.add(new Attack(b,a));
			}				
			return this.getPercepts(null);
		}

	
		/* (non-Javadoc)
		 * @see edu.cs.ai.agents.Environment#execute(java.util.Collection)
		 */
		@Override
		public Set<Perceivable> execute(Collection<? extends Executable> actions) {
			for(Executable action: actions)
				this.execute(action);
			return this.getPercepts(null);
		}

		/* (non-Javadoc)
		 * @see edu.cs.ai.agents.Environment#getPercepts(edu.cs.ai.agents.Agent)
		 */
		@Override
		public Set<Perceivable> getPercepts(Agent agent) {
			Set<Perceivable> percepts = new HashSet<Perceivable>();
			percepts.add(this.commonView);
			return percepts;
		}
		
	}
	
	/**
	 * Creates a new StructuredArgumentationSystem
	 * @param saf the underlying structured argumentation framework
	 */
	public StructuredArgumentationSystem(StructuredArgumentationFramework saf) {
		this(saf,new HashSet<SasAgent>());
	}
	
	/**
	 * Creates a new StructuredArgumentationSystem with the given collection of agents.
	 * @param saf the underlying structured argumentation framework
	 * @param agents a collection of agents.
	 */
	public StructuredArgumentationSystem(StructuredArgumentationFramework saf, Collection<? extends SasAgent> agents){
		super(new SasEnvironment(saf),agents);
	}
}
