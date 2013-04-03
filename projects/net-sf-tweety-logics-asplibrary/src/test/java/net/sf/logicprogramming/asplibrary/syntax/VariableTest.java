package net.sf.logicprogramming.asplibrary.syntax;

import static org.junit.Assert.assertEquals;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Variable;

import org.junit.Test;

public class VariableTest {
	
	@Test
	public void testRange() {
		Throwable t = null;
		try {
			new Variable("A");
			new Variable("Z");
		} catch(Exception e) {
			t = e;
		}
		assertEquals(null, t);
		
		Class<?> cls = null;
		try {
			new Variable("a");
		} catch (Exception e) {
			cls = e.getClass();
		}
		assertEquals(cls, IllegalArgumentException.class);
	}
}
