/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.build;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class VerifyCompileUtils {

	private static String logLocation;
	private static String monitorLocatoin;
	private static StringBuffer buffer = new StringBuffer();
	private static int errorCount = 0;

	/**
	 * @param args[0] ${postingDirectory}/${buildId}/compilelogs
	 * @param args[1] property file location: monitor.properties
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		if (args.length != 2) {
			System.out.println("[VerifyCompile] Missing arguments. VerifyCompile aborts!!");
			return;
		}

		logLocation = args[0];
		monitorLocatoin = args[1];

		if (!logLocation.endsWith("plugins")) {
			logLocation = logLocation + File.separator + "plugins";
		}
		if (!monitorLocatoin.endsWith("monitor.properties")) {
			monitorLocatoin = monitorLocatoin + File.separator + "monitor.properties";
		}

		System.out.println("[VerifyCompile] Log location: " + logLocation);
		System.out.println("[VerifyCompile] Monitor location: " + monitorLocatoin);

		File file = new File(logLocation);
		if (!file.exists()) {
			System.out.println("[VerifyCompile] " + logLocation + " does not exist");
			return;
		}
		File[] plugins = file.listFiles();
		System.out.println("[VerifyCompile] Total plugin: " + plugins.length);

		if (plugins.length > 0) {
			for (int i = 0; i < plugins.length; i++) {
				// Check the compile result for each plugin
				if (plugins[i].isDirectory()) {
					System.out.println("[VerifyCompile] Processing " + plugins[i].getName());

					// Get the HTML compile log
					File[] htmlLogs = plugins[i].listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.getName().toLowerCase().endsWith(".html");
						}
					});
					File[] xmlLogs = plugins[i].listFiles(new FileFilter() {
						public boolean accept(File pathname) {
							return pathname.getName().toLowerCase().endsWith(".xml");
						}
					});

					if (xmlLogs.length == 0) {
						System.out.println("[VerifyCompile] Emtpy log directory, continue for next...");
						continue;
					}
					// Parse the xml result file
					if (checkCompileError(xmlLogs[0]) > 0) {
						errorCount++;
						File errorPlugin = new File(xmlLogs[0].getParent());
						File errorRepo = new File(
								errorPlugin.getParent() + File.separator + errorPlugin.getName() + "_compilelog.html");
						System.out.println(errorRepo.getAbsolutePath());

						// A compile error found. Move the html log to parent folder
						if (htmlLogs.length > 0 && htmlLogs[0].exists()) {
							htmlLogs[0].renameTo(errorRepo);
						}
						buffer.append(errorPlugin.getName());
						buffer.append("\\n");
						buffer.append("\\");
						buffer.append("\n");
					}
				}
			}
		}
		appendProperties();
	}

	private static void appendProperties() {
		File monitor = new File(monitorLocatoin);
		BufferedWriter writer = null;
		System.out.println("[VerifyCompile] Error plugins count: " + errorCount);
		if (errorCount == 0) {
			try {
				writer = new BufferedWriter(new FileWriter(monitor, true));
				writer.write("compileHasError=false");
				writer.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				writer = new BufferedWriter(new FileWriter(monitor, true));
				writer.write("compileHasError=true");
				writer.write("\n");
				writer.write("error.plugin.list=" + buffer.toString());

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static int checkCompileError(File ResultFile) {

		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(ResultFile);
		} catch (org.dom4j.DocumentException dex) {
			dex.printStackTrace();
		}

		// Get problem_summary/errors count
		Element rootElement = document.getRootElement();
		if (rootElement.getName().equals("compiler")) {
			Element stats = rootElement.element("stats");
			if (stats.element("problem_summary") != null) {
				// parse summary
				Element problemSummary = stats.element("problem_summary");
				String sCount = problemSummary.attributeValue("errors");
				System.out.println("[VerifyCompile] Error Count: " + sCount);
				if (sCount.equals("0"))
					return 0;
				else
					return Integer.parseInt(sCount);
			} else {
				System.out.println("There is no element problem_summary!");
				return 0;
			}
		}
		return 1;
	}

}
