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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.Point;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for MasterPageHandle.
 * 
 */
public class MasterPageHandleTest extends BaseTestCase {

	/**
	 * Pages with the default size.
	 */

	private String fileName = "MasterPageHandleTest.xml"; //$NON-NLS-1$

	/**
	 * Pages with the custom size.
	 */

	private String custom_page_fileName = "MasterPageHandleTest1.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test getSize().
	 * <p>
	 * 1. type is not provided. Default value of "USLetter" should be returned, in
	 * session unit.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testDefaultPage() throws Exception {
		openDesign(fileName);

		MasterPageHandle pageHandle = designHandle.findMasterPage("My Page"); //$NON-NLS-1$
		Point size = pageHandle.getSize();

		assertEquals(8.5, size.x, 1.0);
		assertEquals(11.0, size.y, 1.0);

		assertEquals(DesignChoiceConstants.PAGE_SIZE_US_LETTER, pageHandle.getPageType());

		assertEquals(DesignChoiceConstants.PAGE_ORIENTATION_AUTO, pageHandle.getOrientation());

		// should be null, related to getEffectiveHeight() and
		// getEffectiveWidth()

		assertEquals(IMasterPageModel.US_LETTER_HEIGHT, pageHandle.getHeight().getStringValue());
		assertEquals(IMasterPageModel.US_LETTER_WIDTH, pageHandle.getWidth().getStringValue());

		assertEquals("0.75mm", pageHandle.getBottomMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.9mm", pageHandle.getRightMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.8mm", pageHandle.getLeftMargin().getStringValue()); //$NON-NLS-1$
		assertEquals("0.95mm", pageHandle.getTopMargin().getStringValue()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.PAGE_SIZE_US_LETTER, pageHandle.getPageType());
		DimensionValue value = pageHandle.getPageHeight();
		assertEquals(DesignChoiceConstants.UNITS_IN, value.getUnits());
		assertEquals(11, value.getMeasure(), 0.0);
		value = pageHandle.getPageWidth();
		assertEquals(DesignChoiceConstants.UNITS_IN, value.getUnits());
		assertEquals(8.5, value.getMeasure(), 0.0);

		// page type: us-legal
		pageHandle.setPageType(DesignChoiceConstants.PAGE_SIZE_US_LEGAL);
		value = pageHandle.getPageHeight();
		assertEquals(DesignChoiceConstants.UNITS_IN, value.getUnits());
		assertEquals(14, value.getMeasure(), 0.0);
		value = pageHandle.getPageWidth();
		assertEquals(DesignChoiceConstants.UNITS_IN, value.getUnits());
		assertEquals(8.5, value.getMeasure(), 0.0);

		// page type: A4
		pageHandle.setPageType(DesignChoiceConstants.PAGE_SIZE_A4);
		value = pageHandle.getPageHeight();
		assertEquals(DesignChoiceConstants.UNITS_MM, value.getUnits());
		assertEquals(297, value.getMeasure(), 0.0);
		value = pageHandle.getPageWidth();
		assertEquals(DesignChoiceConstants.UNITS_MM, value.getUnits());
		assertEquals(210, value.getMeasure(), 0.0);

	}

	/**
	 * Properties on custom master pages.
	 * <p>
	 * 1. type is "Custom", width="8.3cm", height="13.3cm" , application unit is in.
	 * Return value should be the converted value in inch unit.
	 * 
	 * @throws Exception
	 */

	public void testCustomPage() throws Exception {

		openDesign(custom_page_fileName);

		MasterPageHandle pageHandle = designHandle.findMasterPage("My Page1"); //$NON-NLS-1$
		Point size = pageHandle.getSize();

		double CM_PER_INCH = 2.54;

		assertEquals(8.3 / CM_PER_INCH, size.x, 0.1);
		assertEquals(13.3 / CM_PER_INCH, size.y, 0.1);
		// checks with getHeight() and getWidth()

		pageHandle = designHandle.findMasterPage("My Page1"); //$NON-NLS-1$

		assertEquals("13.3cm", pageHandle.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("8.3cm", pageHandle.getWidth().getStringValue()); //$NON-NLS-1$

		GraphicMasterPageHandle graphicHandle = (GraphicMasterPageHandle) pageHandle;
		assertEquals(2, graphicHandle.getColumnCount());
		assertEquals("0.25mm", graphicHandle.getColumnSpacing().getStringValue()); //$NON-NLS-1$

		assertEquals("13.3cm", pageHandle.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("8.3cm", pageHandle.getWidth().getStringValue()); //$NON-NLS-1$
		DimensionValue value = pageHandle.getPageHeight();
		assertEquals(DesignChoiceConstants.UNITS_CM, value.getUnits());
		assertEquals(13.3, value.getMeasure(), 0.0);
		value = pageHandle.getPageWidth();
		assertEquals(DesignChoiceConstants.UNITS_CM, value.getUnits());
		assertEquals(8.3, value.getMeasure(), 0.0);
	}

	/**
	 * Test the overridden setProperty().
	 * 
	 * @throws Exception
	 * 
	 */
	public void testSetProperty() throws Exception {
		openDesign(fileName);

		MasterPageHandle page = designHandle.findMasterPage("My Page"); //$NON-NLS-1$

		// switch the page type from "custom" to "A4/US Letter/etc." previous
		// height/with property
		// will be cleared.

		page.setPageType(DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		page.setStringProperty(MasterPage.HEIGHT_PROP, "12in"); //$NON-NLS-1$
		page.setStringProperty(MasterPage.WIDTH_PROP, "10in"); //$NON-NLS-1$

		assertEquals("12in", page.getStringProperty(MasterPage.HEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("10in", page.getStringProperty(MasterPage.WIDTH_PROP)); //$NON-NLS-1$

		// Set type to A4, height/width should have been cleaned.

		page.setPageType(DesignChoiceConstants.PAGE_SIZE_A4);
		assertEquals(DesignChoiceConstants.PAGE_SIZE_A4, page.getStringProperty(MasterPage.TYPE_PROP));
		assertEquals(MasterPage.A4_HEIGHT, page.getStringProperty(MasterPage.HEIGHT_PROP));
		assertEquals(MasterPage.A4_WIDTH, page.getStringProperty(MasterPage.WIDTH_PROP));
		assertEquals(IMasterPageModel.A4_HEIGHT, ((DimensionValue) (page.getHeight().getValue())).toString());

		// undo it, type and height/width should recover.

		this.design.getActivityStack().undo();
		assertEquals(DesignChoiceConstants.PAGE_SIZE_CUSTOM, page.getStringProperty(MasterPage.TYPE_PROP));
		assertEquals("12in", page.getStringProperty(MasterPage.HEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("10in", page.getStringProperty(MasterPage.WIDTH_PROP)); //$NON-NLS-1$

		// Set type to US Letter, height/width should also have been cleaned.
		page.setPageType(DesignChoiceConstants.PAGE_SIZE_US_LEGAL);
		assertEquals(DesignChoiceConstants.PAGE_SIZE_US_LEGAL, page.getStringProperty(MasterPage.TYPE_PROP));
		assertEquals(MasterPage.US_LEGAL_HEIGHT, page.getStringProperty(MasterPage.HEIGHT_PROP));
		assertEquals(MasterPage.US_LEGAL_WIDTH, page.getStringProperty(MasterPage.WIDTH_PROP));

	}

	/**
	 * Tests masterpage will be rotated if orientation type is landscape and page
	 * type is not custom, and will NOT be rotated if orientation type is landscape
	 * and page type is custom.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void testLandscape() throws DesignFileException, SemanticException {
		openDesign(fileName);

		MasterPageHandle page = designHandle.findMasterPage("My Page"); //$NON-NLS-1$

		// Tests masterpage will NOT be rotated if orientation type is landscape
		// and page type is custom.
		page.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE);
		page.setPageType(DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		page.setStringProperty(MasterPage.HEIGHT_PROP, "12in"); //$NON-NLS-1$
		page.setStringProperty(MasterPage.WIDTH_PROP, "10in"); //$NON-NLS-1$

		assertEquals("12in", page.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("10in", page.getWidth().getStringValue()); //$NON-NLS-1$

		// Masterpage will be rotated if orientation type is landscape and page
		// type
		// is A4.
		page.setPageType(DesignChoiceConstants.PAGE_SIZE_A4);
		page.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE);
		assertEquals(IMasterPageModel.A4_WIDTH, page.getHeight().getStringValue());
		assertEquals(IMasterPageModel.A4_HEIGHT, page.getWidth().getStringValue());

		// Masterpage will NOT be rotated if orientation type is NOT landscape
		// and page type
		// is A4.
		page.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_PORTRAIT);
		assertEquals(IMasterPageModel.A4_HEIGHT, page.getHeight().getStringValue());
		assertEquals(IMasterPageModel.A4_WIDTH, page.getWidth().getStringValue());

		// Masterpage will be rotated if orientation type is landscape and page
		// type
		// is US_LEGEL.
		page.setPageType(DesignChoiceConstants.PAGE_SIZE_US_LEGAL);
		page.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE);
		assertEquals(IMasterPageModel.US_LEGAL_WIDTH, page.getHeight().getStringValue());
		assertEquals(IMasterPageModel.US_LEGAL_HEIGHT, page.getWidth().getStringValue());

		// Masterpage will NOT be rotated if orientation type is NOT landscape
		// and page type
		// is US_LEGEL.
		page.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_PORTRAIT);
		assertEquals(IMasterPageModel.US_LEGAL_HEIGHT, page.getHeight().getStringValue());
		assertEquals(IMasterPageModel.US_LEGAL_WIDTH, page.getWidth().getStringValue());

		// Masterpage will be rotated if orientation type is landscape and page
		// type
		// is US_LETTER.
		page.setPageType(DesignChoiceConstants.PAGE_SIZE_US_LETTER);
		page.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_LANDSCAPE);
		assertEquals(IMasterPageModel.US_LETTER_WIDTH, page.getHeight().getStringValue());
		assertEquals(IMasterPageModel.US_LETTER_HEIGHT, page.getWidth().getStringValue());

		// Masterpage will NOT be rotated if orientation type is NOT landscape
		// and page type
		// is US_LETTER.
		page.setOrientation(DesignChoiceConstants.PAGE_ORIENTATION_PORTRAIT);
		assertEquals(IMasterPageModel.US_LETTER_HEIGHT, page.getHeight().getStringValue());
		assertEquals(IMasterPageModel.US_LETTER_WIDTH, page.getWidth().getStringValue());

	}

	/**
	 * Tests that height and width are not allowed to be set if masterpage size type
	 * is a pre-defined type.
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 * 
	 */
	public void testPredefinedType() throws DesignFileException, SemanticException {
		openDesign(fileName);

		MasterPageHandle page = designHandle.findMasterPage("My Page"); //$NON-NLS-1$

		page.setPageType(DesignChoiceConstants.PAGE_SIZE_US_LETTER);
		assertSizeUnsettable(page, MasterPage.HEIGHT_PROP);
		assertSizeUnsettable(page, MasterPage.WIDTH_PROP);

		page.setPageType(DesignChoiceConstants.PAGE_SIZE_A4);
		assertSizeUnsettable(page, MasterPage.HEIGHT_PROP);
		assertSizeUnsettable(page, MasterPage.WIDTH_PROP);

		page.setPageType(DesignChoiceConstants.PAGE_SIZE_US_LEGAL);
		assertSizeUnsettable(page, MasterPage.HEIGHT_PROP);
		assertSizeUnsettable(page, MasterPage.WIDTH_PROP);

	}

	private void assertSizeUnsettable(MasterPageHandle page, String property) throws SemanticException {
		try {
			page.setProperty(property, "10in"); //$NON-NLS-1$
			fail();
		} catch (SemanticException expected) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE, expected.getErrorCode());
		}
	}
}
