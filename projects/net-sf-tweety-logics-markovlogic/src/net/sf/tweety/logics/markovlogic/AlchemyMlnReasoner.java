package net.sf.tweety.logics.markovlogic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.markovlogic.syntax.MlnFormula;

/**
 * This class implements a wrapper for Alchemy in order to
 * reason with MLNs.
 * 
 * @author Matthias Thimm
 */
public class AlchemyMlnReasoner extends AbstractMlnReasoner {

	/** The console command for Alchemy inference. */
	private String inferCmd = "infer";
		
	/**
	 * Creates a new AlchemyMlnReasoner for the given Markov logic network.
	 * @param beliefBase a Markov logic network. 
	 */
	public AlchemyMlnReasoner(BeliefBase beliefBase){
		this(beliefBase, (FolSignature) beliefBase.getSignature());
	}
	
	/**
	 * Creates a new AlchemyMlnReasoner for the given Markov logic network.
	 * @param beliefBase a Markov logic network. 
	 * @param name another signature (if the probability distribution should be defined 
	 * on that one (that one should subsume the signature of the Markov logic network)
	 */
	public AlchemyMlnReasoner(BeliefBase beliefBase, FolSignature signature){
		super(beliefBase, signature);		
	}

	/** Sets the console command for Alchemy inference (default is 'infer').
	 * @param inferCmd the console command for Alchemy inference. 
	 */
	public void setAlchemyInferenceCommand(String inferCmd){
		this.inferCmd = inferCmd;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.AbstractMlnReasoner#doQuery(net.sf.tweety.logics.firstorderlogic.syntax.FolFormula)
	 */
	@Override
	public double doQuery(FolFormula query) {
		// NOTE: as the query formula might be an arbitrary formula
		// and Alchemy only supports querying the probabilities
		// of atoms, we need to encode the query in the MLN
		// by stating it to be equivalent to some new atom
		try{
			File mlnFile = this.writeAlchemyMlnFile((MarkovLogicNetwork)this.getKnowledgBase(), this.getSignature(), query);			
			//empty evidence file needed
			File evidenceFile = File.createTempFile("alchemy_ev",null);
			evidenceFile.deleteOnExit();
			//result file
			File resultFile = File.createTempFile("alchemy_res",null);
			resultFile.deleteOnExit();
			//execute Alchemy inference on problem and retrieve console output		
			//TODO parametrize parameters
			Process child = Runtime.getRuntime().exec(this.inferCmd + " -i " + mlnFile.getAbsolutePath() + " -q tweetyQueryFormula(TWEETYQUERYCONSTANT) -e " + evidenceFile.getAbsolutePath() + " -r " + resultFile.getAbsolutePath() + " -p true -numChains 50 -epsilonError 0.000001 -fracConverged 0.9999 -delta 0.0001");
			//int c;				
			//String output = "", error = "";
	        InputStream in = child.getInputStream();
	        while ((in.read()) != -1);
	            //output += ((char)c);
	        in.close();		        		        
	        in = child.getErrorStream();
	        while ((in.read()) != -1);
	            //error += (char)c;
	        in.close();	
	        FileInputStream inStream = new FileInputStream(resultFile.getAbsoluteFile());
			BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(inStream)));
			String strLine;
			while((strLine = br.readLine()) != null){
				System.out.append(strLine);				
			}
			in.close();
			//TODO go on
	        //System.out.println(output);
	        //System.out.println();
	        //System.out.println(error);
			return -1;
		}catch(IOException e) {
			// TODO
			e.printStackTrace();
			return -1;
		}		
	}
	
	/** Writes the given MLN wrt. the given signature to a temporary file
	 * in Alchemy syntax.
	 * @param mln some MLN.
	 * @param signature some fol signature.
	 * @param queryFormula the query formula that has to be encoded as well.
	 * @return the file object of the Alchemy MLN file. 
	 * @throws IOException if file writing fails.
	 */
	private File writeAlchemyMlnFile(MarkovLogicNetwork mln, FolSignature signature, FolFormula formula) throws IOException{
		File mlnFile = File.createTempFile("alchemy_mln",null);
		mlnFile.deleteOnExit();		
		FileWriter fstream = new FileWriter(mlnFile.getAbsoluteFile());
		BufferedWriter out = new BufferedWriter(fstream);
		// write sorts
		for(Sort s: signature.getSorts()){
			out.append(s.getName().toLowerCase() + " = {");
			boolean isFirst = true;
			for(Constant c: s.getConstants()){
				if(isFirst){
					out.append(c.getName().toUpperCase());
					isFirst = false;
				}else{
					out.append("," + c.getName().toUpperCase());
				}
			}
			out.append("}\n");
		}
		// write query sort
		out.append("tweetyqueryconstant = {TWEETYQUERYCONSTANT}\n");
		out.append("\n");
		// write predicates
		for(Predicate p: signature.getPredicates()){
			out.append(p.getName().toLowerCase());
			if(p.getArguments().size()>0)
				out.append("(");
			boolean isFirst = true;
			for(Sort s: p.getArguments()){
				if(isFirst){
					out.append(s.getName().toLowerCase());
					isFirst = false;
				}else{
					out.append("," + s.getName().toLowerCase());
				}
			}				
			if(p.getArguments().size()>0)
				out.append(")\n");
		}
		// write query predicate
		out.append("tweetyQueryFormula(tweetyqueryconstant)");
		out.append("\n");	
		// write query formula
		out.append("tweetyQueryFormula(TWEETYQUERYCONSTANT) <=> " + formula + " .\n\n");
		// write formulas
		for(MlnFormula f: mln){
			if(f.isStrict())
				out.append(this.alchemyStringForFormula(f.getFormula()) + " .\n");
			else
				out.append(f.getWeight() + " " + this.alchemyStringForFormula(f.getFormula()) + "\n");
		}
		out.close();
		return mlnFile;
	}
	
	/**
	 * Returns the string in Alchemy syntax representing the given formula.
	 * @param formula some FOL formula
	 * @return the string in Alchemy syntax representing the given formula.
	 */
	private String alchemyStringForFormula(FolFormula formula){
		if(formula instanceof Conjunction){
			String result = "";
			boolean isFirst = true;
			for(RelationalFormula f: ((Conjunction)formula)){
				if(isFirst){					
					result += "(" + this.alchemyStringForFormula((FolFormula)f) + ")";
					isFirst = false;
				}else{
					result += " ^ (" + this.alchemyStringForFormula((FolFormula)f) + ")";
				}
			}
			return result;
		}
		if(formula instanceof Disjunction){
			String result = "";
			boolean isFirst = true;
			for(RelationalFormula f: ((Disjunction)formula)){
				if(isFirst){					
					result += "(" + this.alchemyStringForFormula((FolFormula)f) + ")";
					isFirst = false;
				}else{
					result += " v (" + this.alchemyStringForFormula((FolFormula)f) + ")";
				}
			}
			return result;
		}
		if(formula instanceof Negation){
			return "!(" + this.alchemyStringForFormula(((Negation)formula).getFormula()) + ")";
		}
		if(formula instanceof ForallQuantifiedFormula){
			Collection<Variable> vars = ((ForallQuantifiedFormula)formula).getQuantifierVariables();
			String result = "FORALL ";
			boolean isFirst = true;
			for(Variable v: vars){
				if(isFirst){
					result += v.toString().toLowerCase();
					isFirst = false;
				}else{
					result += "," + v.toString().toLowerCase();
				}
			}
			return result + " (" + this.alchemyStringForFormula(((ForallQuantifiedFormula)formula).getFormula()) + ")";
		}
		if(formula instanceof ExistsQuantifiedFormula){
			Collection<Variable> vars = ((ExistsQuantifiedFormula)formula).getQuantifierVariables();
			String result = "EXIST ";
			boolean isFirst = true;
			for(Variable v: vars){
				if(isFirst){
					result += v.toString().toLowerCase();
					isFirst = false;
				}else{
					result += "," + v.toString().toLowerCase();
				}
			}
			return result + " (" + this.alchemyStringForFormula(((ExistsQuantifiedFormula)formula).getFormula()) + ")";
		}
		if(formula instanceof Atom){
			Atom a = (Atom) formula;
			String result = a.getPredicate().getName().toLowerCase();
			if(a.getArguments().size()>0)
				result += "(";
			boolean isFirst = true;
			for(Term t: a.getArguments()){
				if(isFirst){
					result += this.alchemyStringForTerm(t);
					isFirst = false;
				}else{
					result += "," + this.alchemyStringForTerm(t);
				}
			}
			if(a.getArguments().size()>0)
				result += ")";
			return result;
		}
		throw new IllegalArgumentException("Representation of tautologies and contradictions not supported in Alchemy.");
	}
	
	/**
	 * Returns the string in Alchemy syntax representing the given term.
	 * @param tern some FOL tern
	 * @return the string in Alchemy syntax representing the given term.
	 */
	private String alchemyStringForTerm(Term t){
		if(t instanceof Constant)
			return t.toString().toUpperCase();
		if(t instanceof Variable)
			return t.toString().toLowerCase();
		throw new IllegalArgumentException("Functional expressions not supported by Alchemy.");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.AbstractMlnReasoner#reset()
	 */
	@Override
	public void reset() { }
}
