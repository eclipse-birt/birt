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
import java.util.Iterator;
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
public class ParseCompileSummary {

	private static File ResultFile = null;
	/*
	 * Variable for auto test suite test report
	 */
	private static String PluginName = null;
	private static String WarningCase = null;
	private static String ErrorCase = null;

	private static String countError = null;
	private static String countWarning = null;

	public ParseCompileSummary() {

	}

	public static void getResultFile(String testResultPath) {

		ResultFile = new File(testResultPath);

		if (ResultFile.exists()) {

			PluginName = ResultFile.getName();

		}
	}

	public static void main(String[] args) {

		int i = 0;

		ParseCompileSummary report = new ParseCompileSummary();

		/* Get xml result folder */

		File[] xmlReports = getTestResult(args[0]);
		String outputPath = args[1];
		countWarning = args[2];
		countError = args[3];

		int reportCount = xmlReports.length;

		/* parse arguments */

		for (i = 0; i < reportCount; i++) {
			File ResultFileFolder = xmlReports[i];
			if (ResultFileFolder.isFile())
				continue;
			File[] files = ResultFileFolder.listFiles(new SuffixFileFilter(".xml"));
			/* read junit test reports */
			PluginName = ResultFileFolder.getName();
			System.out.println("Processing " + PluginName + "...");

			for (int j = 0; j < files.length; ++j) {

				ResultFile = files[j];
				report.getTestResult();
				report.wrtTestReport(outputPath);
			}
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
		Element statElement = rootElement.element("stats");
		Iterator it = statElement.elementIterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			String name = element.getName();
			if (name.equalsIgnoreCase("problem_summary")) {
				if (countWarning.equals("Y")) {
					WarningCase = element.attributeValue("warnings");

				}
				if (countError.equals("Y")) {
					ErrorCase = element.attributeValue("errors");

				}
				return;
			}
		}

		WarningCase = "0";
		ErrorCase = "0";

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
			if (countWarning.equals("Y")) {
				line = PluginName.concat("_warning=");
				line = line.concat(WarningCase);
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
