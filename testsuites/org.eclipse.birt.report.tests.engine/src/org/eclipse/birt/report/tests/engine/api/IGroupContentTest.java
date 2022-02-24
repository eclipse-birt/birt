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

package org.eclipse.birt.report.tests.engine.api;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.tests.engine.EngineCase;

public class IGroupContentTest extends EngineCase {

	/**
	 * Test set/isHeaderRepeat() methods.
	 */
	public void testHeaderRepeat() {
		IGroupContent content = new ReportContent().createTableGroupContent();
		content.setHeaderRepeat(true);
		assertTrue(content.isHeaderRepeat());
		content.setHeaderRepeat(false);
		assertTrue(!content.isHeaderRepeat());
	}

	/**
	 * Test set/getGroupID() methods.
	 */
	public void testGroupID() {
		IGroupContent content = new ReportContent().createTableGroupContent();
		content.setGroupID("1");
		assertEquals("1", content.getGroupID());
		content.setGroupID(null);
		assertNull(content.getGroupID());
	}

	/**
	 * Test getHeader() method.
	 */
	public void testHeader() {
		IGroupContent content = new ReportContent().createTableGroupContent();
		IBandContent header = new ReportContent().createTableBandContent();
		header.setBandType(IBandContent.BAND_GROUP_HEADER);
		content.getChildren().add(header);
		assertEquals(header, content.getHeader());

		header.setBandType(IBandContent.BAND_DETAIL);
		assertNull(content.getHeader());
	}

	/**
	 * Test getFooter() method.
	 */
	public void testFooter() {
		IGroupContent content = new ReportContent().createTableGroupContent();
		IBandContent footer = new ReportContent().createTableBandContent();
		footer.setBandType(IBandContent.BAND_GROUP_FOOTER);
		content.getChildren().add(footer);
		assertEquals(footer, content.getFooter());

		footer.setBandType(IBandContent.BAND_DETAIL);
		assertNull(content.getHeader());
	}

	// TODO: getGroupLevel() method.

}
