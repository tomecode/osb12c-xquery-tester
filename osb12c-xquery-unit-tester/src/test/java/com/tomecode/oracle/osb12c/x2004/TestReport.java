package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

import org.junit.Assert;

/**
 * 
 * @author Tome
 *
 */
public class TestReport extends TestCaseX2004 {

	public final void testGenerateReport() throws Exception {
		Xquery xq = xq("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramElement.xq");
		xq.arg("input").fromFile(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/sample.xml").getAbsoluteFile()).toSoapBody();
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value><helloWorld xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\"/></value>", out);
		report(xq).generate("hello");
		Assert.assertEquals(true, new File("target/xqUnitTestReports/hello_report.html").getAbsoluteFile().exists());
	}
}
