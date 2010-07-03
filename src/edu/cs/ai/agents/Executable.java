package edu.cs.ai.agents;

/**
 * An executable is an action an agent performs within an environment.
 * 
 * @author Matthias Thimm
 */
public interface Executable {

	/**
	 * This constant represents the null operation.
	 */
	public static final Executable NO_OPERATION = new Executable(){
		public String toString(){ return "NO_OPERATION";}		
	};
}
