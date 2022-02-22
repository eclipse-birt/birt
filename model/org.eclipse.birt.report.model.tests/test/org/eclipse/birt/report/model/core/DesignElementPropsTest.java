/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for getProperties of DesignElement.
 * <p>
 * Test Case:
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="black">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected Result</th>
 *
 * <tr>
 * <td>testImageProperties</td>
 * <td>Get the property list of Image item and compare the list with a golden
 * file.</td>
 * <td>The list and the golden file should be identical</td>
 * </tr>
 *
 * <tr>
 * <td>testTableItemProperties</td>
 * <td>Get the property list of Table item and compare the list with a golden
 * file.</td>
 * <td>The list and the golden file should be identical</td>
 * </tr>
 *
 * <tr>
 * <td>testCellProperties</td>
 * <td>Get the property list of Cell item and compare the list with a golden
 * file.</td>
 * <td>The list and the golden file should be identical</td>
 * </tr>
 *
 */
public class DesignElementPropsTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test the image property list.
	 *
	 * @throws Exception
	 */

	public void testImageProperties() throws Exception {
		List list = getProperties(ReportDesignConstants.IMAGE_ITEM);
		assertTrue(compareListWithGoldenFile(list, "ImagePropsTest_golden.txt")); //$NON-NLS-1$
		list = getLocalProperties(ReportDesignConstants.IMAGE_ITEM);
		assertTrue(compareListWithGoldenFile(list, "ImageLocalPropsTest_golden.txt")); //$NON-NLS-1$
	}

	/**
	 * Test the table item property list.
	 *
	 * @throws Exception
	 */

	public void testTableProperties() throws Exception {
		List list = getProperties(ReportDesignConstants.TABLE_ITEM);

		assertTrue(compareListWithGoldenFile(list, "TableItemPropsTest_golden.txt")); //$NON-NLS-1$
		list = getLocalProperties(ReportDesignConstants.TABLE_ITEM);

		assertTrue(compareListWithGoldenFile(list, "TableItemLocalPropsTest_golden.txt")); //$NON-NLS-1$
	}

	/**
	 * Test the cell item property list.
	 *
	 * @throws Exception
	 */
	public void testCellProperties() throws Exception {
		List list = getProperties(ReportDesignConstants.CELL_ELEMENT);
		assertTrue(compareListWithGoldenFile(list, "CellPropsTest_golden.txt")); //$NON-NLS-1$
		list = getLocalProperties(ReportDesignConstants.CELL_ELEMENT);
		assertTrue(compareListWithGoldenFile(list, "CellLocalPropsTest_golden.txt")); //$NON-NLS-1$
	}

	/**
	 * Returns the property list of an element.
	 *
	 * @param elementName the element name.
	 * @return the property list of the provided element.
	 * @throws Exception
	 */
	private List getProperties(String elementName) throws Exception {
		IMetaDataDictionary dictionary = DesignEngine.getMetaDataDictionary();
		IElementDefn eDefn = dictionary.getElement(elementName);
		List list = eDefn.getProperties();
		return list;
	}

	/**
	 * Returns the local property list of an element.
	 *
	 * @param elementName the element name.
	 * @return the property list of the provided element.
	 * @throws Exception
	 */
	private List getLocalProperties(String elementName) throws Exception {
		IMetaDataDictionary dictionary = DesignEngine.getMetaDataDictionary();
		IElementDefn eDefn = dictionary.getElement(elementName);
		List list = eDefn.getLocalProperties();
		return list;
	}

	/**
	 * Compare the list with the golden file line by line.
	 *
	 * @param list       The list to be compared.
	 * @param goldenFile the golden file name.
	 * @return true is every element in the list is identical the corresponding line
	 *         of the golden file.
	 * @throws Exception
	 */
	private boolean compareListWithGoldenFile(List<PropertyDefn> list, String goldenFile) throws Exception {
		boolean isIdentical = true;
		String fullGoldenFile = GOLDEN_FOLDER + goldenFile;
		InputStream is = getResourceAStream(fullGoldenFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		for (int i = 0; i < list.size(); i++) {
			String item = ((PropertyDefn) list.get(i)).getName();
			String line = reader.readLine();
			if (!item.equals(line)) {
				System.out.print("line number:" + i); //$NON-NLS-1$
				System.out.println("\t"); //$NON-NLS-1$
				System.out.println("item:" + item + "\tline:" + line + "\n"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				isIdentical = false;
				break;
			}
		}

		if (!isIdentical) {
			StringBuilder sb = new StringBuilder();
			for (PropertyDefn one : list) {
				sb.append(one.getName() + "\n");
			}

			saveOutputFile(goldenFile, sb.toString());
		}

		// System.out.println( "\n" ); //$NON-NLS-1$
		String line = reader.readLine();
		if (line != null) {
			return false;
		}

		return isIdentical;
	}

}
