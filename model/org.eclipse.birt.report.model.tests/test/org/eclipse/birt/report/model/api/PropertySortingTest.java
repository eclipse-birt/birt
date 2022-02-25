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

package org.eclipse.birt.report.model.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test property sorting feature. <code>PropertyIterator</code> and
 * <code>MemberIterator</code> should return Properties and Members that have
 * been sorted by there display names.
 * <p>
 * <code>StructureIterator</code> return the Structures as they were stored.
 * <p>
 * This test use the 3 Iterators to traverse over an Style element. Dump all
 * it's property names and member names inside a structure into a file.
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #test_EN()}</td>
 * <td>ULocale is English, traverse over an Style element and dump its localized
 * property names int to "propertyIterator_EN.out"</td>
 * <td>The result file should include all property names of Style according to
 * metadata definition. Property Display Names is defined in
 * "Messages_EN.properties". Member properties of a structure should also be
 * sorted by their display name. The structure of the ouput file is like this:
 *
 * <pre>
 *    Axx
 *    Group1.A
 *    Group1.B
 *    Hxx
 *         A_Member
 *         B_Member
 *         C_Member
 *    Jxx.Bxx
 * </pre>
 *
 * </td>
 * </tr>
 *
 * <tr>
 * <td>{@link #test_zh_CN()}</td>
 * <td>The same as above. Except that ULocale is China and output file is
 * "propertyIterator_zh_CN.out"</td>
 * <td>The same as above. The display names is defined in
 * "Messages_zh_CN.properties".</td>
 * </tr>
 * </table>
 *
 */
public class PropertySortingTest extends BaseTestCase {

	/**
	 * Test PropertyIterator on ENGLISH locale.
	 *
	 * @throws Exception
	 */
	public void test_EN() throws Exception {
		openDesign("PropertySortingTest.xml", ULocale.ENGLISH); //$NON-NLS-1$
		dump("propertyIterator_EN.out"); //$NON-NLS-1$
	}

	/**
	 * Test PropertyIterator on CHINA locale.
	 *
	 * @throws Exception
	 */
	public void test_zh_CN() throws Exception {
		openDesign("PropertySortingTest.xml", ULocale.CHINA); //$NON-NLS-1$
		dump("propertyIterator_zh_CN.out"); //$NON-NLS-1$
	}

	/**
	 * Visit over an element, get its properties and dump the display names into a
	 * file.
	 *
	 * @param fileName
	 * @throws Exception
	 */
	private void dump(String fileName) throws Exception {
		PrintWriter pw;

		String outputFolder = getTempFolder() + OUTPUT_FOLDER;
		File f = new File(outputFolder);
		if (!f.exists() && !f.mkdir()) {
			throw new Exception("Can not create the output folder!"); //$NON-NLS-1$
		}

		pw = new PrintWriter(new FileOutputStream(new File(outputFolder + fileName)));

		StyleHandle styleHandle = designHandle.findStyle("My-Style1"); //$NON-NLS-1$
		Iterator iterator = styleHandle.getPropertyIterator();

		while (iterator.hasNext()) {
			PropertyHandle propertyHandle = (PropertyHandle) iterator.next();
			IElementPropertyDefn propDefn = propertyHandle.getPropertyDefn();

			pw.println(propDefn.getDisplayName());

			IStructureDefn structDefn = propDefn.getStructDefn();
			if (structDefn != null) {
				Iterator structIterator = propertyHandle.iterator();

				while (structIterator.hasNext()) {
					StructureHandle structHandle = (StructureHandle) structIterator.next();

					Iterator memberIterator = structHandle.iterator();
					while (memberIterator.hasNext()) {
						MemberHandle memHandle = (MemberHandle) memberIterator.next();
						pw.println("    " + memHandle.getDefn().getDisplayName()); //$NON-NLS-1$
					}
					pw.println();
				}
			}
		}
		pw.close();

	}

}
