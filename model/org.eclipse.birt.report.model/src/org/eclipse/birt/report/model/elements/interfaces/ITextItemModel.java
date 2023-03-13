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
 * The interface for text element to store the constants.
 */
public interface ITextItemModel {

	/**
	 * Name of the property that saves the content of TextItem.
	 */

	String CONTENT_PROP = "content"; //$NON-NLS-1$

	/**
	 * Name of the property that saves the resource-key of the content.
	 */

	String CONTENT_RESOURCE_KEY_PROP = "contentID"; //$NON-NLS-1$

	/**
	 * Name of the cotent type property.
	 */

	String CONTENT_TYPE_PROP = "contentType"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether there is expression in the text
	 * value. By default, the value is FALSE.
	 */
	String HAS_EXPRESSION_PROP = "hasExpression"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates if jTidy is used to validate HTML
	 * content. By default, the value is TRUE.
	 */
	String JTIDY_PROP = "jTidy"; //$NON-NLS-1$
}
