package net.sf.tweety.machinelearning;

/**
 * @author Matthias Thimm
 *
 * @param <S> The type of the observations.
 * @param <T> The type of the categories.
 */
public interface Trainer<S extends Observation, T extends Category> {

	/**
	 * Trains a classifier on the given training set.
	 * @param trainingSet some training set.
	 * @return a classifier
	 */
	public Classifier train(TrainingSet<S,T> trainingSet);
	
	/**
	 * Trains a classifier on the given training set with the given parameters
	 * @param trainingSet some training set.
	 * @param params parameters for the training.
	 * @return a classifier
	 */
	public Classifier train(TrainingSet<S,T> trainingSet, ParameterSet params);
	
	/**
	 * Returns the set of parameters for this trainer.
	 * @return the set of parameters for this trainer.
	 */
	public ParameterSet getParameterSet();
	
	/** 
	 * Sets the parameters for this trainer (calling this
	 * method must ensure that the next time <code>train(TrainingSet<S,T> trainingSet)</code>
	 * is used it uses these parameters.
	 * @param params a parameter set.
	 * @return true if the operation was successful.
	 */
	public boolean setParameterSet(ParameterSet params);
}
