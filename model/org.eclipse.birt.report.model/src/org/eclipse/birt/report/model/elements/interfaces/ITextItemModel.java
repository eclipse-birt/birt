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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for text element to store the constants.
 */
public interface ITextItemModel {

	/**
	 * Name of the property that saves the content of TextItem.
	 */

	public static final String CONTENT_PROP = "content"; //$NON-NLS-1$

	/**
	 * Name of the property that saves the resource-key of the content.
	 */

	public static final String CONTENT_RESOURCE_KEY_PROP = "contentID"; //$NON-NLS-1$

	/**
	 * Name of the cotent type property.
	 */

	public static final String CONTENT_TYPE_PROP = "contentType"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether there is expression in the text
	 * value. By default, the value is FALSE.
	 */
	public static String HAS_EXPRESSION_PROP = "hasExpression"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates if jTidy is used to validate HTML
	 * content. By default, the value is TRUE.
	 */
	public static String JTIDY_PROP = "jTidy"; //$NON-NLS-1$
}
