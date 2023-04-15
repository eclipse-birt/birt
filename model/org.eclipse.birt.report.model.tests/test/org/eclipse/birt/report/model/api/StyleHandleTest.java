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

import java.util.Iterator;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests cases for StyleHandle and HighlightRuleHandle.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * <tr>
 * <td>{@link #testHighlightProperties()}</td>
 * <td>Reads and sets values of properties of a highlight.</td>
 * <td>Gets and sets values in a highlight correctly.</td>
 * </tr>
 * <tr>
 * <td>{@link #testStyleProperties()}</td>
 * <td>Reads and sets values of properties of a style.</td>
 * <td>Gets and sets values in a style correctly.</td>
 * </tr>
 * </table>
 */

public class StyleHandleTest extends BaseTestCase {

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("StyleHandleTest.xml"); //$NON-NLS-1$
	}

	/**
	 * Reads and sets values of properties of a style.
	 *
	 * @throws SemanticException if the value cannot be set properly
	 */

	public void testStyleProperties() throws SemanticException {
		StyleHandle styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$

		ColorHandle colorHandle = styleHandle.getColor();
		colorHandle.setRGB(0xFF0088);
		assertEquals(0xFF0088, colorHandle.getRGB());

		// background

		colorHandle = styleHandle.getBackgroundColor();
		assertNotNull(colorHandle);
		colorHandle.setStringValue("red"); //$NON-NLS-1$
		assertEquals("red", colorHandle.getValue()); //$NON-NLS-1$

		styleHandle.setBackgroundImage("image1"); //$NON-NLS-1$
		assertEquals("image1", styleHandle.getBackgroundImage()); //$NON-NLS-1$

		styleHandle.setBackgroundImageType(DesignChoiceConstants.IMAGE_REF_TYPE_URL);
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_URL, styleHandle.getBackgroundImageType());
		try {
			styleHandle.setBackgroundImageType("NonDefindedType"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_URL, styleHandle.getBackgroundImageType());

		styleHandle.setBackgroundRepeat(DesignChoiceConstants.BACKGROUND_REPEAT_NO_REPEAT);
		assertEquals(DesignChoiceConstants.BACKGROUND_REPEAT_NO_REPEAT, styleHandle.getBackgroundRepeat());

		styleHandle.setBackgroundAttachment(DesignChoiceConstants.BACKGROUND_ATTACHMENT_FIXED);
		assertEquals(DesignChoiceConstants.BACKGROUND_ATTACHMENT_FIXED, styleHandle.getBackgroundAttachment());

		DimensionHandle dimensionHandle = styleHandle.getBackGroundPositionX();
		dimensionHandle = styleHandle.getBackGroundPositionY();

		// border color

		colorHandle = styleHandle.getBorderBottomColor();
		try {
			colorHandle.setStringValue("nocolor"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		colorHandle = styleHandle.getBorderLeftColor();
		colorHandle.setRGB(0x123456);

		colorHandle = styleHandle.getBorderRightColor();
		colorHandle.setRGB(0x654321);

		colorHandle = styleHandle.getBorderTopColor();
		colorHandle.setStringValue("yellow"); //$NON-NLS-1$

		// border line style

		styleHandle.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_DOTTED);
		assertEquals(DesignChoiceConstants.LINE_STYLE_DOTTED, styleHandle.getBorderLeftStyle());
		styleHandle.setBorderRightStyle(DesignChoiceConstants.LINE_STYLE_NONE);
		assertEquals(DesignChoiceConstants.LINE_STYLE_NONE, styleHandle.getBorderRightStyle());
		styleHandle.setBorderTopStyle(DesignChoiceConstants.LINE_STYLE_RIDGE);
		assertEquals(DesignChoiceConstants.LINE_STYLE_RIDGE, styleHandle.getBorderTopStyle());
		styleHandle.setBorderBottomStyle(DesignChoiceConstants.LINE_STYLE_GROOVE);
		assertEquals(DesignChoiceConstants.LINE_STYLE_GROOVE, styleHandle.getBorderBottomStyle());

		// invalid choice value

		try {
			styleHandle.setTextAlign("nochoice"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND);
		}

		// border width

		dimensionHandle = styleHandle.getBorderBottomWidth();
		dimensionHandle.setStringValue(DesignChoiceConstants.LINE_WIDTH_MEDIUM);

		dimensionHandle = styleHandle.getBorderTopWidth();
		dimensionHandle.setStringValue("12pt"); //$NON-NLS-1$

		dimensionHandle = styleHandle.getBorderLeftWidth();
		dimensionHandle.setStringValue("12mm"); //$NON-NLS-1$

		dimensionHandle = styleHandle.getBorderRightWidth();
		dimensionHandle.setStringValue(DesignChoiceConstants.LINE_WIDTH_THICK);

		dimensionHandle = styleHandle.getTextIndent();
		dimensionHandle.setStringValue("2pc"); //$NON-NLS-1$

		// font properties

		FontHandle fontHandle = styleHandle.getFontFamilyHandle();
		fontHandle.setStringValue("song"); //$NON-NLS-1$

		dimensionHandle = styleHandle.getFontSize();
		dimensionHandle.setStringValue("18pc"); //$NON-NLS-1$

		styleHandle.setFontStyle(DesignChoiceConstants.FONT_STYLE_OBLIQUE);
		assertEquals(DesignChoiceConstants.FONT_STYLE_OBLIQUE, styleHandle.getFontStyle());

		styleHandle.setFontVariant(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS);
		assertEquals(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS, styleHandle.getFontVariant());

		styleHandle.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_900);
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_900, styleHandle.getFontWeight());

		// text properties

		dimensionHandle = styleHandle.getWordSpacing();
		dimensionHandle.setStringValue(DesignChoiceConstants.NORMAL_NORMAL);

		dimensionHandle = styleHandle.getLetterSpacing();
		dimensionHandle.setStringValue("12pt"); //$NON-NLS-1$

		styleHandle.setTextUnderline(DesignChoiceConstants.TEXT_UNDERLINE_NONE);
		assertEquals(DesignChoiceConstants.TEXT_UNDERLINE_NONE, styleHandle.getTextUnderline());

		styleHandle.setTextOverline(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE);
		assertEquals(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE, styleHandle.getTextOverline());

		styleHandle.setTextLineThrough(DesignChoiceConstants.TEXT_LINE_THROUGH_NONE);
		assertEquals(DesignChoiceConstants.TEXT_LINE_THROUGH_NONE, styleHandle.getTextLineThrough());

		styleHandle.setVerticalAlign(DesignChoiceConstants.VERTICAL_ALIGN_SUPER);
		assertEquals(DesignChoiceConstants.VERTICAL_ALIGN_SUPER, styleHandle.getVerticalAlign());

		styleHandle.setTextTransform(DesignChoiceConstants.TRANSFORM_CAPITALIZE);
		assertEquals(DesignChoiceConstants.TRANSFORM_CAPITALIZE, styleHandle.getTextTransform());

		styleHandle.setTextAlign(DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_RIGHT, styleHandle.getTextAlign());

		styleHandle.setWhiteSpace(DesignChoiceConstants.WHITE_SPACE_NOWRAP);
		assertEquals(DesignChoiceConstants.WHITE_SPACE_NOWRAP, styleHandle.getWhiteSpace());

		// box properties

		dimensionHandle = styleHandle.getMarginTop();
		String marginTop = dimensionHandle.getStringValue();
		assertEquals(marginTop, "-1pt");//$NON-NLS-1$
		dimensionHandle.setStringValue("-9pt"); //$NON-NLS-1$
		assertEquals("-9pt", dimensionHandle.getStringValue());//$NON-NLS-1$

		dimensionHandle = styleHandle.getMarginBottom();
		dimensionHandle.setStringValue("-9pt"); //$NON-NLS-1$
		assertEquals("-9pt", dimensionHandle.getStringValue());//$NON-NLS-1$

		dimensionHandle = styleHandle.getMarginLeft();
		dimensionHandle.setStringValue("-9pt"); //$NON-NLS-1$
		assertEquals("-9pt", dimensionHandle.getStringValue());//$NON-NLS-1$

		dimensionHandle = styleHandle.getMarginRight();
		dimensionHandle.setStringValue("-9pt"); //$NON-NLS-1$
		assertEquals("-9pt", dimensionHandle.getStringValue());//$NON-NLS-1$

		// format

		try {
			styleHandle.setNumberFormatCategory("#,###,###.##"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		styleHandle.setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY);
		styleHandle.setNumberFormat("$##,##"); //$NON-NLS-1$
		assertEquals("$##,##", styleHandle.getNumberFormat()); //$NON-NLS-1$

		styleHandle.setStringFormatCategory(DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE);
		styleHandle.setStringFormat("***"); //$NON-NLS-1$
		assertEquals("***", styleHandle.getStringFormat()); //$NON-NLS-1$

		styleHandle.setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE);
		styleHandle.setDateTimeFormat("MM/DD/YYYY"); //$NON-NLS-1$
		assertEquals("MM/DD/YYYY", styleHandle.getDateTimeFormat()); //$NON-NLS-1$

		// section

		styleHandle.setDisplay(DesignChoiceConstants.DISPLAY_BLOCK);
		assertEquals(DesignChoiceConstants.DISPLAY_BLOCK, styleHandle.getDisplay());

		// pagination

		styleHandle.setOrphans(DesignChoiceConstants.ORPHANS_INHERIT);
		assertEquals(DesignChoiceConstants.ORPHANS_INHERIT, styleHandle.getOrphans());

		styleHandle.setWidows("15"); //$NON-NLS-1$

		assertEquals("15", styleHandle.getWidows()); //$NON-NLS-1$

		styleHandle.setPageBreakAfter(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, styleHandle.getPageBreakAfter());

		styleHandle.setPageBreakBefore(DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS, styleHandle.getPageBreakBefore());

		styleHandle.setPageBreakInside(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO, styleHandle.getPageBreakInside());

		styleHandle.setCanShrink(false);
		assertEquals(false, styleHandle.canShrink());

		styleHandle.setShowIfBlank(false);
		assertEquals(false, styleHandle.showIfBlank());

		styleHandle.setMapTestExpr("new map rule test expr"); //$NON-NLS-1$
		// assertEquals( "new map rule test expr", styleHandle.getMapTestExpr( )
		// ); //$NON-NLS-1$

		styleHandle.setHighlightTestExpr("new highlight rule test expr"); //$NON-NLS-1$
		// assertEquals(
		// "new highlight rule test expr", styleHandle.getHighlightTestExpr( )
		// ); //$NON-NLS-1$

		styleHandle.setMasterPage("new master page"); //$NON-NLS-1$
		assertEquals("new master page", styleHandle.getMasterPage()); //$NON-NLS-1$

		styleHandle.setStringFormat("new string format"); //$NON-NLS-1$
		assertEquals("new string format", styleHandle.getStringFormat()); //$NON-NLS-1$

		styleHandle.setBackgroundImage("new background image"); //$NON-NLS-1$
		assertEquals("new background image", styleHandle.getBackgroundImage()); //$NON-NLS-1$

		styleHandle.setOverflow(DesignChoiceConstants.OVERFLOW_HIDDEN);
		assertEquals(DesignChoiceConstants.OVERFLOW_HIDDEN, styleHandle.getOverflow());
	}

	/**
	 * set operator to eq and make sure value2 element is not exist in xml file
	 *
	 * @throws SemanticException   if the value cannot be set properly
	 * @throws DesignFileException if there is something wrong with opening xml file
	 */
	public void testSetOperator() throws SemanticException, DesignFileException {
		openDesign("HighlightRuleHandleText.xml"); //$NON-NLS-1$
		DataItemHandle dataHandle = (DataItemHandle) designHandle.getElementByID(4);

		PropertyHandle propHandle = dataHandle.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP);
		Iterator propIterator = propHandle.iterator();
		HighlightRuleHandle handle = (HighlightRuleHandle) propIterator.next();

		handle.setOperator(DesignChoiceConstants.MAP_OPERATOR_BETWEEN);
		assertEquals("1", handle.getValue1());//$NON-NLS-1$
		assertEquals("3", handle.getValue2());//$NON-NLS-1$

		handle.setOperator(DesignChoiceConstants.MAP_OPERATOR_EQ);
		assertEquals("1", handle.getValue1());//$NON-NLS-1$
		assertNull(handle.getValue2());

		handle.setOperator(DesignChoiceConstants.MAP_OPERATOR_NULL);
		assertNull(handle.getValue1());
		assertNull(handle.getValue2());
	}

	/**
	 * Reads and sets values of properties of a highlight.
	 *
	 * @throws SemanticException if the value cannot be set properly
	 */

	public void testHighlightProperties() throws SemanticException {

		StyleHandle styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$

		Iterator highlightHandles = styleHandle.highlightRulesIterator();
		assertNotNull(highlightHandles);

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		ColorHandle colorHandle = highlightHandle.getColor();
		colorHandle.setRGB(0xFF0088);

		colorHandle = highlightHandle.getBackgroundColor();
		assertNotNull(colorHandle);
		colorHandle.setStringValue("red"); //$NON-NLS-1$

		colorHandle = highlightHandle.getBorderBottomColor();
		try {
			colorHandle.setStringValue("nocolor"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE);
		}

		colorHandle = highlightHandle.getBorderLeftColor();
		colorHandle.setRGB(0x123456);

		colorHandle = highlightHandle.getBorderRightColor();
		colorHandle.setRGB(0x654321);

		colorHandle = highlightHandle.getBorderTopColor();
		colorHandle.setStringValue("yellow"); //$NON-NLS-1$

		highlightHandle.setBorderLeftStyle(DesignChoiceConstants.LINE_STYLE_DOTTED);
		highlightHandle.setBorderRightStyle(DesignChoiceConstants.LINE_STYLE_NONE);
		highlightHandle.setBorderTopStyle(DesignChoiceConstants.LINE_STYLE_RIDGE);
		highlightHandle.setBorderBottomStyle(DesignChoiceConstants.LINE_STYLE_GROOVE);

		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_EQ);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_NE);

		highlightHandle.setValue1("   dataSet.name  "); //$NON-NLS-1$

		// No trim() for expression property type.
		assertEquals("   dataSet.name  ", highlightHandle.getValue1()); //$NON-NLS-1$

		// invalid choice value

		try {
			highlightHandle.setTextAlign("nochoice"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND);
		}

		highlightHandle.setTextAlign(DesignChoiceConstants.TEXT_ALIGN_JUSTIFY);
		highlightHandle.setTextLineThrough(DesignChoiceConstants.TEXT_LINE_THROUGH_NONE);

		highlightHandle.setTextOverline(DesignChoiceConstants.TEXT_OVERLINE_NONE);
		highlightHandle.setTextUnderline(DesignChoiceConstants.TEXT_UNDERLINE_NONE);
		highlightHandle.setTextTransform(DesignChoiceConstants.TRANSFORM_CAPITALIZE);

		highlightHandle.setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY);
		highlightHandle.setNumberFormat("$000,000"); //$NON-NLS-1$

		highlightHandle.setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE);
		highlightHandle.setDateTimeFormat("mm dd, yyyy"); //$NON-NLS-1$

		highlightHandle.setStringFormatCategory(DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE);
		highlightHandle.setStringFormat("no format"); //$NON-NLS-1$

		try {
			highlightHandle.setStringFormatCategory("no format"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		DimensionHandle dimensionHandle = highlightHandle.getBorderBottomWidth();
		dimensionHandle.setStringValue(DesignChoiceConstants.LINE_WIDTH_MEDIUM);

		dimensionHandle = highlightHandle.getBorderTopWidth();
		dimensionHandle.setStringValue("12pt"); //$NON-NLS-1$

		dimensionHandle = highlightHandle.getBorderLeftWidth();
		dimensionHandle.setStringValue("12mm"); //$NON-NLS-1$

		dimensionHandle = highlightHandle.getBorderRightWidth();
		dimensionHandle.setStringValue(DesignChoiceConstants.LINE_WIDTH_THICK);

		dimensionHandle = highlightHandle.getTextIndent();
		dimensionHandle.setStringValue("2pc"); //$NON-NLS-1$

		FontHandle fontHandle = highlightHandle.getFontFamilyHandle();
		fontHandle.setStringValue("song"); //$NON-NLS-1$

		dimensionHandle = highlightHandle.getFontSize();
		dimensionHandle.setStringValue("18pc"); //$NON-NLS-1$

		highlightHandle.setFontStyle(DesignChoiceConstants.FONT_STYLE_OBLIQUE);
		highlightHandle.setFontVariant(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS);
		highlightHandle.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_900);

	}

	/**
	 * Reads and sets values of properties of a map rule.
	 *
	 * @throws SemanticException if the value cannot be set properly
	 */

	public void testMapRules() throws SemanticException {
		StyleHandle styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		Iterator iter = styleHandle.mapRulesIterator();

		MapRuleHandle ruleHandle = (MapRuleHandle) iter.next();
		assertEquals("Closed", ruleHandle.getDisplay()); //$NON-NLS-1$
		assertNull(ruleHandle.getDisplayKey());
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_LIKE, ruleHandle.getOperator());
		assertEquals("X", ruleHandle.getValue1()); //$NON-NLS-1$
		assertEquals("Y", ruleHandle.getValue2()); //$NON-NLS-1$

		ruleHandle.setDisplay("new closed"); //$NON-NLS-1$
		assertEquals("new closed", ruleHandle.getDisplay()); //$NON-NLS-1$

		ruleHandle.setDisplayKey("new closed display id"); //$NON-NLS-1$
		assertEquals("new closed display id", ruleHandle.getDisplayKey()); //$NON-NLS-1$

		ruleHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_LE);
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_LE, ruleHandle.getOperator());

		ruleHandle.setValue1("new x"); //$NON-NLS-1$
		ruleHandle.setValue2("new y"); //$NON-NLS-1$
		assertEquals("new x", ruleHandle.getValue1()); //$NON-NLS-1$
		assertEquals("new y", ruleHandle.getValue2()); //$NON-NLS-1$

		// to test parse the

		ruleHandle = (MapRuleHandle) iter.next();
		assertEquals("id for open", ruleHandle.getDisplayKey()); //$NON-NLS-1$
	}

	/**
	 * Test set format of style
	 *
	 * @throws Exception
	 */

	public void testSetStyleFormat() throws Exception {
		designHandle = new SessionHandle(ULocale.getDefault()).createDesign();
		design = (ReportDesign) designHandle.getModule();

		ElementFactory factory = new ElementFactory(design);
		StyleHandle style1 = factory.newStyle("style1"); //$NON-NLS-1$

		try {
			style1.setNumberFormatCategory("no format"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		style1.setNumberFormat("****"); //$NON-NLS-1$
		style1.setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED);

		FactoryPropertyHandle factoryHandle = style1.getFactoryPropertyHandle(IStyleModel.NUMBER_FORMAT_PROP);

		assertEquals("****", factoryHandle.getStringValue()); //$NON-NLS-1$

		style1.setDateFormatCategory(DesignChoiceConstants.DATE_FORMAT_TYPE_SHORT_DATE);
		style1.setDateFormat("MM/DD/YYYY"); //$NON-NLS-1$
		assertEquals("MM/DD/YYYY", style1.getDateFormat()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.DATE_FORMAT_TYPE_SHORT_DATE, style1.getDateFormatCategory());

		style1.setTimeFormatCategory(DesignChoiceConstants.TIME_FORMAT_TYPE_SHORT_TIME);
		style1.setTimeFormat("hh/mm"); //$NON-NLS-1$
		assertEquals("hh/mm", style1.getTimeFormat()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.TIME_FORMAT_TYPE_SHORT_TIME, style1.getTimeFormatCategory());
	}

	/**
	 * Test style is predefined or not.
	 *
	 */

	public void testIsPredefinedStyle() {
		createDesign();

		StyleHandle style = designHandle.getElementFactory().newStyle("myStyle"); //$NON-NLS-1$
		assertFalse(style.isPredefined());

		style = designHandle.getElementFactory().newStyle("grid"); //$NON-NLS-1$
		assertTrue(style.isPredefined());

		style = designHandle.getElementFactory().newStyle("myStyle1"); //$NON-NLS-1$
		assertFalse(style.isPredefined());

		style = designHandle.getElementFactory().newStyle("table-group-footer-cell"); //$NON-NLS-1$
		assertTrue(style.isPredefined());
	}

	/**
	 * Test PropertyMark method.
	 *
	 */

	public void testPropertyMask() {
		createDesign();

		StyleHandle style = designHandle.getElementFactory().newStyle("myStyle"); //$NON-NLS-1$
		assertFalse(style.isPredefined());

		try {
			style.setPropertyMask(IStyleModel.BACKGROUND_IMAGE_PROP, DesignChoiceConstants.PROPERTY_MASK_TYPE_LOCK);
			style.setBackgroundImage("new url"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_LOCKED, e.getErrorCode());
		}
	}

	/**
	 * Highlight should be:
	 *
	 * 1. no cascading. 2. effect for all elements.
	 *
	 */

	public void testHightlightRules() {
		TableHandle table = (TableHandle) designHandle.findElement("My Table"); //$NON-NLS-1$
		Iterator highlightRules = table.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP).iterator();
		HighlightRuleHandle highLight = (HighlightRuleHandle) highlightRules.next();
		assertNotNull(highLight);
		assertEquals("#C0C0C0", highLight.getBackgroundColor().getStringValue()); //$NON-NLS-1$

		RowHandle row = (RowHandle) table.getDetail().get(0);
		highlightRules = row.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP).iterator();
		highLight = (HighlightRuleHandle) highlightRules.next();
		assertNotNull(highLight);
		assertEquals("red", highLight.getBackgroundColor().getStringValue()); //$NON-NLS-1$

		CellHandle cell = (CellHandle) row.getCells().get(0);
		highlightRules = cell.getPropertyHandle(IStyleModel.HIGHLIGHT_RULES_PROP).iterator();
		assertFalse(highlightRules.hasNext());
	}

	/**
	 * Test null style. 1.Create new style. 2.Let Label binds with this style
	 * 3.Delete style. 4.Set style in label to null 5.Test if style information
	 * exists in design file or not. The expected result is no information exists.
	 *
	 * @throws Exception
	 */

	public void testSetNullStyle() throws Exception {
		designHandle.getStyles().drop(0);

		DesignElementHandle handle = designHandle.findElement("My Label1"); //$NON-NLS-1$

		handle.setProperty(IStyledElementModel.STYLE_PROP, null);

		assertNull(handle.getProperty(IStyledElementModel.STYLE_PROP));

	}

	/**
	 * Tests get properties from cell selector.
	 *
	 * @throws Exception
	 */

	public void testGetCellSelectorProperty() throws Exception {
		openDesign("CellSelectorTest.xml"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		GroupHandle group = (GroupHandle) table.getGroups().get(0);
		RowHandle row = (RowHandle) group.getHeader().get(0);
		CellHandle cell = (CellHandle) row.getCells().get(0);

		assertEquals(DesignChoiceConstants.LINE_STYLE_DOUBLE, cell.getProperty(IStyleModel.BORDER_LEFT_STYLE_PROP));
	}

	/**
	 * Tests set background size property value.
	 *
	 * @throws Exception
	 */
	public void testBackgroundSize() throws Exception {
		openDesign("BackgroundSizeTest.xml"); //$NON-NLS-1$

		// original width value is 30% and height value is 60%

		// if the input value is contain or cover, both the width and height of
		// the background size are set as the input value.

		StyleHandle styleHandle = designHandle.findStyle("style1"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);

		// if both the original height value and input width value are neither
		// cover nor contain, the width will be set as input value and the
		// height will not be set.

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH, "80%", "80%", "60%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT, "80%", "30%", "80%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO, "60%"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, "30%", //$NON-NLS-1$
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);

		// both width and height value are cover

		styleHandle = designHandle.findStyle("style2"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH, "80%", "80%", "80%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT, "80%", "80%", "80%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);

		// width is contain and height is 30%
		styleHandle = designHandle.findStyle("style3"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, "30%"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH, "60%", "60%", "30%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT, "60%", "60%", "60%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO, "30%"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);

		// width is contain and height is auto
		styleHandle = designHandle.findStyle("style4"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH, "60%", "60%", //$NON-NLS-1$ //$NON-NLS-2$
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT, "60%", "60%", "60%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);

		// both the width and height are auto
		styleHandle = designHandle.findStyle("style5"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH, "30%", "30%", //$NON-NLS-1$ //$NON-NLS-2$
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT, "30%", //$NON-NLS-1$
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, "30%"); //$NON-NLS-1$

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);

		// width is contain and height is cover
		styleHandle = designHandle.findStyle("style6"); //$NON-NLS-1$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_COVER,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_COVER);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN,
				DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO, DesignChoiceConstants.BACKGROUND_SIZE_AUTO,
				DesignChoiceConstants.BACKGROUND_SIZE_AUTO);

		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_WIDTH, "80%", "80%", "80%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		comparedBackgroundSize(styleHandle, IStyleModel.BACKGROUND_SIZE_HEIGHT, "80%", "80%", "80%"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * @param styleHandle    the style handle.
	 * @param propName       the property name.
	 * @param value          the input value
	 * @param expectedWidth  the expected width value
	 * @param expectedHeight the expected height value
	 * @throws Exception
	 */
	private void comparedBackgroundSize(StyleHandle styleHandle, String propName, String value, String expectedWidth,
			String expectedHeight) throws Exception {
		DimensionHandle width = styleHandle.getBackgroundWidth();
		DimensionHandle height = styleHandle.getBackgroundHeight();

		String oldWidth = width.getStringValue();
		String oldHeight = height.getStringValue();

		if (IStyleModel.BACKGROUND_SIZE_WIDTH.equals(propName)) {
			width.setValue(value);
		} else {
			height.setValue(value);
		}

		assertEquals(expectedWidth, width.getStringValue());
		assertEquals(expectedHeight, height.getStringValue());

		ActivityStack stack = design.getActivityStack();

		if (stack.canUndo())

		{
			design.getActivityStack().undo();

			assertEquals(oldWidth, width.getStringValue());
			assertEquals(oldHeight, height.getStringValue());
		} else if ("style3".equals(styleHandle.getName()) //$NON-NLS-1$
				&& IStyleModel.BACKGROUND_SIZE_WIDTH.equals(propName)
				&& DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(value)) {
			assert true;

		} else if ("style4".equals(styleHandle.getName()) //$NON-NLS-1$
				&& IStyleModel.BACKGROUND_SIZE_WIDTH.equals(propName)
				&& DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(value)) {
			assert true;
		} else if ("style4".equals(styleHandle.getName()) //$NON-NLS-1$
				&& IStyleModel.BACKGROUND_SIZE_HEIGHT.equals(propName)
				&& DesignChoiceConstants.BACKGROUND_SIZE_AUTO.equals(value)) {
			assert true;
		} else if ("style5".equals(styleHandle.getName()) //$NON-NLS-1$
				&& IStyleModel.BACKGROUND_SIZE_HEIGHT.equals(propName)
				&& DesignChoiceConstants.BACKGROUND_SIZE_AUTO.equals(value)) {
			assert true;
		} else if ("style5".equals(styleHandle.getName()) //$NON-NLS-1$
				&& IStyleModel.BACKGROUND_SIZE_WIDTH.equals(propName)
				&& DesignChoiceConstants.BACKGROUND_SIZE_AUTO.equals(value)) {
			assert true;
		} else if ("style6".equals(styleHandle.getName()) //$NON-NLS-1$
				&& IStyleModel.BACKGROUND_SIZE_HEIGHT.equals(propName)
				&& DesignChoiceConstants.BACKGROUND_SIZE_COVER.equals(value)) {
			assert true;
		} else if ("style6".equals(styleHandle.getName()) //$NON-NLS-1$
				&& IStyleModel.BACKGROUND_SIZE_WIDTH.equals(propName)
				&& DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN.equals(value)) {
			assert true;
		} else {
			assert false;
		}
	}
}
