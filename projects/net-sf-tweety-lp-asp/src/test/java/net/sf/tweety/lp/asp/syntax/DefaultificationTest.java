package net.sf.tweety.lp.asp.syntax;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
    	onlyRule.setConclusion(new DLPAtom("x"));
    	onlyRule.addPremise(new DLPAtom("y"));
    	p.add(onlyRule);
    	
    	Program dp = Program.defaultification(p);
    	assertTrue(dp.size() == p.size());
    	Rule dr = dp.iterator().next();
    	
    	assertEquals(dr.getConclusion(), onlyRule.getConclusion());
    	assertTrue(dr.getPremise().contains(onlyRule.getPremise().iterator().next()));
    	DLPNot defNot = new DLPNot(new DLPNeg(dr.getConclusion().iterator().next().getAtom()));
    	assertTrue(dr.getPremise().contains(defNot));
    	
    	p = new Program();
    	onlyRule = new Rule();
    	onlyRule.setConclusion(new DLPNeg(new DLPAtom("x")));
    	
    	p.add(onlyRule);
    	dp = Program.defaultification(p);
    	assertEquals(p.size(), dp.size());
    	dr = dp.iterator().next();
    	
    	assertEquals(dr.getConclusion(), onlyRule.getConclusion());
    	defNot = new DLPNot(dr.getConclusion().iterator().next().getAtom());
    	assertEquals(true, dr.getPremise().contains(defNot));
    }
    
    public void testDefaultificationOfAlreadyDefaulticated() {
    	Program p = new Program();
    	Rule r = new Rule();
    	DLPAtom a = new DLPAtom("x");
    	r.setConclusion(a);
    	DLPNot defLit = new DLPNot(new DLPNeg(a));
    	r.addPremise(defLit);
    	p.add(r);
    	
    	// We want the program dp look like p cause p was already
    	// defaultisated
    	Program dp = Program.defaultification(p);
    	Rule rd = dp.iterator().next();
    	assertEquals(rd, r);
    	assertEquals(dp, p);
    }
}
