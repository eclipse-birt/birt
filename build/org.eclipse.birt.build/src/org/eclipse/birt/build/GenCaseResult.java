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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.FileWriter;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;
import org.dom4j.DocumentFactory;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;
import java.lang.Integer;

public class GenCaseResult {

	private static File ResultFile = null;
	/*
	 * Variable for auto test suite test report
	 */
	private static String PluginName = null;
	private static String TotalCase = null;
	private static String FailCase = null;
	private static String ErrorCase = null;
	private static boolean compileError = false;
	/*
	 * Variable for performance test report
	 */
	private static String performancePath = null;
	private File ReportPath = null;
	/*
	 * Variable for Eninge Smoke and DTE Smoke test report
	 */
	private String TotalRpt = null;
	private String SameRpt = null;
	private String DiffRpt = null;

	private static String CONSTANT_RED = "#FF0000";
	private static String CONSTANT_BLACK = "#000000";
	private static String CONSTANT_BOLD = "font-weight:700";
	private static String CONSTANT_NORMAL = "font-weight:100";

	private DOMElement rootElement = new DOMElement("TestSuiteReport");

	public GenCaseResult() {

	}

	public File getReportPath(String testReport) {

		this.ReportPath = new File(testReport);
		return this.ReportPath;

	}

	public void setPerformancePath(String path) {
		performancePath = path;
	}

	public static void getResultFile(String testResultPath) {
		ResultFile = new File(testResultPath);

		if (!ResultFile.exists()) {
			compileError = true;

			PluginName = ResultFile.getName();
			TotalCase = "N/A";
			FailCase = "N/A";
			ErrorCase = "N/A";
		} else {
			compileError = false;
		}
	}

	public static void main(String[] args) {

		int i = 0;

		GenCaseResult report = new GenCaseResult();

		/* parse arguments */
		for (i = 1; i < args.length; i++) {
			System.out.println(args[i]);
			String param = args[i];
			/* parse enine smoke test result */
			if (param.startsWith("--") && param.endsWith("performance")) {
				report.setPerformancePath(args[++i]);
				report.wrtTestReport("performance");
				continue;
			}
			if (param.startsWith("--") && param.endsWith("smoketest")) {
				report.setPerformancePath(args[++i]);
				getResultFile(args[i]);
				report.parseSmokeTestResult();
				report.wrtTestReport("engine." + args[i]);
				continue;
			}
			getResultFile(args[i]);
			report.getTestResult();
			report.wrtTestReport("autotest");
		}
		report.genReport(args[0]);
	}

	private void genReport(String path) {

		/*
		 * Add genDate node to report
		 */

		DOMElement testElement = new DOMElement("ReportDate");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");

		String strDate = currentDate.format(cal.getTime());
		testElement.setText(strDate);
		this.rootElement.add(testElement);

		DocumentFactory factory = new DocumentFactory();
		Document doc = factory.createDocument(rootElement);
		OutputFormat format = OutputFormat.createPrettyPrint();

		try {

			XMLWriter writer = new XMLWriter(new FileWriter(this.getReportPath(path)), format);
			writer.write(doc);
			writer.close();

		} catch (Exception ex) {

			ex.printStackTrace();

		}

	}

	private void getTestResult() {

		if (!compileError) {
			SAXReader saxReader = new SAXReader();
			Document document = null;
			try {
				document = saxReader.read(ResultFile);
			} catch (org.dom4j.DocumentException dex) {
				dex.printStackTrace();
			}
			// Get total case number, fail number and success number
			Element rootElement = document.getRootElement();
			PluginName = rootElement.attributeValue("name");
			TotalCase = rootElement.attributeValue("tests");
			FailCase = rootElement.attributeValue("failures");
			ErrorCase = rootElement.attributeValue("errors");

			System.out.println(PluginName);
			System.out.println(TotalCase);
			System.out.println(FailCase);
			System.out.println(ErrorCase);
		} else {
			System.out.println("Compile Error: " + PluginName);
			System.out.println("Compile Error: " + TotalCase);
			System.out.println("Compile Error: " + FailCase);
			System.out.println("Compile Error: " + ErrorCase);

		}
	}

	private void parseSmokeTestResult() {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		String SumName;
		String keyName;
		try {
			document = saxReader.read(ResultFile);
		} catch (org.dom4j.DocumentException dex) {
			dex.printStackTrace();
		}
		// Get total case number, fail number and success number
		Element rootElement = document.getRootElement();
		Iterator it = rootElement.elementIterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			SumName = element.getName();
			if (SumName.equalsIgnoreCase("sum")) {
				Iterator ittmp = element.elementIterator();
				while (ittmp.hasNext()) {
					Element keyelement = (Element) ittmp.next();
					keyName = keyelement.getName();
					if (keyName.equalsIgnoreCase("same")) {
						this.SameRpt = keyelement.getText();
					} else if (keyName.equalsIgnoreCase("differ")) {
						this.DiffRpt = keyelement.getText();
					} else
						continue;
				}
			}
		}
		int totalRpt;

		totalRpt = Integer.valueOf(this.SameRpt.trim()).intValue();
		totalRpt += Integer.valueOf(this.DiffRpt.trim()).intValue();
		this.TotalRpt = Integer.toString(totalRpt);

		System.out.println("Engine Smoke Total:" + this.TotalRpt);
		System.out.println("Engine Smoke Same" + this.SameRpt);
		System.out.println("Engine Smoke Diff" + this.DiffRpt);

	}

	public static String getPerformanceFilePath(String folder) {

		File file = new File(folder);
		File[] files = file.listFiles(new PrefixFileFilter("reporting_2006"));

		if (files.length == 0) {
			compileError = true;
			System.out.println("Performance no file");
			return "http://www.actuate.com";
		} else {
			compileError = false;
			return files[0].toString();
		}
	}

	private void wrtTestReport(String reportType) {

		try {
			if (reportType.equalsIgnoreCase("autotest")) {
				DOMElement testElement = new DOMElement("TestPlugin");
				String testSuitePlugin;
				testSuitePlugin = PluginName.replace(".AllTests", "");
				testElement.setAttribute("id", testSuitePlugin);
				testElement.setAttribute("total", TotalCase);

				/* Add Fail Node Leaf */
				DOMElement detailElement = new DOMElement("fail");
				detailElement.setText(FailCase);

				if ((FailCase != "N/A") && (Integer.valueOf(FailCase.trim()).intValue() > 0)) {
					detailElement.setAttribute("color", CONSTANT_RED);
					detailElement.setAttribute("boldStyle", CONSTANT_BOLD);
				} else {
					detailElement.setAttribute("color", CONSTANT_BLACK);
					detailElement.setAttribute("boldStyle", CONSTANT_NORMAL);
				}
				testElement.add(detailElement);

				/* Add Error Node Leaf */
				detailElement = new DOMElement("error");
				detailElement.setText(ErrorCase);
				if ((FailCase != "N/A") && (Integer.valueOf(ErrorCase.trim()).intValue() > 0)) {
					detailElement.setAttribute("color", CONSTANT_RED);
					detailElement.setAttribute("boldStyle", CONSTANT_BOLD);
				} else {
					detailElement.setAttribute("color", CONSTANT_BLACK);
					detailElement.setAttribute("boldStyle", CONSTANT_NORMAL);
				}
				testElement.add(detailElement);
				/* Add compile status */
				detailElement = new DOMElement("compileStatus");
				if (compileError) {
					detailElement.setText("Fail");
					detailElement.setAttribute("color", CONSTANT_RED);
					detailElement.setAttribute("boldStyle", CONSTANT_BOLD);

				} else {
					detailElement.setText("Pass");
					detailElement.setAttribute("color", CONSTANT_BLACK);
					detailElement.setAttribute("boldStyle", CONSTANT_NORMAL);
				}
				testElement.add(detailElement);
				this.rootElement.add(testElement);

			} else if (reportType.equalsIgnoreCase("performance")) {
				DOMElement testElement = new DOMElement("Performance");
				testElement.setAttribute("id", "org.eclipse.birt.tests.performance");
				testElement.setAttribute("URL", getPerformanceFilePath(performancePath));
				/*
				 * Add compile status
				 */
				DOMElement detailElement = new DOMElement("compileStatus");
				if (compileError) {
					detailElement.setText("Fail");
					detailElement.setAttribute("color", CONSTANT_RED);
					detailElement.setAttribute("boldStyle", CONSTANT_BOLD);

				} else {
					detailElement.setText("Pass");
					detailElement.setAttribute("color", CONSTANT_BLACK);
					detailElement.setAttribute("boldStyle", CONSTANT_NORMAL);

				}
				testElement.add(detailElement);
				this.rootElement.add(testElement);
			} else {
				DOMElement testElement = new DOMElement("smoketest");
				int index;
				index = reportType.indexOf("DTE");
				if (index == -1) {
					index = reportType.indexOf("Engine");
					if (index == -1)
						return;
					else
						testElement.setAttribute("id", "Engine Smoke");
				} else {
					testElement.setAttribute("id", "DTE Smoke");
				}
				testElement.setAttribute("total", this.TotalRpt.trim());
				testElement.setAttribute("same", this.SameRpt.trim());
				/*
				 * testElement.setAttribute("diff",this.DiffRpt);
				 */
				DOMElement detailElement = new DOMElement("diff");
				detailElement.setText(this.DiffRpt);

				if (Integer.valueOf(this.DiffRpt.trim()).intValue() > 0) {
					detailElement.setAttribute("color", CONSTANT_RED);
					detailElement.setAttribute("boldStyle", CONSTANT_BOLD);
				} else {
					detailElement.setAttribute("color", CONSTANT_BLACK);
					detailElement.setAttribute("boldStyle", CONSTANT_NORMAL);
				}
				testElement.add(detailElement);
				this.rootElement.add(testElement);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
