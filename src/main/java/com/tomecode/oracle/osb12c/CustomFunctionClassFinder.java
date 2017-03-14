package com.tomecode.oracle.osb12c;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * find classes in the classpath @ *
 * 
 * @author Tome
 *
 */
public final class CustomFunctionClassFinder {

	/**
	 * for all elements of java.class.path get a Collection of resources Pattern
	 * pattern = Pattern.compile(".*"); gets all resources
	 * 
	 * @param pattern
	 *            the pattern to match
	 * @param jars
	 * @return the resources in the order they are found
	 * 
	 * @throws Exception
	 */
	public final static HashMap<String, URL> getResources(List<File> jars) throws Exception {
		HashMap<String, URL> retval = new HashMap<String, URL>();
		final String classPath = System.getProperty("java.class.path", ".");
		final String[] classPathElements = classPath.split(System.getProperty("path.separator"));
		for (final String element : classPathElements) {
			findResources(element, retval);
		}

		if (jars != null) {
			for (File jar : jars) {
				resourcesFromJarFile(jar, retval);
			}
		}
		return retval;
	}

	private final static void findResources(final String element, HashMap<String, URL> retval) throws Exception {
		final File file = new File(element);
		if (file.isDirectory()) {
			resourcesFromDirectory(file, retval);
		} else {
			resourcesFromJarFile(file, retval);
		}

	}

	private final static void resourcesFromJarFile(final File file, HashMap<String, URL> retval) throws Exception {

		if (isZip(file)) {

			ZipFile zf = null;
			try {
				zf = new ZipFile(file);
				final Enumeration<? extends ZipEntry> e = zf.entries();
				while (e.hasMoreElements()) {
					final ZipEntry ze = (ZipEntry) e.nextElement();
					appendResource(ze.getName(), file, retval);
				}
			} finally {
				if (zf != null) {
					zf.close();
				}
			}
		} else {
			appendResource(file.toString(), null, retval);
		}
	}

	private final static void appendResource(String fileName, File file, HashMap<String, URL> retval) throws Exception {
		if (isFunctionDescriptor(fileName)) {
			if (file != null) {
				retval.put(fileName, file.toURI().toURL());
			} else {
				retval.put(fileName, null);
			}
		}
	}

	private static boolean isFunctionDescriptor(String fileName) {
		File f = new File(fileName);
		String name = f.getName().toLowerCase();
		return (name.endsWith(".xml") && name.contains("-xqf"));
	}

	private final static void resourcesFromDirectory(final File directory, HashMap<String, URL> retval) throws Exception {

		final File[] fileList = directory.listFiles();
		for (final File file : fileList) {
			if (file.isDirectory()) {
				resourcesFromDirectory(file, retval);
			} else {
				try {
					final String fileName = file.getCanonicalPath();

					if (isFunctionDescriptor(fileName)) {
						findResources(fileName, retval);
					}
					// final boolean accept =
					// pattern.matcher(fileName).matches();
					// if (accept) {
					// findResources(fileName, retval);
					// }
				} catch (final IOException e) {
					throw new Error(e);
				}
			}
		}

	}

	private static final boolean isZip(File file) {

		ZipInputStream zip = null;
		try {
			zip = new ZipInputStream(new FileInputStream(file));
			return zip.getNextEntry() != null;
		} catch (Exception e) {

		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param jars
	 * @return
	 * @throws Exception
	 */
	public final static HashMap<String, URL> listCustomFunctions(List<File> jars) throws Exception {
		// Pattern pattern = Pattern.compile("(sb)*(xqf)*(.xml)");
		return CustomFunctionClassFinder.getResources(jars);
	}
	//
	// public static final void main(String[] arg) throws Exception {
	//
	// boolean pattern =
	// Pattern.compile("([^\\s]+(\\.(?i)(xml))$)").matcher("/a/a/sb-xqf-uuid.xml").find();
	// boolean pattern1 =
	// Pattern.compile("(sb)*(xqf)(.xml)").matcher("/a/a/pom.xml").find();
	// if (pattern) {
	// "".toCharArray();
	// }
	// /// listCustomFunctions(null);
	//
	// // HashMap<String, URL> s = listOfClass();
	// // s.toString();
	// }

	// public final static HashMap<String, URL> listOfClass() throws Exception {
	// Pattern pattern = Pattern.compile(".*");
	// return ClasspathFinder.getResources(pattern);
	// }
}