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
package org.eclipse.birt.report.tests.model.api;

import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestCases for AutoTextHandle class. AutoTextHandle can be created from
 * ElementFactory.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 *
 * <tr>
 * <td>{@link #testAddAndDeleteAutotext()}</td>
 * <td>Add and delete autotext to master page.</td>
 * <td>Autotext can be added and deleted, only one autotext allowed as top level
 * in master page.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testStyleOnAutotext()}</td>
 * <td>Apply style to autotext item.</td>
 * <td>Style properties are set into autotext.</td>
 * </tr>
 * </table>
 *
 */
public class AutotextHandleTest extends BaseTestCase {
	private ElementFactory factory = null;
	private AutoTextHandle autotext = null;
	private AutoTextHandle autotext1 = null;
	private AutoTextHandle autotext2 = null;
	private SimpleMasterPageHandle masterpage = null;

	public AutotextHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {

		return new TestSuite(AutotextHandleTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SessionHandle designSession = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = designSession.createDesign();
		factory = new ElementFactory(designHandle.getModule());
		autotext = factory.newAutoText("autotext");
		autotext1 = factory.newAutoText("autotext1");
		masterpage = factory.newSimpleMasterPage("masterpage");
		designHandle.getMasterPages().add(masterpage);
	}

	/**
	 * test add and delete Autotext
	 *
	 * @throws Exception
	 */
	public void testAddAndDeleteAutotext() throws Exception {

		// add autotext to master page header/footer
		masterpage.getPageHeader().add(autotext);
		assertEquals(1, masterpage.getPageHeader().getContents().size());

		masterpage.getPageFooter().add(autotext1);
		assertEquals(1, masterpage.getPageFooter().getContents().size());

		// add more autotext
		try {
			autotext2 = factory.newAutoText("autotext2");
			masterpage.getPageHeader().add(autotext2);
			fail();
		} catch (ContentException e) {
			assertNotNull(e);
		}

		// delete autotext from master page header/footer
		masterpage.getPageHeader().drop(autotext);
		assertEquals(0, masterpage.getPageHeader().getContents().size());
		masterpage.getPageFooter().drop(autotext1);
		assertEquals(0, masterpage.getPageFooter().getContents().size());

		// add autotext to the container
		TableHandle table = factory.newTableItem("table", 3, 1, 1, 1);
		RowHandle detail = (RowHandle) table.getSlot(TableItem.HEADER_SLOT).get(0);
		CellHandle cell = (CellHandle) detail.getSlot(TableRow.CONTENT_SLOT).get(0);
		cell.getSlot(Cell.CONTENT_SLOT).add(autotext);

	}

	/**
	 * test apply style to autotext.
	 *
	 * @throws Exception
	 */
	public void testStyleOnAutotext() throws Exception {
		// add custom style

		SharedStyleHandle style = factory.newStyle("style");

		style.setStringProperty(IStyleModel.BACKGROUND_COLOR_PROP, "red");
		style.setStringProperty(IStyleModel.FONT_SIZE_PROP, "small");

		designHandle.getStyles().add(style);
		autotext.setStyle(style);

		assertEquals("red", autotext.getStringProperty(IStyleModel.BACKGROUND_COLOR_PROP));
		assertEquals("small", autotext.getStringProperty(IStyleModel.FONT_SIZE_PROP));

	}

}
