package com.tomecode.oracle.osb12c;

import java.io.File;
import java.io.StringReader;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import oracle.xml.jaxp.JXDocumentBuilderFactory;

/**
 * 
 * @author Tome
 *
 */
public final class Utils {

	private static DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY;

	private static final Pattern REGEX_END_OF_FILE = Pattern.compile("\\Z");

	public final static String getContent(File file) {
		try (Scanner scannes = new Scanner(file)) {
			return scannes.useDelimiter(REGEX_END_OF_FILE).next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final DocumentBuilderFactory getDocumentBuilderFactory() {
		if (DOCUMENT_BUILDER_FACTORY != null) {
			return DOCUMENT_BUILDER_FACTORY;
		}
		synchronized (Utils.class) {
			if (DOCUMENT_BUILDER_FACTORY == null) {
				DOCUMENT_BUILDER_FACTORY = new JXDocumentBuilderFactory();
			}
		}
		return DOCUMENT_BUILDER_FACTORY;
	}

	public final static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = getDocumentBuilderFactory();
		dbf.setNamespaceAware(true);
		return dbf.newDocumentBuilder();
	}

	public final static Node toElement(String xml) throws Exception {
		return getDocumentBuilder().parse(new InputSource(new StringReader(xml))).getDocumentElement();
	}

}
