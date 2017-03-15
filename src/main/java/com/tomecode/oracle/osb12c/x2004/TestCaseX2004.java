package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

import junit.framework.TestCase;

public class TestCaseX2004 extends TestCase {

	public final Xquery xq(File file) throws Exception {
		return new XqueryExecutor().loadXquery(file);
	}

	public final Xquery xq(String file) throws Exception {
		return new XqueryExecutor().loadXquery(new File(file).getAbsoluteFile());
	}
}
