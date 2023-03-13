/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.birt.chart.examples.ChartExamplesPlugin;
import org.eclipse.birt.chart.examples.view.description.Messages;
import org.eclipse.birt.core.internal.util.EclipseUtil;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;

import com.ibm.icu.util.StringTokenizer;

public class ItemContentProvider {

	/**
	 * The only instance of ItemContentProvider
	 */
	private static ItemContentProvider content = null;

	/**
	 * When the view is open, the descriptor file is read (only once) and stored in
	 * a StringBuffer.
	 */
	private static StringBuffer dFile = new StringBuffer(""); //$NON-NLS-1$

	// Examples types for each category
	private ArrayList<String> iTypes;

	// Category types
	private ArrayList<String> cTypes;

	// Chart model description for each example
	private String description;

	// Chart model class name for each example
	private String modelClassName;

	// Chart model method name for each example
	private String methodName;

	/**
	 * All category types are stored in a string array
	 */
	private static final String[] categoryTypes = { "SampleChartCategory.PrimitiveCharts", //$NON-NLS-1$
			"SampleChartCategory.3DCharts", //$NON-NLS-1$
			"SampleChartCategory.CombinationCharts", //$NON-NLS-1$
			"SampleChartCategory.FormattedCharts", //$NON-NLS-1$
			"SampleChartCategory.ScriptedCharts", //$NON-NLS-1$
			"SampleChartCategory.DataOperations" //$NON-NLS-1$
	};

	/**
	 * Provide the unique ItemContentProvider instance
	 */
	public static ItemContentProvider instance() {
		if (content == null) {
			content = new ItemContentProvider();
			try {
				openDescriptorFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * Read the descriptor file from file system and stored it in a StringBuffer.
	 *
	 * @throws IOException
	 */
	private static void openDescriptorFile() throws IOException {
		Bundle bundle = EclipseUtil.getBundle(ChartExamplesPlugin.ID);
		URL fileURL;
		if (bundle != null) {
			Path path = new Path("/src/org/eclipse/birt/chart/examples/view/util/description.txt"); //$NON-NLS-1$
			fileURL = FileLocator.find(bundle, path, null);
		} else {
			fileURL = ItemContentProvider.class.getResource("description.txt");
		}

		if (fileURL != null) {
			InputStream file = fileURL.openStream();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(file)))) {
				while (true) {
					String sTmp = reader.readLine();
					if (sTmp == null) {
						break;
					}
					dFile.append(sTmp.trim());
				}
			}
		}
	}

	/**
	 * Retrieve all the item names belonging to a specific category from dFile.
	 *
	 * @param categoryName Category name
	 */
	private void parseItems(String categoryName) {
		String sTmp = dFile.toString();
		String startCategory = categoryName + ">"; //$NON-NLS-1$
		String endCategory = "/" + categoryName + ">"; //$NON-NLS-1$ //$NON-NLS-2$
		StringTokenizer tokens = new StringTokenizer(sTmp, "<"); //$NON-NLS-1$
		boolean bThisCategory = false;
		iTypes = new ArrayList<>();
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (startCategory.equals(token)) {
				bThisCategory = true;
			} else if (endCategory.equals(token)) {
				break;
			} else if (bThisCategory) {
				String sKey = token.substring(0, token.indexOf(">")); //$NON-NLS-1$
				iTypes.add(sKey);
			}
		}
	}

	/**
	 * Retrieve the exampe description according to the example display name.
	 *
	 * @param itemName Item name
	 */
	private void parseDescription(String itemName) {
		description = Messages.getDescription(itemName);
	}

	/**
	 * Retrieve the example class name according to the example display name.
	 *
	 * @param itemName Item name
	 */
	private void parseClassName(String itemName) {
		modelClassName = itemName;

		if (modelClassName != null) {
			int idStart = modelClassName.indexOf('.');

			if (idStart > 0) {
				modelClassName = modelClassName.substring(idStart + 1);
			}
		}
	}

	/**
	 * Retrieve the method name according to the example class name.
	 *
	 * @param className Class name
	 */
	private void parseMethodName(String className) {
		if (className != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("create"); //$NON-NLS-1$
			sb.append(className);
			methodName = sb.toString();
		}
	}

	/**
	 * Retrieve the category names from the String array.
	 *
	 * @return Category names list
	 */
	public ArrayList<String> getCategoryTypes() {
		cTypes = new ArrayList<>();
		for (int iC = 0; iC < categoryTypes.length; iC++) {
			cTypes.add(categoryTypes[iC]);
		}
		return cTypes;
	}

	/**
	 * @param categoryName Category name
	 *
	 * @return Example names list
	 */
	public ArrayList<String> getItemTypes(String categoryName) {
		parseItems(categoryName);
		return iTypes;
	}

	/**
	 * @return Default description (If no example is selected)
	 */
	public String getDefaultDescription() {
		return Messages.getDescription("DefaultDescription");//$NON-NLS-1$
	}

	/**
	 * @param itemName Item name
	 *
	 * @return Description
	 */
	public String getDescription(String itemName) {
		parseDescription(itemName);
		if (description == null) {
			return getDefaultDescription();
		} else {
			return description;
		}
	}

	/**
	 * @param itemName Item name
	 *
	 * @return Example class name
	 */
	public String getClassName(String itemName) {
		parseClassName(itemName);
		return modelClassName;
	}

	/**
	 * @param className Example class name
	 *
	 * @return Chart generation method name
	 */
	public String getMethodName(String className) {
		parseMethodName(className);
		return methodName;
	}

}
