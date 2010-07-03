package edu.cs.ai.kr.dung.syntax;

import java.util.*;

import edu.cs.ai.kr.*;

/**
 * This class captures the signature of a Dung theory,
 * i.e. a set of arguments.
 * 
 * @author Matthias Thimm
 */
public class DungSignature extends SetSignature<Argument>{

	/**
 	  * Creates a new (empty) Dung signature.
 	  */
	public DungSignature(){
		super();
	}

	/**
	 * Creates a new signature with the single given argument.
	 * @param arguments an argument.
	 */
	public DungSignature(Argument argument){
		super(argument);
	}
	
	/**
	 * Creates a new signature with the given set of arguments.
	 * @param arguments a set of arguments.
	 */
	public DungSignature(Collection<? extends Argument> arguments){
		super(arguments);		
	}	
	
}
