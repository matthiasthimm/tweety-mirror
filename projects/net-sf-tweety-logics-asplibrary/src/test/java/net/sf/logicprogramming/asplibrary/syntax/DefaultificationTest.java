package net.sf.logicprogramming.asplibrary.syntax;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.tweety.logicprogramming.asplibrary.syntax.ELPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Not;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

public class DefaultificationTest extends TestCase {
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DefaultificationTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DefaultificationTest.class );
    }
    
    public void testSimpleDefaultifcation() {
    	Program p = new Program();
    	Rule onlyRule = new Rule();
    	onlyRule.addHead(new ELPAtom("x"));
    	onlyRule.addBody(new ELPAtom("y"));
    	p.add(onlyRule);
    	
    	Program dp = Program.defaultification(p);
    	assertTrue(dp.size() == p.size());
    	Rule dr = dp.iterator().next();
    	
    	assertTrue(dr.getHead().equals(onlyRule.getHead()));
    	assertTrue(dr.getBody().contains(onlyRule.getBody().get(0)));
    	Not defNot = new Not(new Neg(dr.getHead().iterator().next().getAtom()));
    	assertTrue(dr.getBody().contains(defNot));
    	
    	p = new Program();
    	onlyRule = new Rule();
    	onlyRule.addHead(new Neg(new ELPAtom("x")));
    	
    	p.add(onlyRule);
    	dp = Program.defaultification(p);
    	assertTrue(p.size() == dp.size());
    	dr = dp.iterator().next();
    	
    	assertTrue(dr.getHead().equals(onlyRule.getHead()));
    	defNot = new Not(dr.getHead().iterator().next().getAtom());
    	assertTrue(dr.getBody().contains(defNot));
    }
    
    public void testDefaultificationOfAlreadyDefaulticated() {
    	Program p = new Program();
    	Rule r = new Rule();
    	ELPAtom a = new ELPAtom("x");
    	r.addHead(a);
    	Not defLit = new Not(new Neg(a));
    	r.addBody(defLit);
    	p.add(r);
    	
    	// We want the program dp look like p cause p was already
    	// defaultisated
    	Program dp = Program.defaultification(p);
    	Rule rd = dp.iterator().next();
    	assertTrue(rd.equals(r));
    	assertTrue(dp.equals(p));
    }
}
