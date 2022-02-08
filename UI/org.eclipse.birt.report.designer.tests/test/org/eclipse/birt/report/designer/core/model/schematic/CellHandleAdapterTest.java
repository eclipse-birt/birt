/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;

import com.ibm.icu.util.ULocale;

public class CellHandleAdapterTest extends TestCase {

	private SessionHandle sessionHandle;

	private ReportDesignHandle designHandle;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		sessionHandle = DesignEngine.newSession(ULocale.getDefault());

		designHandle = sessionHandle.createDesign();

	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetChildren() {
		CellHandle cell = designHandle.getElementFactory().newCell();
		TextItemHandle text = designHandle.getElementFactory().newTextItem("abcd");

		try {
			cell.getContent().add(text);
		} catch (ContentException e) {
			fail("error when add text to tabel cell");
			e.printStackTrace();
		} catch (NameException e) {
			fail("error when add text to tabel cell");
			e.printStackTrace();
		}

		CellHandleAdapter adapter = new CellHandleAdapter(cell, null);
		for (Iterator it = adapter.getChildren().iterator(); it.hasNext();) {
			Object obj = it.next();
			assertTrue(obj instanceof TextItemHandle);
		}

	}

}
