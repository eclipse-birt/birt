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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TextItem;

/**
 * AttributeConstant defines property key constant.
 */
public class AttributeConstant
{

	public final static String DIS = "DIS";//$NON-NLS-1$

	/**
	 * Width property key
	 */

	public final static String WIDTH = ReportItem.WIDTH_PROP;

	/**
	 * Height property key
	 */

	public final static String HEIGHT = ReportItem.HEIGHT_PROP;

	/***************************************************************************
	 * Name property key
	 */

	public static final String NAME = DesignElement.NAME_PROP;

	/**
	 * Background color property key
	 */

	public static final String BACKGROUND_COLOR = Style.BACKGROUND_COLOR_PROP;

	/**
	 * Cell Padding top property key
	 */

	public static final String PADDING_TOP = Style.PADDING_TOP_PROP;

	/**
	 * Cell Padding bottom property key
	 */

	public static final String PADDING_BOTTOM = Style.PADDING_BOTTOM_PROP;

	/**
	 * Cell Padding left property key
	 */

	public static final String PADDING_LEFT = Style.PADDING_LEFT_PROP;

	/**
	 * Cell Padding right property key
	 */

	public static final String PADDING_RIGHT = Style.PADDING_RIGHT_PROP;

	/**
	 * DataSet property key
	 */

	public static final String DATASET = ReportItem.DATA_SET_PROP;

	/**
	 * Font width property key
	 */

	public static final String FONT_WIDTH = Style.FONT_WEIGHT_PROP;

	/**
	 * Font name property key
	 */

	public static final String FONT_FAMILY = Style.FONT_FAMILY_PROP;

	/**
	 * Font style property key
	 */

	public static final String FONT_STYLE = Style.FONT_STYLE_PROP;

	/**
	 * Text underline property key
	 */

	public static final String TEXT_UNDERLINE = Style.TEXT_UNDERLINE_PROP;

	/**
	 * Text line through property key
	 */

	public static final String TEXT_LINE_THROUGH = Style.TEXT_LINE_THROUGH_PROP;

	/**
	 * Font color property key
	 */

	public static final String FONT_COLOR = Style.COLOR_PROP;

	/**
	 * Common presentation of 'border-top-style',
	 * 'border-left-style','border-bottom-style' and 'border-right-style'
	 * properties.
	 */

	public static final String BORDER_STYLE = "ui_border_style";//$NON-NLS-1$

	/**
	 * Border top style property.
	 */

	public static final String BORDER_TOP_STYLE = Style.BORDER_TOP_STYLE_PROP;

	/**
	 * Border bottom style property.
	 */

	public static final String BORDER_BOTTOM_STYLE = Style.BORDER_BOTTOM_STYLE_PROP;

	/**
	 * Border left style property.
	 */

	public static final String BORDER_LEFT_STYLE = Style.BORDER_LEFT_STYLE_PROP;

	/**
	 * Border right style property.
	 */

	public static final String BORDER_RIGHT_STYLE = Style.BORDER_RIGHT_STYLE_PROP;

	/**
	 * Common presentation of 'border-top-width',
	 * 'border-left-width','border-bottom-width' and 'border-right-width'
	 * properties.
	 */

	public static final String BORDER_WIDTH = "ui_border_WIDTH";//$NON-NLS-1$

	/**
	 * Border top width property.
	 */

	public static final String BORDER_TOP_WIDTH = Style.BORDER_TOP_WIDTH_PROP;

	/**
	 * Border bottom width property.
	 */

	public static final String BORDER_BOTTOM_WIDTH = Style.BORDER_BOTTOM_WIDTH_PROP;

	/**
	 * Border left width property.
	 */

	public static final String BORDER_LEFT_WIDTH = Style.BORDER_LEFT_WIDTH_PROP;

	/**
	 * Border right width property.
	 */

	public static final String BORDER_RIGHT_WIDTH = Style.BORDER_RIGHT_WIDTH_PROP;

	/**
	 * Border top color property.
	 */

	public static final String BORDER_TOP_COLOR = Style.BORDER_TOP_COLOR_PROP;

	/**
	 * Border bottom color property.
	 */

	public static final String BORDER_BOTTOM_COLOR = Style.BORDER_BOTTOM_COLOR_PROP;

	/**
	 * Border left color property.
	 */

	public static final String BORDER_LEFT_COLOR = Style.BORDER_LEFT_COLOR_PROP;

	/**
	 * Border right color property.
	 */

	public static final String BORDER_RIGHT_COLOR = Style.BORDER_RIGHT_COLOR_PROP;

	public static final String TEXT_FORMAT = TextItem.CONTENT_TYPE_PROP;

	public static final String TEXT_ALIGN = Style.TEXT_ALIGN_PROP;

	public static final String FONT_SIZE = Style.FONT_SIZE_PROP;

	//	public static final String TEXT_ALTERNATE = TextItem.HELP_TEXT_PROP;
	public static final String IMAGE_ALTERNATE = ImageItem.ALT_TEXT_PROP;

	public static final String HORIZONTAL_ALIGN = "GUI_HORIZONTAL_ALIGN";//$NON-NLS-1$

}