package com.tomecode.oracle.osb12c.x2004;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.bea.wli.common.xquery.x2004.iterators.NullIterator;

import weblogic.xml.query.exceptions.XQueryException;
import weblogic.xml.query.operators.Operator;
import weblogic.xml.query.tokens.Token;
import weblogic.xml.query.xdbc.Context;
import weblogic.xml.query.xdbc.JavaMappingHelper;
import weblogic.xml.query.xdbc.iterators.TokenIterator;
import weblogic.xml.query.xdbc.iterators.TokenSource;

/**
 * External function eval
 * 
 * @author Tome
 *
 */
public final class ExternalFunctionEval extends weblogic.xml.query.iterators.FirstOrderIterator {

	private TokenSource _resultIterator;

	public ExternalFunctionEval(Context inContext, TokenIterator[] inSubIters) {
		super(inContext, inSubIters);
	}

	protected final void init() throws weblogic.xml.query.exceptions.XQueryException {
		try {
			Operator operator = getOperator();
			this._resultIterator = execute(operator.getName().asQName(), this.m_subIters);
		} catch (com.bea.wli.common.xquery.x2004.XQueryException xqe) {
			xqe.throwXQueryException();
		}
		this._resultIterator.open();
	}

	@Override
	protected final Token fetchNext() throws XQueryException {
		return this._resultIterator.next();
	}

	protected final void deinit() throws weblogic.xml.query.exceptions.XQueryException {
		this._resultIterator.close();
	}

	/**
	 * execute external/custom function impl
	 * 
	 * @param funcname
	 * @param args
	 * @return
	 * @throws com.bea.wli.common.xquery.x2004.XQueryException
	 * @throws weblogic.xml.query.exceptions.XQueryException
	 */
	protected final TokenSource execute(QName funcname, TokenIterator[] args) throws com.bea.wli.common.xquery.x2004.XQueryException, weblogic.xml.query.exceptions.XQueryException {
		Method m = ExternalFunctionRegister.get().getFunctions().get(funcname).getMethod();

		Class<?>[] params = m.getParameterTypes();
		List<Object> listArgs = new ArrayList<Object>();
		for (int i = 0; i <= args.length - 1; i++) {
			listArgs.add(JavaMappingHelper.toJavaObject(args[i], params[i]));
		}

		Object result = null;
		try {
			m.setAccessible(true);
			result = m.invoke(null, listArgs.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result == null) {
			return new NullIterator();
		}

		return JavaMappingHelper.toTokenSource(result);
	}

}
