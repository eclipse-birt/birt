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

import java.net.URL;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 *
 * Unit test for Class DesignElement.
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testOpenDesign}</td>
 * <td>Open design with null ULocale</td>
 * <td>The design is not null and the locale is the default value.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Open design with a given ULocale</td>
 * <td>The design is not null and the locale is the given value.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Open design with a given design file</td>
 * <td>The design is not null and the locale is the specified value in the
 * design file.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Retrieve the session from the opened designs</td>
 * <td>The returned session is correct.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Check the number of designs which are kept in the session's design list
 * </td>
 * <td>The number of designs is correct.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Drop a design then check the size of the design list.</td>
 * <td>The size of the list is changed correctly.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSetValue}</td>
 * <td>Get the default RGB and units value.</td>
 * <td>The RGB and unit value are returned.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Set correct new value for RGB and units.</td>
 * <td>Values are changed respectively.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Set incorrect new default value for RGB and units.</td>
 * <td>Exception expected.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Set correct default value for dimension type property.</td>
 * <td>Get the value correctly.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Set incorrect default value for it.</td>
 * <td>Exception expected.</td>
 * </tr>
 *
 * </table>
 *
 */

public class DesignSessionTest extends BaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}

	/**
	 * Tests open design method. Test cases:
	 *
	 * <ul>
	 * <li>Case1:Open design with null ULocale.
	 * <li>Case2:Open design with a given ULocale.
	 * <li>Case3:Open design with a given design file.
	 * <li>Case4:Retrieve the session from the opened designs.
	 * <li>Case5:Check the number of designs which are kept in the session's design
	 * list.
	 * <li>Case6:Drop a design then check the size of the design list.
	 * </ul>
	 *
	 * @throws DesignFileException
	 */

	public void testOpenDesign() throws DesignFileException {
		ULocale defaultULocale = ULocale.getDefault();
		ULocale.setDefault(ULocale.CHINESE);

		DesignSession session = new DesignSession(null);
		assertEquals(ULocale.CHINESE, CoreTestUtil.getSessionLocale(session));

		session = new DesignSession(ULocale.ENGLISH);
		assertEquals(ULocale.ENGLISH, CoreTestUtil.getSessionLocale(session));

		ReportDesign design = session.createDesign(null, null);
		assertNotNull(design);
		assertEquals(session, CoreTestUtil.getDesignSession(design));

		design = session.openDesign(getResource("input/DesignSessionTest.xml").toString()); //$NON-NLS-1$
		assertEquals(session, CoreTestUtil.getDesignSession(design));

		design = session.openDesign(getResource("input/DesignSessionTest.xml").toString()); //$NON-NLS-1$

		assertEquals(3, CoreTestUtil.getDesigns(session).size());
		session.drop(design);
		assertEquals(2, CoreTestUtil.getDesigns(session).size());

		design = session.createDesign("template", null); //$NON-NLS-1$
		assertEquals(3, CoreTestUtil.getDesigns(session).size());

		ULocale.setDefault(defaultULocale);
	}

	/**
	 * Tests createDesignFromTemplate()
	 *
	 * @throws DesignFileException
	 */

	public void testCreateDesignFromTemplate() throws DesignFileException {
		DesignSession session = new DesignSession(null);
		URL url = getResource("input/CreateDesignFromTemplateTest.xml"); //$NON-NLS-1$
		design = session.createDesignFromTemplate(url.toString());

		Label label = (Label) design.findElement("Label1"); //$NON-NLS-1$
		assertNotNull(label);
		assertEquals("Test", label.getProperty(design.getRoot(), "text")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(null, design.getFileName());

		TextItem text = (TextItem) design.findElement("NewText"); //$NON-NLS-1$
		assertEquals("blue", text.getProperty(design.getRoot(), "color")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("\"Arial\"", text.getProperty(design.getRoot(), "fontFamily")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("white", text.getProperty(design.getRoot(), "backgroundColor")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("hello world", text.getProperty(design.getRoot(), "content")); //$NON-NLS-1$ //$NON-NLS-2$
		List libs = design.getLibraries();
		assertEquals(1, libs.size());
		Library lib = (Library) libs.get(0);
		assertEquals("LibraryForCreateDesignFromTemplateTest", lib.getNamespace());//$NON-NLS-1$

	}

	/**
	 * Tests createLibraryFromTemplate()
	 *
	 * @throws DesignFileException
	 */

	public void testCreateLibraryFromTemplate() throws Exception {
		DesignSession session = new DesignSession(null);
		URL url = getResource("input/CreateLibraryFromTemplateTest.xml"); //$NON-NLS-1$
		Library library = session.createLibraryFromTemplate(url.toString());

		Label label = (Label) library.findElement("NewLabel"); //$NON-NLS-1$
		assertNotNull(label);
		assertEquals(5, label.getID());
		assertEquals("aaa", label.getProperty(library.getRoot(), ILabelModel.TEXT_PROP)); //$NON-NLS-1$
		assertEquals("blue", label.getProperty(library.getRoot(), IStyleModel.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("aqua", label.getProperty(library.getRoot(), IStyleModel.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$

		assertEquals(null, library.getFileName());

		// this library template has no theme, then create
		url = getResource("input/CreateLibraryFromTemplateTest_1.xml"); //$NON-NLS-1$
		library = session.createLibraryFromTemplate(url.toString());
		save(library.getModuleHandle());
		assertTrue(compareFile("DesignSessionTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests setValue methods. Test cases:
	 *
	 * <ul>
	 * <li>Case1:Get the default RGB and units value.
	 * <li>Case2:Set correct new value for RGB and units.
	 * <li>Case3:Set incorrect new default value for RGB and units. Exception
	 * expected.
	 * <li>Case4:Set correct default value for dimension type property.Get the
	 * value.
	 * <li>Case5:Set incorrect default value for it. Exception expected.
	 * </ul>
	 *
	 * @throws PropertyValueException
	 */

	public void testSetValue() throws PropertyValueException {

		DesignSession session = new DesignSession(null);

		// get the default RGBvalue
		assertEquals(3, session.getColorFormat());

		// set the new RGB value
		session.setColorFormat(2);
		assertEquals(2, session.getColorFormat());

		try {
			session.setColorFormat(999999);
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		assertEquals(2, session.getColorFormat());

		// the default units value
		assertEquals("in", session.getUnits()); //$NON-NLS-1$

		// set the new unit value
		session.setUnits("cm"); //$NON-NLS-1$
		assertEquals("cm", session.getUnits()); //$NON-NLS-1$

		// set the invalid units value
		try {
			session.setUnits("wrong units"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		assertEquals("cm", session.getUnits()); //$NON-NLS-1$

		// get the default value of font-size. The default value is not set.
		assertNull(session.getDefaultValue(IStyleModel.FONT_SIZE_PROP));
		// set a default value for it
		session.setDefaultValue(IStyleModel.FONT_SIZE_PROP, "3cm"); //$NON-NLS-1$

		assertEquals("3cm", ((DimensionValue) session.getDefaultValue(IStyleModel.FONT_SIZE_PROP)).toString()); //$NON-NLS-1$

		// set the wrong value for font-size.
		try {
			session.setDefaultValue(IStyleModel.FONT_SIZE_PROP, "wrong value"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException pve) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, pve.getErrorCode());
		}

		// set the default value of font-size as null
		session.setDefaultValue(IStyleModel.FONT_SIZE_PROP, null);
	}

}
