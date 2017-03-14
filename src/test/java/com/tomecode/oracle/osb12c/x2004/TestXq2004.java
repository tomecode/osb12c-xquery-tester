package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

import junit.framework.TestCase;

/**
 * 
 * @author Tome
 *
 */
public class TestXq2004 extends TestCase {

	/**
	 * 
	 * @throws Exception
	 */
	public final void testCompileFail() throws Exception {
		try {
			new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/customXq.xq").getAbsoluteFile());
			throw new Exception("Opss, something wrong with compiler!");
		} catch (Exception e) {
		}
	}

	public final void testCompileOk() throws Exception {
		try {
			new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramBoolean.xq").getAbsoluteFile());
		} catch (Exception e) {
			throw new Exception("Opss, something wrong with compiler!");
		}
	}

	public final void testArgBoolean() throws Exception {
		Xquery xq = new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramBoolean.xq").getAbsoluteFile());
		xq.arg("input").asBoolean(true);
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value>true</value>", out);
	}

	public final void testArgDouble() throws Exception {
		Xquery xq = new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramDouble.xq").getAbsoluteFile());
		xq.arg("input").asDouble(new Double("2.2"));
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value>2.2</value>", out);
	}

	public final void testArgFloat() throws Exception {
		Xquery xq = new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramFloat.xq").getAbsoluteFile());
		xq.arg("input").asFloat(new Float("2.2"));
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value>2.2</value>", out);
	}

	public final void testArgInt() throws Exception {
		Xquery xq = new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramInt.xq").getAbsoluteFile());
		xq.arg("input").asInt(1);
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value>1</value>", out);
	}

	public final void testArgString() throws Exception {
		Xquery xq = new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramString.xq").getAbsoluteFile());
		xq.arg("input").asString("tome");
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value>tome</value>", out);
	}

	public final void testArgElement() throws Exception {
		Xquery xq = new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramElement.xq").getAbsoluteFile());
		xq.arg("input").fromString("<e><r/></e>").toSoapBody();
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value><e xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\"><r/></e></value>", out);
	}

	public final void testArgFile() throws Exception {
		Xquery xq = new XqueryExecutor().loadXquery(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/paramElement.xq").getAbsoluteFile());
		xq.arg("input").fromFile(new File("src/test/resources/com/tomecode/oracle/osb12c/x2004/sample.xml").getAbsoluteFile()).toSoapBody();
		String out = xq.execute().asXmlString();
		org.junit.Assert.assertEquals("<value><helloWorld xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\"/></value>", out);
	}

}
