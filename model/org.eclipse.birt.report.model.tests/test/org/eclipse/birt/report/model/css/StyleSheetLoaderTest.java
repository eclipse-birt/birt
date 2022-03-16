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

package org.eclipse.birt.report.model.css;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetParserException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.CssPropertyConstants;

import com.ibm.icu.util.ULocale;

/**
 * Tests the function of the style sheet loader.
 */

public class StyleSheetLoaderTest extends BaseTestCase {

	private String fileName = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle((ULocale) null);
		designHandle = session.createDesign();
	}

	private CssStyleSheetHandle loadStyleSheet(String fileName) throws Exception {
		fileName = INPUT_FOLDER + fileName;
		InputStream is = getResourceAStream(fileName);
		return designHandle.openCssStyleSheet(is);
	}

	/**
	 * Tests get css style sheet's file name when load css file.
	 *
	 * @throws Exception
	 */

	public void testCssStyleSheetFileName() throws Exception {
		openDesign("BlankStyleSheetLoaderTest.xml"); //$NON-NLS-1$

		fileName = "base.css"; //$NON-NLS-1$
		CssStyleSheetHandle sheetHandle = designHandle.openCssStyleSheet(fileName);
		assertEquals("base.css", sheetHandle.getFileName()); //$NON-NLS-1$

	}

	/**
	 * Tests a normal input css file, and all the input is loaded into the report.
	 *
	 * @throws Exception
	 */

	public void testParserForAllProperties() throws Exception {
		fileName = "base.css"; //$NON-NLS-1$

		Iterator<StyleHandle> styles = loadStyleSheet(fileName).getStyleIterator();

		StyleHandle style1 = styles.next();
		StyleHandle style2 = styles.next();
		StyleHandle style3 = styles.next();
		assertFalse(styles.hasNext());

		assertEquals("fullstyle", style1.getName()); //$NON-NLS-1$
		assertEquals("table", style2.getName()); //$NON-NLS-1$

		// font

		assertEquals("\"Bitstream Vera Sans\", \"Tahoma\", \"Verdana\", \"Myriad Web\", \"Syntax\", sans-serif", //$NON-NLS-1$
				style1.getFontFamilyHandle().getStringValue());
		assertEquals("2em", style1.getFontSize().getStringValue());//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FONT_STYLE_ITALIC, style1.getFontStyle());
		assertEquals(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS, style1.getFontVariant());
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_BOLD, style1.getFontWeight());

		// text
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_JUSTIFY, style1.getTextAlign());
		assertEquals("2em", style1.getTextIndent().getStringValue());//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE, style1.getTextUnderline());
		assertEquals(DesignChoiceConstants.TEXT_OVERLINE_OVERLINE, style1.getTextOverline());
		assertEquals(DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH, style1.getTextLineThrough());
		assertEquals("1pt", style1.getLetterSpacing().getStringValue());//$NON-NLS-1$
		assertEquals("2pt", style1.getWordSpacing().getStringValue());//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.TRANSFORM_UPPERCASE, style1.getTextTransform());
		assertEquals(DesignChoiceConstants.WHITE_SPACE_PRE, style1.getWhiteSpace());

		// margin
		assertEquals("1em", style1.getMarginBottom().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style1.getMarginLeft().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style1.getMarginRight().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style1.getMarginTop().getStringValue());//$NON-NLS-1$

		// padding
		assertEquals("1em", style1.getPaddingBottom().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style1.getPaddingLeft().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style1.getPaddingRight().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style1.getPaddingTop().getStringValue());//$NON-NLS-1$

		// background

		// color is rgb( 0, 79, 147)
		assertEquals("#004F93", style1.getColor().getStringValue());//$NON-NLS-1$
		assertEquals(IColorConstants.BLACK, style1.getBackgroundColor().getStringValue());
		assertEquals("images/header", style1.getBackgroundImage()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.BACKGROUND_REPEAT_NO_REPEAT, style1.getBackgroundRepeat());
		assertEquals(DesignChoiceConstants.BACKGROUND_ATTACHMENT_SCROLL, style1.getBackgroundAttachment());
		assertEquals("50%", style1.getBackGroundPositionX().getStringValue()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.BACKGROUND_POSITION_TOP, style1.getBackGroundPositionY().getStringValue());

		// page meida

		assertEquals("1", style1.getOrphans());//$NON-NLS-1$
		assertEquals("3", style1.getWidows());//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.DISPLAY_INLINE, style1.getDisplay());
		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, style1.getPageBreakBefore());

		assertEquals(DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS, style1.getPageBreakAfter());
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO, style1.getPageBreakInside());

		// visual

		assertEquals(DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE, style1.getVerticalAlign());
		assertEquals("120%", style1.getLineHeight().getStringValue());//$NON-NLS-1$

		// border

		assertEquals("#445566", style1.getBorderBottomColor().getStringValue());//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, style1.getBorderBottomStyle());
		assertEquals("10px", style1.getBorderBottomWidth().getDisplayValue());//$NON-NLS-1$

		assertEquals("test", style3.getName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.BACKGROUND_SIZE_CONTAIN, style3.getBackgroundSizeWidth().getStringValue());

		assertEquals(DesignChoiceConstants.BACKGROUND_SIZE_AUTO, style3.getBackgroundSizeHeight().getStringValue());

	}

	/**
	 * @throws Exception
	 */

	public void testStyleIterator() throws Exception {
		fileName = "base.css"; //$NON-NLS-1$
		CssStyleSheetHandle styleSheetHandle = loadStyleSheet(fileName);
		assertNotNull(styleSheetHandle);

		Iterator<StyleHandle> iter = styleSheetHandle.getStyleIterator();
		int i = 0;
		while (iter.hasNext()) {
			SharedStyleHandle styleHandle = (SharedStyleHandle) iter.next();
			switch (i++) {
			case 0:
				assertEquals("fullstyle", styleHandle.getName()); //$NON-NLS-1$
				break;
			case 1:
				assertEquals("table", styleHandle.getName()); //$NON-NLS-1$
				break;

			}
		}
	}

	/**
	 * Tests all the right input is loaded into the report.
	 *
	 * @throws Exception
	 */

	public void testStyleIterator1() throws Exception {
		fileName = "base1.css"; //$NON-NLS-1$
		CssStyleSheetHandle styleSheetHandle = loadStyleSheet(fileName);
		assertNotNull(styleSheetHandle);

		Iterator<StyleHandle> iter = styleSheetHandle.getStyleIterator();
		int i = 0;
		while (iter.hasNext()) {
			SharedStyleHandle styleHandle = (SharedStyleHandle) iter.next();
			switch (i++) {
			case 0:
				assertEquals("test4", styleHandle.getName()); //$NON-NLS-1$
				break;
			case 1:
				assertEquals("test6", styleHandle.getName()); //$NON-NLS-1$
				break;

			}
		}
	}

	/**
	 * Tests a css file with wrong at rule key word. The parser will ignore it and
	 * parse on.
	 *
	 * @throws Exception
	 */

	public void testWrongAtKeyWord() throws Exception {
		fileName = "wrong_1.css"; //$NON-NLS-1$
		Iterator<StyleHandle> styles = loadStyleSheet(fileName).getStyleIterator();
		assertNotNull(styles.next());
		assertFalse(styles.hasNext());
	}

	/**
	 * Tests a css file with wrong selector. The parser will ignore the entire
	 * selector and its declaration and parse on.
	 *
	 * @throws Exception
	 */

	public void testWrongSelector() throws Exception {
		fileName = "wrong_2.css"; //$NON-NLS-1$
		Iterator<StyleHandle> styles = loadStyleSheet(fileName).getStyleIterator();
		assertNotNull(styles.next());
		assertFalse(styles.hasNext());
	}

	/**
	 * @throws Exception
	 */
	public void testPropertyCombination() throws Exception {
		fileName = "property_combination.css"; //$NON-NLS-1$
		Iterator<StyleHandle> styles = loadStyleSheet(fileName).getStyleIterator();

		isSame(styles, IStyleModel.FONT_FAMILY_PROP,
				"\"Bitstream Vera Sans\", \"Tahoma\", \"Verdana\", \"Myriad Web\", \"Syntax\", sans-serif"); //$NON-NLS-1$
		isSame(styles, IStyleModel.FONT_SIZE_PROP, "2em"); //$NON-NLS-1$
		isSame(styles, IStyleModel.FONT_STYLE_PROP, DesignChoiceConstants.FONT_STYLE_ITALIC);
		isSame(styles, IStyleModel.FONT_VARIANT_PROP, DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS);
		isSame(styles, IStyleModel.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
	}

	/**
	 * @throws Exception
	 */
	public void testShortHand() throws Exception {
		fileName = "property_shorthand.css"; //$NON-NLS-1$
		Iterator<StyleHandle> styles = loadStyleSheet(fileName).getStyleIterator();
		StyleHandle style = styles.next();
		assertEquals("table", style.getName()); //$NON-NLS-1$

		assertFalse(styles.hasNext());

		// font
		assertEquals("\"Bitstream Vera Sans\", \"Tahoma\", \"Verdana\", \"Myriad Web\", \"Syntax\", sans-serif", //$NON-NLS-1$
				style.getFontFamilyHandle().getStringValue());
		assertEquals("2em", style.getFontSize().getStringValue());//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FONT_STYLE_ITALIC, style.getFontStyle());
		assertEquals(DesignChoiceConstants.FONT_VARIANT_SMALL_CAPS, style.getFontVariant());
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_BOLD, style.getFontWeight());

		// margin

		assertEquals("1em", style.getMarginBottom().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style.getMarginLeft().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style.getMarginRight().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style.getMarginTop().getStringValue());//$NON-NLS-1$

		// padding

		assertEquals("1em", style.getPaddingBottom().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style.getPaddingLeft().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style.getPaddingRight().getStringValue());//$NON-NLS-1$
		assertEquals("1em", style.getPaddingTop().getStringValue());//$NON-NLS-1$

		// background

		assertEquals(IColorConstants.BLACK, style.getBackgroundColor().getStringValue());
		assertEquals("images/header", style.getBackgroundImage()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.BACKGROUND_REPEAT_NO_REPEAT, style.getBackgroundRepeat());
		assertEquals(DesignChoiceConstants.BACKGROUND_ATTACHMENT_SCROLL, style.getBackgroundAttachment());
		assertEquals("50%", style.getBackGroundPositionX().getStringValue()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.BACKGROUND_POSITION_TOP, style.getBackGroundPositionY().getStringValue());

		// visual

		assertEquals("120%", style.getLineHeight().getStringValue());//$NON-NLS-1$

		// border

		assertEquals("#445566", style.getBorderBottomColor().getStringValue());//$NON-NLS-1$
		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, style.getBorderBottomStyle());
		assertEquals("10px", style.getBorderBottomWidth().getStringValue());//$NON-NLS-1$

	}

	/**
	 * Tests warnings related.
	 *
	 * @throws Exception
	 */

	public void testWarnings() throws Exception {
		fileName = "wrong.css"; //$NON-NLS-1$
		CssStyleSheetHandle styleSheet = loadStyleSheet(fileName);
		Iterator<StyleHandle> styles = styleSheet.getStyleIterator();

		StyleHandle style = styles.next();
		assertEquals("fullstyle", style.getName()); //$NON-NLS-1$

		List<StyleSheetParserException> errors = styleSheet.getWarnings(style.getName());
		assertEquals(4, errors.size());
		StyleSheetParserException e;
		e = errors.get(0);
		assertEquals(StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE, e.getErrorCode());
		assertEquals(CssPropertyConstants.ATTR_FONT, e.getCSSPropertyName());
		assertEquals("2em small-caps \"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax, sans-serif", //$NON-NLS-1$
				e.getCSSValue());
		e = errors.get(1);
		assertEquals(StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE, e.getErrorCode());
		assertEquals(CssPropertyConstants.ATTR_BACKGROUND_IMAGE, e.getCSSPropertyName());
		assertEquals("uattr(images) / attr(header))", e.getCSSValue()); //$NON-NLS-1$

		e = errors.get(2);
		assertEquals(StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE, e.getErrorCode());
		assertEquals(CssPropertyConstants.ATTR_BACKGROUND_SIZE, e.getCSSPropertyName());
		assertEquals("test1", e.getCSSValue()); //$NON-NLS-1$

		e = errors.get(3);
		assertEquals(StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE, e.getErrorCode());
		assertEquals(CssPropertyConstants.ATTR_BACKGROUND_SIZE, e.getCSSPropertyName());
		assertEquals("test2", e.getCSSValue()); //$NON-NLS-1$

		style = styles.next();
		assertEquals("table", style.getName()); //$NON-NLS-1$
		errors = styleSheet.getWarnings(style.getName());
		assertEquals(5, errors.size());
		e = errors.get(4);
		assertEquals(StyleSheetParserException.DESIGN_EXCEPTION_PROPERTY_NOT_SUPPORTED, e.getErrorCode());
		assertEquals("wrongproperty", e.getCSSPropertyName()); //$NON-NLS-1$
		assertEquals("value", e.getCSSValue()); //$NON-NLS-1$

		List<String> unsupportedStyles = styleSheet.getUnsupportedStyles();
		assertEquals("table:link", unsupportedStyles.get(0)); //$NON-NLS-1$

	}

	/**
	 * Tests warnings related.
	 *
	 * @throws Exception
	 */

	public void testParserErrors() throws Exception {
		fileName = "wrong_3.css"; //$NON-NLS-1$
		CssStyleSheetHandle styleSheet = loadStyleSheet(fileName);

		assertEquals(1, styleSheet.getParserErrors().size());
		assertEquals(
				"[11:1] encountered \"_\". Was expecting one of: \"{\" \",\" \"[\" \".\" \":\" <HASH> \"+\" \">\" ", //$NON-NLS-1$
				styleSheet.getParserErrors().get(0));
		assertEquals(0, styleSheet.getParserFatalErrors().size());
		assertEquals(0, styleSheet.getParserWarnings().size());

	}

	/**
	 * Tests a group styles has a same property with a given property name. Each one
	 * in the list is instance of <code>StyleHandle</code>.
	 *
	 * @param styles
	 * @param propName
	 * @param value
	 */

	private void isSame(Iterator<StyleHandle> styles, String propName, Object value) {
		for (; styles.hasNext();) {
			StyleHandle style = styles.next();
			assertEquals(value, style.getStringProperty(propName));
		}
	}
}
