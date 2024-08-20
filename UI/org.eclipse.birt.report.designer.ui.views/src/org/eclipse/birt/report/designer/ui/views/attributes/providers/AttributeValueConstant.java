/*******************************************************************************
 * Copyright (c) 2004, 2024 Actuate Corporation and others
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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * AttributeValueConstant defines predefined values for some properties.
 *
 */
public class AttributeValueConstant {

	/**
	 * The value defines characters with bold style.
	 */

	public final static String FONT_BOLD = DesignChoiceConstants.FONT_WEIGHT_BOLD;

	/**
	 * The value defines characters with no-bold style.
	 */

	public final static String FONT_BOLD_NORMAL = DesignChoiceConstants.FONT_WEIGHT_NORMAL;

	/**
	 * The value defines characters with italic style.
	 */

	public final static String FONT_ITALIC = DesignChoiceConstants.FONT_STYLE_ITALIC;

	/**
	 * The value defines characters with no-italic style.
	 */

	public final static String FONT_ITALIC_NORMAL = DesignChoiceConstants.FONT_STYLE_NORMAL;

	/**
	 * The value defines characters with no-underline.
	 */

	public final static String TEXT_UNDERLINE_NORMAL = DesignChoiceConstants.TEXT_UNDERLINE_NONE;

	/**
	 * The value defines characters with underline.
	 */

	public final static String TEXT_UNDERLINE = DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE;

	/**
	 * The value defines characters with no-through line.
	 */

	public final static String TEXT_LINE_THROUGH_NORMAL = DesignChoiceConstants.TEXT_LINE_THROUGH_NONE;

	/**
	 * The value defines characters with through line.
	 */

	public final static String TEXT_LINE_THROUGH = DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH;

	/**
	 * The value defines border with no line.
	 */

	public final static String BORDER_STYLE_NONE = DesignChoiceConstants.LINE_STYLE_NONE;

	public final static String TEXT_ALIGN_RIGHT = DesignChoiceConstants.TEXT_ALIGN_RIGHT;

	public final static String TEXT_ALIGN_LEFT = DesignChoiceConstants.TEXT_ALIGN_LEFT;

	public final static String TEXT_ALIGN_JUSTIFY = DesignChoiceConstants.TEXT_ALIGN_JUSTIFY;

	public final static String TEXT_ALIGN_CENTER = DesignChoiceConstants.TEXT_ALIGN_CENTER;

	/**
	 * The value defines characters with hyperlink style normal.
	 */
	public final static String TEXT_HYPERLINK_STYLE_NORMAL = DesignChoiceConstants.TEXT_HYPERLINK_STYLE_NORMAL;

	/**
	 * The value defines characters with hyperlink style undecorated.
	 */
	public final static String TEXT_HYPERLINK_STYLE_UNDECORATED = DesignChoiceConstants.TEXT_HYPERLINK_STYLE_UNDECORATED;
}
