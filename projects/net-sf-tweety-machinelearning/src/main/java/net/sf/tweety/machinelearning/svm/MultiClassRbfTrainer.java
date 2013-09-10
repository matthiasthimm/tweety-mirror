package net.sf.tweety.machinelearning.svm;

import libsvm.svm;
import libsvm.svm_parameter;
import net.sf.tweety.machinelearning.DefaultObservation;
import net.sf.tweety.machinelearning.DoubleCategory;
import net.sf.tweety.machinelearning.Trainer;
import net.sf.tweety.machinelearning.TrainingSet;

/**
 * Trains a standard multi-class RBF support vector machine.
 * @author Matthias Thimm
 *
 */
public class MultiClassRbfTrainer implements Trainer<DefaultObservation,DoubleCategory> {

	/**The c parameter for learning */
	private double c; 
	/** The gamma parameter for learning */
	private double gamma;
	
	/**
	 * Initializes the trainer with the given parameters.
	 * @param c the c parameter for learning
	 * @param gamma the gamma parameter for learning
	 */
	public MultiClassRbfTrainer(double c, double gamma){
		this.c = c;
		this.gamma = gamma;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.machinelearning.Trainer#train(net.sf.tweety.machinelearning.TrainingSet)
	 */
	@Override
	public SupportVectorMachine train(TrainingSet<DefaultObservation, DoubleCategory> trainingSet) {
		svm_parameter param = new svm_parameter();
		//TODO the following properties should be parameterized
		// Type of SVM
		param.svm_type = svm_parameter.C_SVC;
		// Kernel type (leave it at RBF for now)
		param.kernel_type = svm_parameter.RBF;
		// stopping criteria
		param.eps = 0.001;
		// cache size of kernel
		param.cache_size = 256;
		// do not set penalties for specific classes
		param.nr_weight = 0;

		// Given parameters
		// gamma parameter of RBF kernel
		param.gamma = this.gamma;
		// C parameter of RBF kernel
		param.C = this.c;
				
		return new SupportVectorMachine(svm.svm_train(trainingSet.toLibsvmProblem(), param));		
	}

}
