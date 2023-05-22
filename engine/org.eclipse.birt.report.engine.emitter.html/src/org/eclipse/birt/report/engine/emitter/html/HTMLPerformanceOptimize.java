/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.html;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.emitter.HTMLWriter;
import org.eclipse.birt.report.engine.emitter.html.util.HTMLEmitterUtil;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.css.CSSValue;

/**
 *
 */

public class HTMLPerformanceOptimize extends HTMLEmitter {

	/**
	 * Constructor
	 *
	 * @param reportEmitter     report emitter
	 * @param writer            HTML writer
	 * @param fixedReport       fixed report layout
	 * @param enableInlineStyle enabled inline style
	 * @param browserVersion    browser version
	 */
	public HTMLPerformanceOptimize(HTMLReportEmitter reportEmitter, HTMLWriter writer, boolean fixedReport,
			boolean enableInlineStyle, int browserVersion) {
		super(reportEmitter, writer, fixedReport, enableInlineStyle, browserVersion);
	}

	/**
	 * Build the report default style
	 */
	@Override
	public void buildDefaultStyle(StringBuffer styleBuffer, IStyle style) {
		if (style == null || style.isEmpty()) {
			return;
		}

		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);

		// bidi_hcg start
		// Build direction.
		AttributeBuilder.buildBidiDirection(styleBuffer, style);
		// bidi_hcg end

		// Build the textAlign
		String value = style.getTextAlign();
		if (null != value) {
			styleBuffer.append(" text-align:");
			styleBuffer.append(value);
			styleBuffer.append(";");
		}
	}

	/**
	 * Build attribute class
	 */
	@Override
	public void buildStyle(StringBuffer styleBuffer, IStyle style) {
		if (style == null || style.isEmpty()) {
			return;
		}

		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildBox(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, null);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);
		AttributeBuilder.buildSize(styleBuffer, style);
	}

	/**
	 * Build the style of the page head and page footer
	 */
	@Override
	public void buildPageBandStyle(StringBuffer styleBuffer, IStyle style) {
		if (style == null || style.isEmpty()) {
			return;
		}

		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);

		// Build the vertical-align
		String value = style.getVerticalAlign();
		if (null != value) {
			styleBuffer.append(" vertical-align:");
			styleBuffer.append(value);
			styleBuffer.append(";");
		}
		// Build the textAlign
		value = style.getTextAlign();
		if (null != value) {
			styleBuffer.append(" text-align:");
			styleBuffer.append(value);
			styleBuffer.append(";");
		}
	}

	/**
	 * Build the style of table content
	 */
	@Override
	public void buildTableStyle(ITableContent table, StringBuffer styleBuffer) {
		addDefaultTableStyles(styleBuffer);

		// The method getStyle( ) will nevel return a null value;
		IStyle style = table.getStyle();

		boolean isInline = false;
		// output the display
		CSSValue display = style.getProperty(StyleConstants.STYLE_DISPLAY);
		if (CSSValueConstants.NONE_VALUE == display) {
			styleBuffer.append(" display: none;");
		} else if (CSSValueConstants.INLINE_VALUE == display || CSSValueConstants.INLINE_BLOCK_VALUE == display) {
			isInline = true;
			// implement the inline table for old version browser
			if (!reportEmitter.browserSupportsInlineBlock) {
				styleBuffer.append(" display:table !important; display:inline;");
			} else {
				styleBuffer.append(" display:inline-block; zoom:1; *+display:inline;");
			}
		}

		// height
		DimensionType height = table.getHeight();
		if (null != height) {
			buildSize(styleBuffer, HTMLTags.ATTR_HEIGHT, height);
		}
		// width
		boolean widthOutputFlag = false;
		DimensionType width = table.getWidth();
		if (null != width) {
			buildSize(styleBuffer, HTMLTags.ATTR_WIDTH, width);
			widthOutputFlag = true;
		} else // Shrink table will not output the 100% as the default width in
		// HTML.
		// This is different with the PDF. PDF will use the 100% as the
		// default width for a shrink table.
		// If the table's columns all have a absolute width, we should not
		// output the 100% as the default width.
		if (!"true".equalsIgnoreCase(style.getCanShrink())) {
			boolean absoluteWidth = true;
			for (int i = 0; i < table.getColumnCount(); i++) {
				IColumn column = table.getColumn(i);
				DimensionType columnWidth = column.getWidth();
				if (columnWidth == null) {
					absoluteWidth = false;
					break;
				}
				if ("%".endsWith(columnWidth.getUnits())) {
					absoluteWidth = false;
					break;
				}
			}
			if (!absoluteWidth) {
				styleBuffer.append(" width: 100%;");
				widthOutputFlag = true;
			}
		}

		// implement table-layout
		if (fixedReport) {
			// shrink table will not output table-layout;
			if (!"true".equalsIgnoreCase(style.getCanShrink())) {
				if (!widthOutputFlag) {
					// In Firefox, if a table hasn't a width, the
					// " table-layout:fixed;"
					if (isInline) {
						styleBuffer.append(" width: auto;");
					} else {
						styleBuffer.append(" width: 1px;");
					}
				}
				CSSValue overflowValue = style.getProperty(StyleConstants.STYLE_OVERFLOW);
				if (overflowValue == null) {
					// only inline table support it in Chrome and IE
					if (isInline) {
						styleBuffer.append(" overflow:hidden;");
					}
				} else {
					styleBuffer.append(" overflow:").append(overflowValue.getCssText()).append(";");
				}
				// build the table-layout
				styleBuffer.append(" table-layout:fixed;");
			}
		}

		// Build the textAlign
		String value = style.getTextAlign();
		if (null != value) {
			if (isInline) {
				styleBuffer.append(" text-align:");
				styleBuffer.append("-moz-");
				styleBuffer.append(value);
				styleBuffer.append(" !important;");
			}
			styleBuffer.append("text-align:");
			styleBuffer.append(value);
			styleBuffer.append(";");
		}
		// Table doesn't support vertical-align.

		style = getElementStyle(table);
		if (style == null) {
			return;
		}

		DimensionType[] tableSize = { table.getHeight(), table.getWidth() };
		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildBox(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, tableSize);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);
	}

	/**
	 * Build the style of column
	 */
	@Override
	public void buildColumnStyle(IColumn column, StringBuffer styleBuffer) {
		buildSize(styleBuffer, HTMLTags.ATTR_WIDTH, column.getWidth());

		// The method getStyle( ) will nevel return a null value;
		IStyle style = column.getStyle();

		// output the none value of the display
		CSSValue display = style.getProperty(StyleConstants.STYLE_DISPLAY);
		if (CSSValueConstants.NONE_VALUE == display) {
			styleBuffer.append(" display:none;");
		}

		// Build the vertical-align
		// In performance optimize model the vertical-align can't be setted to
		// the column. Because we output the vertical-align directly here, and
		// it will cause the conflict with the BIRT, CSS, IE, Firefox. The user
		// should set the vertical-align to the row or cells in this model.
		String value = style.getVerticalAlign();
		if (null != value) {
			styleBuffer.append(" vertical-align:");
			styleBuffer.append(value);
			styleBuffer.append(";");
		}

		style = column.getInlineStyle();
		if (style == null || style.isEmpty()) {
			return;
		}

		DimensionType[] columnSize = { null, column.getWidth() };
		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildBox(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, columnSize);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);
	}

	/**
	 * Handles the alignment property of the column content.
	 */
	@Override
	public void handleColumnAlign(IColumn column) {
		// Column doesn't support text-align in BIRT.
	}

	/**
	 * Build the style of row content.
	 */
	@Override
	public void buildRowStyle(IRowContent row, StringBuffer styleBuffer) {
		buildSize(styleBuffer, HTMLTags.ATTR_HEIGHT, row.getHeight()); // $NON-NLS-1$

		// The method getStyle( ) will never return a null value;
		IStyle style = row.getStyle();

		// output the none value of the display
		CSSValue display = style.getProperty(StyleConstants.STYLE_DISPLAY);
		if (CSSValueConstants.NONE_VALUE == display) {
			styleBuffer.append(" display: none;");
		}

		style = getElementStyle(row);
		if (style == null) {
			return;
		}

		DimensionType[] rowSize = { row.getHeight(), row.getWidth() };
		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildBox(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, rowSize);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);
	}

	/**
	 * Handles the Text-Align property of the row content.
	 */
	@Override
	public void handleRowAlign(IRowContent row) {
		// The method getStyle( ) will nevel return a null value;
		IStyle style = row.getStyle();

		// Build the Vertical-Align property of the row content
		CSSValue vAlign = style.getProperty(StyleConstants.STYLE_VERTICAL_ALIGN);
		if (null == vAlign || CSSValueConstants.BASELINE_VALUE == vAlign) {
			// The default vertical-align value of cell is top. And the cell can
			// inherit the valign from parent row.
			vAlign = CSSValueConstants.TOP_VALUE;
		}
		writer.attribute(HTMLTags.ATTR_VALIGN, vAlign.getCssText());

		// Build the Text-Align property.
		CSSValue hAlign = style.getProperty(StyleConstants.STYLE_TEXT_ALIGN);
		if (null != hAlign) {
			writer.attribute(HTMLTags.ATTR_ALIGN, hAlign.getCssText());
		}
	}

	/**
	 * Build the style of cell content.
	 */
	@Override
	public void buildCellStyle(ICellContent cell, StringBuffer styleBuffer, boolean isHead, boolean fixedCellHeight) {
		// The method getStyle( ) will never return a null value;
		IStyle style = cell.getStyle();

		if (style == null) {
			return;
		}

		// implement the cell's clip.
		if (fixedReport && !fixedCellHeight) {
			HTMLEmitterUtil.buildOverflowStyle(styleBuffer, style, true);
		}
		// output the none value of the display
		CSSValue display = style.getProperty(StyleConstants.STYLE_DISPLAY);
		if (CSSValueConstants.NONE_VALUE == display) {
			styleBuffer.append(" display: none !important; display: block;");
		}

		style = getElementStyle(cell);
		if (style == null) {
			if (fixedCellHeight) {
				// Fixed cell height requires the padding must be 0px.
				styleBuffer.append(" padding: 0px;");
			}
			return;
		}

		DimensionType[] cellSize = { cell.getHeight(), cell.getWidth() };
		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildMargins(styleBuffer, style);
		if (fixedCellHeight) {
			// Fixed cell height requires the padding must be 0px.
			styleBuffer.append(" padding: 0px;");
		} else {
			AttributeBuilder.buildPaddings(styleBuffer, style);
		}
		AttributeBuilder.buildBorders(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, cellSize);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);
	}

	/**
	 * Handles the vertical align property of the element content.
	 */
	@Override
	public void handleCellVAlign(ICellContent cell) {
		// The method getStyle( ) will never return a null value;
		IStyle style = cell.getStyle();

		// Build the Vertical-Align property of the row content
		CSSValue vAlign = style.getProperty(StyleConstants.STYLE_VERTICAL_ALIGN);
		if (CSSValueConstants.BASELINE_VALUE == vAlign) {
			vAlign = CSSValueConstants.TOP_VALUE;
		}
		if (null != vAlign) {
			// The default vertical-align value has already been outputted on
			// the parent row.
			writer.attribute(HTMLTags.ATTR_VALIGN, vAlign.getCssText());
		}
	}

	/**
	 * Build the style of contianer content.
	 */
	@Override
	public void buildContainerStyle(IContainerContent container, StringBuffer styleBuffer) {
		int display = containerDisplayStack.peek().intValue();
		// shrink
		handleShrink(display, container.getStyle(), container.getHeight(), container.getWidth(), styleBuffer);
		if ((display & HTMLEmitterUtil.DISPLAY_NONE) > 0) {
			styleBuffer.append("display: none;"); //$NON-NLS-1$
		} else if (((display & HTMLEmitterUtil.DISPLAY_INLINE) > 0)
				|| ((display & HTMLEmitterUtil.DISPLAY_INLINE_BLOCK) > 0)) {
			styleBuffer.append("display:inline-block; zoom:1; *+display:inline;"); //$NON-NLS-1$
		}

		IStyle style = getElementStyle(container);
		if (style == null) {
			return;
		}

		DimensionType[] containerSize = { container.getHeight(), container.getWidth() };
		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildBox(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, containerSize);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);
	}

	/**
	 * Handles the alignment property of the container content.
	 */
	@Override
	public void handleContainerAlign(IContainerContent container) {
		// The method getStyle( ) will nevel return a null value;
		IStyle style = container.getStyle();
		// Container doesn't support vertical-align.
		// Build the Text-Align property.
		CSSValue hAlign = style.getProperty(StyleConstants.STYLE_TEXT_ALIGN);
		if (null != hAlign) {
			writer.attribute(HTMLTags.ATTR_ALIGN, hAlign.getCssText());
		}
	}

	/**
	 * Build the style of text content.
	 */
	@Override
	public void buildTextStyle(ITextContent text, StringBuffer styleBuffer, int display) {
		IStyle style = text.getStyle();
		// check 'can-shrink' property
		handleTextShrink(display, style, text.getHeight(), text.getWidth(), styleBuffer);

		setDisplayProperty(display, HTMLEmitterUtil.DISPLAY_INLINE_BLOCK, styleBuffer);

		// bidi_hcg start
		// Build direction.
		AttributeBuilder.buildBidiDirection(styleBuffer, text.getComputedStyle());
		// bidi_hcg end

		// build the text-align
		String textAlign = style.getTextAlign();
		if (textAlign != null) {
			styleBuffer.append(" text-align:");
			styleBuffer.append(textAlign);
			styleBuffer.append(";");
		}

		style = getElementStyle(text);
		if (style == null) {
			return;
		}

		DimensionType[] textSize = { text.getHeight(), text.getWidth() };
		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildBox(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, textSize);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);
	}

	/**
	 * Build the style of foreign content.
	 */
	@Override
	public void buildForeignStyle(IForeignContent foreign, StringBuffer styleBuffer, int display) {
		IStyle style = foreign.getStyle();
		// check 'can-shrink' property
		handleShrink(display, style, foreign.getHeight(), foreign.getWidth(), styleBuffer);

		setDisplayProperty(display, HTMLEmitterUtil.DISPLAY_INLINE_BLOCK, styleBuffer);

		// bidi_hcg start
		// Build direction.
		AttributeBuilder.buildBidiDirection(styleBuffer, foreign.getComputedStyle());
		// bidi_hcg end

		// build the text-align
		String textAlign = style.getTextAlign();
		if (textAlign != null) {
			styleBuffer.append(" text-align:");
			styleBuffer.append(textAlign);
			styleBuffer.append(";");
		}

		style = getElementStyle(foreign);
		if (style == null) {
			return;
		}

		DimensionType[] foreignSize = { foreign.getHeight(), foreign.getWidth() };
		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildBox(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, foreignSize);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);
	}

	/**
	 * Build the style of image content.
	 */
	@Override
	public void buildImageStyle(IImageContent image, StringBuffer styleBuffer, int display) {
		// image size
		buildSize(styleBuffer, HTMLTags.ATTR_WIDTH, image.getWidth()); // $NON-NLS-1$
		buildSize(styleBuffer, HTMLTags.ATTR_HEIGHT, image.getHeight()); // $NON-NLS-1$
		// build the value of display
		// An image is indeed inline by default, and align itself on the text
		// baseline with room for descenders. That caused the gap and extra height,
		// and the gap/height will change with font-resizing. Set "display:block" to
		// get rid of space at the top and bottom.
		setDisplayProperty(display, HTMLEmitterUtil.DISPLAY_BLOCK, styleBuffer);

		IStyle style = image.getStyle();
		String verticalAlign = style.getVerticalAlign();
		if (verticalAlign != null) {
			styleBuffer.append(" vertical-align:");
			styleBuffer.append(verticalAlign);
			styleBuffer.append(";");
		}

		style = getElementStyle(image);
		if (style == null) {
			return;
		}

		DimensionType[] imageSize = { image.getHeight(), image.getWidth() };
		AttributeBuilder.buildFont(styleBuffer, style);
		AttributeBuilder.buildBox(styleBuffer, style);
		AttributeBuilder.buildBackground(styleBuffer, style, reportEmitter, imageSize);
		AttributeBuilder.buildText(styleBuffer, style);
		AttributeBuilder.buildVisual(styleBuffer, style);
		AttributeBuilder.buildTextDecoration(styleBuffer, style);

		// Image doesn't text-align.
		// Text-align has been build in the style class. But the text-align
		// doesn't work with the image.
	}
}
