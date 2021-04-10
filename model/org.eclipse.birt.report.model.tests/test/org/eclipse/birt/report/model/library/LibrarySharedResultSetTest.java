/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.library;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.ExtendsForbiddenException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests cases in the library.
 * 
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
 * collapse" bordercolor="#111111" width="100%" id="AutoNumber3" height="50">
 * <tr>
 * <td width="33%" height="16"><b>Method </b></td>
 * <td width="33%" height="16"><b>Test Case </b></td>
 * <td width="34%" height="16"><b>Expected Result </b></td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testCannotExtends()}</td>
 * <td>Shared Result set report items cannot be extended.</td>
 * <td>Extends exception throws.</td>
 * </tr>
 * 
 * 
 * </table>
 * 
 */

public class LibrarySharedResultSetTest extends BaseTestCase {

	/**
	 * Shared Result set report items cannot be extended.
	 * 
	 * @throws Exception
	 */

	public void testCannotExtends() throws Exception {
		openDesign("BlankDesign.xml"); //$NON-NLS-1$

		designHandle.includeLibrary("SharedResultSetLibrary.xml", "lib1"); //$NON-NLS-1$ //$NON-NLS-2$

		libraryHandle = designHandle.getLibrary("lib1"); //$NON-NLS-1$
		assertTrue(libraryHandle.isValid());
		TableHandle table2 = (TableHandle) libraryHandle.findElement("table2"); //$NON-NLS-1$
		assertEquals(ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF, table2.getDataBindingType());

		try {
			designHandle.getElementFactory().newElementFrom(table2, "newTable2"); //$NON-NLS-1$
			fail();
		} catch (ExtendsException e) {
			assertEquals(ExtendsForbiddenException.DESIGN_EXCEPTION_RESULT_SET_SHARED_CANT_EXTEND, e.getErrorCode());
		}

		// tests extend report item which contains an result set shared report
		// item.

		DesignElementHandle grid = libraryHandle.findElement("NewGrid");//$NON-NLS-1$

		try {
			designHandle.getElementFactory().newElementFrom(grid, "newGrid"); //$NON-NLS-1$
			fail();
		} catch (ExtendsException e) {
			assertEquals(ExtendsForbiddenException.DESIGN_EXCEPTION_RESULT_SET_SHARED_CANT_EXTEND, e.getErrorCode());
		}

	}
}
