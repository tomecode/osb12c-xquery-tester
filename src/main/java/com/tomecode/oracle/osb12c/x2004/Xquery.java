package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

/**
 * 
 * @author Tome
 *
 */
public interface Xquery {

	/**
	 * load XQuery
	 * 
	 * @param xq
	 * @return
	 * @throws Exception
	 */
	public Xquery loadXquery(File xq) throws Exception;

	/**
	 * input i.e xquery + file
	 * 
	 * @return
	 */
	public Input input();

	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public XqueryArgument arg(String name) throws Exception;

	/**
	 * 
	 * @throws Exception
	 */
	public void compile() throws Exception;

	/**
	 * execute Xquery
	 * 
	 * @return
	 * @throws Exception
	 */
	public Output execute() throws Exception;

	public Output output();
}
