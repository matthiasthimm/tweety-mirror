package net.sf.tweety.logicprogramming.asplibrary.solver;

import net.sf.tweety.logicprogramming.asplibrary.util.*;

/**
 * this interface models a callback that classes
 * should implement if they want run a solver and
 * a java application asynchronously.
 * 
 * @author Thomas Vengels
 */
public interface SolverEvent<T> {

	/**
	 * this method informs a caller whenever
	 * a model was successfully parsed from
	 * a solver. a return value indicates if
	 * another model should be parsed or if
	 * the solving process should be aborted.
	 * 
	 * @param as answer set ontained
	 * @param T user context
	 * @return true if another model should be parsed,
	 * 		   false if not.
	 */
	public boolean	oneModelComplete(T Context, AnswerSet as);
	
	/**
	 * this method is called when
	 * @param Context
	 * 
	 * @param T user context
	 * @param asl
	 */
	public void allModelsComplete(T Context, AnswerSetList asl);
	
	/**
	 * this method informs a caller that a solver process
	 * exceeded its time limit. a timeout will not happen
	 * once a first model is being parsed by the asp library.
	 * 
	 * @param Context user context
	 * @return
	 */
	public boolean onTimeOut(T Context);
	
	public boolean onBeginModels(T Context);
	
	public void onFailure(T Context, SolverException err);
}
