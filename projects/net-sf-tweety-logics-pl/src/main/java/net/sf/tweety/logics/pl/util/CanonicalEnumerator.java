package net.sf.tweety.logics.pl.util;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import net.sf.tweety.commons.util.ConversionTools;
import net.sf.tweety.commons.util.Pair;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

/**
 * Provides an iterator on all syntactically equivalent knowledge bases.
 * 
 * @author Matthias Thimm
 *
 */
public class CanonicalEnumerator implements Iterator<PlBeliefSet>{
	
	/** The upper bound used for enumerating knowledge bases.*/
	private BigInteger upperBoundIndex = null;
	/** The next value used for compiling a knowledge base.*/
	private BigInteger next = null;
	
	/**
	 * Default constructor. Creates an enumerator that enumerates
	 * knowledge bases starting from index 1.
	 */
	public CanonicalEnumerator(){
		this(BigInteger.ONE,null);
	}
	
	/**
	 * Creates a new enumerator that enumerates 
	 * knowledge bases starting from the given index
	 * @param startIndex some integer value
	 */
	public CanonicalEnumerator(Long startIndex){		
		this(new BigInteger(startIndex.toString()),null);
	}
	
	/**
	 * Creates a new enumerator that enumerates 
	 * knowledge bases starting from the given index
	 * @param startIndex some integer value
	 */
	public CanonicalEnumerator(Integer startIndex){		
		this(new BigInteger(startIndex.toString()),null);
	}
	
	/**
	 * Creates a new enumerator that enumerates 
	 * knowledge bases starting from the given index
	 * @param startIndex some integer value
	 */
	public CanonicalEnumerator(BigInteger startIndex){
		this(startIndex, null);
	}
	
	/**
	 * Creates a new enumerator that enumerates 
	 * knowledge bases starting from the given start
	 * index up to the given end index (including)
	 * @param startIndex some integer value
	 * @param endIndex some integer value
	 */
	public CanonicalEnumerator(Long startIndex, Long endIndex){		
		this(new BigInteger(startIndex.toString()),new BigInteger(endIndex.toString()));
	}
	
	/**
	 * Creates a new enumerator that enumerates 
	 * knowledge bases starting from the given start
	 * index up to the given end index (including)
	 * @param startIndex some integer value
	 * @param endIndex some integer value
	 */
	public CanonicalEnumerator(Integer startIndex, Integer endIndex){		
		this(new BigInteger(startIndex.toString()),new BigInteger(endIndex.toString()));
	}
	
	/**
	 * Creates a new enumerator that enumerates 
	 * knowledge bases starting from the given start
	 * index up to the given end index (including)
	 * @param startIndex some integer value
	 * @param endIndex some integer value
	 */
	public CanonicalEnumerator(BigInteger startIndex, BigInteger endIndex){		
		this.next = startIndex;
		this.upperBoundIndex = endIndex;
		this.gotoNextValidIndex();
	}
	
	/**
	 * Sets this.next to the next value actually encoding a belief base (or to this.upperBoundIndex if set). 
	 */
	private void gotoNextValidIndex(){
		// determine the first value actually encoding a belief base
		do{
			if(this.bitset2BeliefSet(ConversionTools.bigInteger2BitSet(this.next)) != null)
				break;
			this.next = this.next.add(BigInteger.ONE);
		}while(this.upperBoundIndex == null || this.next.compareTo(this.upperBoundIndex) <= 0);
	}
	
	/**
	 * Checks whether the bitvector at positions [i,...,j] is less
	 * than the bitvector at positions [k,...,l]. If equal=true, "less"
	 * is substituted by "less or equal".
	 * @param s some bitset
	 * @param equal whether equality is allowed
	 * @param i start index of first bitvector (including)
	 * @param j end index of first bitvector (including)
	 * @param k start index of second bitvector (including)
	 * @param l end index of second bitvector (including)
	 * @return "true" if [i,...,j] is less (or equal) [k,...,l]
	 */
	private boolean isLessThan(BitSet s, boolean equal, int i, int j, int k, int l){
		// if one of the bitvectors is larger, check whether the larger one has a 1 in the first part
		int idx1=0;
		int idx2=0;
		if(j-i < l-k){
			for(; idx2 < (l-k)-(j-i);idx2++)
				if(s.get(k+idx2))
					return true;
		}
		if(l-k < j-i){
			for(; idx1 < (j-i)-(l-k);idx1++)
				if(s.get(i+idx1))
					return false;
		}		
		while(i+idx1 <= j){
			if(s.get(i+idx1) && !s.get(k+idx2))
				return false;
			if(!s.get(i+idx1) && s.get(k+idx2))
				return true;
			idx1++;
			idx2++;
		}		
		return equal;
	}
	
	/**
	 * Parses an associate formula from the given bitset starting at index idx, with the given number
	 * of terms and the given signature
	 * @param s some bitset
	 * @param idx the starting index of the formula to be parsed
	 * @param numOfTerms the number of terms to be parsed
	 * @param sig the current signature
	 * @return the set of parsed terms and the index position in s after this formula
	 */
	private Pair<Collection<PropositionalFormula>,Integer> parseAssociativeFormula(BitSet s, int idx, int numOfTerms, PropositionalSignature sig){
		Collection<PropositionalFormula> forms = new LinkedList<PropositionalFormula>();
		int prev_start = 0, prev_end = 0;
		int current_start, current_end;
		for(int i = 0; i < numOfTerms; i++){
			current_start = idx;
			Pair<PropositionalFormula,Integer> p = this.parseFormula(s,idx,sig);
			if(p == null)
				return null;
			current_end = p.getSecond()-1;
			if(prev_start != 0){
				if(!this.isLessThan(s,true,prev_start,prev_end,current_start,current_end))
					return null;
			}	
			prev_start = current_start;
			prev_end = current_end;
			idx = p.getSecond();
			if(idx> s.size())
				return null;
			forms.add(p.getFirst());
		}
		return new Pair<Collection<PropositionalFormula>,Integer>(forms,idx);
	}
	
	/**
	 * Parses a proposition in bitset s starting from idx.
	 * @param s some bitset
	 * @param idx the starting index of the proposition
	 * @param sig the current signature
	 * @return the formula parsed and the index in s after this formula
	 */
	private Pair<PropositionalFormula,Integer> parseProposition(BitSet s, int idx, PropositionalSignature sig){
		int i = 0;
		while(s.get(idx) && idx < s.size()){
			i++;
			idx++;
		}
		if(i==0 || idx>= s.size())
			return null;
		idx++;
		// check whether we encountered X1 before X2, etc.
		Proposition prop = new Proposition("X"+i);
		if(!sig.contains(prop)){
			if(i != sig.size()+1)
				return null;
			sig.add(prop);
		}				
		return new Pair<PropositionalFormula,Integer>(prop,idx);
	}	
	
	/**
	 * Reads the next formula of the given bitset, starting at the given index.
	 * @param b a bitset
	 * @param idx the start index of a formula
	 * @param sig the current signature 
	 * @return the formula that has been read and the index right in the bitset right
	 * 	after this formula; if the bitset if invalid, "null" is returned
	 */
	private Pair<PropositionalFormula,Integer> parseFormula(BitSet s, int idx, PropositionalSignature sig){
		// next two bits encode type of formula
		// 00 - proposition
		// 01 - negation
		// 10 - conjunction
		// 11 - disjunction
		boolean first = s.get(idx++);
		boolean second = s.get(idx++);
		// if 10 or 11 it follows a series of 1s indicating
		// the number of conjuncts/disjuncts
		int numOfTerms = 0;
		if(first){
			while(s.get(idx++) && idx < s.size())
				numOfTerms++;
			if(numOfTerms <= 1)
				return null;
		}
		if(idx >= s.size())
			return null;
		// case differentiation by formula type			
		if(first && second){
			// 11 - disjunction
			Pair<Collection<PropositionalFormula>,Integer> p = this.parseAssociativeFormula(s,idx,numOfTerms,sig);
			if(p == null)
				return null;
			// no nested disjunctions
			for(PropositionalFormula f: p.getFirst())
				if(f instanceof Disjunction)
					return null;
			idx = p.getSecond();
			return new Pair<PropositionalFormula,Integer>(new Disjunction(p.getFirst()),idx);
		}else if(first){
			// 10 - conjunction
			Pair<Collection<PropositionalFormula>,Integer> p = this.parseAssociativeFormula(s,idx,numOfTerms,sig);
			if(p == null)
				return null;
			// no nested conjunctions
			for(PropositionalFormula f: p.getFirst())
				if(f instanceof Conjunction)
					return null;
			idx = p.getSecond();
			return new Pair<PropositionalFormula,Integer>(new Conjunction(p.getFirst()),idx);
		}else if(second){
			// 01 - negation
			Pair<PropositionalFormula,Integer> p = this.parseFormula(s,idx,sig);
			if(p == null)
				return null;
			idx = p.getSecond();
			if(idx> s.size())
				return null;
			return new Pair<PropositionalFormula,Integer>(new Negation(p.getFirst()),idx);
		}else{
			// 00 - proposition
			return this.parseProposition(s,idx,sig);
		}		
	}
	
	/**
	 * Parses a knowledge base (PlBeliefSet) from the the given bitset.
	 * If the bitset does not encode a PlBeliefSet, null is returned
	 * @param s some bitset
	 * @return the knowledge base encoded by the bitset, or null.
	 */
	private PlBeliefSet bitset2BeliefSet(BitSet s){
		// first bit has to be 1
		int idx = s.nextSetBit(0);
		if(idx == -1)
			return null;
		PlBeliefSet result = new PlBeliefSet();
		PropositionalSignature sig = new PropositionalSignature();
		idx++;
		int prev_start = 0, prev_end = 0;
		int current_start, current_end;
		while(idx < s.size()){
			current_start = idx;
			Pair<PropositionalFormula,Integer> p = parseFormula(s,idx,sig);
			if(p == null)
				return null;
			if(result.contains(p.getFirst()))
				return null;
			current_end = p.getSecond()-1;
			if(prev_start != 0){
				if(!isLessThan(s,false,prev_start,prev_end,current_start,current_end))
					return null;
			}	
			prev_start = current_start;
			prev_end = current_end;
			result.add(p.getFirst());
			idx = p.getSecond();		
			if(idx > s.size())
				return null;
		}
		return result;
	}

	@Override
	public boolean hasNext() {
		return this.upperBoundIndex == null || this.next.compareTo(this.upperBoundIndex) <= 0;
	}

	@Override
	public PlBeliefSet next() {
		if(!this.hasNext())
			throw new NoSuchElementException();
		PlBeliefSet bs = this.bitset2BeliefSet(ConversionTools.bigInteger2BitSet(this.next));
		this.next = this.next.add(BigInteger.ONE);
		this.gotoNextValidIndex();
		return bs;
	}

	/**
	 * Returns the index of the next element. 
	 * @return the index of the next element.
	 */
	public BigInteger nextIndex(){
		return this.next;
	}	
	
	/**
	 * Encodes the given formula as a bitstring.
	 * @param f some formula
	 * @param prop map of propositions to their indices
	 * @return a bitstring representing the formula
	 */
	private static String formula2String(PropositionalFormula f, Map<Proposition,Integer> prop){
		String result = "";
		if(f instanceof Proposition){
			int idx = prop.get(f);
			result += "00";
			for(int i = 0; i < idx; i++)
				result += "1";
			result += "0";
		}else if(f instanceof Negation){
			result += "01" + formula2String(((Negation)f).getFormula(),prop);			
		}else if(f instanceof Conjunction){
			result += "10";
			for(PropositionalFormula f2: (Conjunction)f)
				result += formula2String(f2,prop);
		}else if(f instanceof Disjunction){
			result += "11";
			for(PropositionalFormula f2: (Disjunction)f)
				result += formula2String(f2,prop);
		}else{
			throw new IllegalArgumentException("Only formulas of type proposition, negation, conjunction, and disjunction allowed.");
		}		
		return result;
	}
	
	/**
	 * Creates a bitset representation of the given belief set.
	 * @param bs some belief set
	 * @return a bitset representation of the belief set
	 */
	public static BitSet beliefSet2BitSet(PlBeliefSet bs){
		String s = "1";
		Map<Proposition,Integer> prop = new HashMap<Proposition,Integer>();
		int idx = 1;
		for(Proposition p: (PropositionalSignature)bs.getSignature())
			prop.put(p, idx++);
		for(PropositionalFormula f: bs)
			s += CanonicalEnumerator.formula2String(f,prop);
		return ConversionTools.binaryString2BitSet(s);
	}
}
