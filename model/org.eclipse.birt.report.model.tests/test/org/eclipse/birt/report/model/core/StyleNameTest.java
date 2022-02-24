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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

public class StyleNameTest extends BaseTestCase {

	private String fileName = "StyleNameTest.xml"; //$NON-NLS-1$

	public void testNameSpace() throws Exception {
		openDesign(fileName);

		// three files in the name space and all name is lower-case
		NameSpace styleNs = design.getNameHelper().getNameSpace(ReportDesign.STYLE_NAME_SPACE);
		assertEquals(3, styleNs.getCount());
		assertTrue(styleNs.contains("my-style-test")); //$NON-NLS-1$
		assertTrue(styleNs.contains("my-style-test-1")); //$NON-NLS-1$
		assertTrue(styleNs.contains("styletest")); //$NON-NLS-1$

		// test the contains and get method in Namspace about the
		// case-insensitive of style name not the upper-case string
		List<DesignElement> styles = design.getSlot(ReportDesign.STYLE_SLOT).getContents();
		DesignElement style = styles.get(0);
		assertEquals(style, styleNs.getElement(style.getName().toLowerCase()));
		assertTrue(styleNs.contains(style.getName()));
		assertTrue(styleNs.getElements().containsAll(styles));

		// test rename/makeuniquename
		ElementFactory factory = designHandle.getElementFactory();
		String styleName = styles.get(0).getName();
		assertEquals("My-style-test", styleName); //$NON-NLS-1$
		StyleHandle styleHandle = factory.newStyle(styleName);
		assertEquals("My-style-test1", styleHandle.getName()); //$NON-NLS-1$

		styleHandle = factory.newStyle("my-Style-tesT1"); //$NON-NLS-1$
		assertEquals("my-Style-tesT11", styleHandle.getName()); //$NON-NLS-1$

		styleHandle = factory.newStyle("My-Style-tesT1"); //$NON-NLS-1$
		assertEquals("My-Style-tesT12", styleHandle.getName()); //$NON-NLS-1$
	}

	/**
	 * Tests the validation about the style name. Validation is done when inserting
	 * a named style to design tree and when renaming a style.
	 *
	 * @throws Exception
	 */
	public void testNameValidator() throws Exception {
		openDesign(fileName);

		// name contains space
		String invalidName = "style name"; //$NON-NLS-1$
		DesignElementHandle styleHandle = designHandle.getElementFactory().newStyle(invalidName);

		// makeUniqueName just generates a unique name, not do the validation
		// for the name, however, validation is done when added to design tree
		try {
			designHandle.getStyles().add(styleHandle);
			fail();
		} catch (SemanticException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_INVALID_STYLE_NAME, e.getErrorCode());
		}

		// validation is done when calling setName too
		styleHandle = designHandle.getStyles().get(0);
		try {
			styleHandle.setName(invalidName);
			fail();
		} catch (SemanticException e) {
			assertEquals(NameException.DESIGN_EXCEPTION_INVALID_STYLE_NAME, e.getErrorCode());
		}
	}

	/**
	 * Parser will do some compatibilities for old design files to correct the
	 * invalid name and rename the style with different cases.
	 *
	 * @throws Exception
	 */
	public void testCompatibleWithName() throws Exception {
		openDesign("StyleNameTest_1.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("StyleNameTest_golden.xml")); //$NON-NLS-1$

		openLibrary("StyleNameTest_2.xml"); //$NON-NLS-1$
		saveLibrary();
		assertTrue(compareFile("StyleNameTest_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the renaming an old design file style name. At the same time, we will
	 * update the style reference in all styled elements.
	 *
	 * @throws Exception
	 */
	public void testCompatibleRename() throws Exception {
		// renaming style for the invalid style name and same name with
		// different cases
		openDesign("StyleNameTest_3.xml"); //$NON-NLS-1$

		// this style name has spaces, then we rename it and the referring label
		// changes the style property to ensure the right resolve
		DesignElementHandle styleHandle = designHandle.getStyles().get(0);
		assertEquals("My style", styleHandle.getName()); //$NON-NLS-1$
		DesignElementHandle labelHandle = designHandle.findElement("label_1"); //$NON-NLS-1$
		assertEquals(styleHandle, labelHandle.getStyle());

		// this style has the valid style name while another style has the same
		// name with different cases, another style will be renamed, but this
		// style and the label referring it will have no change.
		styleHandle = designHandle.getStyles().get(1);
		assertEquals("test-style", styleHandle.getName()); //$NON-NLS-1$
		labelHandle = designHandle.findElement("label_2"); //$NON-NLS-1$
		assertEquals(styleHandle, labelHandle.getStyle());

		// this style has the same name with the second style and we will rename
		// it and all the style reference
		styleHandle = designHandle.getStyles().get(2);
		assertEquals("Test-Style1", styleHandle.getName()); //$NON-NLS-1$
		labelHandle = designHandle.findElement("label_3"); //$NON-NLS-1$
		assertEquals(styleHandle, labelHandle.getStyle());

		// after check the memory logic, then check the design file output
		save();
		assertTrue(compareFile("StyleNameTest_golden_2.xml")); //$NON-NLS-1$

		// test the same cases in the library file
		openLibrary("StyleNameTest_4.xml"); //$NON-NLS-1$
		saveLibrary();
		assertTrue(compareFile("StyleNameTest_golden_3.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the renaming logic that the styles in design with duplicate name with
	 * the library theme style.
	 *
	 * @throws Exception
	 */
	public void testCompatibleRename_1() throws Exception {
		openDesign("StyleNameTest_5.xml"); //$NON-NLS-1$
		save();
		assertTrue(compareFile("StyleNameTest_golden_4.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the failed case: design element refers theme styles that will have to
	 * be renamed for its name is invalid or duplicate with different cases.
	 *
	 * @throws Exception
	 */
	public void testCompatibleRename_2() throws Exception {
		openDesign("StyleNameTest_6.xml"); //$NON-NLS-1$

		// this label refers a style in library theme, however the style is
		// renamed for the invalidation, so the style will not be resolved
		DesignElementHandle labelHandle = designHandle.findElement("label_1"); //$NON-NLS-1$
		assertNotNull(labelHandle.getStringProperty(StyledElement.STYLE_PROP));
		assertNotNull(labelHandle.getStyle());
		ThemeHandle themeHandle = designHandle.getTheme();
		assertNotNull(themeHandle);
		StyleHandle libStyle = themeHandle.findStyle("My style"); //$NON-NLS-1$
		assertEquals(libStyle, labelHandle.getStyle());

		// this label refers a style in library theme, however the style is
		// renamed for its duplicate name, so the style will be resolved to
		// another style different with the original result
		labelHandle = designHandle.findElement("label_3"); //$NON-NLS-1$
		assertNotNull(labelHandle.getStringProperty(StyledElement.STYLE_PROP));
		assertNotNull(labelHandle.getStyle());
	}
}
