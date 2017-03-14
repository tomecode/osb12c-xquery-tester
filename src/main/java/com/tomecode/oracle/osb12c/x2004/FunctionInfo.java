package com.tomecode.oracle.osb12c.x2004;

import java.lang.reflect.Method;
import java.util.List;

import weblogic.xml.query.xdbc.XQType;

/**
 * Function dec
 * 
 * @author Tome
 *
 */
final class FunctionInfo {

	private final Method method;
	private final List<XQType> params;
	private final XQType returnType;
	private final boolean deterministic;

	public FunctionInfo(XQType returnType, Method xqMethod, List<XQType> params, boolean deterministic) {
		this.params = params;
		this.method = xqMethod;
		this.returnType = returnType;
		this.deterministic = deterministic;
	}

	public final boolean isDeterministic() {
		return deterministic;
	}

	public final List<XQType> getParams() {
		return params;
	}

	public final XQType getReturnType() {
		return returnType;
	}

	public final Method getMethod() {
		return method;
	}

}
