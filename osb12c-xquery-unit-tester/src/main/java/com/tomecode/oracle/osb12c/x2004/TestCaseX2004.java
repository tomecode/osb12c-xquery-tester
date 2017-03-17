package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

import junit.framework.TestCase;

/**
 * 
 * @author Tome
 *
 */
public class TestCaseX2004 extends TestCase {

	/**
	 * load {@link Xquery} from file
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public final Xquery xq(File file) throws Exception {
		return new XqueryExecutor().loadXquery(file);
	}

	/**
	 * load {@link Xquery} from file
	 * 
	 * @param file
	 *            path to xquery file
	 * @return
	 * @throws Exception
	 */
	public final Xquery xq(String file) throws Exception {
		return new XqueryExecutor().loadXquery(new File(file).getAbsoluteFile());
	}

	/**
	 * generate test report
	 * 
	 * @param xquery
	 * @return
	 */
	public final XqueryTestReport report(Xquery xquery) {
		return new XqueryTestReport(xquery);
	}
}
