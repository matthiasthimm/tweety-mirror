package net.sf.tweety.logics.fol.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.ParserException;
import net.sf.tweety.logics.fol.parser.FolParser;

public class FolTest {

	public static void main(String[] args) throws FileNotFoundException, ParserException, IOException{		
		FolParser parser = new FolParser();
		BeliefBase b = parser.parseBeliefBaseFromFile("examplebeliefbase.fologic");		
		System.out.println(b);
	}
}
