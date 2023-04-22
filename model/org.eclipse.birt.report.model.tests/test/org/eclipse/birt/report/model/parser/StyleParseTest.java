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

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ColorHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.FontHandle;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.DateFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.TimeFormatValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.ModuleImpl;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IFreeFormModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IInternalReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test cases for style parsing, writing and referring.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Test whether all style properties can be read.</td>
 * <td>All style property values should be right.</td>
 * </tr>
 * <tr>
 * <td>{@link #testSearchingProperty()}</td>
 * <td>Test getting non-style property value set in element itself.</td>
 * <td>The property value should be from this element itself.</td>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value set in element's local
 * non-predefined style.</td>
 * <td>The property value should be from this element's local non-predefined
 * style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value set in element's predefined
 * style.</td>
 * <td>The property value should be from this element's predefined style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value set in element's style.</td>
 * <td>The property value should be from this element's style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value set in element's private
 * style.</td>
 * <td>The property value should be from this element's private style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the unset non-style property value whose "canInherit" is
 * false.</td>
 * <td>The property value should be default value, if possible.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the unset non-style property value whose "canInherit" is
 * true.</td>
 * <td>The property value should be default value, if possible.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the unset style property value whose "canInherit" is
 * false.</td>
 * <td>The property value should be default value, if possible.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the unset style property value whose "canInherit" is
 * true.</td>
 * <td>The property value should be default value, if possible.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the non-style property value that is set on element's
 * parent.</td>
 * <td>The property value should be from parent.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the local style,
 * including predefined style, of element's parent.</td>
 * <td>The property value should be from parent's local style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the non-local style
 * of element's parent.</td>
 * <td>The property value should be null.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the predefined style
 * of one element's container/slot combination.</td>
 * <td>The property value should be from the predefined style of this element's
 * container/slot combination.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the predefined style
 * of the slot in which one element's.</td>
 * <td>The property value should be from the predefined style of the slot in
 * which this element's.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the non-predefined
 * style of one element's container.</td>
 * <td>The property value should be from the non-predefined style of one
 * element's container.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the predefined style
 * of one element's container.</td>
 * <td>The property value should be from the predefined style of one element's
 * container.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the non-style property value that is set on one element's
 * container.</td>
 * <td>The property value should be null.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is not set on the
 * non-predefined style, but set on one element's container.</td>
 * <td>The property value should be from the non-predefined style of this
 * element's container.</td>
 * </tr>
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Test writer.</td>
 * <td>The output file should be same as golden file.</td>
 * </tr>
 * <tr>
 * <td>{@link #testPredefinedStyle()}</td>
 * <td>Test all predefined style in rom.def.</td>
 * <td>All predefined style should be right.</td>
 * </tr>
 * <tr>
 * <td>{@link #testPropertySearchWithLevel()}</td>
 * <td>Test searching property with level.</td>
 * <td>The property value should be right.</td>
 * </tr>
 * <tr>
 * <td>{@link #testReadHighlightRules()}</td>
 * <td>Test highlight property. Get the member value to see if they're identical
 * to those defined in the input file.</td>
 * <td>The member values should be identical to those in the input file</td>
 * </tr>
 * <tr>
 * <td>{@link #testWriteHighlightRules()}</td>
 * <td>Test highlight property. Set the member value to see if they're identical
 * to those defined in the golden file.</td>
 * <td>Output file matches the golden file.</td>
 * </tr>
 * <tr>
 * <td>{@link #testSessionDefault()}</td>
 * <td>Test searching property with session default value.</td>
 * <td>The property value should be from session.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test searching property with metadata default value.</td>
 * <td>The property value should be from metadata.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Clear session default value.</td>
 * <td>The property value should be from metadata.</td>
 * </tr>
 * <tr>
 * <td>{@link #testSemanticError()}</td>
 * <td>Test semantic errors with the design file input.</td>
 * <td>The errors are collected, such as the font-size and width is
 * negative.</td>
 * </tr>
 * </table>
 * >>>> ORIGINAL StyleParseTest.java#7 ==== THEIRS StyleParseTest.java#8
 *
 * @see org.eclipse.birt.report.model.core.StyledElement ==== YOURS
 *      StyleParseTest.java < < < <
 */

public class StyleParseTest extends BaseTestCase {

	String fileName = "StyleParseTest.xml"; //$NON-NLS-1$
	String goldenFileName = "StyleParseTest_golden.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test parser and getting property.
	 *
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(fileName);
		StyleElement style = design.findStyle("My-Style"); //$NON-NLS-1$

		DateFormatValue dateFormatValue = (DateFormatValue) style.getProperty(design, IStyleModel.DATE_FORMAT_PROP);
		assertEquals("yyyy/mm/dd", dateFormatValue.getPattern());//$NON-NLS-1$
		assertEquals("Short Date", dateFormatValue.getCategory());//$NON-NLS-1$

		TimeFormatValue timeFormatValue = (TimeFormatValue) style.getProperty(design, IStyleModel.TIME_FORMAT_PROP);
		assertEquals("hh/mm", timeFormatValue.getPattern());//$NON-NLS-1$
		assertEquals("Short Time", timeFormatValue.getCategory());//$NON-NLS-1$

		assertEquals("fantasy", style.getStringProperty(design, IStyleModel.FONT_FAMILY_PROP)); //$NON-NLS-1$
		assertEquals("red", style.getStringProperty(design, IStyleModel.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("larger", style.getStringProperty(design, IStyleModel.FONT_SIZE_PROP)); //$NON-NLS-1$
		assertEquals("italic", style.getStringProperty(design, IStyleModel.FONT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("normal", style.getStringProperty(design, IStyleModel.FONT_VARIANT_PROP)); //$NON-NLS-1$
		assertEquals("bold", style.getStringProperty(design, IStyleModel.FONT_WEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("line-through", style.getStringProperty(design, IStyleModel.TEXT_LINE_THROUGH_PROP)); //$NON-NLS-1$
		assertEquals("overline", style.getStringProperty(design, IStyleModel.TEXT_OVERLINE_PROP)); //$NON-NLS-1$
		assertEquals("underline", style.getStringProperty(design, IStyleModel.TEXT_UNDERLINE_PROP)); //$NON-NLS-1$

		assertEquals("dotted", style.getStringProperty(design, IStyleModel.BORDER_TOP_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", style.getStringProperty(design, IStyleModel.BORDER_TOP_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("blue", style.getStringProperty(design, IStyleModel.BORDER_TOP_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("dashed", style.getStringProperty(design, IStyleModel.BORDER_LEFT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", style.getStringProperty(design, IStyleModel.BORDER_LEFT_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("green", style.getStringProperty(design, IStyleModel.BORDER_LEFT_COLOR_PROP)); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID,
				style.getStringProperty(design, IStyleModel.BORDER_BOTTOM_STYLE_PROP));
		assertEquals("thin", style.getStringProperty(design, IStyleModel.BORDER_BOTTOM_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("red", style.getStringProperty(design, IStyleModel.BORDER_BOTTOM_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("double", style.getStringProperty(design, IStyleModel.BORDER_RIGHT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals("thin", style.getStringProperty(design, IStyleModel.BORDER_RIGHT_WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("maroon", style.getStringProperty(design, IStyleModel.BORDER_RIGHT_COLOR_PROP)); //$NON-NLS-1$

		assertEquals("1mm", style.getStringProperty(design, IStyleModel.PADDING_TOP_PROP)); //$NON-NLS-1$
		assertEquals("2mm", style.getStringProperty(design, IStyleModel.PADDING_LEFT_PROP)); //$NON-NLS-1$
		assertEquals("3mm", style.getStringProperty(design, IStyleModel.PADDING_RIGHT_PROP)); //$NON-NLS-1$
		assertEquals("4mm", style.getStringProperty(design, IStyleModel.PADDING_BOTTOM_PROP)); //$NON-NLS-1$

		assertEquals("scroll", style.getStringProperty(design, IStyleModel.BACKGROUND_ATTACHMENT_PROP)); //$NON-NLS-1$
		assertEquals("red", style.getStringProperty(design, IStyleModel.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$
		assertEquals("file", style.getStringProperty(design, IStyleModel.BACKGROUND_IMAGE_PROP)); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED,
				style.getStringProperty(design, IStyleModel.BACKGROUND_IMAGE_TYPE_PROP)); // $NON-NLS-1$
		assertEquals("center", style.getStringProperty(design, IStyleModel.BACKGROUND_POSITION_X_PROP)); //$NON-NLS-1$
		assertEquals("top", style.getStringProperty(design, IStyleModel.BACKGROUND_POSITION_Y_PROP)); //$NON-NLS-1$
		assertEquals("repeat", style.getStringProperty(design, IStyleModel.BACKGROUND_REPEAT_PROP)); //$NON-NLS-1$

		assertEquals("right", style.getStringProperty(design, IStyleModel.TEXT_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("5mm", style.getStringProperty(design, IStyleModel.TEXT_INDENT_PROP)); //$NON-NLS-1$
		assertEquals("normal", style.getStringProperty(design, IStyleModel.LETTER_SPACING_PROP)); //$NON-NLS-1$
		assertEquals("normal", style.getStringProperty(design, IStyleModel.LINE_HEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("19", style.getStringProperty(design, IStyleModel.ORPHANS_PROP)); //$NON-NLS-1$
		assertEquals("uppercase", style.getStringProperty(design, IStyleModel.TEXT_TRANSFORM_PROP)); //$NON-NLS-1$
		assertEquals("middle", style.getStringProperty(design, IStyleModel.VERTICAL_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("nowrap", style.getStringProperty(design, IStyleModel.WHITE_SPACE_PROP)); //$NON-NLS-1$
		assertEquals("12", style.getStringProperty(design, IStyleModel.WIDOWS_PROP)); //$NON-NLS-1$
		assertEquals("normal", style.getStringProperty(design, IStyleModel.WORD_SPACING_PROP)); //$NON-NLS-1$

		assertEquals("inline", style.getStringProperty(design, IStyleModel.DISPLAY_PROP)); //$NON-NLS-1$
		assertEquals("My Page", style.getStringProperty(design, IStyleModel.MASTER_PAGE_PROP)); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_AUTO,
				style.getStringProperty(design, IStyleModel.PAGE_BREAK_AFTER_PROP));
		assertEquals(DesignChoiceConstants.PAGE_BREAK_BEFORE_AUTO,
				style.getStringProperty(design, IStyleModel.PAGE_BREAK_BEFORE_PROP));
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO,
				style.getStringProperty(design, IStyleModel.PAGE_BREAK_INSIDE_PROP));
		assertEquals("true", style.getStringProperty(design, IStyleModel.SHOW_IF_BLANK_PROP)); //$NON-NLS-1$
		assertEquals("true", style.getStringProperty(design, IStyleModel.CAN_SHRINK_PROP)); //$NON-NLS-1$

		assertEquals("right", style.getStringProperty(design, IStyleModel.NUMBER_ALIGN_PROP)); //$NON-NLS-1$

		assertEquals("auto", style.getStringProperty(design, IStyleModel.MARGIN_TOP_PROP)); //$NON-NLS-1$
		assertEquals("auto", style.getStringProperty(design, IStyleModel.MARGIN_LEFT_PROP)); //$NON-NLS-1$
		assertEquals("auto", style.getStringProperty(design, IStyleModel.MARGIN_RIGHT_PROP)); //$NON-NLS-1$
		assertEquals("auto", style.getStringProperty(design, IStyleModel.MARGIN_BOTTOM_PROP)); //$NON-NLS-1$

		// bidi

		assertEquals(DesignChoiceConstants.BIDI_DIRECTION_RTL,
				style.getStringProperty(design, IStyleModel.TEXT_DIRECTION_PROP));

		// assertEquals(
		// "[somefield]", style.getStringProperty( design,
		// Style.MAP_TEST_EXPR_PROP ) ); //$NON-NLS-1$

		List<?> mapRules = (List<?>) style.getProperty(design, IStyleModel.MAP_RULES_PROP);
		assertEquals(5, mapRules.size());
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, ((MapRule) mapRules.get(0)).getOperator());
		// ((MapRule) mapRules.get( 0 ) ).setTestExpression("[somefield]");
		assertEquals("[somefield]", ((MapRule) mapRules.get(0)).getTestExpression()); //$NON-NLS-1$
		assertEquals("Closed", ((MapRule) mapRules.get(0)).getDisplay()); //$NON-NLS-1$
		assertEquals("\"X\"", ((MapRule) mapRules.get(0)).getValue1()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.MAP_OPERATOR_TRUE, ((MapRule) mapRules.get(1)).getOperator());
		assertEquals("Open", ((MapRule) mapRules.get(1)).getDisplay()); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.MAP_OPERATOR_LIKE, ((MapRule) mapRules.get(2)).getOperator());
		assertEquals("Unknown", ((MapRule) mapRules.get(2)).getDisplay()); //$NON-NLS-1$

		NameSpace ns = design.getNameHelper().getNameSpace(ModuleImpl.STYLE_NAME_SPACE);
		assertEquals(19, ns.getCount());

		// Overflow
		assertEquals(DesignChoiceConstants.OVERFLOW_SCROLL, style.getStringProperty(design, IStyleModel.OVERFLOW_PROP));

		// Predefined style is defined by user.
		StyleElement predefinedStyle = design.findStyle("table-detail"); //$NON-NLS-1$
		assertEquals("table-detail", predefinedStyle.getName()); //$NON-NLS-1$
		assertEquals("large", predefinedStyle.getStringProperty(design, IStyleModel.FONT_SIZE_PROP)); //$NON-NLS-1$

		StyleHandle sh = (StyleHandle) style.getHandle(design);
		Iterator<?> iter = sh.mapRulesIterator();
		assertNotNull(iter.next());
		assertNotNull(iter.next());
		assertNotNull(iter.next());

		DimensionHandle handle = sh.getBackgroundWidth();
		assertEquals(DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, handle.getStringValue());

		handle = sh.getBackgroundHeight();
		assertEquals(DesignChoiceConstants.BACKGROUND_SIZE_COVER, handle.getStringValue());

		style = design.findStyle("test"); //$NON-NLS-1$
		StyleHandle styleHandle = (StyleHandle) style.getHandle(design);

		assertEquals(ULocale.ENGLISH, getLocale(styleHandle, IStyleModel.DATE_TIME_FORMAT_PROP));
		assertEquals(ULocale.CANADA, getLocale(styleHandle, IStyleModel.DATE_FORMAT_PROP));
		assertEquals(ULocale.US, getLocale(styleHandle, IStyleModel.TIME_FORMAT_PROP));
		assertEquals(ULocale.FRANCE, getLocale(styleHandle, IStyleModel.NUMBER_FORMAT_PROP));
		assertEquals(ULocale.JAPANESE, getLocale(styleHandle, IStyleModel.STRING_FORMAT_PROP));

	}

	/**
	 * Gets the <code>ULocale<code> according to the input property name.
	 *
	 * @param handle   the style handle.
	 * @param propName the property name
	 * @return the ulocale
	 */
	private ULocale getLocale(StyleHandle handle, String propName) {
		PropertyHandle propHandle = handle.getPropertyHandle(propName);
		FormatValue formatValueToSet = (FormatValue) handle.getProperty(propName);
		FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
		return formatHandle.getLocale();
	}

	/**
	 * Test property searching algorithm. At the same time, test the
	 * {@link org.eclipse.birt.report.model.core.StyledElement #getFactoryProperty }
	 * method in StyledElement.
	 *
	 * @throws Exception any exception caught
	 */

	public void testSearchingProperty() throws Exception {
		openDesign(fileName);
		// Test getting non-style property value set in element itself.

		FreeForm form = (FreeForm) design.findElement("My First Form"); //$NON-NLS-1$
		Label label = (Label) form.getSlot(IFreeFormModel.REPORT_ITEMS_SLOT).getContent(0);
		assertEquals("4mm", label.getStringProperty(design, IInternalReportItemModel.WIDTH_PROP)); //$NON-NLS-1$
		assertEquals("4mm", label.getFactoryProperty(design, IInternalReportItemModel.WIDTH_PROP).toString()); //$NON-NLS-1$

		// Test getting the style property value set in element's local
		// non-predefined style.

		form = (FreeForm) design.findElement("My Second Form"); //$NON-NLS-1$
		label = (Label) form.getSlot(IFreeFormModel.REPORT_ITEMS_SLOT).getContent(0);
		assertEquals("My-Style", label.getStyleName()); //$NON-NLS-1$
		assertEquals("bold", label.getStringProperty(design, IStyleModel.FONT_WEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("bold", label.getFactoryProperty(design, IStyleModel.FONT_WEIGHT_PROP).toString()); //$NON-NLS-1$

		// Test getting the style property value set in element's predefined
		// style.

		form = (FreeForm) design.findElement("My Third Form"); //$NON-NLS-1$
		label = (Label) form.getSlot(IFreeFormModel.REPORT_ITEMS_SLOT).getContent(0);
		assertEquals(null, label.getStyleName());
		assertEquals("bolder", label.getStringProperty(design, IStyleModel.FONT_WEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("bolder", label.getFactoryProperty(design, IStyleModel.FONT_WEIGHT_PROP).toString()); //$NON-NLS-1$

		// Test getting the style property value set in element's private style.

		form = (FreeForm) design.findElement("My Fourth Form"); //$NON-NLS-1$
		label = (Label) form.getSlot(IFreeFormModel.REPORT_ITEMS_SLOT).getContent(0);
		assertEquals("My-Style", label.getStyleName()); //$NON-NLS-1$
		assertEquals("lighter", label.getStringProperty(design, IStyleModel.FONT_WEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("lighter", label.getFactoryProperty(design, IStyleModel.FONT_WEIGHT_PROP).toString()); //$NON-NLS-1$

		// Test getting the style property value set in element's anonymous
		// style.

		form = (FreeForm) design.findElement("My Fifth Form"); //$NON-NLS-1$
		label = (Label) form.getSlot(IFreeFormModel.REPORT_ITEMS_SLOT).getContent(0);
		assertEquals(null, label.getStyleName());
		assertEquals("100", label.getStringProperty(design, IStyleModel.FONT_WEIGHT_PROP)); //$NON-NLS-1$
		assertEquals("100", label.getFactoryProperty(design, IStyleModel.FONT_WEIGHT_PROP).toString()); //$NON-NLS-1$

		// Test getting the unset non-style property value whose "canInherit" is
		// false.

		assertEquals(null, design.getStringProperty(design, IModuleModel.AUTHOR_PROP));
		assertEquals(null, design.getFactoryProperty(design, IModuleModel.AUTHOR_PROP));

		// Test getting the unset non-style property value whose "canInherit" is
		// true.

		MasterPage page = (MasterPage) design.getSlot(IModuleModel.PAGE_SLOT).getContent(0);
		assertEquals(IMasterPageModel.US_LETTER_WIDTH, page.getStringProperty(design, IMasterPageModel.WIDTH_PROP));
		assertEquals(null, page.getFactoryProperty(design, IMasterPageModel.WIDTH_PROP));
		assertEquals("1", page.getStringProperty(design, IMasterPageModel.COLUMNS_PROP)); //$NON-NLS-1$
		assertEquals("1", page.getFactoryProperty(design, IMasterPageModel.COLUMNS_PROP).toString()); //$NON-NLS-1$

		// Test getting the unset style property value whose "canInherit" is
		// false.

		form = (FreeForm) design.findElement("My Fifth Form"); //$NON-NLS-1$
		assertEquals(null, form.getStringProperty(design, IStyleModel.BACKGROUND_COLOR_PROP));
		assertEquals("repeat", form.getStringProperty(design, IStyleModel.BACKGROUND_REPEAT_PROP)); //$NON-NLS-1$
		assertEquals(null, form.getFactoryProperty(design, IStyleModel.BACKGROUND_COLOR_PROP));
		assertEquals(null, form.getFactoryProperty(design, IStyleModel.BACKGROUND_REPEAT_PROP));

		// Test getting the unset style property value whose "canInherit" is
		// true.

		form = (FreeForm) design.findElement("My Fifth Form"); //$NON-NLS-1$
		assertEquals("serif", form.getStringProperty(design, //$NON-NLS-1$
				IStyleModel.FONT_FAMILY_PROP));
		assertEquals("normal", form.getStringProperty(design, IStyleModel.FONT_STYLE_PROP)); //$NON-NLS-1$
		assertEquals(null, form.getFactoryProperty(design, IStyleModel.FONT_FAMILY_PROP));
		assertEquals(null, form.getFactoryProperty(design, IStyleModel.FONT_STYLE_PROP));

		// Test getting the non-style property value that is set
		// on element's parent.

		FreeForm childForm = (FreeForm) design.findElement("Child Form"); //$NON-NLS-1$
		assertEquals(null, childForm.getStyleName());
		assertEquals("999mm", childForm.getStringProperty(design, IInternalReportItemModel.X_PROP)); //$NON-NLS-1$
		assertEquals("999mm", childForm.getFactoryProperty(design, IInternalReportItemModel.X_PROP).toString()); //$NON-NLS-1$

		// Test getting the style property value that is set on the local style,
		// including predefined style, of element's parent.

		childForm = (FreeForm) design.findElement("Child Form"); //$NON-NLS-1$
		assertEquals(null, childForm.getStyleName());
		assertEquals("x-small", childForm.getStringProperty(design, IStyleModel.FONT_SIZE_PROP)); //$NON-NLS-1$
		assertEquals("small-caps", childForm.getStringProperty(design, IStyleModel.FONT_VARIANT_PROP)); //$NON-NLS-1$
		assertEquals("x-small", childForm.getFactoryProperty(design, //$NON-NLS-1$
				IStyleModel.FONT_SIZE_PROP));
		assertEquals("small-caps", childForm.getFactoryProperty(design, IStyleModel.FONT_VARIANT_PROP).toString()); //$NON-NLS-1$

		// Test getting the style property value that is set on
		// the non-local style of element's parent.

		assertEquals("red", childForm.getStringProperty(design, IStyleModel.COLOR_PROP)); //$NON-NLS-1$
		assertEquals("red", childForm.getFactoryProperty(design, IStyleModel.COLOR_PROP)); //$NON-NLS-1$

		// Test getting the style property value that is set on the predefined
		// style of
		// one element's container/slot combination.

		TableItem table = (TableItem) design.findElement("My Fourth Table"); //$NON-NLS-1$
		assertNotNull(table);
		TableRow row = (TableRow) (table.getSlot(IListingElementModel.DETAIL_SLOT).getContent(0));
		assertEquals("large", row.getStringProperty(design, IStyleModel.FONT_SIZE_PROP)); //$NON-NLS-1$
		assertEquals("large", row //$NON-NLS-1$
				.getFactoryProperty(design, IStyleModel.FONT_SIZE_PROP));

		TableRow row1 = (TableRow) (table.getSlot(IListingElementModel.HEADER_SLOT).getContent(0));
		assertEquals("red", row1.getFactoryProperty(design, IStyleModel.COLOR_PROP)); //$NON-NLS-1$

		// Test getting the style property value that is set on the predefined
		// style of
		// the slot in which one element's.

		// table = (TableItem) design.findElement( "My Fifth Table" );
		// //$NON-NLS-1$
		// assertNotNull(table);
		// row = (TableRow) (table.getSlot( TableItem.FOOTER_SLOT
		// ).getContent(0));
		// assertEquals("x-large", row.getStringProperty( design,
		// Style.FONT_SIZE_PROP)); //$NON-NLS-1$

		// Test getting the style property value that is set on the
		// non-predefined style of one element's container.

		table = (TableItem) design.findElement("My Sixth Table"); //$NON-NLS-1$
		assertNotNull(table);
		row = (TableRow) (table.getSlot(IListingElementModel.HEADER_SLOT).getContent(0));
		assertEquals("xx-large", row.getStringProperty(design, IStyleModel.FONT_SIZE_PROP)); //$NON-NLS-1$
		assertEquals(null, row.getFactoryProperty(design, IStyleModel.FONT_SIZE_PROP));

		// Test getting the style property value that is set on the predefined
		// style of one element's container.

		table = (TableItem) design.findElement("My Seventh Table"); //$NON-NLS-1$
		assertNotNull(table);
		row = (TableRow) (table.getSlot(IListingElementModel.HEADER_SLOT).getContent(0));
		assertEquals("100", row.getStringProperty(design, IStyleModel.FONT_WEIGHT_PROP)); //$NON-NLS-1$
		assertEquals(null, row.getFactoryProperty(design, IStyleModel.FONT_WEIGHT_PROP));

		// Test getting the non-style property value that is set on one
		// element's container.

		table = (TableItem) design.findElement("My Eighth Inner Table"); //$NON-NLS-1$
		assertNotNull(table);
		assertEquals(null, table.getStringProperty(design, IInternalReportItemModel.X_PROP));
		assertEquals(null, table.getFactoryProperty(design, IInternalReportItemModel.X_PROP));

		// Test getting the non-style property value that is not set on its
		// style,
		// but set on its container style.

		table = (TableItem) design.findElement("My Tenth Table"); //$NON-NLS-1$
		assertNotNull(table);
		row = (TableRow) (table.getSlot(IListingElementModel.HEADER_SLOT).getContent(0));
		assertEquals("xx-large", row.getStringProperty(design, IStyleModel.FONT_SIZE_PROP)); //$NON-NLS-1$
		assertEquals(null, row.getFactoryProperty(design, IStyleModel.FONT_SIZE_PROP));
	}

	/**
	 * Test writer.
	 *
	 * @throws Exception any exception caught
	 */
	public void testWriter() throws Exception {
		openDesign(fileName, ULocale.ENGLISH);

		StyleHandle style = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		style.setNumberFormat(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC);

		style.setTextDirection(DesignChoiceConstants.BIDI_DIRECTION_LTR);
		style.setBackgroundImageType(DesignChoiceConstants.IMAGE_REF_TYPE_URL);

		DimensionHandle handle = style.getBackgroundHeight();
		handle.setStringValue("19pt"); //$NON-NLS-1$
		handle = style.getBackgroundWidth();
		handle.setStringValue("0.5in"); //$NON-NLS-1$

		// Overflow
		style.setOverflow(DesignChoiceConstants.OVERFLOW_AUTO);

		DataItemHandle label = (DataItemHandle) designHandle.findElement("my data 2"); //$NON-NLS-1$
		style = label.getPrivateStyle();
		style.setNumberFormat("###.**"); //$NON-NLS-1$
		style.setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY);

		style = designHandle.findStyle("test"); //$NON-NLS-1$
		setLocale(style, IStyleModel.DATE_TIME_FORMAT_PROP, ULocale.CANADA);
		setLocale(style, IStyleModel.DATE_FORMAT_PROP, ULocale.US);
		setLocale(style, IStyleModel.TIME_FORMAT_PROP, ULocale.FRANCE);
		setLocale(style, IStyleModel.NUMBER_FORMAT_PROP, ULocale.JAPANESE);
		setLocale(style, IStyleModel.STRING_FORMAT_PROP, ULocale.ENGLISH);

		save();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Sets the <code>ULocale</code>.
	 *
	 * @param handle   the style handle.
	 * @param propName the property name.
	 * @param locale   the locale.
	 * @throws Exception
	 */
	private void setLocale(StyleHandle handle, String propName, ULocale locale) throws Exception {
		PropertyHandle propHandle = handle.getPropertyHandle(propName);
		FormatValue formatValueToSet = (FormatValue) handle.getProperty(propName);
		FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
		formatHandle.setLocale(locale);
	}

	/**
	 * Test highlight writer.
	 *
	 * @throws Exception any exception during reading/writting the design file.
	 */

	public void testWriteHighlightRules() throws Exception {
		openDesign("StyleParseTest_1.xml"); //$NON-NLS-1$

		StyleHandle styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		Iterator<?> highlightHandles = styleHandle.highlightRulesIterator();
		assertNotNull(highlightHandles);

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertEquals("[this]", highlightHandle.getTestExpression()); //$NON-NLS-1$

		// set isDesignTime
		highlightHandle.setDesignTime(true);

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
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_LT);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_LE);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_GE);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_BETWEEN);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_NOT_BETWEEN);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_NOT_NULL);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_TRUE);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_FALSE);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_LIKE);
		highlightHandle.setOperator(DesignChoiceConstants.MAP_OPERATOR_GT);

		highlightHandle.setValue1("dataSet.name"); //$NON-NLS-1$
		highlightHandle.setValue2("table.column"); //$NON-NLS-1$

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

		highlightHandle.setNumberFormat("$000,000");//$NON-NLS-1$

		highlightHandle.setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE);
		highlightHandle.setDateTimeFormat("mm dd, yyyy"); //$NON-NLS-1$

		try {
			highlightHandle.setStringFormatCategory("no format"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
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

		// bidi

		highlightHandle.setTextDirection(DesignChoiceConstants.BIDI_DIRECTION_LTR);

		// the second highlight rule.

		highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);
		assertEquals("[this]", highlightHandle.getTestExpression()); //$NON-NLS-1$

		dimensionHandle = highlightHandle.getFontSize();
		dimensionHandle.setStringValue("18pt"); //$NON-NLS-1$

		highlightHandle.setStyleName("Empty Style"); //$NON-NLS-1$
		highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNull(highlightHandle);

		save();
		assertTrue(compareFile("StyleParseTest_golden_1.xml")); //$NON-NLS-1$
	}

	/**
	 * Test predefined style defined in rom.def.
	 *
	 * @throws Exception any exception caught
	 */
	public void testPredefinedStyle() throws Exception {
		openDesign(fileName);
		MetaDataDictionary dd = MetaDataDictionary.getInstance();

		ElementDefn elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.REPORT_DESIGN_ELEMENT);
		assertEquals("report", elementDefn.getSelector()); //$NON-NLS-1$
		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT);
		assertEquals("page", elementDefn.getSelector()); //$NON-NLS-1$
		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.LABEL_ITEM);
		assertEquals("label", elementDefn.getSelector()); //$NON-NLS-1$
		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.TEXT_ITEM);
		assertEquals("text", elementDefn.getSelector()); //$NON-NLS-1$
		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.GRID_ITEM);
		assertEquals("grid", elementDefn.getSelector()); //$NON-NLS-1$
		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.FREE_FORM_ITEM);
		assertEquals("free-form", elementDefn.getSelector()); //$NON-NLS-1$
		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.LINE_ITEM);
		assertEquals("line", elementDefn.getSelector()); //$NON-NLS-1$
		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.RECTANGLE_ITEM);
		assertEquals("rectangle", elementDefn.getSelector()); //$NON-NLS-1$

		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.LIST_ITEM);
		assertEquals("list", elementDefn.getSelector()); //$NON-NLS-1$
		assertEquals("list-header", ((SlotDefn) elementDefn.getSlot(IListingElementModel.HEADER_SLOT)).getSelector()); //$NON-NLS-1$
		assertEquals("list-footer", ((SlotDefn) elementDefn.getSlot(IListingElementModel.FOOTER_SLOT)).getSelector()); //$NON-NLS-1$
		assertEquals("list-detail", ((SlotDefn) elementDefn.getSlot(IListingElementModel.DETAIL_SLOT)).getSelector()); //$NON-NLS-1$

		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.LIST_GROUP_ELEMENT);
		assertEquals(null, elementDefn.getSelector());
		assertEquals("list-group-header", //$NON-NLS-1$
				((SlotDefn) elementDefn.getSlot(IGroupElementModel.HEADER_SLOT)).getSelector());
		assertEquals("list-group-footer", //$NON-NLS-1$
				((SlotDefn) elementDefn.getSlot(IGroupElementModel.FOOTER_SLOT)).getSelector());

		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.TABLE_ITEM);
		assertEquals("table", elementDefn.getSelector()); //$NON-NLS-1$
		assertEquals("table-header", ((SlotDefn) elementDefn.getSlot(IListingElementModel.HEADER_SLOT)).getSelector()); //$NON-NLS-1$
		assertEquals("table-footer", ((SlotDefn) elementDefn.getSlot(IListingElementModel.FOOTER_SLOT)).getSelector()); //$NON-NLS-1$
		assertEquals("table-detail", ((SlotDefn) elementDefn.getSlot(IListingElementModel.DETAIL_SLOT)).getSelector()); //$NON-NLS-1$

		elementDefn = (ElementDefn) dd.getElement(ReportDesignConstants.TABLE_GROUP_ELEMENT);
		assertEquals(null, elementDefn.getSelector());
		assertEquals("table-group-header", //$NON-NLS-1$
				((SlotDefn) elementDefn.getSlot(IGroupElementModel.HEADER_SLOT)).getSelector());
		assertEquals("table-group-footer", //$NON-NLS-1$
				((SlotDefn) elementDefn.getSlot(IGroupElementModel.FOOTER_SLOT)).getSelector());
	}

	/**
	 * Test getting style property with different level.
	 *
	 * @throws Exception any exception caught
	 */

	public void testPropertySearchWithLevel() throws Exception {
		openDesign(fileName);
		TableItem table = (TableItem) design.findElement("My Ninth Table"); //$NON-NLS-1$
		assertNotNull(table);

		// Test group level one

		TableGroup group = (TableGroup) (table.getSlot(IListingElementModel.GROUP_SLOT).getContent(0));
		TableRow row = (TableRow) (group.getSlot(IListingElementModel.HEADER_SLOT).getContent(0));
		assertEquals("center", row.getStringProperty(design, IStyleModel.TEXT_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("center", row.getFactoryProperty(design, //$NON-NLS-1$
				IStyleModel.TEXT_ALIGN_PROP));

		// Test group level two

		group = (TableGroup) (table.getSlot(IListingElementModel.GROUP_SLOT).getContent(1));
		row = (TableRow) (group.getSlot(IListingElementModel.HEADER_SLOT).getContent(0));
		assertEquals("right", row.getStringProperty(design, IStyleModel.TEXT_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("right", row //$NON-NLS-1$
				.getFactoryProperty(design, IStyleModel.TEXT_ALIGN_PROP));

		// Test group level nine

		group = (TableGroup) (table.getSlot(IListingElementModel.GROUP_SLOT).getContent(8));
		row = (TableRow) (group.getSlot(IListingElementModel.HEADER_SLOT).getContent(0));
		assertEquals("center", row.getStringProperty(design, IStyleModel.TEXT_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("center", row //$NON-NLS-1$
				.getFactoryProperty(design, IStyleModel.TEXT_ALIGN_PROP));

		// Test group level ten

		group = (TableGroup) (table.getSlot(IListingElementModel.GROUP_SLOT).getContent(9));
		row = (TableRow) (group.getSlot(IListingElementModel.HEADER_SLOT).getContent(0));
		assertEquals("center", row.getStringProperty(design, IStyleModel.TEXT_ALIGN_PROP)); //$NON-NLS-1$
		assertEquals("center", row //$NON-NLS-1$
				.getFactoryProperty(design, IStyleModel.TEXT_ALIGN_PROP));
	}

	/**
	 * Get the member value of highlight rule to see if they are identical to those
	 * specified in the input file.
	 *
	 * @throws Exception any exception during writing the design file.
	 */

	public void testReadHighlightRules() throws Exception {
		openDesign("StyleParseTest_1.xml"); //$NON-NLS-1$

		StyleHandle styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		Iterator<?> highlightHandles = styleHandle.highlightRulesIterator();
		assertNotNull(highlightHandles);

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);

		// isDesignTime
		assertFalse(highlightHandle.isDesignTime());

		assertEquals("[this]", highlightHandle.getTestExpression()); //$NON-NLS-1$

		ColorHandle colorHandle = highlightHandle.getColor();
		assertEquals("blue", colorHandle.getCssValue()); //$NON-NLS-1$

		colorHandle = highlightHandle.getBackgroundColor();
		assertNotNull(colorHandle);
		assertEquals("white", colorHandle.getCssValue()); //$NON-NLS-1$

		colorHandle = highlightHandle.getColor();
		assertEquals("blue", colorHandle.getCssValue()); //$NON-NLS-1$

		colorHandle = highlightHandle.getBorderBottomColor();
		assertEquals("black", colorHandle.getCssValue()); //$NON-NLS-1$
		colorHandle = highlightHandle.getBorderLeftColor();
		assertEquals("black", colorHandle.getCssValue()); //$NON-NLS-1$

		colorHandle = highlightHandle.getBorderRightColor();
		assertEquals("black", colorHandle.getCssValue()); //$NON-NLS-1$

		colorHandle = highlightHandle.getBorderTopColor();
		assertEquals("black", colorHandle.getCssValue()); //$NON-NLS-1$

		String borderStyle = highlightHandle.getBorderBottomStyle();
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, borderStyle);
		borderStyle = highlightHandle.getBorderLeftStyle();
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, borderStyle);

		borderStyle = highlightHandle.getBorderRightStyle();
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, borderStyle);

		borderStyle = highlightHandle.getBorderTopStyle();
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, borderStyle);

		String operator = highlightHandle.getOperator();
		assertEquals(DesignChoiceConstants.MAP_OPERATOR_EQ, operator);

		String value1 = highlightHandle.getValue1();
		assertEquals("\"10\"", value1); //$NON-NLS-1$

		String value2 = highlightHandle.getValue2();
		assertEquals("\"20\"", value2); //$NON-NLS-1$

		String textValue = highlightHandle.getTextAlign();
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_RIGHT, textValue);

		textValue = highlightHandle.getTextLineThrough();
		assertEquals(DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH, textValue);

		textValue = highlightHandle.getTextOverline();
		assertEquals(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE, textValue);

		textValue = highlightHandle.getTextUnderline();
		assertEquals(DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE, textValue);

		textValue = highlightHandle.getTextTransform();
		assertEquals(DesignChoiceConstants.TRANSFORM_LOWERCASE, textValue);

		assertEquals("#.00", highlightHandle.getNumberFormat()); //$NON-NLS-1$
		assertEquals("yyyy/mm/dd", highlightHandle.getDateTimeFormat()); //$NON-NLS-1$

		assertEquals("string-format", highlightHandle.getStringFormat()); //$NON-NLS-1$

		DimensionHandle dimensionHandle = highlightHandle.getBorderBottomWidth();
		assertEquals(DesignChoiceConstants.LINE_WIDTH_THIN, dimensionHandle.getStringValue());

		dimensionHandle = highlightHandle.getBorderLeftWidth();
		assertEquals(DesignChoiceConstants.LINE_WIDTH_THIN, dimensionHandle.getStringValue());

		dimensionHandle = highlightHandle.getBorderTopWidth();
		assertEquals(DesignChoiceConstants.LINE_WIDTH_THIN, dimensionHandle.getStringValue());

		dimensionHandle = highlightHandle.getBorderRightWidth();
		assertEquals(DesignChoiceConstants.LINE_WIDTH_THIN, dimensionHandle.getStringValue());

		FontHandle fontHandle = highlightHandle.getFontFamilyHandle();
		assertEquals("\"Arial\"", fontHandle.getStringValue()); //$NON-NLS-1$

		String fontValue = highlightHandle.getFontStyle();
		assertEquals(DesignChoiceConstants.FONT_STYLE_NORMAL, fontValue);

		fontValue = highlightHandle.getFontVariant();
		assertEquals(DesignChoiceConstants.FONT_VARIANT_NORMAL, fontValue);

		fontValue = highlightHandle.getFontWeight();
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_BOLD, fontValue);

		dimensionHandle = highlightHandle.getFontSize();
		assertEquals("9pt", dimensionHandle.getStringValue()); //$NON-NLS-1$

		dimensionHandle = highlightHandle.getTextIndent();
		assertEquals("1pc", dimensionHandle.getStringValue()); //$NON-NLS-1$

		// bidi

		assertEquals(DesignChoiceConstants.BIDI_DIRECTION_RTL, highlightHandle.getTextDirection());

		// the second highlight rule

		highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);
		assertEquals("[this]", highlightHandle.getTestExpression()); //$NON-NLS-1$

		colorHandle = highlightHandle.getColor();
		assertEquals("blue", colorHandle.getCssValue()); //$NON-NLS-1$

		dimensionHandle = highlightHandle.getFontSize();
		assertEquals("5pc", dimensionHandle.getStringValue()); //$NON-NLS-1$

		dimensionHandle = highlightHandle.getTextIndent();
		assertNull(dimensionHandle.getStringValue());

		assertNull(highlightHandle.getStyle());
		assertEquals("nonExistedStyle", highlightHandle //$NON-NLS-1$
				.getProperty(HighlightRule.STYLE_MEMBER));
		highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNull(highlightHandle);

	}

	/**
	 * Test property search with Session Default.
	 *
	 * @throws DesignFileException
	 * @throws PropertyValueException
	 */
	public void testSessionDefault() throws DesignFileException, PropertyValueException {
		openDesign(fileName);

		FreeForm form = (FreeForm) design.findElement("My Sixth Form"); //$NON-NLS-1$
		assertNotNull(form);

		design.getSession().setDefaultValue(IStyleModel.BORDER_BOTTOM_COLOR_PROP, "#0000ff"); //$NON-NLS-1$

		// Session default value

		assertEquals("#0000FF", form.getStringProperty(design, IStyleModel.BORDER_BOTTOM_COLOR_PROP).toString()); //$NON-NLS-1$

		// Metadata default value

		assertEquals("black", form.getStringProperty(design, IStyleModel.BORDER_TOP_COLOR_PROP).toString()); //$NON-NLS-1$

		// Remove session default value
		design.getSession().setDefaultValue(IStyleModel.BORDER_BOTTOM_COLOR_PROP, null);
		assertEquals("black", form.getStringProperty(design, IStyleModel.BORDER_BOTTOM_COLOR_PROP).toString()); //$NON-NLS-1$
	}

	/**
	 * Tests getting property from selector.
	 *
	 * @throws Exception if any exception
	 */

	public void testPropertyFromSelector() throws Exception {
		openDesign(fileName);

		LabelHandle label = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		String bkColor = label.getStringProperty(IStyleModel.BACKGROUND_COLOR_PROP);
		assertEquals("gray", bkColor); //$NON-NLS-1$
	}

	/**
	 * Tests to open and save the old design file.
	 *
	 * @throws Exception
	 */

	public void testOpenSaveObsoleteFile() throws Exception {
		openDesign("StyleParseTest_obsolete.xml"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("StyleParseTest_obsolete_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the validation for font-family.
	 *
	 * @throws Exception
	 */
	public void testFontFamily() throws Exception {
		openDesign(fileName);
		StyleHandle style = designHandle.getElementFactory().newStyle(null);
		designHandle.getStyles().add(style);

		// general family
		String value = DesignChoiceConstants.FONT_FAMILY_CURSIVE;
		style.getFontFamilyHandle().setValue(value);
		assertEquals(DesignChoiceConstants.FONT_FAMILY_CURSIVE, style.getStringProperty(IStyleModel.FONT_FAMILY_PROP));

		// custom family
		value = "a b"; //$NON-NLS-1$
		style.getFontFamilyHandle().setValue(value);
		assertEquals("\"a b\"", style.getStringProperty(IStyleModel.FONT_FAMILY_PROP)); //$NON-NLS-1$

		value = " a b, " + "\"cd\" ," + DesignChoiceConstants.FONT_FAMILY_FANTASY; //$NON-NLS-1$//$NON-NLS-2$
		style.getFontFamilyHandle().setValue(value);
		assertEquals("\"a b\", \"cd\", " + DesignChoiceConstants.FONT_FAMILY_FANTASY, //$NON-NLS-1$
				style.getStringProperty(IStyleModel.FONT_FAMILY_PROP));
	}

	/**
	 * Tests the 'style' in toc or highlight rule.
	 *
	 * @throws Exception
	 */
	public void testTOC() throws Exception {
		openDesign(fileName);
		LabelHandle label = designHandle.getElementFactory().newLabel(null);
		designHandle.getBody().add(label);

		label.setTocExpression("label toc expression"); //$NON-NLS-1$
		TOCHandle tocHandle = label.getTOC();

		CommandStack stack = designHandle.getCommandStack();
		stack.startTrans(null);
		StyleHandle style = designHandle.getElementFactory().newStyle("newTocStyle"); //$NON-NLS-1$
		designHandle.getStyles().add(style);
		tocHandle.setStyleName(style.getName());
		stack.commit();

		assertEquals(style.getName(), tocHandle.getStyleName());
		stack.undo();

		assertNull(tocHandle.getStyleName());

		// add style and set toc style
		designHandle.getStyles().add(style);
		tocHandle.setStyleName(style.getName());
		assertEquals(1, ((Style) style.getElement()).getClientList().size());

		stack.undo();
		assertEquals(0, ((Style) style.getElement()).getClientList().size());
	}
}
