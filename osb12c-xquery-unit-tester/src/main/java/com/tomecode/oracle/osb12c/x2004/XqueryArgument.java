package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Node;

/**
 * 
 * @author Tome
 *
 */
public interface XqueryArgument {

	/**
	 * value as string
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void asString(String value) throws Exception;

	/**
	 * value as integer
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void asInt(Integer value) throws Exception;

	/**
	 * value as float
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void asFloat(Float value) throws Exception;

	/**
	 * value as double
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void asDouble(Double value) throws Exception;

	/**
	 * value as boolean
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void asBoolean(Boolean value) throws Exception;

	/**
	 * value as long
	 * 
	 * @param value
	 * @throws Exception
	 */
	public void asLong(Long value) throws Exception;

	/**
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public XqueryArgumentXml fromFile(File file) throws Exception;

	/**
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public XqueryArgumentXml fromString(String xml) throws Exception;

	/**
	 * 
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public XqueryArgumentXml fromNode(Node node) throws Exception;

	/**
	 * 
	 * @author Tome
	 *
	 */
	public interface XqueryArgumentXml {

		XmlObject node();

		void toSoapBody() throws Exception;
	}

	public String getFile();

	public String contentAsXml() throws Exception;

	public String contentAsString() throws Exception;
}
