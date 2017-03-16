package com.tomecode.oracle.osb12c.x2004;

import java.io.File;
import java.util.HashMap;

import weblogic.xml.query.xdbc.iterators.TokenIterator;

/**
 * 
 * @author Tome
 *
 */
public final class XqueryExecutor implements Xquery {

	private Executor executor;
	private Input input;
	private HashMap<String, XqueryArgument> arguments;
	private Output output;

	public XqueryExecutor() throws Exception {
		this.arguments = new HashMap<>();
	}

	public final Xquery loadXquery(File xq) throws Exception {
		this.input = new Input(xq);
		this.executor = new Executor(this.input.getXquery());
		return this;
	}

	@Override
	public final Input input() {
		return this.input;
	}

	@Override
	public final XqueryArgument arg(String name) throws Exception {
		Argument argument = new Argument(name, executor.getPreparedStatement());
		this.arguments.put(name, argument);
		return argument;
	}

	@Override
	public final void compile() throws Exception {
		this.executor.validate();
	}

	@Override
	public final Output execute() throws Exception {

		TokenIterator out = executor.getPreparedStatement().execute();
		return this.output = new Output(weblogic.xml.query.xmlbeans.apache.ApacheXmlBeansUtil.toApacheXmlObjects(out));
	}

	@Override
	public final Output output() {
		return this.output;
	}

	@Override
	public final HashMap<String, XqueryArgument> arg() {
		return this.arguments;
	}

}
