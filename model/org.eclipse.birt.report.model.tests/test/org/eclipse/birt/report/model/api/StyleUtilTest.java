/*******************************************************************************
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009, 2010 Actuate Corporation.
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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.util.StyleUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

public class StyleUtilTest extends BaseTestCase {

	public void testCopyStyle() throws Exception {
		createDesign();
		TextItemHandle text1 = designHandle.getElementFactory().newTextItem("text1"); //$NON-NLS-1$
		TextItemHandle text2 = designHandle.getElementFactory().newTextItem("text2"); //$NON-NLS-1$
		designHandle.getBody().add(text1);
		designHandle.getBody().add(text2);
		HighlightRule highlight = StructureFactory.createHighlightRule();
		highlight.setTestExpression("test"); //$NON-NLS-1$
		text1.setProperty(IStyleModel.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		text1.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP).addItem(highlight);
		StyleUtil.copyLocalStyles(text1, text2);

		List highlights = text2.getListProperty(IStyleModel.HIGHLIGHT_RULES_PROP);
		assertEquals(1, highlights.size());
		assertEquals(highlight, highlights.get(0));
		assertNotSame(highlight, highlights.get(0));

		save();
		assertTrue(compareFile("StyleUtilTest_golden.xml")); //$NON-NLS-1$

		text1.clearProperty(IStyleModel.HIGHLIGHT_RULES_PROP);
		highlights = text1.getListProperty(IStyleModel.HIGHLIGHT_RULES_PROP);
		assertNull(highlights);
		highlights = text2.getListProperty(IStyleModel.HIGHLIGHT_RULES_PROP);
		assertEquals(1, highlights.size());

		save();
		assertTrue(compareFile("StyleUtilTest_1_golden.xml")); //$NON-NLS-1$
	}
}
