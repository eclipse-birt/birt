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
 * The interface for text data element to store the constants.
 */
public interface ITextDataItemModel
{

	/**
	 * Name of the value expression property that gives an expression that
	 * provides the text. The expression is most often simply a reference to a
	 * query column. But, it can also reference a report parameter, a formula, a
	 * special value, a file, or other data item..
	 */

	public static final String VALUE_EXPR_PROP = "valueExpr"; //$NON-NLS-1$

	/**
	 * Name of the content type expression property. It is an optional property
	 * that defines the type of text. Applies to both static text and the value
	 * expression. The choices are:
	 * <ul>
	 * <li>Auto (default): BIRT will infer the format as explained above.
	 * <li>Plain: Plain text.
	 * <li>HTML: HTML format.
	 * <li>RTF: Rich Text Format.
	 * <li>Expression: an expression that returns one of the above strings.
	 * </ul>
	 * 
	 */

	public static final String CONTENT_TYPE_PROP = "contentType"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether there is expression in the
	 * text value. By default, the value is FALSE.
	 */
	String HAS_EXPRESSION_PROP = "hasExpression"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates if jTidy is used to validate HTML content.
	 * By default, the value is TRUE.
	 */
	public static String JTIDY_PROP = "jTidy"; //$NON-NLS-1$
}
