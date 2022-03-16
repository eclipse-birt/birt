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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for Image element to store the constants.
 */
public interface IImageItemModel {

	/**
	 * Name of the embedded image name property, which identifies the image if its
	 * type is embedded image.
	 */

	String IMAGE_NAME_PROP = "imageName"; //$NON-NLS-1$

	/**
	 * Name of the optional text property, which is used to display in place of the
	 * image in a web browser.
	 */

	String ALT_TEXT_PROP = "altText"; //$NON-NLS-1$

	/**
	 * Name of the optional text resource key property.
	 */

	String ALT_TEXT_KEY_PROP = "altTextID"; //$NON-NLS-1$

	/**
	 * Name of the size property, which defines how to manage the relationship
	 * between image and item size.
	 */

	String SIZE_PROP = "size"; //$NON-NLS-1$

	/**
	 * Name of the scale property.
	 */

	String SCALE_PROP = "scale"; //$NON-NLS-1$

	/**
	 * Name of the uri property, which identifies the image if its type it file or
	 * URL.
	 */
	String URI_PROP = "uri"; //$NON-NLS-1$

	/**
	 * Name of the image reference type property.
	 */

	String SOURCE_PROP = "source"; //$NON-NLS-1$

	/**
	 * Name of the value expression property, which returns the image contents.
	 */

	String VALUE_EXPR_PROP = "valueExpr"; //$NON-NLS-1$

	/**
	 * Name of the type expression property.
	 */

	String TYPE_EXPR_PROP = "typeExpr"; //$NON-NLS-1$

	/**
	 * Name of the action property, which defines what action can be performed when
	 * clicking this image.
	 */

	String ACTION_PROP = "action"; //$NON-NLS-1$

	/**
	 * Name of the help text property.
	 */

	String HELP_TEXT_PROP = "helpText"; //$NON-NLS-1$

	/**
	 * Name of the help text id property.
	 */

	String HELP_TEXT_ID_PROP = "helpTextID"; //$NON-NLS-1$

	/**
	 * Name of the fit to container property.The image size will be scaled if this
	 * property is set true.
	 */
	String FIT_TO_CONTAINER_PROP = "fitToContainer"; //$NON-NLS-1$

	/**
	 * Name of the property which indicates whether the image scales proportionally
	 * or not.
	 */
	String PROPORTIONAL_SCALE_PROP = "proportionalScale"; //$NON-NLS-1$
}
