package com.tomecode.oracle.osb12c.x2004;

import java.io.File;
import java.math.BigDecimal;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Node;

import com.tomecode.oracle.osb12c.Utils;

import weblogic.xml.query.xdbc.PreparedStatement;
import weblogic.xml.query.xmlbeans.apache.ApacheXmlBeansUtil;

/**
 * 
 * @author Tome
 *
 */
final class Argument implements XqueryArgument {

	private PreparedStatement preparedStatement;
	private XqueryArgumentXml xml;
	private String name;
	private Object value;

	public Argument(String name, PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
		this.name = name;
	}

	public final Object getObject() {
		return this.value;
	}

	public final XqueryArgumentXml getXml() {
		return this.xml;
	}

	public final String getName() {
		return this.name;
	}

	@Override
	public final void asString(String value) throws Exception {
		this.value = value;
		this.xml = null;
		this.preparedStatement.setString(name, value);
	}

	@Override
	public final void asInt(Integer value) throws Exception {
		this.value = value;
		this.xml = null;
		this.preparedStatement.setInteger(name, value);
	}

	@Override
	public final void asFloat(Float value) throws Exception {
		this.value = value;
		this.xml = null;
		this.preparedStatement.setFloat(name, value);
	}

	@Override
	public final void asDouble(Double value) throws Exception {
		this.value = value;
		this.xml = null;
		this.preparedStatement.setDouble(name, value);
	}

	@Override
	public final void asBoolean(Boolean value) throws Exception {
		this.value = value;
		this.xml = null;
		this.preparedStatement.setBoolean(name, value);
	}

	@Override
	public final void asLong(Long value) throws Exception {
		BigDecimal v = new BigDecimal(value);
		this.value = v;
		this.xml = null;
		this.preparedStatement.setDecimal(name, v);
	}

	@Override
	public final XqueryArgumentXml fromFile(File file) throws Exception {
		return this.xml = new XqueryArrgumentXmlImpl(this.preparedStatement, this.name, XmlObject.Factory.parse(file));
	}

	@Override
	public final XqueryArgumentXml fromString(String xml) throws Exception {
		return this.xml = new XqueryArrgumentXmlImpl(this.preparedStatement, this.name, Utils.toElement(xml));

	}

	@Override
	public final XqueryArgumentXml fromNode(Node node) throws Exception {
		return this.xml = new XqueryArrgumentXmlImpl(this.preparedStatement, this.name, XmlObject.Factory.parse(node));
	}

	/**
	 * 
	 * @author Tomas.Frastia@erstegroupIT.com
	 *
	 */
	private static class XqueryArrgumentXmlImpl implements XqueryArgumentXml {

		private PreparedStatement preparedStatement;
		private String name;
		private XmlObject node;

		private XqueryArrgumentXmlImpl(PreparedStatement preparedStatement, String name, XmlObject node) throws Exception {
			this.preparedStatement = preparedStatement;
			this.name = name;
			this.node = node;
			preparedStatement.setComplex(name, ApacheXmlBeansUtil.toTokenIterator(node));
		}

		private XqueryArrgumentXmlImpl(PreparedStatement preparedStatement, String name, Node element) throws Exception {
			this(preparedStatement, name, XmlObject.Factory.parse(element));
		}

		@Override
		public final void toSoapBody() throws Exception {
			XmlObject inputXo = XmlObject.Factory.parse("<soap-env:Body xmlns:soap-env='http://schemas.xmlsoap.org/soap/envelope/'>" + node.xmlText() + "</soap-env:Body>");
			XmlCursor inputCur = inputXo.newCursor();
			inputCur.toFirstChild();
			preparedStatement.setComplex(name, ApacheXmlBeansUtil.toTokenIterator(inputCur.getObject()));

			inputCur.dispose();
		}
	}

}
