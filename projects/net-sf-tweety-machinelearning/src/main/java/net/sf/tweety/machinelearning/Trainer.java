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
}
