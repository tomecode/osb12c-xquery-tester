package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

/**
 * 
 * @author Tome
 *
 */
public class TestCaseXq2004 extends TestCaseX2004 {

	public final void testFile() throws Exception {
		Xquery xq = xq(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramInt.xq").getAbsoluteFile());
		xq.arg("input").asInt(1);
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value>1</value>", out);
	}

	public final void testFileStr() throws Exception {
		Xquery xq = xq("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramInt.xq");
		xq.arg("input").asInt(1);
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value>1</value>", out);
	}

}
