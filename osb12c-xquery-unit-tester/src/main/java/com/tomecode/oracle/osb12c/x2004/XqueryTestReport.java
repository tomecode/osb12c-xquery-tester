package com.tomecode.oracle.osb12c.x2004;

import java.io.File;
import java.io.FileWriter;
import java.util.Map.Entry;

import com.tomecode.oracle.osb12c.Utils;

/**
 * 
 * Generate report for {@link Xquery} transformation
 * 
 * @author Tome
 *
 */
public final class XqueryTestReport {

	private Xquery xquery;

	public XqueryTestReport(Xquery xquery) {
		this.xquery = xquery;
	}

	/**
	 * report name
	 * 
	 * @param name
	 * @throws Exception
	 */
	public final void generate(String name) throws Exception {

		String templateReport = Utils.readFile(this.getClass().getResourceAsStream("testReport.template"));
		String templateTest = Utils.readFile(this.getClass().getResourceAsStream("test.template"));
		String templateTestPhase = Utils.readFile(this.getClass().getResourceAsStream("testPhase.template"));

		String template = null;

		if (name == null) {
			template = templateReport.replace("@{testcase}", "");
		} else {
			template = templateReport.replace("@{testcase}", name);
		}

		StringBuilder phases = new StringBuilder();

		//
		// XQuery
		//
		phases.append(renderInput(templateTestPhase));

		//
		// arguments
		//
		phases.append(renderArguments(templateTestPhase));

		//
		// output
		//
		phases.append(renderOutput(templateTestPhase));

		// // append details about test
		String sbTest = templateTest.replace("@{phases}", phases);
		// replace test
		template = template.replace("@{test}", sbTest);

		File reportFile = new File(mkDir().getAbsolutePath() + File.separator + (name == null ? "" : name + "_") + "report.html");

		try (FileWriter fw = new FileWriter(reportFile)) {
			fw.write(template.toString());
		}
		reportFile.toPath();
	}

	public final void generate() throws Exception {
		generate(null);
	}

	/**
	 * 
	 * @param templateTestPhase
	 * @return
	 */
	private final String renderInput(String templateTestPhase) {
		return templateTestPhase.replace("@{title}", "XQuery ").replace("@{location}", inputLocation()).replace("@{type}", "xquery").replace("@{content}", xquery.input().getXquery());
	}

	/**
	 * 
	 * @param templateTestPhase
	 * @return
	 * @throws Exception
	 */
	private final String renderArguments(String templateTestPhase) throws Exception {
		StringBuilder phases = new StringBuilder();
		for (Entry<String, XqueryArgument> arg : xquery.arg().entrySet()) {
			String tempPhase = templateTestPhase;

			tempPhase = tempPhase.replace("@{title}", "@argument: " + arg.getKey());

			String location = arg.getValue().getFile();
			if (location != null) {
				tempPhase = tempPhase.replace("@{location}", location);
			} else {
				tempPhase = tempPhase.replace("@{location}", "");

			}

			String xml = arg.getValue().contentAsXml();
			if (xml != null) {
				tempPhase = tempPhase.replace("@{type}", "xml");
				tempPhase = tempPhase.replace("@{content}", Utils.xmlEscapeText(xml));
			} else {
				tempPhase = tempPhase.replace("@{type}", "string");
				tempPhase = tempPhase.replace("@{content}", arg.getValue().contentAsString());
			}

			phases.append(tempPhase.toString());
		}
		return phases.toString();
	}

	/**
	 * 
	 * @param templateTestPhase
	 * @return
	 */
	private final String renderOutput(String templateTestPhase) {
		return templateTestPhase.replace("@{title}", "@Output ").replace("@{location}", "").replace("@{type}", "xml").replace("@{content}", outputContent());
	}

	private final File mkDir() {
		File reportFolders = new File(System.getProperty("user.dir") + File.separator + "target" + File.separator + "xqUnitTestReports");
		if (!reportFolders.exists()) {
			reportFolders.mkdirs();
		}
		return reportFolders;
	}

	private final String inputLocation() {
		if (xquery.input() == null || xquery.input().getFile() == null) {
			return "";
		}
		return xquery.input().getFile().getAbsolutePath();
	}

	private final String outputContent() {
		if (this.xquery.output() == null) {
			return "";
		}
		return Utils.xmlEscapeText(this.xquery.output().contentAsXml());
	}

}
