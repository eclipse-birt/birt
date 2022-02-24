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

package org.eclipse.birt.report.designer.ui;

import junit.framework.TestCase;

/*
 * Class of test for ReportPlatformUIImages
 */
public class ReportPlatformUIImagesTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetImage() {
		String iconName[] = {
				// common icons
				IReportGraphicConstants.ICON_NEW_REPORT, IReportGraphicConstants.ICON_REPORT_FILE,
				IReportGraphicConstants.ICON_QUIK_EDIT, IReportGraphicConstants.ICON_REPORT_PERSPECTIVE,
				IReportGraphicConstants.ICON_REPORT_PROJECT,

				// element icons
				IReportGraphicConstants.ICON_ELEMENT_CELL, IReportGraphicConstants.ICON_ELEMENT_DATA,
				IReportGraphicConstants.ICON_ELEMENT_DATA_SET, IReportGraphicConstants.ICON_ELEMENT_DATA_SOURCE,
				IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SET, IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SOURCE,
				// IReportGraphicConstants.ICON_ELEMENT_EXTENDED_ITEM,
				IReportGraphicConstants.ICON_ELEMENT_GRID, IReportGraphicConstants.ICON_ELEMENT_GROUP,
				IReportGraphicConstants.ICON_ELEMENT_IMAGE, IReportGraphicConstants.ICON_ELEMENT_LABEL,
				IReportGraphicConstants.ICON_ELEMENT_LINE, IReportGraphicConstants.ICON_ELEMENT_LIST,
				IReportGraphicConstants.ICON_ELEMENT_LIST_GROUP, IReportGraphicConstants.ICON_ELEMNET_MASTERPAGE,
				IReportGraphicConstants.ICON_ELEMENT_PARAMETER, IReportGraphicConstants.ICON_ELEMENT_PARAMETER_GROUP,
				IReportGraphicConstants.ICON_ELEMENT_ROW, IReportGraphicConstants.ICON_ELEMENT_SCALAR_PARAMETER,
				IReportGraphicConstants.ICON_ELEMNET_SIMPLE_MASTERPAGE, IReportGraphicConstants.ICON_ELEMENT_STYLE,
				IReportGraphicConstants.ICON_ELEMENT_TABLE, IReportGraphicConstants.ICON_ELEMENT_TABLE_GROUP,
				IReportGraphicConstants.ICON_ELEMENT_TEXT, IReportGraphicConstants.ICON_ELEMENT_LIBRARY,
				IReportGraphicConstants.ICON_ELEMENT_LIBRARY_REFERENCED, IReportGraphicConstants.ICON_ELEMENT_THEME,

				// outline view icons
				IReportGraphicConstants.ICON_NODE_BODY, IReportGraphicConstants.ICON_NODE_MASTERPAGES,
				IReportGraphicConstants.ICON_NODE_STYLES, IReportGraphicConstants.ICON_NODE_HEADER,
				IReportGraphicConstants.ICON_NODE_DETAILS, IReportGraphicConstants.ICON_NODE_FOOTER,
				IReportGraphicConstants.ICON_NODE_GROUPS, IReportGraphicConstants.ICON_NODE_THEMES,

				// add image constants for border
				IReportGraphicConstants.ICON_BORDER_ALL, IReportGraphicConstants.ICON_BORDER_BOTTOM,
				IReportGraphicConstants.ICON_BORDER_LEFT, IReportGraphicConstants.ICON_BORDER_NOBORDER,
				IReportGraphicConstants.ICON_BORDER_RIGHT, IReportGraphicConstants.ICON_BORDER_TOP,

				// add image constants for data explore
				IReportGraphicConstants.ICON_DATA_EXPLORER_VIEW, IReportGraphicConstants.ICON_NODE_DATA_SETS,
				IReportGraphicConstants.ICON_NODE_DATA_SOURCES, IReportGraphicConstants.ICON_NODE_PARAMETERS,
				IReportGraphicConstants.ICON_DATA_COLUMN,

				// auto text icon
				IReportGraphicConstants.ICON_AUTOTEXT,
				// IReportGraphicConstants.ICON_AUTOTEXT_PAGE,
				// IReportGraphicConstants.ICON_AUTOTEXT_DATE,
				// IReportGraphicConstants.ICON_AUTOTEXT_CREATEDON,
				// IReportGraphicConstants.ICON_AUTOTEXT_CREATEDBY,
				// IReportGraphicConstants.ICON_AUTOTEXT_FILENAME,
				// IReportGraphicConstants.ICON_AUTOTEXT_LAST_PRINTED,
				// IReportGraphicConstants.ICON_AUTOTEXT_PAGEXOFY,
				// IReportGraphicConstants.ICON_AUTOTEXT_AUTHOR_PAGE_DATE,
				// IReportGraphicConstants.ICON_AUTOTEXT_CONFIDENTIAL_PAGE,

				// expression builder icons
				IReportGraphicConstants.ICON_EXPRESSION_DATA_TABLE, IReportGraphicConstants.ICON_EXPRESSION_OPERATOR,
				IReportGraphicConstants.ICON_EXPRESSION_GLOBAL, IReportGraphicConstants.ICON_EXPRESSION_METHOD,
				IReportGraphicConstants.ICON_EXPRESSION_STATIC_METHOD, IReportGraphicConstants.ICON_EXPRESSION_MEMBER,
				IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER, IReportGraphicConstants.ICON_EXPRESSION_BUILDER,

				// data wizards
				IReportGraphicConstants.ICON_WIZARD_DATASOURCE, IReportGraphicConstants.ICON_WIZARD_DATASET,
				IReportGraphicConstants.ICON_WIZARDPAGE_DATASETSELECTION,

				// attribute icon constants
				IReportGraphicConstants.ICON_ATTRIBUTE_FONT_WIDTH, IReportGraphicConstants.ICON_ATTRIBUTE_FONT_STYLE,
				IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_UNDERLINE,
				IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_LINE_THROUGH,
				IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_NONE, IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_FRAME,
				IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_LEFT, IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_RIGHT,
				IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_TOP, IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_BOTTOM,
				IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_CENTER,
				IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_JUSTIFY,
				IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_LEFT,
				IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_RIGHT,
				IReportGraphicConstants.ICON_ATTRIBUTE_TOP_MARGIN, IReportGraphicConstants.ICON_ATTRIBUTE_BOTTOM_MARGIN,
				IReportGraphicConstants.ICON_ATTRIBUTE_LEFT_MARGIN, IReportGraphicConstants.ICON_ATTRIBUTE_RIGHT_MARGIN,

				IReportGraphicConstants.ICON_MISSING_IMG, IReportGraphicConstants.ICON_PREVIEW_PARAMETERS,
				IReportGraphicConstants.ICON_PREVIEW_REFRESH,

				IReportGraphicConstants.ICON_DEFAULT,

				IReportGraphicConstants.ICON_OPEN_FILE, };
		for (int i = 0; i < iconName.length; i++) {
			assertNotNull(iconName[i], ReportPlatformUIImages.getImage(iconName[i]));
		}

		assertNull(ReportPlatformUIImages.getImage("Invalid~~Image"));
	}

	public void testGetImageDescriptor() {
		assertNotNull(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_ELEMENT_TEXT));
	}

	public void testGetImageRegistry() {
		assertNotNull(ReportPlatformUIImages.getImageRegistry());
	}
}
