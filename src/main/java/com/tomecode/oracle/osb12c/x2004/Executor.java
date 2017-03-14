package com.tomecode.oracle.osb12c.x2004;

import com.bea.wli.common.xquery.x2004.XQueryInfo;

import weblogic.xml.query.xdbc.Connection;
import weblogic.xml.query.xdbc.DriverManager;
import weblogic.xml.query.xdbc.PreparedStatement;
import weblogic.xml.query.xdbc.XDBCAttributes;

/**
 * 
 * @author Tome
 *
 */
final class Executor {

	private String xquery;
	private Connection connection;
	private PreparedStatement preparedStatement;

	public Executor(String xquery) throws Exception {
		this.xquery = xquery;
		this.connection = initConnection();
		this.preparedStatement = connection.prepareStatement(xquery);
	}

	private final Connection initConnection() throws Exception {
		weblogic.xml.query.xdbc.Connection connection = DriverManager.getConnection();

		connection.setExternalFunctionImplFactory(ExternalFunctionRegister.get());
		connection.loadPrologue(getConnectionProlog());

		return connection;
	}

	public final Connection getConnection() {
		return connection;
	}

	public final PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}

	private final String getConnectionProlog() {
		StringBuilder strbuf = new StringBuilder(1024);
		ExternalFunctionRegister.get().prologNamespaces(strbuf);
		ExternalFunctionRegister.get().prologFunctions(strbuf);
		return strbuf.toString();
	}

	public final XQueryInfo.Options createDefaultOptions() {
		XDBCAttributes attrs = new XDBCAttributes();
		attrs.enableFeature("allow_execute_sql");
		XQueryInfo.Options options = new XQueryInfo.Options();
		options.setXdbcAttrs(attrs);
		return options;
	}

	/**
	 * validate/compile
	 * 
	 * @throws Exception
	 */
	public final void validate() throws Exception {
		XQueryInfo.parse(xquery, this.connection, createDefaultOptions());
	}
}
