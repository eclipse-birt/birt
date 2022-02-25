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

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * The Test Case of StyledElement.
 *
 * We test the get-set style operation and check the container-client
 * relationship.At the same time, we test the getIntrinsicProperty() function.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testSetStyle}</td>
 * <td>add new style</td>
 * <td>can get style</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetIntrinsicProperty}</td>
 * <td>get intrinsic property from styledElement which has name</td>
 * <td>equal to value "styled element"</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>get intrinsic property from styledElement which has style</td>
 * <td>equal to value "style"</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>get intrinsic property from Label which has extends element</td>
 * <td>get name value from parent element , equal to value "hexingjie"</td>
 * </tr>
 *
 * </table>
 *
 */
public class StyledElementTest extends BaseTestCase {

	StyledElement styledElement;
	ElementRefValue styleRef;
	Style style;

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		styledElement = new Label();
		style = new Style();
		styleRef = new ElementRefValue(null, style);
	}

	/**
	 * Test setStyle( StyleElement ) , and getStyle().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>add new style</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>can get style</li>
	 * </ul>
	 */
	public void testSetStyle() {
		assertNull(styledElement.getStyle(design));
		styledElement.setStyle(style);
		assertEquals(style, styledElement.getStyle(design));
		assertEquals(styledElement, ((BackRef) style.getClientList().get(0)).getElement());
	}

	/**
	 * Test getIntrinsicProperty( String ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>get intrinsic property from styledElement which has name</li>
	 * <li>get intrinsic property from styledElement which has style</li>
	 * <li>get intrinsic property from Label which has extends element</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>equal to value "styled element"</li>
	 * <li>equal to value "style"</li>
	 * <li>get name value from parent element , equal to value "hexingjie"</li>
	 * </ul>
	 */
	public void testGetIntrinsicProperty() {
		// get intrinsic property from styledElement which has name

		styledElement.setName("styled element"); //$NON-NLS-1$
		Object o = CoreTestUtil.getIntrinsicProperty(styledElement, DesignElement.NAME_PROP);
		assertEquals("styled element", o); //$NON-NLS-1$

		// get intrinsic property from styledElement which has style

		style.setName("style"); //$NON-NLS-1$
		o = CoreTestUtil.getIntrinsicProperty(styledElement, StyledElement.STYLE_PROP);
		assertNull(o);
		styledElement.setStyle(style);
		o = CoreTestUtil.getIntrinsicProperty(styledElement, StyledElement.STYLE_PROP);
		assertEquals(style, ((ElementRefValue) o).getElement());

		// get intrinsic property from Label which has extends element

		Label label1 = new Label();
		Label label2 = new Label();
		label2.setName("hexingjie"); //$NON-NLS-1$
		label1.setExtendsElement(label2);
		ElementRefValue elementRefValue = (ElementRefValue) CoreTestUtil.getIntrinsicProperty(label1,
				DesignElement.EXTENDS_PROP);
		assertEquals("hexingjie", elementRefValue.getElement().getName()); //$NON-NLS-1$

	}

	/**
	 *
	 * @throws Exception
	 */
	public void testStyleProperty() throws Exception {
		sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle((ULocale) null);
		designHandle = sessionHandle.createDesign();
		design = (ReportDesign) designHandle.getModule();

		SharedStyleHandle style1 = designHandle.getElementFactory().newStyle("style-one"); //$NON-NLS-1$
		SharedStyleHandle style2 = designHandle.getElementFactory().newStyle("style-two"); //$NON-NLS-1$

		designHandle.getStyles().add(style1);
		designHandle.getStyles().add(style2);

		LabelHandle label = designHandle.getElementFactory().newLabel("label"); //$NON-NLS-1$
		LabelHandle parent = designHandle.getElementFactory().newLabel("parent"); //$NON-NLS-1$

		designHandle.getBody().add(label);
		designHandle.getComponents().add(parent);

		// first, the label has un-set style

		assertNull(label.getStyle());
		assertNull(((StyledElement) label.getElement()).getStyle(design));
		assertNull(((StyledElement) label.getElement()).getStyleName());

		// set style1 to label

		label.setStyle(style1);
		assertEquals(style1, label.getStyle());
		assertEquals(style1.getElement(), ((StyledElement) label.getElement()).getStyle(design));
		assertEquals(style1.getName(), ((StyledElement) label.getElement()).getStyleName());

		// remove the style1 and set the "null" to label

		style1.drop();
		assertNull(designHandle.findStyle(style1.getName()));
		assertNull(label.getStyle());
		assertNull(((StyledElement) label.getElement()).getStyle(design));
		assertEquals(style1.getName(), ((StyledElement) label.getElement()).getStyleName());
		label.setStyle(null);
		assertNull(label.getStyle());
		assertNull(((StyledElement) label.getElement()).getStyle(design));
		assertNull(((StyledElement) label.getElement()).getStyleName());
	}

	/**
	 * Case 1:
	 * <li>color property defined on parent & defined on the child itself.
	 * getFactoryProperty() should return the local value.</li>
	 * <li>color property not defined on both of the parent & child. It defined on
	 * the selector. Returns the selector defined value.</li>
	 *
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */

	public void testGetFactoryProperty() throws DesignFileException, SemanticException, IOException {

		openDesign("StyledElementTest.xml"); //$NON-NLS-1$

		LabelHandle label1 = (LabelHandle) designHandle.findElement("insideLabel1"); //$NON-NLS-1$
		LabelHandle label2 = (LabelHandle) designHandle.findElement("insideLabel2"); //$NON-NLS-1$
		LabelHandle label3 = (LabelHandle) designHandle.findElement("insideLabel3"); //$NON-NLS-1$
		LabelHandle label4 = (LabelHandle) designHandle.findElement("insideLabel4"); //$NON-NLS-1$

		FactoryPropertyHandle factoryColorPropHandle1 = label1.getFactoryPropertyHandle(Style.COLOR_PROP);
		FactoryPropertyHandle factoryColorPropHandle2 = label2.getFactoryPropertyHandle(Style.COLOR_PROP);
		FactoryPropertyHandle factoryColorPropHandle3 = label3.getFactoryPropertyHandle(Style.COLOR_PROP);
		FactoryPropertyHandle factoryColorPropHandle4 = label4.getFactoryPropertyHandle(Style.COLOR_PROP);

		assertEquals("yellow", factoryColorPropHandle1.getValue()); //$NON-NLS-1$
		assertEquals("green", factoryColorPropHandle2.getValue()); //$NON-NLS-1$
		assertEquals("red", factoryColorPropHandle3.getValue()); //$NON-NLS-1$
		assertEquals("blue", factoryColorPropHandle4.getValue()); //$NON-NLS-1$

		assertEquals("yellow", label1.getProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("green", label2.getProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("red", label3.getProperty(Style.COLOR_PROP)); //$NON-NLS-1$

		designHandle.getStyles().drop(designHandle.findStyle("label")); //$NON-NLS-1$
		assertEquals("gray", label3.getProperty(Style.COLOR_PROP)); //$NON-NLS-1$
		assertEquals(null, label3.getFactoryPropertyHandle(Style.COLOR_PROP));

		designHandle.includeLibrary("Library_1.xml", "newLib"); //$NON-NLS-1$ //$NON-NLS-2$
		LibraryHandle lib = designHandle.getLibrary("newLib"); //$NON-NLS-1$
		assertNotNull(lib);

		ThemeHandle theme = (ThemeHandle) lib.getThemes().get(0);

		designHandle.setTheme(theme);

		StyleHandle style = (StyleHandle) theme.getStyles().get(0);

		assertEquals("style1", style.getName()); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory().newTableItem("myTable"); //$NON-NLS-1$
		designHandle.getBody().add(table);

		table.setStyle((SharedStyleHandle) style);

		assertEquals("red", table.getProperty("color")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("red", table.getFactoryPropertyHandle("color") //$NON-NLS-1$ //$NON-NLS-2$
				.getValue());

		// get background color from the parent

		TableHandle parentTable = (TableHandle) designHandle.getElementFactory().newTableItem("parent"); //$NON-NLS-1$
		designHandle.getComponents().add(parentTable);
		parentTable.setProperty(IStyleModel.BACKGROUND_COLOR_PROP, "red"); //$NON-NLS-1$
		TableHandle childTable = (TableHandle) designHandle.getElementFactory().newTableItem("child"); //$NON-NLS-1$
		designHandle.getBody().add(childTable);
		assertNull(childTable.getFactoryPropertyHandle(IStyleModel.BACKGROUND_COLOR_PROP));
		childTable.setExtends(parentTable);
		assertEquals("red", childTable.getFactoryPropertyHandle(IStyleModel.BACKGROUND_COLOR_PROP).getValue()); //$NON-NLS-1$

	}
}
