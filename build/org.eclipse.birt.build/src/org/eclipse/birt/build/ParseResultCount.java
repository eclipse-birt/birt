/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.build;

import java.io.File;
import java.io.FileWriter;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.Element;

/**
 * 
 * @author Farrah
 * 
 *         Generate error/failure summary according into
 *         unitTestReport.properties Properties format
 *         pluginId.failure/error=<count> eg:
 *         org.eclipse.birt.tests.data.engine.AllTests.failure=1
 *         org.eclipse.birt.tests.data.engine.AllTests.error=0
 */
public class ParseResultCount {

	private static File ResultFile = null;
	/*
	 * Variable for auto test suite test report
	 */
	private static String PluginName = null;
	private static String FailCase = null;
	private static String ErrorCase = null;

	private static String countError = null;
	private static String countFailure = null;

	public ParseResultCount() {

	}

	public static void getResultFile(String testResultPath) {

		ResultFile = new File(testResultPath);

		if (ResultFile.exists()) {

			PluginName = ResultFile.getName();

		}
	}

	public static void main(String[] args) {

		int i = 0;

		ParseResultCount report = new ParseResultCount();

		/* Get xml result folder */

		File[] xmlReports = getTestResult(args[0]);
		String outputPath = args[1];
		countFailure = args[2];
		countError = args[3];

		int reportCount = xmlReports.length;

		/* parse arguments */
		for (i = 0; i < reportCount; i++) {

			/* read junit test reports */

			ResultFile = xmlReports[i];
			report.getTestResult();
			report.wrtTestReport(outputPath);
		}

	}

	private void getTestResult() {

		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(ResultFile);
		} catch (org.dom4j.DocumentException dex) {
			dex.printStackTrace();
		}
		// Get total case number, fail number and success number
		Element rootElement = document.getRootElement();
		if (rootElement.getName().equals("testsuite")) {
			PluginName = rootElement.attributeValue("name");
			if (countFailure.equals("Y")) {
				FailCase = rootElement.attributeValue("failures");
			}
			if (countError.equals("Y")) {
				ErrorCase = rootElement.attributeValue("errors");
			}

		} else {
			Element testsuiteElement = rootElement.element("testsuite");
			PluginName = testsuiteElement.attributeValue("name");
			if (countFailure.equals("Y")) {
				FailCase = testsuiteElement.attributeValue("failures");
			}
			if (countError.equals("Y")) {
				ErrorCase = testsuiteElement.attributeValue("errors");
			}

		}

	}

	public static File[] getTestResult(String folder) {

		File file = new File(folder);
		File[] files = file.listFiles();

		if (files.length == 0) {
			return null;
		} else {
			return files;
		}
	}

	private void wrtTestReport(String output) {

		try {

			File summaryPath = new File(output);
			FileWriter summaryOutput = new FileWriter(summaryPath, true);
			String line = null;

			/* write the error cases total number */
			if (countError.equals("Y")) {
				line = PluginName.concat("_error=");
				line = line.concat(ErrorCase);
				line = line.concat("\n");
				summaryOutput.write(line);
			}
			/* write the failure cases total number */
			if (countFailure.equals("Y")) {
				line = PluginName.concat("_failure=");
				line = line.concat(FailCase);
				line = line.concat("\n");
				summaryOutput.write(line);
			}

			summaryOutput.flush();
			summaryOutput.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
