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

import java.util.List;

import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test CssSharedStyleHandle class
 * <tr>
 * <td>Test css style can't set any properies
 * <td>Test css style can't add/remove any item
 * <td>Test css style can't change property of reference property/structure
 * handle
 * </tr>
 */

public class CssSharedStyleHandleTest extends BaseTestCase {

	/**
	 * Test CssSharedStyleHandle is readonly.
	 *
	 * @throws Exception
	 */

	public void testReadOnlyCssStyleHandle() throws Exception {
		openDesign("BlankReportDesign.xml"); //$NON-NLS-1$

		CssStyleSheetHandle sheetHandle = designHandle.openCssStyleSheet(getResource("input/base.css").getFile());//$NON-NLS-1$
		designHandle.addCss(sheetHandle);

		List styles = designHandle.getAllStyles();
		SharedStyleHandle styleHandle = (SharedStyleHandle) styles.get(0);

		// can't modify any properties of CssSharedStyleHandle
		try {
			styleHandle.setFontStyle(DesignChoiceConstants.FONT_STYLE_ITALIC);
			fail();
		} catch (IllegalOperationException e) {
			assertEquals(CssException.DESIGN_EXCEPTION_READONLY, e.getMessage());
		}

		HighlightRule highlightRule = StructureFactory.createHighlightRule();

		PropertyHandle propHandle = styleHandle.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);

		try {
			propHandle.addItem(highlightRule);
			fail();
		} catch (IllegalOperationException e) {
			assertEquals(CssException.DESIGN_EXCEPTION_READONLY, e.getMessage());
		}

		DimensionHandle fontSize = styleHandle.getFontSize();

		try {
			fontSize.setStringValue("10.00pt"); //$NON-NLS-1$
			fail();
		} catch (IllegalOperationException e) {
			assertEquals(CssException.DESIGN_EXCEPTION_READONLY, e.getMessage());
		}
	}

}
