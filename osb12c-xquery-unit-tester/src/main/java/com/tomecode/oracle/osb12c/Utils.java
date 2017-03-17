package com.tomecode.oracle.osb12c;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static final String readFile(InputStream inputStream) throws Exception {
		try (Scanner sc = new Scanner(inputStream)) {
			return sc.useDelimiter(REGEX_END_OF_FILE).next();
		}
	}

	public static final String prettyFormatXml(InputStream newInputStream) {
		if (newInputStream == null) {
			return null;
		}
		return formatXml(new StreamSource(newInputStream));
	}

	public final static String prettyFormatXml(String input) {
		if (input == null) {
			return null;
		}
		return formatXml(new StreamSource(new StringReader(input)));
	}

	public final static String prettyFormatXml(Node input) {
		if (input == null) {
			return null;
		}
		return formatXml(new DOMSource(input));
	}

	private final static String formatXml(Source xmlInput) {
		if (xmlInput == null) {
			return null;
		}
		try (StringWriter stringWriter = new StringWriter()) {
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			// transformerFactory.setAttribute("indent-number", 2);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static final String xmlEscapeText(String t) {
		if (t == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < t.length(); i++) {
			char c = t.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				if (c > 0x7e) {
					sb.append("&#" + ((int) c) + ";");
				} else
					sb.append(c);
			}
		}
		return sb.toString();
	}
}
