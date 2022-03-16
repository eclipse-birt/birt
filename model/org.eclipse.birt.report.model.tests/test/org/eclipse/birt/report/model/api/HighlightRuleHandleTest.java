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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for HighlightRule.
 *
 */

public class HighlightRuleHandleTest extends BaseTestCase {

	private static final String inputFile = "HighlightRuleHandleTest.xml"; //$NON-NLS-1$

	/**
	 * Tested cases:
	 *
	 * <ul>
	 * <li>The getProperty() algorithm. If the structure member has no local value,
	 * uses values of the referred style.
	 * <li>The back reference must be right for undo/redo.
	 * <li>The back reference must be right if the style member is set to a new
	 * value.
	 * <li>Circular references must throw exceptions.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testStyle() throws Exception {
		openDesign(inputFile);
		StyleHandle style2 = designHandle.findStyle("My-Style2"); //$NON-NLS-1$
		StyleHandle style3 = designHandle.findStyle("My-Style3"); //$NON-NLS-1$
		StyleHandle style4 = designHandle.findStyle("My-Style4"); //$NON-NLS-1$
		Iterator highlightRules = style2.highlightRulesIterator();
		assert (highlightRules.hasNext());

		HighlightRuleHandle style2Highlight = (HighlightRuleHandle) highlightRules.next();
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_RIGHT, style2Highlight.getTextAlign());
		assertEquals(ColorPropertyType.RED, style2Highlight.getColor().getStringValue());
		assertNull(style2Highlight.getProperty(IStyleModel.HIGHLIGHT_RULES_PROP));

		// should have no NPE.

		assertNull(style2Highlight.getProperty(HighlightRule.VALUE2_MEMBER));

		StyleHandle style1 = designHandle.findStyle("My-Style1"); //$NON-NLS-1$
		List refs = ((ReferenceableElement) style1.getElement()).getClientList();
		assertEquals(1, refs.size());
		BackRef ref1 = (BackRef) refs.get(0);
		assertEquals("My-Style2", ref1.getElement().getName()); //$NON-NLS-1$
		assertEquals(HighlightRule.STYLE_MEMBER, ref1.getPropertyName()); // $NON-NLS-1$

		// if remove the structure, the back reference should be break.

		style2Highlight.drop();

		refs = ((ReferenceableElement) style1.getElement()).getClientList();
		assertEquals(0, refs.size());

		designHandle.getCommandStack().undo();
		refs = ((ReferenceableElement) style1.getElement()).getClientList();
		assertEquals(1, refs.size());

		designHandle.getCommandStack().redo();
		refs = ((ReferenceableElement) style1.getElement()).getClientList();
		assertEquals(0, refs.size());

		designHandle.getCommandStack().undo();

		// set to the new style.

		// the old reference is dropped, make the highlight rule in style2
		// refers to the style 3

		style2Highlight.setStyle(style3);

		assertEquals(style3, style2Highlight.getStyle());

		refs = ((ReferenceableElement) style1.getElement()).getClientList();
		assertEquals(0, refs.size());

		// the new reference is added.

		refs = ((ReferenceableElement) style3.getElement()).getClientList();
		assertEquals(1, refs.size());

		assertEquals("My-Style2", ref1.getElement().getName()); //$NON-NLS-1$
		assertEquals(HighlightRule.STYLE_MEMBER, ref1.getPropertyName());

		// clear the style reference now.

		style2Highlight.setStyle(null);
		assertNull(style2Highlight.getStyle());
		assertNull(style2Highlight.getProperty(HighlightRule.STYLE_MEMBER));

		// exception test cases.

		try {
			style2Highlight.setStyle(style2);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE, e.getErrorCode());
		}

		// make the highlight rule in style2 refers to the style 3
		style2Highlight.setStyle(style3);

		Iterator style3HighlightRules = style3.highlightRulesIterator();
		assertTrue(style3HighlightRules.hasNext());

		HighlightRuleHandle style3Highlight = (HighlightRuleHandle) style3HighlightRules.next();

		// highlight rule in style 4 cannot refers to style 2 again.
		Iterator style4HighlightRules = style4.highlightRulesIterator();
		assertTrue(style4HighlightRules.hasNext());
		HighlightRuleHandle style4Highlight = (HighlightRuleHandle) style4HighlightRules.next();
		DimensionValue dv = (DimensionValue) style4Highlight.getLineHeight().getValue();
		assertEquals(10.0, dv.getMeasure());
		assertEquals("in", dv.getUnits());
		try {
			style3Highlight.setStyleName("My-Style2"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE, e.getErrorCode());
		}

		// test cases on add item

		HighlightRule newRule1 = StructureFactory.createHighlightRule();
		newRule1.setProperty(HighlightRule.STYLE_MEMBER, "My-Style2"); //$NON-NLS-1$

		try {
			style3.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP).addItem(newRule1);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE, e.getErrorCode());
		}
	}

	/**
	 * Tested cases:
	 *
	 * <ul>
	 * <li>The HighlightRule structure can refers to a style element. getProperty()
	 * can work properly.
	 * <li>if the HighlightRule structure refers to a style that is not on the
	 * design tree, the style value cannot be gotten after the highlightRule is
	 * added to a table.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testStyleOnHighlightRuleStruct() throws Exception {
		openDesign(inputFile);

		StyleHandle style2 = designHandle.findStyle("My-Style2"); //$NON-NLS-1$
		TableHandle table = (TableHandle) designHandle.findElement("myTable"); //$NON-NLS-1$

		HighlightRule rule = StructureFactory.createHighlightRule();
		rule.setStyle(style2);
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_BOLDER, rule.getProperty(design, IStyleModel.FONT_WEIGHT_PROP));

		PropertyHandle propHandle = table.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);
		propHandle.addItem(rule);
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_BOLDER, rule.getProperty(design, IStyleModel.FONT_WEIGHT_PROP));

		// drop highlight rule from the table
		designHandle.getCommandStack().undo();

		propHandle = style2.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);

		try {
			propHandle.addItem(rule);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE, e.getErrorCode());
		}

		StyleHandle newStyle = designHandle.getElementFactory().newStyle("newStyle"); //$NON-NLS-1$
		newStyle.getColor().setValue(ColorPropertyType.RED);

		HighlightRule rule1 = StructureFactory.createHighlightRule();
		rule1.setStyle(newStyle);
		assertEquals(ColorPropertyType.RED, rule1.getProperty(design, IStyleModel.COLOR_PROP));
		assertNotNull(rule1.getStyle());

		propHandle = table.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);
		propHandle.addItem(rule1);

		assertNull(rule1.getProperty(design, IStyleModel.COLOR_PROP));
		assertNull(rule1.getStyle());
	}

	/**
	 * Tests set list value when map operator is 'in'.
	 *
	 * @throws Exception
	 */

	public void testOperatorIn() throws Exception {
		createDesign();
		StyleHandle style = designHandle.getElementFactory().newStyle("style"); //$NON-NLS-1$
		designHandle.getStyles().add(style);

		PropertyHandle propHandle = style.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);

		HighlightRule rule = StructureFactory.createHighlightRule();
		propHandle.addItem(rule);
		HighlightRuleHandle ruleHandle = (HighlightRuleHandle) propHandle.get(0);

		ruleHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_IN);
		List values = new ArrayList();
		values.add("a");//$NON-NLS-1$
		values.add("b");//$NON-NLS-1$
		ruleHandle.setValue1(values);
		ruleHandle.setTestExpression("expr");//$NON-NLS-1$

		HighlightRule rule2 = StructureFactory.createHighlightRule();

		rule2.setProperty(StyleRule.VALUE1_MEMBER, "C");//$NON-NLS-1$

		List values2 = new ArrayList();
		values2.add("a");//$NON-NLS-1$
		values2.add("b");//$NON-NLS-1$
		rule2.setValue1(values);
		rule2.setTestExpression("expr2");//$NON-NLS-1$
		rule2.setOperator(DesignChoiceConstants.MAP_OPERATOR_IN);

		propHandle.addItem(rule2);
	}

	/**
	 * Tests the copyPropertyTo for highlightrule property with style reference set.
	 *
	 * @throws Exception
	 */
	public void testCopyTo() throws Exception {
		openDesign(inputFile);

		StyleHandle style2 = designHandle.findStyle("My-Style2"); //$NON-NLS-1$

		StyleHandle newStyle = designHandle.getElementFactory().newStyle(null);

		designHandle.getStyles().add(newStyle);

		style2.copyPropertyTo(StyleHandle.HIGHLIGHT_RULES_PROP, newStyle);

		assertNotNull(newStyle.getProperty(StyleHandle.HIGHLIGHT_RULES_PROP));

	}
}
