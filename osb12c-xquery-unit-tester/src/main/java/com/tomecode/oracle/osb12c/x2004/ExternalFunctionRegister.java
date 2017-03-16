package com.tomecode.oracle.osb12c.x2004;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.w3c.dom.Element;

import com.bea.wli.sb.xpath.config.CategoryType;
import com.bea.wli.sb.xpath.config.FunctionType;
import com.bea.wli.sb.xpath.config.XpathFunctionsDocument;
import com.tomecode.oracle.osb12c.CustomFunctionClassFinder;

import weblogic.xml.query.tokens.QNameToken;
import weblogic.xml.query.xdbc.JavaMappingHelper;
import weblogic.xml.query.xdbc.XQType;

/**
 * Simple cache where are registered all external functions
 * 
 * @author Tome
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class ExternalFunctionRegister extends weblogic.xml.query.xdbc.Connection.EnhancedExternalFunctionImplFactory {

	private static final String NAMESPACE_PREFIXES = "0123456789abcdefghijklmonpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private final HashMap<String, String> namespaces = new HashMap<String, String>();

	private final Set<Class> _supportedClasses;
	/**
	 * table with all imported/registered external functions
	 */
	private final HashMap<QName, FunctionInfo> functions = new HashMap<QName, FunctionInfo>();

	private static ExternalFunctionRegister me;

	/**
	 * get instance
	 * 
	 * @return
	 */
	public static final ExternalFunctionRegister get() {
		return init(null, new ArrayList<>());
	}

	private ExternalFunctionRegister() {
		namespaces.put("fn", "http://www.w3.org/2004/07/xpath-functions");
		namespaces.put("fn-bea", "http://www.bea.com/xquery/xquery-functions");
		namespaces.put("xs", "http://www.w3.org/2001/XMLSchema");

		Class[] clazzes = { String.class, String[].class, Integer.TYPE, int[].class, Integer.class, Integer[].class, Boolean.TYPE, boolean[].class, Boolean.class, Boolean[].class, Long.TYPE, long[].class, Long.class, Long[].class, Short.TYPE,
				short[].class, Short.class, Short[].class, Byte.TYPE, byte[].class, Byte.class, Byte[].class, Double.TYPE, double[].class, Double.class, Double[].class, Float.TYPE, float[].class, Float.class, Float[].class, Character.TYPE,
				char[].class, Character.class, Character[].class, BigInteger.class, BigInteger[].class, BigDecimal.class, BigDecimal[].class, java.util.Date.class, java.util.Date[].class, java.sql.Date.class, java.sql.Date[].class, Time.class,
				Time[].class, QName.class, QName[].class, XmlObject.class, XmlObject[].class, Element.class, Element[].class };

		_supportedClasses = new HashSet(Arrays.asList(clazzes));
	}

	public final HashMap<QName, FunctionInfo> getFunctions() {
		return functions;
	}

	// private final void init(ClassLoader parentClassLoader) throws
	// RuntimeException {
	// try {
	// parseFunctionDescriptor(parentClassLoader,
	// this.getClass().getResourceAsStream("/com/egit/icc/fmw12c/xq/osb-build-in_uuid.xml"));
	// } catch (Exception e) {
	// throw new RuntimeException(e.getMessage(), e);
	// }
	// }

	public final static ExternalFunctionRegister init(ClassLoader parentClassLoader, List<File> dependencies) {
		if (me == null) {
			synchronized (ExternalFunctionRegister.class) {
				ExternalFunctionRegister instance = me;
				if (instance == null) {
					synchronized (ExternalFunctionRegister.class) {
						me = new ExternalFunctionRegister();
						// me.init();
						me.loadCustomFunctions(parentClassLoader, dependencies);
					}
				}
			}
		}

		return me;

	}

	/**
	 * load custom xq functions from classpath
	 * 
	 * @param parentClassLoader
	 * @param artifactMap
	 */
	private final void loadCustomFunctions(ClassLoader parentClassLoader, List<File> dependencies) {

		try {
			HashMap<String, URL> customFunctions = CustomFunctionClassFinder.listCustomFunctions(dependencies);
			prepareClassloader(customFunctions.values());

			for (Entry<String, URL> xqDef : customFunctions.entrySet()) {
				try {
					parseFunctionDescriptor(parentClassLoader, Thread.currentThread().getContextClassLoader().getResourceAsStream(xqDef.getKey()));
				} catch (Exception e) {
					throw new RuntimeException("Failed to parse custom xpath def: " + xqDef + " reason: " + e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * configure classloader for external jars( custom functions)
	 * 
	 * @param urlList
	 */
	private final void prepareClassloader(Collection<URL> urlList) {
		URL[] urls = new URL[urlList.size()];
		urlList.toArray(urls);
		URLClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
		Thread.currentThread().setContextClassLoader(classLoader);
	}

	/**
	 * parse external function description
	 * 
	 * @param parentClassLoader
	 * @param inputStream
	 * @throws Exception
	 */
	private final void parseFunctionDescriptor(ClassLoader parentClassLoader, InputStream inputStream) throws Exception {
		XpathFunctionsDocument doc = XpathFunctionsDocument.Factory.parse(inputStream);
		for (CategoryType categoryType : doc.getXpathFunctions().getCategoryArray()) {
			for (FunctionType function : categoryType.getFunctionArray()) {
				registerFunction(parentClassLoader, function);
			}
		}
	}

	private final Class loadFunctionImpl(ClassLoader parentClassLoader, FunctionType function) throws Exception {
		Class<?> clazz = loadClass(parentClassLoader, function.getClassName().trim());
		if (clazz == null) {
			throw new Exception("Not found implementation class: " + function.getClassName() + " for custom XPath function: " + function.getName());
		}
		return clazz;
	}

	/**
	 * register new function i.e. parse in/out params from function
	 * 
	 * @param function
	 * @throws Exception
	 */
	private final void registerFunction(ClassLoader parentClassLoader, FunctionType function) throws Exception {
		Class clazz = loadFunctionImpl(parentClassLoader, function);

		Method xqMethod = null;
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.getName().equals(function.getName())) {
				xqMethod = m;
				break;
			}
		}

		if (xqMethod == null) {
			throw new RuntimeException("Not found method: " + function.getName() + " in class: " + function.getClassName());
		}

		String prefix = getNamespacePrefix(function.getNamespaceURI());
		if (prefix != null) {
			// register new prefix+ namespace if not exists
			namespaces.put(prefix, function.getNamespaceURI());
		} else {
			prefix = resiterFunctionNamespaces(function.getNamespaceURI());
		}

		// parse function param + return type

		List<XQType> params = new ArrayList<>();
		for (int i = 0; i <= xqMethod.getParameters().length - 1; i++) {
			params.add(javaClassToXQType(xqMethod.getParameters()[i].getType()));
		}

		XQType returnType = javaClassToXQType(xqMethod.getReturnType());

		// register new function
		functions.put(new QName(function.getNamespaceURI(), function.getName(), prefix), new FunctionInfo(returnType, xqMethod, params, function.getIsDeterministic()));
	}

	/**
	 * load class from classpath
	 * 
	 * @param parentClassOLoader
	 * @param function
	 * @return
	 * @throws Exception
	 */
	public static final Class loadClass(ClassLoader parentClassLoader, String className) throws Exception {
		try {
			return Class.forName(className, true, ClassLoader.getSystemClassLoader());
		} catch (ClassNotFoundException e) {
		}

		if (parentClassLoader != null) {
			try {
				return Class.forName(className, true, parentClassLoader);
			} catch (ClassNotFoundException e) {
			}

		}
		try {
			return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
		}

		return null;
	}

	/**
	 * register function namespace
	 * 
	 * @param namespaceURI
	 * @return
	 */
	private final String resiterFunctionNamespaces(String namespaceURI) {
		int len = NAMESPACE_PREFIXES.length();

		String prefix = getNamespacePrefix(namespaceURI);

		if (prefix == null) {
			String suggprefix = QNameHelper.suggestPrefix(namespaceURI);
			int h = namespaceURI.hashCode();
			if (h == Integer.MIN_VALUE) {
				h = Integer.MAX_VALUE;
			}
			h = Math.abs(h) % (len * len);
			char first = NAMESPACE_PREFIXES.charAt(h / len);
			char second = NAMESPACE_PREFIXES.charAt(h % len);

			prefix = suggprefix + first + second;
			int i = 0;
			while (namespaces.containsKey(prefix)) {
				prefix = suggprefix + i++;
			}
			namespaces.put(prefix, namespaceURI);
			return prefix;
		} else {
			for (Entry<String, String> e : namespaces.entrySet()) {
				if (e.getValue().equals(namespaceURI)) {
					return e.getKey();
				}
			}
		}
		return prefix;
	}

	private final String getNamespacePrefix(String namespace) {
		for (Entry<String, String> e : namespaces.entrySet()) {
			if (e.getValue().equals(namespace)) {
				return e.getKey();
			}
		}

		return null;
	}

	private final XQType javaClassToXQType(Class clazz) throws Exception {
		if (_supportedClasses.contains(clazz)) {
			return JavaMappingHelper.xqTypeFor(clazz);
		}
		throw new Exception("Unsaported class: " + clazz.getName());
	}

	/**
	 * declare prolog for functions (external)
	 * 
	 * @param strbuf
	 */
	public final void prologFunctions(StringBuilder sb) {
		sb.append("\n");
		//
		for (Entry<QName, FunctionInfo> e : functions.entrySet()) {

			QName qn = e.getKey();

			sb.append("declare function ").append(qn.getPrefix()).append(":").append(qn.getLocalPart()).append("(");

			Iterator<XQType> ip = e.getValue().getParams().iterator();
			int i = 0;
			while (ip.hasNext()) {
				sb.append("$arg").append(i).append(" as ");
				appendXQParam(ip.next(), sb);
				if (ip.hasNext()) {
					sb.append(", ");
					i++;
				}
			}

			sb.append(")");
			sb.append(" as ");
			appendXQParam(e.getValue().getReturnType(), sb);
			sb.append(" external;\n");
		}
	}

	/**
	 * declare prolog for namespace
	 * 
	 * @param sb
	 */
	public final void prologNamespaces(StringBuilder sb) {
		for (Entry<String, String> e : namespaces.entrySet()) {
			sb.append("declare namespace ").append(e.getKey()).append("='").append(e.getValue()).append("';").append("\n");
		}
	}

	/**
	 * append XQ param
	 * 
	 * @param param
	 * @param buf
	 */
	private final void appendXQParam(XQType param, StringBuilder buf) {
		if (param.isSimple()) {
			appendQName(param.qname().asQName(), buf);
		} else if (param.kind() == 4) {
			buf.append("element()");
		} else {
			throw new UnsupportedOperationException();
		}
		switch (param.quantifier()) {
		case 1:
			break;
		case 3:
			buf.append('+');
			break;
		case 2:
			buf.append('?');
			break;
		case 4:
			buf.append('*');
		}
	}

	private final Map<String, String> appendQName(QName qname, StringBuilder buf) {
		String prefix = null;
		for (Map.Entry<String, String> e : namespaces.entrySet()) {
			if (((String) e.getValue()).equals(qname.getNamespaceURI())) {
				prefix = (String) e.getKey();
				break;
			}
		}
		Object newNs = null;
		if (prefix == null) {
			int i = 0;
			do {
				prefix = "ns" + i++;
			} while (namespaces.containsKey(prefix));
			newNs = Collections.singletonMap(prefix, qname.getNamespaceURI());
		}
		buf.append(prefix).append(":").append(qname.getLocalPart());
		return (Map<String, String>) newNs;
	}

	/**
	 * 
	 * @author Tome
	 *
	 */

	@Override
	public final Class getClassFor(QNameToken token) throws ClassNotFoundException {
		return ExternalFunctionEval.class;
	}

	@Override
	public final boolean isNonDeterministic(QNameToken arg0) {
		return this.functions.get(arg0.asQName()).isDeterministic();
	}

}
