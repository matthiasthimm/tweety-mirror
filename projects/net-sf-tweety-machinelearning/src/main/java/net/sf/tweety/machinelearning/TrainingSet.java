package net.sf.tweety.machinelearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

import libsvm.svm_node;
import libsvm.svm_problem;
import net.sf.tweety.commons.util.Pair;

/**
 * A set of observations together with their category. 
 * @author Matthias Thimm
 *
 * @param <S> The type of the observations.
 * @param <T> The type of the categories.
 */
public class TrainingSet<S extends Observation, T extends Category> extends HashSet<Pair<S,T>>{

	/** For serialization. */
	private static final long serialVersionUID = 6814079760992723045L;
	
	/**
	 * Adds the specified elements as a pair to this set
	 * if it is not already present. More formally,
	 * adds the specified element e to this set if
	 * this set contains no element e2 such that
	 * (e==null ? e2==null : e.equals(e2)). If this
	 * set already contains the element, the call
	 * leaves the set unchanged and returns false.
	 * @param obs some observation
	 * @param cat the category of the observation.
	 * @return see above.
	 */
	public boolean add(S obs, T cat){
		return this.add(new Pair<S,T>(obs,cat));
	}

	/**
	 * Returns the collection of categories present in this 
	 * training set.
	 * @return a set of categories.
	 */
	public Collection<T> getCategories(){
		Collection<T> cats = new HashSet<T>();
		for(Pair<S,T> entry: this)
			cats.add(entry.getSecond());
		return cats;
	}
	
	/**
	 * Returns all observations of the given category.
	 * @param cat a category
	 * @return all observations of the given category.
	 */
	public TrainingSet<S,T> getObservations(T cat){
		TrainingSet<S,T> result = new TrainingSet<S,T>();
		for(Pair<S,T> entry: this)
			if(entry.getSecond().equals(cat))
				result.add(entry);
		return result;
	}
	
	/**
	 * Returns a svm_problem (the data data model of libsvm) of this training set.
	 * @return a svm_problem (the data data model of libsvm) of this training set.
	 */
	public svm_problem toLibsvmProblem(){
		svm_problem problem = new svm_problem();
		problem.l = this.size();
		problem.y = new double[problem.l];
		problem.x = new svm_node[problem.l][];
		int idx = 0;
		for(Pair<S,T> entry: this){
			problem.y[idx] = entry.getSecond().asDouble();
			problem.x[idx] = entry.getFirst().toSvmNode();
			idx++;
		}
		return problem;
	}
	
	/**
	 * Loads a training file in LIBSVM syntax
	 * @param file some file
	 * @return a training set.
	 * @throws IOException if some IO exception occurs.
	 * @throws NumberFormatException if the format of the file is not valid.
	 */
	public static TrainingSet<DefaultObservation, DoubleCategory> loadLibsvmTrainingFile(File file) throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line;				
		TrainingSet<DefaultObservation, DoubleCategory> set = new TrainingSet<DefaultObservation, DoubleCategory>();
		while ((line = br.readLine()) != null) {
			StringTokenizer tokens = new StringTokenizer(line, " ");
			DoubleCategory cat = new DoubleCategory(new Double(tokens.nextToken()));
			DefaultObservation obs = new DefaultObservation();
		    while(tokens.hasMoreElements()){
		    	StringTokenizer tokens2 = new StringTokenizer(tokens.nextToken(), ":");
		    	tokens2.nextToken();
		    	obs.add(new Double(tokens2.nextToken()));
		    }
		    set.add(obs,cat);
		}
		br.close();
		return set;
	}
	
}
