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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;

/**
 * AttributeConstant defines property key constant.
 */
public class AttributeConstant {

	public final static String DIS = "DIS";//$NON-NLS-1$

	/**
	 * Width property key
	 */

	public final static String WIDTH = ReportItemHandle.WIDTH_PROP;

	/**
	 * Height property key
	 */

	public final static String HEIGHT = ReportItemHandle.HEIGHT_PROP;

	/***************************************************************************
	 * Name property key
	 */

	public static final String NAME = DesignElementHandle.NAME_PROP;

	/**
	 * Background color property key
	 */

	public static final String BACKGROUND_COLOR = StyleHandle.BACKGROUND_COLOR_PROP;

	/**
	 * Cell Padding top property key
	 */

	public static final String PADDING_TOP = StyleHandle.PADDING_TOP_PROP;

	/**
	 * Cell Padding bottom property key
	 */

	public static final String PADDING_BOTTOM = StyleHandle.PADDING_BOTTOM_PROP;

	/**
	 * Cell Padding left property key
	 */

	public static final String PADDING_LEFT = StyleHandle.PADDING_LEFT_PROP;

	/**
	 * Cell Padding right property key
	 */

	public static final String PADDING_RIGHT = StyleHandle.PADDING_RIGHT_PROP;

	/**
	 * DataSet property key
	 */

	public static final String DATASET = ReportItemHandle.DATA_SET_PROP;

	/**
	 * Font width property key
	 */

	public static final String FONT_WIDTH = StyleHandle.FONT_WEIGHT_PROP;

	/**
	 * Font name property key
	 */

	public static final String FONT_FAMILY = StyleHandle.FONT_FAMILY_PROP;

	/**
	 * Font style property key
	 */

	public static final String FONT_STYLE = StyleHandle.FONT_STYLE_PROP;

	/**
	 * Text underline property key
	 */

	public static final String TEXT_UNDERLINE = StyleHandle.TEXT_UNDERLINE_PROP;

	/**
	 * Text line through property key
	 */

	public static final String TEXT_LINE_THROUGH = StyleHandle.TEXT_LINE_THROUGH_PROP;

	/**
	 * Font color property key
	 */

	public static final String FONT_COLOR = StyleHandle.COLOR_PROP;

	/**
	 * Common presentation of 'border-top-style',
	 * 'border-left-style','border-bottom-style' and 'border-right-style'
	 * properties.
	 */

	public static final String BORDER_STYLE = "ui_border_style";//$NON-NLS-1$

	/**
	 * Border top style property.
	 */

	public static final String BORDER_TOP_STYLE = StyleHandle.BORDER_TOP_STYLE_PROP;

	/**
	 * Border bottom style property.
	 */

	public static final String BORDER_BOTTOM_STYLE = StyleHandle.BORDER_BOTTOM_STYLE_PROP;

	/**
	 * Border left style property.
	 */

	public static final String BORDER_LEFT_STYLE = StyleHandle.BORDER_LEFT_STYLE_PROP;

	/**
	 * Border right style property.
	 */

	public static final String BORDER_RIGHT_STYLE = StyleHandle.BORDER_RIGHT_STYLE_PROP;

	/**
	 * Common presentation of 'border-top-width',
	 * 'border-left-width','border-bottom-width' and 'border-right-width'
	 * properties.
	 */

	public static final String BORDER_WIDTH = "ui_border_WIDTH";//$NON-NLS-1$

	/**
	 * Border top width property.
	 */

	public static final String BORDER_TOP_WIDTH = StyleHandle.BORDER_TOP_WIDTH_PROP;

	/**
	 * Border bottom width property.
	 */

	public static final String BORDER_BOTTOM_WIDTH = StyleHandle.BORDER_BOTTOM_WIDTH_PROP;

	/**
	 * Border left width property.
	 */

	public static final String BORDER_LEFT_WIDTH = StyleHandle.BORDER_LEFT_WIDTH_PROP;

	/**
	 * Border right width property.
	 */

	public static final String BORDER_RIGHT_WIDTH = StyleHandle.BORDER_RIGHT_WIDTH_PROP;

	/**
	 * Border top color property.
	 */

	public static final String BORDER_TOP_COLOR = StyleHandle.BORDER_TOP_COLOR_PROP;

	/**
	 * Border bottom color property.
	 */

	public static final String BORDER_BOTTOM_COLOR = StyleHandle.BORDER_BOTTOM_COLOR_PROP;

	/**
	 * Border left color property.
	 */

	public static final String BORDER_LEFT_COLOR = StyleHandle.BORDER_LEFT_COLOR_PROP;

	/**
	 * Border right color property.
	 */

	public static final String BORDER_RIGHT_COLOR = StyleHandle.BORDER_RIGHT_COLOR_PROP;

	public static final String TEXT_FORMAT = TextItemHandle.CONTENT_TYPE_PROP;

	public static final String TEXT_ALIGN = StyleHandle.TEXT_ALIGN_PROP;

	public static final String FONT_SIZE = StyleHandle.FONT_SIZE_PROP;

	// public static final String TEXT_ALTERNATE = TextItem.HELP_TEXT_PROP;
	public static final String IMAGE_ALTERNATE = IImageItemModel.ALT_TEXT_PROP;

	public static final String HORIZONTAL_ALIGN = "GUI_HORIZONTAL_ALIGN";//$NON-NLS-1$

}