package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

/**
 * 
 * @author Tome
 *
 */
public class TestCustomXpath extends TestCaseX2004 {

	/**
	 * unit test where is used build in OSB function: fn-bea:uuid
	 * @throws Exception
	 */
	public final void testBuildInFunction() throws Exception {
		Xquery xq = xq(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/buildInFunction.xq").getAbsoluteFile());
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertNotNull(out);
	}
	
	/**
	 * unit test where is used custom OSB function: hello('')
	 * @throws Exception
	 */
	public final void testCustomFunction() throws Exception {
		Xquery xq = xq(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/customFunction.xq").getAbsoluteFile());
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<uuid>xquery world</uuid>", out);
	}

}
