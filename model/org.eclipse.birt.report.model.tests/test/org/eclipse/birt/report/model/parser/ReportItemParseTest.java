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

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test ReportItemHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * 
 * <tr>
 * <td>testParseProperties()</td>
 * <td>Gets visibility rules, parameter bindings in elements and tests whether
 * values match with those defined the design file.</td>
 * <td>Returned values match with the design file. If "format" values are not
 * defined, the default value "all" is used.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The number of visibility rules in elements.</td>
 * <td>The number is 2.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The number of parameter bindings in the label.</td>
 * <td>The number is 0.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The number of parameter bindings in the data.</td>
 * <td>The number is 2.</td>
 * </tr>
 * 
 * <tr>
 * <td>testWriteProperties</td>
 * <td>The default format value in the visibility rule.</td>
 * <td>The default value can be written out to the design file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets "format" and "valueExpr" properties of a visibility rule.</td>
 * <td>"format" and "valueExpr" can be written out and the output file matches
 * with the golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets "name" and "express" properties of a parameter binding.</td>
 * <td>"name" and "express" can be written out and the output file matches with
 * the golden file.</td>
 * </tr>
 * 
 */

public class ReportItemParseTest extends BaseTestCase {

	DesignElement element;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();

		openDesign("ReportItemParseTest.xml"); //$NON-NLS-1$
	}

	/**
	 * Test to read hide rules.
	 * 
	 * @throws Exception if open the design file with errors.
	 */

	public void testParseProperties() throws Exception {
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("bodyLabel"); //$NON-NLS-1$

		assertEquals("birt.js.labelHandler", labelHandle //$NON-NLS-1$
				.getEventHandlerClass());

		// check bookmark display name property
		assertEquals("Bookmark Display Name", labelHandle.getBookmarkDisplayName()); //$NON-NLS-1$

		// check zIndex properties.

		assertEquals(2, labelHandle.getZIndex());

		// check cube property
		assertEquals(designHandle.findCube("testCube"), labelHandle.getCube()); //$NON-NLS-1$

		// checks on-prepare, on-create and on-render values

		assertEquals("hello, show me on create.", labelHandle.getOnCreate()); //$NON-NLS-1$

		assertEquals("hello, show me on render.", labelHandle.getOnRender()); //$NON-NLS-1$

		assertEquals("hello, show me on prepare.", labelHandle.getOnPrepare()); //$NON-NLS-1$

		assertEquals("hello, show me on page break.", labelHandle.getOnPageBreak()); //$NON-NLS-1$

		Iterator rules = labelHandle.visibilityRulesIterator();

		// checks with the first visibility rule.

		StructureHandle structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		MemberHandle memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_PDF, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		assertEquals("word, 10 people", memberHandle.getStringValue()); //$NON-NLS-1$

		// the second visibility rule

		structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_ALL, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		assertEquals("excel, 10 people", memberHandle.getStringValue()); //$NON-NLS-1$

		// no third, must be null.

		structHandle = (StructureHandle) rules.next();
		assertNull(structHandle);

		// parameter binding on this label, no bindings in the list.

		Iterator bindings = labelHandle.paramBindingsIterator();
		structHandle = (StructureHandle) bindings.next();
		assertNull(structHandle);

		// tests visibility on the data item.

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("bodyData"); //$NON-NLS-1$

		assertEquals("birt.js.dataHandler", dataHandle //$NON-NLS-1$
				.getEventHandlerClass());

		// checks on-prepare, on-create and on-render values

		assertEquals("hello, show data on prepare.", dataHandle.getOnPrepare()); //$NON-NLS-1$
		assertEquals("hello, show data on render.", dataHandle.getOnRender()); //$NON-NLS-1$

		assertEquals(null, dataHandle.getOnCreate());

		assertEquals("acl expression test", dataHandle.getACLExpression()); //$NON-NLS-1$
		assertTrue(((Boolean) dataHandle.getElement().getLocalProperty(design, IReportItemModel.CASCADE_ACL_PROP))
				.booleanValue());
		assertFalse(dataHandle.cascadeACL());
		assertFalse(dataHandle.canCascadeACL());
		assertFalse(((Boolean) dataHandle.getProperty(IReportItemModel.CASCADE_ACL_PROP)).booleanValue());
		assertFalse(dataHandle.getBooleanProperty(IReportItemModel.CASCADE_ACL_PROP));

		rules = dataHandle.visibilityRulesIterator();
		structHandle = (StructureHandle) rules.next();

		// if no format attribute, use the default value.

		memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_ALL, memberHandle.getStringValue());

		// if no expression, should be empty string

		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		assertNull(memberHandle.getStringValue());

		// the second visibility rule for the data item

		structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_PDF, memberHandle.getStringValue());

		// if no expression, should be empty string

		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		assertNull(memberHandle.getStringValue());

		// reads bindings for data.

		bindings = dataHandle.paramBindingsIterator();
		structHandle = (StructureHandle) bindings.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(ParamBinding.PARAM_NAME_MEMBER);
		assertEquals("param1", memberHandle.getValue()); //$NON-NLS-1$
		memberHandle = structHandle.getMember(ParamBinding.EXPRESSION_MEMBER);
		assertEquals("value1", memberHandle.getStringValue()); //$NON-NLS-1$

		structHandle = (StructureHandle) bindings.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(ParamBinding.PARAM_NAME_MEMBER);
		assertEquals("param2", memberHandle.getValue()); //$NON-NLS-1$
		memberHandle = structHandle.getMember(ParamBinding.EXPRESSION_MEMBER);
		assertEquals("value2", memberHandle.getStringValue()); //$NON-NLS-1$

		structHandle = (StructureHandle) bindings.next();
		assertNull(structHandle);

		// tests toc on the free form item.

		assertEquals("2005 Statistics", dataHandle.getTocExpression()); //$NON-NLS-1$

		FreeFormHandle form = (FreeFormHandle) designHandle.findElement("free form"); //$NON-NLS-1$
		assertEquals("\"This Section\"", form.getTocExpression()); //$NON-NLS-1$

		TOCHandle tocHandle = dataHandle.getTOC();

		assertEquals("2005 Statistics", tocHandle.getExpression()); //$NON-NLS-1$
	}

	/**
	 * Test to write hide rules to the design file.
	 * 
	 * @throws Exception if open/write the design file with IO errors.
	 */

	public void testWriteProperties() throws Exception {
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("bodyLabel"); //$NON-NLS-1$

		// test cube
		labelHandle.setCube(designHandle.findCube("testCube_one")); //$NON-NLS-1$

		labelHandle.setBookmarkDisplayName("new display name"); //$NON-NLS-1$

		labelHandle.setZIndex(1);
		labelHandle.setOnCreate("my new label on create"); //$NON-NLS-1$
		labelHandle.setOnRender(null);
		labelHandle.setOnPrepare("my new label on prepare"); //$NON-NLS-1$
		labelHandle.setOnPageBreak("my new label on page break"); //$NON-NLS-1$

		Iterator rules = labelHandle.visibilityRulesIterator();

		// sets with the first visibility rule.

		StructureHandle structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);
		MemberHandle memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);

		memberHandle.setValue(DesignChoiceConstants.FORMAT_TYPE_REPORTLET);

		// visibility rule now support user defined format, no exception

		memberHandle.setValue("userDefinedformat"); //$NON-NLS-1$

		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		memberHandle.setValue("10*20"); //$NON-NLS-1$

		labelHandle.setTocExpression("new 2005 statistics"); //$NON-NLS-1$

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("bodyData"); //$NON-NLS-1$

		dataHandle.setOnCreate("my new data on create"); //$NON-NLS-1$
		dataHandle.setOnRender("my new data on render"); //$NON-NLS-1$
		dataHandle.setOnPrepare(null);
		dataHandle.setEventHandlerClass("my new data handler class"); //$NON-NLS-1$

		dataHandle.setACLExpression("new acl expression test"); //$NON-NLS-1$
		dataHandle.setCascadeACL(true);

		rules = dataHandle.visibilityRulesIterator();

		// get the second visibility rule handle.

		structHandle = (StructureHandle) rules.next();
		structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		memberHandle.setValue(DesignChoiceConstants.FORMAT_TYPE_REPORTLET);

		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		memberHandle.setValue("bodyData 2nd rule."); //$NON-NLS-1$

		// no expression originally, now add one expression.

		structHandle = (StructureHandle) rules.next();
		assertNull(structHandle);

		// writes bindings for data.

		Iterator bindings = dataHandle.paramBindingsIterator();
		structHandle = (StructureHandle) bindings.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(ParamBinding.PARAM_NAME_MEMBER);
		memberHandle.setValue("no paramter 1"); //$NON-NLS-1$
		memberHandle = structHandle.getMember(ParamBinding.EXPRESSION_MEMBER);
		memberHandle.setValue("setting value 1"); //$NON-NLS-1$

		structHandle = (StructureHandle) bindings.next();
		assertNotNull(structHandle);
		memberHandle = structHandle.getMember(ParamBinding.PARAM_NAME_MEMBER);
		memberHandle.setValue("no paramter 2"); //$NON-NLS-1$
		memberHandle = structHandle.getMember(ParamBinding.EXPRESSION_MEMBER);
		memberHandle.setValue("setting value 2"); //$NON-NLS-1$

		// clear toc on Data

		dataHandle.setTocExpression(null);

		save();
		assertTrue(compareFile("ReportItemParseTest_golden.xml")); //$NON-NLS-1$

		dataHandle.addTOC("toc1");//$NON-NLS-1$
		TOCHandle tocHandle = dataHandle.getTOC();
		assertNotNull(tocHandle);

		dataHandle.addTOC((String) null);

		TOC toc = StructureFactory.createTOC("toc2");//$NON-NLS-1$
		dataHandle.addTOC(toc);

		save();
		assertTrue(compareFile("ReportItemParseTest_2_golden.xml"));//$NON-NLS-1$

	}

	/**
	 * Test translate TOC expression to TOC structure.
	 * 
	 * @throws Exception
	 */

	public void testOpenTOCStructure() throws Exception {
		openDesign("ReportItemParseTest_2.xml");//$NON-NLS-1$
		DataItemHandle dataHandle = (DataItemHandle) designHandle.getBody().get(0);
		TOCHandle tocHandle = dataHandle.getTOC();
		assertNotNull(tocHandle);
		assertEquals("2005 Statistics", tocHandle.getExpression());//$NON-NLS-1$

		assertEquals("NewStyle", tocHandle.getStyleName());//$NON-NLS-1$

		assertEquals("double", tocHandle.getBorderTopStyle());//$NON-NLS-1$
		assertEquals("thick", tocHandle.getBorderTopWidth().getStringValue());//$NON-NLS-1$
		assertEquals("gray", tocHandle.getBorderTopColor().getStringValue());//$NON-NLS-1$

		assertEquals("double", tocHandle.getBorderLeftStyle());//$NON-NLS-1$
		assertEquals("thick", tocHandle.getBorderLeftWidth().getStringValue());//$NON-NLS-1$
		assertEquals("blue", tocHandle.getBorderLeftColor().getStringValue());//$NON-NLS-1$

		assertEquals("double", tocHandle.getBorderBottomStyle());//$NON-NLS-1$
		assertEquals("thick", tocHandle.getBorderBottomWidth().getStringValue());//$NON-NLS-1$
		assertEquals("red", tocHandle.getBorderBottomColor().getStringValue());//$NON-NLS-1$

		assertEquals("double", tocHandle.getBorderRightStyle());//$NON-NLS-1$
		assertEquals("thick", tocHandle.getBorderRightWidth().getStringValue());//$NON-NLS-1$
		assertEquals("yellow", tocHandle.getBorderRightColor().getStringValue());//$NON-NLS-1$

		assertEquals("center", tocHandle.getNumberAlign());//$NON-NLS-1$
		assertEquals("cursive", tocHandle.getFontFamily().getValue());//$NON-NLS-1$
		assertEquals("10pc", tocHandle.getFontSize().getStringValue());//$NON-NLS-1$
		assertEquals("italic", tocHandle.getFontStyle());//$NON-NLS-1$
		assertEquals("bold", tocHandle.getFontWeight());//$NON-NLS-1$
		assertEquals("normal", tocHandle.getFontVariant());//$NON-NLS-1$
		assertEquals("#000000", tocHandle.getColor().getStringValue());//$NON-NLS-1$
		assertEquals("underline", tocHandle.getTextUnderline());//$NON-NLS-1$
		assertEquals("overline", tocHandle.getTextOverline());//$NON-NLS-1$
		assertEquals("line-through", tocHandle.getTextLineThrough());//$NON-NLS-1$
		assertEquals("right", tocHandle.getTextAlign());//$NON-NLS-1$
		assertEquals("4in", tocHandle.getTextIndent().getStringValue());//$NON-NLS-1$
		assertEquals("lowercase", tocHandle.getTextTransform());//$NON-NLS-1$

		assertEquals("yyyy/mm/dd", tocHandle.getDateTimeFormat());//$NON-NLS-1$
		assertEquals("#.00", tocHandle.getNumberFormat());//$NON-NLS-1$
		assertEquals("string-format", tocHandle.getStringFormat());//$NON-NLS-1$

		assertEquals("Short Date", tocHandle.getDateTimeFormatCategory());//$NON-NLS-1$
		assertEquals("Currency", tocHandle.getNumberFormatCategory());//$NON-NLS-1$
		assertEquals("<", tocHandle.getStringFormatCategory());//$NON-NLS-1$

		StyleHandle styleHandle = (StyleHandle) designHandle.getStyles().get(0);
		styleHandle.setProperty(IStyleModel.BORDER_TOP_COLOR_PROP, "red");//$NON-NLS-1$

		styleHandle.setProperty(IStyleModel.BORDER_LEFT_COLOR_PROP, "yellow");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_LEFT_COLOR_MEMBER, null);

		tocHandle.setProperty(TOC.BORDER_BOTTOM_WIDTH_MEMBER, null);
		tocHandle.setProperty(TOC.BORDER_RIGHT_COLOR_MEMBER, "white");//$NON-NLS-1$

		assertEquals("gray", tocHandle.getBorderTopColor().getStringValue());//$NON-NLS-1$
		assertEquals("medium", tocHandle.getBorderBottomWidth().getStringValue());//$NON-NLS-1$

		assertEquals("white", tocHandle.getBorderRightColor().getStringValue());//$NON-NLS-1$
		assertEquals("yellow", tocHandle.getBorderLeftColor().getStringValue());//$NON-NLS-1$

		save();
		assertTrue(compareFile("ReportItemParseTest_3_golden.xml"));//$NON-NLS-1$
	}

	/**
	 * Test write a TOC structure.
	 * 
	 * @throws Exception
	 */

	public void testWriteTOCStructure() throws Exception {
		createDesign();

		DataItemHandle dataHandle = designHandle.getElementFactory().newDataItem("bodyData");//$NON-NLS-1$
		designHandle.getBody().add(dataHandle);

		TOC toc = StructureFactory.createTOC("2005 Statistics");//$NON-NLS-1$

		TOCHandle tocHandle = dataHandle.addTOC(toc);
		assertNotNull(tocHandle);
		assertEquals("2005 Statistics", tocHandle.getExpression());//$NON-NLS-1$

		tocHandle.setStyleName("NewStyle");//$NON-NLS-1$

		tocHandle.setProperty(TOC.BORDER_TOP_STYLE_MEMBER, "double");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_TOP_COLOR_MEMBER, "gray");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_TOP_WIDTH_MEMBER, "thick");//$NON-NLS-1$

		tocHandle.setProperty(TOC.BORDER_BOTTOM_STYLE_MEMBER, "double");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_BOTTOM_COLOR_MEMBER, "red");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_BOTTOM_WIDTH_MEMBER, "thick");//$NON-NLS-1$

		tocHandle.setProperty(TOC.BORDER_LEFT_STYLE_MEMBER, "double");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_LEFT_COLOR_MEMBER, "blue");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_LEFT_WIDTH_MEMBER, "thick");//$NON-NLS-1$

		tocHandle.setProperty(TOC.BORDER_RIGHT_STYLE_MEMBER, "double");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_RIGHT_COLOR_MEMBER, "yellow");//$NON-NLS-1$
		tocHandle.setProperty(TOC.BORDER_RIGHT_WIDTH_MEMBER, "thick");//$NON-NLS-1$

		tocHandle.setProperty(TOC.BACKGROUND_COLOR_MEMBER, "#808080");//$NON-NLS-1$
		tocHandle.setProperty(TOC.NUMBER_ALIGN_MEMBER, "center");//$NON-NLS-1$
		tocHandle.setProperty(TOC.FONT_FAMILY_MEMBER, "cursive");//$NON-NLS-1$
		tocHandle.setProperty(TOC.FONT_SIZE_MEMBER, "10pc");//$NON-NLS-1$
		tocHandle.setProperty(TOC.FONT_STYLE_MEMBER, "italic");//$NON-NLS-1$
		tocHandle.setProperty(TOC.FONT_WEIGHT_MEMBER, "bold");//$NON-NLS-1$
		tocHandle.setProperty(TOC.FONT_VARIANT_MEMBER, "normal");//$NON-NLS-1$
		tocHandle.setProperty(TOC.COLOR_MEMBER, "#000000");//$NON-NLS-1$
		tocHandle.setProperty(TOC.TEXT_UNDERLINE_MEMBER, "underline");//$NON-NLS-1$
		tocHandle.setProperty(TOC.TEXT_OVERLINE_MEMBER, "overline");//$NON-NLS-1$
		tocHandle.setProperty(TOC.TEXT_LINE_THROUGH_MEMBER, "line-through");//$NON-NLS-1$
		tocHandle.setProperty(TOC.TEXT_ALIGN_MEMBER, "right");//$NON-NLS-1$
		tocHandle.setProperty(TOC.TEXT_INDENT_MEMBER, "4in");//$NON-NLS-1$
		tocHandle.setProperty(TOC.TEXT_TRANSFORM_MEMBER, "lowercase");//$NON-NLS-1$

		FormatValue value = new DateTimeFormatValue();
		value.setPattern("yyyy/mm/dd");//$NON-NLS-1$
		value.setCategory("Short Date");//$NON-NLS-1$
		tocHandle.setProperty(TOC.DATE_TIME_FORMAT_MEMBER, value);

		value = new StringFormatValue();
		value.setPattern("string-format");//$NON-NLS-1$
		value.setCategory("<");//$NON-NLS-1$
		tocHandle.setProperty(TOC.STRING_FORMAT_MEMBER, value);

		value = new NumberFormatValue();
		value.setPattern("#.00");//$NON-NLS-1$
		value.setCategory("Currency");//$NON-NLS-1$
		tocHandle.setProperty(TOC.NUMBER_FORMAT_MEMBER, value);

		save();
		assertTrue(compareFile("ReportItemParseTest_4_golden.xml"));//$NON-NLS-1$
	}
}
