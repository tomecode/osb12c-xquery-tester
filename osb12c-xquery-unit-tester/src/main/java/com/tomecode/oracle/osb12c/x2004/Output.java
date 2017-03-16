package com.tomecode.oracle.osb12c.x2004;

import org.apache.xmlbeans.XmlObject;

/**
 * 
 * @author Tome
 *
 */
public final class Output {

	private XmlObject[] results;

	public Output(XmlObject[] results) {
		this.results = results;
	}

	public final XmlObject[] results() {
		return this.results;
	}

	public final String asXmlString() {
		StringBuilder sb = new StringBuilder();
		if (this.results != null) {
			for (XmlObject result : this.results) {
				sb.append(result.xmlText());
			}
		}
		return sb.toString();
	}

	public final String contentAsXml() {
		if (results != null) {
			StringBuilder sb = new StringBuilder();
			for (XmlObject result : results) {
				sb.append(result.xmlText());
			}
			return sb.toString();
		}
		return null;
	}

}
