package net.sf.tweety.agents;

/**
 * Classes implementing this interface represent protocols 
 * for games in the game theoretic meaning.
 * @author Matthias Thimm
 */
public interface GameProtocol {
	
	/**
	 * Returns "true" if the game has finished and a winner
	 * is determined, otherwise it returns "false"
	 * @return "true" if the game has finished and a winner
	 * is determined, otherwise it returns "false" 
	 */
	public boolean hasWinner();
	
	/**
	 * If the game has a winner, this methods returns it.
	 * Otherwise it throws a RuntimeException.
	 * @return the winner of the game.
	 */
	public Agent getWinner();
}
