package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;

/**
 * this class models a logical program, which is
 * a collection of rules.
 * 
 * @author Thomas Vengels
 *
 */
public class Program extends ArrayList<Rule> {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Preamble	preamble = new Preamble();
	
	public void add(Program other) {
		this.addAll( other );
		this.preamble.join( other.preamble );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.size() > 0) {
			sb.append( this.get(0));
		}
		for (int i = 1; i < this.size(); i++) {
			sb.append("\n");
			sb.append(this.get(i));
		}
		return sb.toString();
	}
	
	public static Program loadFrom(String file) {
		Program ret = null;
		
		try {
			ELPParser ep = new ELPParser( new FileReader( file ));
			List<Rule> lr = ep.program();
			ret = new Program();
			ret.addAll(lr);
		} catch (Exception e) {
			System.err.println("Error while loading program from "+file);
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void saveTo(String filename) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(filename));
			for (Rule r : this) {
				w.write(r.toString());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toStringFlat() {
		StringBuilder sb = new StringBuilder();
		
		Iterator<Rule> rIter = this.iterator();
		while (rIter.hasNext()) {
			Rule r = rIter.next();
			if (r.isComment())
				continue;
			sb.append(r.toString());
		}
		
		return sb.toString();
	}
}
