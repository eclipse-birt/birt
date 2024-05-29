/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html;

import java.util.Stack;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTValueConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.emitter.HTMLWriter;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.util.ULocale;

/**
 *
 */

public abstract class HTMLEmitter {

	protected HTMLReportEmitter reportEmitter;
	protected HTMLWriter writer;
	protected boolean fixedReport = false;
	protected boolean enableInlineStyle = false;
	protected int browserVersion = -1;

	/**
	 * The <code>containerDisplayStack</code> that stores the display value of
	 * container.
	 */
	protected Stack<Integer> containerDisplayStack = new Stack<>();
	private static final DecimalFormat FORMATTER = new DecimalFormat("#.###", //$NON-NLS-1$
			new DecimalFormatSymbols(ULocale.ENGLISH));

	/**
	 * HTML emitter
	 *
	 * @param reportEmitter     report emitter
	 * @param writer            HTML writer
	 * @param fixedReport       fixed report
	 * @param enableInlineStyle enabled inline style
	 * @param browserVersion    browser version
	 */
	public HTMLEmitter(HTMLReportEmitter reportEmitter, HTMLWriter writer, boolean fixedReport,
			boolean enableInlineStyle, int browserVersion) {
		this.reportEmitter = reportEmitter;
		this.writer = writer;
		this.fixedReport = fixedReport;
		this.enableInlineStyle = enableInlineStyle;
		this.browserVersion = browserVersion;
	}

	// FIXME: code review: We shouldn't pass the style directly. We should pass
	// the element and get the style form the element in the method.
	/**
	 * Build default style
	 *
	 * @param styleBuffer style buffer
	 * @param style       style
	 */
	public abstract void buildDefaultStyle(StringBuffer styleBuffer, IStyle style);

	/**
	 * Build style
	 *
	 * @param styleBuffer style buffer
	 * @param style       style
	 */
	public abstract void buildStyle(StringBuffer styleBuffer, IStyle style);

	/**
	 * Build page band style
	 *
	 * @param styleBuffer style buffer
	 * @param style       style
	 */
	public abstract void buildPageBandStyle(StringBuffer styleBuffer, IStyle style);

	/**
	 * Build table style
	 *
	 * @param table       table
	 * @param styleBuffer style buffer
	 */
	public abstract void buildTableStyle(ITableContent table, StringBuffer styleBuffer);

	/**
	 * Build column style
	 *
	 * @param column      column
	 * @param styleBuffer style buffer
	 */
	public abstract void buildColumnStyle(IColumn column, StringBuffer styleBuffer);

	/**
	 * Build column alignment
	 *
	 * @param column column
	 */
	public abstract void handleColumnAlign(IColumn column);

	/**
	 * Build row style
	 *
	 * @param row         row
	 * @param styleBuffer style buffer
	 */
	public abstract void buildRowStyle(IRowContent row, StringBuffer styleBuffer);

	/**
	 * Build row alignment
	 *
	 * @param row row
	 */
	public abstract void handleRowAlign(IRowContent row);

	/**
	 * Build cell style
	 *
	 * @param cell            cell content
	 * @param styleBuffer     style buffer
	 * @param isHead          is head
	 * @param fixedCellHeight fixed cell height
	 */
	public abstract void buildCellStyle(ICellContent cell, StringBuffer styleBuffer, boolean isHead,
			boolean fixedCellHeight);

	/**
	 * Handles the text align property of the element content.
	 *
	 * @param cell cell content
	 */
	public void handleCellAlign(ICellContent cell) {
		// The method getStyle( ) will nevel return a null value;
		IStyle style = cell.getStyle();

		// Build the Text-Align property.
		CSSValue hAlign = style.getProperty(StyleConstants.STYLE_TEXT_ALIGN);
		if (null != hAlign) {
			writer.attribute(HTMLTags.ATTR_ALIGN, hAlign.getCssText());
		}
	}

	/**
	 * Handle cell valignment
	 *
	 * @param cell cell content
	 */
	public abstract void handleCellVAlign(ICellContent cell);

	/**
	 * Build container style
	 *
	 * @param container
	 * @param styleBuffer style buffer
	 */
	public abstract void buildContainerStyle(IContainerContent container, StringBuffer styleBuffer);

	/**
	 * Handle container alignment
	 *
	 * @param container container content
	 */
	public abstract void handleContainerAlign(IContainerContent container);

	// FIXME: code review: Because the display has already been calculated in
	// the HTMLReportEmitter, so we can build the display there too. We needn't
	// pass the display here.
	/**
	 * Build text style
	 *
	 * @param text        text content
	 * @param styleBuffer style buffer
	 * @param display     display
	 */
	public abstract void buildTextStyle(ITextContent text, StringBuffer styleBuffer, int display);

	/**
	 * Build foreign style
	 *
	 * @param foreign     foreign content
	 * @param styleBuffer style buffer
	 * @param display     display
	 */
	public abstract void buildForeignStyle(IForeignContent foreign, StringBuffer styleBuffer, int display);

	/**
	 * Build image style
	 *
	 * @param image       image content
	 * @param styleBuffer style buffer
	 * @param display     display
	 */
	public abstract void buildImageStyle(IImageContent image, StringBuffer styleBuffer, int display);

	/**
	 * Build the style of the page
	 *
	 * @param page                     page content
	 * @param styleBuffer              style buffer
	 * @param needOutputBackgroundSize need output background size
	 */
	public void buildPageStyle(IPageContent page, StringBuffer styleBuffer, boolean needOutputBackgroundSize) {
		// The method getStyle( ) will nevel return a null value;
		IStyle style = page.getStyle();
		if (!needOutputBackgroundSize) {
			DimensionType[] pageSize = { page.getPageHeight(), page.getPageWidth() };
			AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, pageSize);
		} else {
			AttributeBuilder.buildBackgroundColor(styleBuffer, style, reportEmitter);
		}
		AttributeBuilder.buildBorders(styleBuffer, style);
	}

	/**
	 * Build size style string say, "width: 10.0mm;". The min-height should be
	 * implemented by sepcial way.
	 *
	 * @param content The <code>StringBuffer</code> to which the result is output.
	 * @param name    The property name
	 * @param value   The values of the property
	 */
	public void buildSize(StringBuffer content, String name, DimensionType value) {
		if (value != null) {
			boolean percentageUnit = value.getUnits().equals(DesignChoiceConstants.UNITS_PERCENTAGE);
			String size = formatSize(value);
			if (HTMLTags.ATTR_MIN_HEIGHT.equals(name)) {
				// Percentage unit cannot use auto
				if (fixedReport || percentageUnit) {
					content.append(" height: "); //$NON-NLS-1$
					content.append(size);
					content.append(';');
				} else {
					// To solve the problem that IE do not support min-height.
					// Use this way to make Firefox and IE both work well.
					content.append(" height: auto !important; height: "); //$NON-NLS-1$
					content.append(size);
					content.append("; min-height: "); //$NON-NLS-1$
					content.append(size);
					content.append(';');
				}
			} else if (HTMLTags.ATTR_MIN_WIDTH.equals(name)) {
				// Percentage unit cannot use auto
				if (fixedReport || percentageUnit) {
					content.append(" width: "); //$NON-NLS-1$
					content.append(size);
					content.append(';');
				} else {
					content.append(" width: auto !important; width: "); //$NON-NLS-1$
					content.append(size);
					content.append("; min-width: "); //$NON-NLS-1$
					content.append(size);
					content.append(';');
				}
			} else {
				content.append(' ');
				content.append(name);
				content.append(": "); //$NON-NLS-1$
				content.append(size);
				content.append(';');
			}
		}
	}

	/**
	 * Convert the dimension type value into a string. The returned value has
	 * "#.###" format.
	 *
	 * @param value
	 * @return Return the converted dimension type value into a string.
	 */
	private String formatSize(DimensionType value) {
		assert value != null;
		if (value.getValueType() == DimensionType.TYPE_DIMENSION) {
			return FORMATTER.format(value.getMeasure()) + value.getUnits();
		}
		return value.toString();
	}

	protected IStyle getElementStyle(IContent content) {
		IStyle style = null;
		if (enableInlineStyle) {
			style = content.getStyle();
		} else {
			style = content.getInlineStyle();
		}
		if (style == null || style.isEmpty()) {
			return null;
		}
		return style;
	}

	// FIXME: code review: We should remove all the codes about the x and y.
	// BIRT doesn't supoort the x and y now.
	/**
	 * Checks whether the element is block, inline or inline-block level. In BIRT,
	 * the absolute positioning model is used and a box is explicitly offset with
	 * respect to its containing block. When an element's x or y is set, it will be
	 * treated as a block level element regardless of the 'Display' property set in
	 * style. When designating width or height value to an inline element, or when
	 * the element has right-to-left base direction, it it will be treated as
	 * inline-block.
	 *
	 * @param x      Specifies how far a box's left margin edge is offset to the
	 *               right of the left edge of the box's containing block.
	 * @param y      Specifies how far an absolutely positioned box's top margin
	 *               edge is offset below the top edge of the box's containing
	 *               block.
	 * @param width  The width of the element.
	 * @param height The height of the element.
	 * @param style  The <code>IStyle</code> object.
	 * @return The display type of the element.
	 */
	public CSSValue getElementDisplay(DimensionType x, DimensionType y, DimensionType width, DimensionType height,
			IStyle style) {
		CSSValue display = null;
		if (style != null) {
			display = style.getProperty(StyleConstants.STYLE_DISPLAY);
		}

		if (CSSValueConstants.NONE_VALUE == display) {
			return CSSValueConstants.NONE_VALUE;
		}

		if (x != null || y != null) {
			return CSSValueConstants.BLOCK_VALUE;
		} else if (CSSValueConstants.INLINE_VALUE == display) {
			if (width != null || height != null) {
				return CSSValueConstants.INLINE_BLOCK_VALUE;
			}
			// RTL text is also treated as inline-block
			else if (CSSConstants.CSS_RTL_VALUE.equals(style.getDirection())) {
				return CSSValueConstants.INLINE_BLOCK_VALUE;
			} else {
				return CSSValueConstants.INLINE_VALUE;
			}

		}
		return CSSValueConstants.BLOCK_VALUE;
	}

	/**
	 * Checks whether the element is block, inline or inline-block level. In BIRT,
	 * the absolute positioning model is used and a box is explicitly offset with
	 * respect to its containing block. When an element's x or y is set, it will be
	 * treated as a block level element regardless of the 'Display' property set in
	 * style. When designating width or height value to an inline element, or when
	 * the element has right-to-left base direction, it it will be treated as
	 * inline-block.
	 *
	 * @param x      Specifies how far a box's left margin edge is offset to the
	 *               right of the left edge of the box's containing block.
	 * @param y      Specifies how far an absolutely positioned box's top margin
	 *               edge is offset below the top edge of the box's containing
	 *               block.
	 * @param width  The width of the element.
	 * @param height The height of the element.
	 * @param style  The <code>IStyle</code> object.
	 * @return The display type of the element.
	 */
	public int getElementType(DimensionType x, DimensionType y, DimensionType width, DimensionType height,
			IStyle style) {
		int type = 0;
		String display = null;
		if (style != null) {
			display = style.getDisplay();
		}

		if (DesignChoiceConstants.DISPLAY_NONE.equalsIgnoreCase(display)) {
			type |= HTMLEmitterUtil.DISPLAY_NONE;
		}

		if (x != null || y != null) {
			return type | HTMLEmitterUtil.DISPLAY_BLOCK;
		} else if (DesignChoiceConstants.DISPLAY_INLINE.equalsIgnoreCase(display)) {
			type |= HTMLEmitterUtil.DISPLAY_INLINE;
			if (width != null || height != null) {
				type |= HTMLEmitterUtil.DISPLAY_INLINE_BLOCK;
			}
			// RTL element is also treated as inline-block
			else if (CSSConstants.CSS_RTL_VALUE.equals(style.getDirection())) {
				type |= HTMLEmitterUtil.DISPLAY_INLINE_BLOCK;
			}
			return type;
		}

		return type | HTMLEmitterUtil.DISPLAY_BLOCK;
	}

	/**
	 * Checks whether the element is block, inline or inline-block level. In BIRT,
	 * the absolute positioning model is used and a box is explicitly offset with
	 * respect to its containing block. When an element's x or y is set, it will be
	 * treated as a block level element regardless of the 'Display' property set in
	 * style. When designating width or height value to an inline element, or when
	 * the element has right-to-left base direction, it will be treated as
	 * inline-block.
	 *
	 * @param x      Specifies how far a box's left margin edge is offset to the
	 *               right of the left edge of the box's containing block.
	 * @param y      Specifies how far an absolutely positioned box's top margin
	 *               edge is offset below the top edge of the box's containing
	 *               block.
	 * @param width  The width of the element.
	 * @param height The height of the element.
	 * @param style  The <code>IStyle</code> object.
	 * @return The display type of the element.
	 */
	public int getTextElementType(DimensionType x, DimensionType y, DimensionType width, DimensionType height,
			IStyle style) {
		int type = 0;
		String display = null;
		if (style != null) {
			display = style.getDisplay();
		}

		if (DesignChoiceConstants.DISPLAY_NONE.equalsIgnoreCase(display)) {
			type |= HTMLEmitterUtil.DISPLAY_NONE;
		}

		if (x != null || y != null) {
			return type | HTMLEmitterUtil.DISPLAY_BLOCK;
		} else if (DesignChoiceConstants.DISPLAY_INLINE.equalsIgnoreCase(display)) {
			type |= HTMLEmitterUtil.DISPLAY_INLINE;
			// Inline text doesn't support height.
			if (width != null) {
				type |= HTMLEmitterUtil.DISPLAY_INLINE_BLOCK;
			}
			// RTL element is also treated as inline-block
			else if (CSSConstants.CSS_RTL_VALUE.equals(style.getDirection())) {
				type |= HTMLEmitterUtil.DISPLAY_INLINE_BLOCK;
			}
			return type;
		}

		return type | HTMLEmitterUtil.DISPLAY_BLOCK;
	}

	/**
	 * adds the default table styles
	 *
	 * @param styleBuffer
	 */
	protected void addDefaultTableStyles(StringBuffer styleBuffer) {
		styleBuffer.append("border-collapse: collapse; empty-cells: show;"); //$NON-NLS-1$
	}

	/**
	 * Checks the 'CanShrink' property and sets the width and height according to
	 * the table below:
	 * <p>
	 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart showing symbol,
	 * location, localized, and meaning.">
	 * <tr bgcolor="#ccccff">
	 * <th align=left>CanShrink</th>
	 * <th align=left>Element Type</th>
	 * <th align=left>Width</th>
	 * <th align=left>Height</th>
	 * </tr>
	 * <tr valign=middle>
	 * <td rowspan="2"><code>true(by default)</code></td>
	 * <td>in-line</td>
	 * <td>ignor</td>
	 * <td>set</td>
	 * </tr>
	 * <tr valign=top bgcolor="#eeeeff">
	 * <td><code>block</code></td>
	 * <td>set</td>
	 * <td>ignor</td>
	 * </tr>
	 * <tr valign=middle>
	 * <td rowspan="2" bgcolor="#eeeeff"><code>false</code></td>
	 * <td>in-line</td>
	 * <td>replaced by 'min-width' property</td>
	 * <td>set</td>
	 * </tr>
	 * <tr valign=top bgcolor="#eeeeff">
	 * <td><code>block</code></td>
	 * <td>set</td>
	 * <td>replaced by 'min-height' property</td>
	 * </tr>
	 * </table>
	 *
	 * @param type        The display type of the element.
	 * @param style       The style of an element.
	 * @param height      The height property.
	 * @param width       The width property.
	 * @param styleBuffer The <code>StringBuffer</code> object that returns 'style'
	 *                    content.
	 * @return A <code>boolean</code> value indicating 'Can-Shrink' property is set
	 *         to <code>true</code> or not.
	 */
//	protected boolean handleShrink( CSSValue display, IStyle style,
//			DimensionType height, DimensionType width, StringBuffer styleBuffer )
//	{
//		boolean canShrink = style != null
//				&& "true".equalsIgnoreCase( style.getCanShrink( ) ); //$NON-NLS-1$
//
//		if ( IStyle.BLOCK_VALUE == display )
//		{
//			buildSize( styleBuffer, HTMLTags.ATTR_WIDTH, width );
//			if ( !canShrink )
//			{
//				buildSize( styleBuffer, HTMLTags.ATTR_MIN_HEIGHT, height );
//			}
//		}
//		else if ( IStyle.INLINE_VALUE == display
//				|| IStyle.INLINE_BLOCK_VALUE == display )
//		{
//			buildSize( styleBuffer, HTMLTags.ATTR_HEIGHT, height );
//			if ( !canShrink )
//			{
//				buildSize( styleBuffer, HTMLTags.ATTR_MIN_WIDTH, width );
//			}
//		}
//
//		return canShrink;
//	}

	/**
	 * Checks the 'CanShrink' property and sets the width and height according to
	 * the table below:
	 * <p>
	 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart showing symbol,
	 * location, localized, and meaning.">
	 * <tr bgcolor="#ccccff">
	 * <th align=left>CanShrink</th>
	 * <th align=left>Element Type</th>
	 * <th align=left>Width</th>
	 * <th align=left>Height</th>
	 * </tr>
	 * <tr valign=middle>
	 * <td rowspan="2"><code>true(by default)</code></td>
	 * <td>in-line</td>
	 * <td>ignor</td>
	 * <td>set</td>
	 * </tr>
	 * <tr valign=top bgcolor="#eeeeff">
	 * <td><code>block</code></td>
	 * <td>set</td>
	 * <td>ignor</td>
	 * </tr>
	 * <tr valign=middle>
	 * <td rowspan="2" bgcolor="#eeeeff"><code>false</code></td>
	 * <td>in-line</td>
	 * <td>replaced by 'min-width' property</td>
	 * <td>set</td>
	 * </tr>
	 * <tr valign=top bgcolor="#eeeeff">
	 * <td><code>block</code></td>
	 * <td>set</td>
	 * <td>replaced by 'min-height' property</td>
	 * </tr>
	 * </table>
	 *
	 * @param type        The display type of the element.
	 * @param style       The style of an element.
	 * @param height      The height property.
	 * @param width       The width property.
	 * @param styleBuffer The <code>StringBuffer</code> object that returns 'style'
	 *                    content.
	 * @return A <code>boolean</code> value indicating 'Can-Shrink' property is set
	 *         to <code>true</code> or not.
	 */
	protected boolean handleShrink(int type, IStyle style, DimensionType height, DimensionType width,
			StringBuffer styleBuffer) {
		boolean canShrink = style != null && "true".equalsIgnoreCase(style.getCanShrink()); //$NON-NLS-1$

		if ((type & HTMLEmitterUtil.DISPLAY_BLOCK) > 0) {
			buildSize(styleBuffer, HTMLTags.ATTR_WIDTH, width);
			if (!canShrink) {
				buildSize(styleBuffer, HTMLTags.ATTR_MIN_HEIGHT, height);
			}
		} else if ((type & HTMLEmitterUtil.DISPLAY_INLINE) > 0) {
			buildSize(styleBuffer, HTMLTags.ATTR_HEIGHT, height);
			if (!canShrink) {
				buildSize(styleBuffer, HTMLTags.ATTR_MIN_WIDTH, width);
			}

		} else {
			assert false;
		}
		HTMLEmitterUtil.buildOverflowStyle(styleBuffer, style, false);
		return canShrink;
	}

	/**
	 * Checks the 'CanShrink' property and sets the width and height according
	 *
	 * @param type        The display type of the element.
	 * @param style       The style of an element.
	 * @param height      The height property.
	 * @param width       The width property.
	 * @param styleBuffer The <code>StringBuffer</code> object that returns 'style'
	 *                    content.
	 * @return A <code>boolean</code> value indicating 'Can-Shrink' property is set
	 *         to <code>true</code> or not.
	 */
	protected boolean handleTextShrink(int type, IStyle style, DimensionType height, DimensionType width,
			StringBuffer styleBuffer) {
		boolean canShrink = style != null && "true".equalsIgnoreCase(style.getCanShrink()); //$NON-NLS-1$
		boolean outputHidden = false;
		if ((type & HTMLEmitterUtil.DISPLAY_BLOCK) > 0) {
			if (width != null) {
				buildSize(styleBuffer, HTMLTags.ATTR_WIDTH, width);
				outputHidden = true;
			}
			if (!canShrink) {
				buildSize(styleBuffer, HTMLTags.ATTR_MIN_HEIGHT, height);
			}
		} else if ((type & HTMLEmitterUtil.DISPLAY_INLINE) > 0) {
			// Inline text doesn't support height. Inline-block text supports height.
			// The user can use line height to implement the height effect of inline text.
			if ((type & HTMLEmitterUtil.DISPLAY_INLINE_BLOCK) > 0) {
				buildSize(styleBuffer, HTMLTags.ATTR_HEIGHT, height);
			}
			if (!canShrink) {
				if (width != null) {
					buildSize(styleBuffer, HTMLTags.ATTR_WIDTH, width);
					outputHidden = true;
				}
			}

		} else {
			assert false;
		}
		HTMLEmitterUtil.buildOverflowStyle(styleBuffer, style, outputHidden);
		return canShrink;
	}

	// FIXME: code review: implement the openContainerTag and closeContainerTag
	// in the HTMLReportEmitter directly.
	/**
	 * Open the container tag.
	 *
	 * @param container container content
	 */
	public void openContainerTag(IContainerContent container) {
		DimensionType x = container.getX();
		DimensionType y = container.getY();
		DimensionType width = container.getWidth();
		DimensionType height = container.getHeight();
		int display = getElementType(x, y, width, height, container.getStyle());
		// The display value is pushed in Stack. It will be popped when
		// close the container tag.
		containerDisplayStack.push(display);
		if (!reportEmitter.browserSupportsInlineBlock) {
			if (((display & HTMLEmitterUtil.DISPLAY_INLINE) > 0)
					|| ((display & HTMLEmitterUtil.DISPLAY_INLINE_BLOCK) > 0)) {
				// Open the inlineBox tag when implement the inline box.
				openInlineBoxTag();
				// FIXME: code review: We should implement the shrink here.
			}
		}
		writer.openTag(HTMLTags.TAG_DIV);
	}

	/**
	 * Close the container tag.
	 */
	public void closeContainerTag() {
		writer.closeTag(HTMLTags.TAG_DIV);
		int display = containerDisplayStack.pop();
		if (!reportEmitter.browserSupportsInlineBlock) {
			if (((display & HTMLEmitterUtil.DISPLAY_INLINE) > 0)
					|| ((display & HTMLEmitterUtil.DISPLAY_INLINE_BLOCK) > 0)) {
				// Close the inlineBox tag when implement the inline box.
				closeInlineBoxTag();
			}
		}
	}

	/**
	 * Open the tag when implement the inline box. This solution only works for
	 * IE5.5, IE6, IE7, Firefox1.5 and Firefox2.
	 */
	protected void openInlineBoxTag() {
		writer.openTag(HTMLTags.TAG_DIV);
		// Only the IE5.5, IE6, IE7 can identify the "*+".
		// only the Firefox1.5 and Firefox2 can identify the
		// "-moz-inline-box".
		writer.attribute(HTMLTags.ATTR_STYLE, " display:-moz-inline-box; display:inline-block; *+display:inline;"); //$NON-NLS-1$
		writer.openTag(HTMLTags.TAG_TABLE);
		writer.attribute(HTMLTags.ATTR_STYLE, " display:table !important; display:inline;"); //$NON-NLS-1$
		writer.openTag(HTMLTags.TAG_TR);
		writer.openTag(HTMLTags.TAG_TD);
	}

	/**
	 * Close the tag when implement the inline box.
	 */
	protected void closeInlineBoxTag() {
		writer.closeTag(HTMLTags.TAG_TD);
		writer.closeTag(HTMLTags.TAG_TR);
		writer.closeTag(HTMLTags.TAG_TABLE);
		writer.closeTag(HTMLTags.TAG_DIV);
	}

	/**
	 * Set the display property to style.
	 *
	 * @param display     The display type.
	 * @param mask        The mask.
	 * @param styleBuffer The <code>StringBuffer</code> object that returns 'style'
	 *                    content.
	 */
	protected void setDisplayProperty(int display, int mask, StringBuffer styleBuffer) {
		int flag = display & mask;
		if ((display & HTMLEmitterUtil.DISPLAY_NONE) > 0) {
			styleBuffer.append("display: none;"); //$NON-NLS-1$
		} else if (flag > 0) {
			if ((flag & HTMLEmitterUtil.DISPLAY_BLOCK) > 0) {
				styleBuffer.append("display: block;"); //$NON-NLS-1$
			} else if ((flag & HTMLEmitterUtil.DISPLAY_INLINE_BLOCK) > 0) {
				styleBuffer.append("display:inline-block; zoom:1; *+display:inline;"); //$NON-NLS-1$
			} else if ((flag & HTMLEmitterUtil.DISPLAY_INLINE) > 0) {
				styleBuffer.append("display: inline;"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Open the vertical-align box tag if the element needs implementing the
	 * vertical-align.
	 *
	 * @param element content
	 */
	// FIXME: code review of text: Inline text doesn't need outputting the
	// vertical-align. Block and inline-block texts need outputting the
	// vertical-align.
	public void handleTextVerticalAlignBegin(IContent element) {
		IStyle style = element.getStyle();
		CSSValue vAlign = style.getProperty(StyleConstants.STYLE_VERTICAL_ALIGN);
		CSSValue canShrink = style.getProperty(StyleConstants.STYLE_CAN_SHRINK);
		DimensionType height = element.getHeight();
		// FIXME: code review: the top value of the vAlign shouldn't be outputed too.
		if (vAlign != null && vAlign != CSSValueConstants.BASELINE_VALUE && height != null
				&& canShrink != BIRTValueConstants.TRUE_VALUE) {
			// implement vertical align.
			writer.openTag(HTMLTags.TAG_TABLE);
			StringBuilder nestingTableStyleBuffer = new StringBuilder();
			nestingTableStyleBuffer.append(" width:100%; height:"); //$NON-NLS-1$
			nestingTableStyleBuffer.append(height.toString());
			writer.attribute(HTMLTags.ATTR_STYLE, nestingTableStyleBuffer.toString());
			writer.openTag(HTMLTags.TAG_TR);
			writer.openTag(HTMLTags.TAG_TD);

			StringBuilder textStyleBuffer = new StringBuilder();
			textStyleBuffer.append(" vertical-align:"); //$NON-NLS-1$
			textStyleBuffer.append(vAlign.getCssText());
			textStyleBuffer.append(";"); //$NON-NLS-1$
			writer.attribute(HTMLTags.ATTR_STYLE, textStyleBuffer.toString());
		}
	}

	/**
	 * Close the vertical-align box tag if the element needs implementing the
	 * vertical-align.
	 *
	 * @param element content
	 */
	public void handleVerticalAlignEnd(IContent element) {
		IStyle style = element.getStyle();
		CSSValue vAlign = style.getProperty(StyleConstants.STYLE_VERTICAL_ALIGN);
		CSSValue canShrink = style.getProperty(StyleConstants.STYLE_CAN_SHRINK);
		DimensionType height = element.getHeight();
		if (vAlign != null && vAlign != CSSValueConstants.BASELINE_VALUE && height != null
				&& canShrink != BIRTValueConstants.TRUE_VALUE) {
			writer.closeTag(HTMLTags.TAG_TD);
			writer.closeTag(HTMLTags.TAG_TR);
			writer.closeTag(HTMLTags.TAG_TABLE);
		}
	}
}
