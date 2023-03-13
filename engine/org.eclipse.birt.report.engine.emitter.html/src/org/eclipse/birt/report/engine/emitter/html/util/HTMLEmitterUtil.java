/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.html.util;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.emitter.HTMLWriter;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.ReportElementHandle;

/**
 * Utility class for html emitter.
 *
 */
public class HTMLEmitterUtil {
	/**
	 * display type of Block
	 */
	public static final int DISPLAY_BLOCK = 1;

	/**
	 * display flag which contains all display types
	 */
	public static final int DISPLAY_FLAG_ALL = 0xffff;

	/**
	 * display type of Inline
	 */
	public static final int DISPLAY_INLINE = 2;

	/**
	 * display type of Inline-Block
	 */
	public static final int DISPLAY_INLINE_BLOCK = 4;

	/**
	 * display type of none
	 */
	public static final int DISPLAY_NONE = 8;

	public static int getElementType(IContent content) {
		return getElementType(content.getX(), content.getY(), content.getWidth(), content.getHeight(),
				content.getStyle());
	}

	public static String getTagByType(int display, int mask) {
		int flag = display & mask;
		String tag = null;
		if ((flag & DISPLAY_BLOCK) > 0) {
			tag = HTMLTags.TAG_DIV;
		}

		if ((flag & DISPLAY_INLINE) > 0) {
			tag = HTMLTags.TAG_SPAN;
		}

		return tag;
	}

	/**
	 * Outputs the 'bookmark' property. Destination anchors in HTML documents may be
	 * specified either by the A element (naming it with the 'name' attribute), or
	 * by any other elements (naming with the 'id' attribute).
	 *
	 * @param tagName  The tag's name.
	 * @param bookmark The bookmark value.
	 */
	public static void setBookmark(HTMLWriter writer, String tagName, String htmlIDNamespace, String bookmark) {
		String htmlBookmark;
		if (null != htmlIDNamespace && null != bookmark && bookmark.length() > 0) {
			htmlBookmark = htmlIDNamespace + bookmark;
		} else {
			htmlBookmark = bookmark;
		}

		writer.attribute(HTMLTags.ATTR_ID, htmlBookmark);
		if (HTMLTags.TAG_A.equalsIgnoreCase(tagName)) {
			writer.attribute(HTMLTags.ATTR_NAME, htmlBookmark);
		}
	}

	private static int getElementType(DimensionType x, DimensionType y, DimensionType width, DimensionType height,
			IStyle style) {
		int type = 0;
		String display = null;
		if (style != null) {
			display = style.getDisplay();
		}

		if (EngineIRConstants.DISPLAY_NONE.equalsIgnoreCase(display)) {
			type |= DISPLAY_NONE;
		}
		if (x != null || y != null) {
			return type | DISPLAY_BLOCK;
		} else if (EngineIRConstants.DISPLAY_INLINE.equalsIgnoreCase(display)) {
			type |= DISPLAY_INLINE;
			if (width != null || height != null) {
				type |= DISPLAY_INLINE_BLOCK;
			}
			return type;
		}
		return type | DISPLAY_BLOCK;
	}

	/**
	 * Convert DimensionType to a pixel value.
	 *
	 * @param d DimensionType value
	 * @return pixel value
	 */
	public static int getDimensionPixelValue(DimensionType d, int dpi) {
		if (d == null) {
			return 0;
		}
		int valueType = d.getValueType();
		if (valueType == DimensionType.TYPE_DIMENSION) {
			String units = d.getUnits();
			if (EngineIRConstants.UNITS_PX.equals(units)) {
				// use the default DPI.
				return (int) d.getMeasure();
			} else if (EngineIRConstants.UNITS_PT.equals(units) || EngineIRConstants.UNITS_CM.equals(units)
					|| EngineIRConstants.UNITS_MM.equals(units) || EngineIRConstants.UNITS_PC.equals(units)
					|| EngineIRConstants.UNITS_IN.equals(units)) {
				double point = d.convertTo(EngineIRConstants.UNITS_PT);
				if (dpi > 0) {
					return (int) (point / 72 * dpi);
				} else {
					// Use the default DPI.
					return (int) (point / 72 * 96);
				}
			}
		} else if (valueType == DimensionType.TYPE_CHOICE) {
			String choice = d.getChoice();
			if ("medium".equalsIgnoreCase(choice)) {
				return 3;
			} else if ("thick".equalsIgnoreCase(choice)) {
				return 5;
			} else if ("thin".equalsIgnoreCase(choice)) {
				return 1;
			}
		}
		return 0;
	}

	public static ReportElementHandle getElementHandle(Object element) {
		Object generateBy = null;
		if (element instanceof IContent) {
			generateBy = ((IContent) element).getGenerateBy();
		} else if (element instanceof IColumn) {
			generateBy = ((IColumn) element).getGenerateBy();
		}

		if (generateBy instanceof ReportItemDesign) {
			Object handle = ((ReportItemDesign) generateBy).getHandle();
			if (handle instanceof ReportElementHandle) {
				return (ReportElementHandle) handle;
			}
		} else if (generateBy instanceof ColumnDesign) {
			Object handle = ((ColumnDesign) generateBy).getHandle();
			if (handle instanceof ReportElementHandle) {
				return (ReportElementHandle) handle;
			}
		} else if (generateBy instanceof ReportElementHandle) {
			return (ReportElementHandle) generateBy;
		}

		return null;
	}

	public static int BROWSER_UNKNOW = -1;
	public static int BROWSER_FIREFOX = 10;
	public static int BROWSER_FIREFOX1 = 11;
	public static int BROWSER_FIREFOX2 = 12;
	public static int BROWSER_FIREFOX3 = 13;
	public static int BROWSER_IE5 = 21;
	public static int BROWSER_IE6 = 22;
	public static int BROWSER_IE7 = 23;
	public static int BROWSER_IE8 = 24;

	public static int getBrowserVersion(String userAgent) {
		if (userAgent == null || userAgent.length() <= 0) {
			return BROWSER_UNKNOW;
		} else if (userAgent.contains("; MSIE 5")) {
			return BROWSER_IE5;
		} else if (userAgent.contains("; MSIE 6")) {
			return BROWSER_IE6;
		} else if (userAgent.contains("; MSIE 7")) {
			return BROWSER_IE7;
		} else if (userAgent.contains("Firefox/1")) {
			return BROWSER_FIREFOX1;
		} else if (userAgent.contains("Firefox/2")) {
			return BROWSER_FIREFOX2;
		} else if (userAgent.contains("Firefox")) {
			return BROWSER_FIREFOX;
		} else {
			return BROWSER_UNKNOW;
		}
	}

	/**
	 * build overflow style which displays the vertical or horizontal scrollbar
	 * automatically if content inside the div tag exceed the limit in any aspect.
	 *
	 * @param buf          - the buffer to build the overflow style
	 * @param style        - the style of current report item
	 * @param outputHidden - whether to output the overflow property with hidden
	 *                     value
	 */
	public static void buildOverflowStyle(StringBuffer buf, IStyle style, boolean outputHidden) {
		String overflow = null;
		if (style != null) {
			overflow = style.getOverflow();
		}
		if (outputHidden || (overflow != null && !CSSConstants.CSS_OVERFLOW_HIDDEN_VALUE.equals(overflow))) {
			buf.append(" overflow:");
			buf.append(overflow != null ? overflow : CSSConstants.CSS_OVERFLOW_HIDDEN_VALUE);
			buf.append(";");
		}
	}
}
