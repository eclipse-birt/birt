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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITextDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;

/**
 * Represents a multi-line data item element. The multi-line data item displays
 * blocks of text retrieved from the database, from a file, or from an
 * expression. The text can be plain text, HTML, RTF or an expression. The
 * format of the text can be fixed at design time, or can be dynamically
 * selected at run time to match the format of the incoming text.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.TextDataItem
 */

public class TextDataHandle extends ReportItemHandle implements ITextDataItemModel {

	/**
	 * Constructs the handle with the report design and the element it holds. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TextDataHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the expression that gives the text that the multi-line data item
	 * displays.
	 * 
	 * @return the value expression
	 */

	public String getValueExpr() {
		return getStringProperty(ITextDataItemModel.VALUE_EXPR_PROP);
	}

	/**
	 * Sets the expression that gives the text that this multi-line data item
	 * displays.
	 * 
	 * @param expr the new expression for the value expression
	 * @throws SemanticException if the expression contains errors, or the property
	 *                           is locked.
	 */

	public void setValueExpr(String expr) throws SemanticException {
		setProperty(ITextDataItemModel.VALUE_EXPR_PROP, expr);
	}

	/**
	 * Returns the expression that that defines the type of text the multi-line data
	 * item holds. The content type can be one of Auto (default); Plain: Plain text;
	 * HTML: HTML format; RTF: Rich Text Format; Expression: an expression that
	 * returns one of the above strings.
	 * 
	 * @return the expression for the text type
	 * 
	 * @deprecated by the method {@link #getContentType()}
	 */

	public String getContentTypeExpr() {
		return getStringProperty(ITextDataItemModel.CONTENT_TYPE_PROP);
	}

	/**
	 * Sets the expression that defines the text type this multi-line data item
	 * holds. The content type can be one of Auto (default); Plain: Plain text;
	 * HTML: HTML format; RTF: Rich Text Format; Expression: an expression that
	 * returns one of the above strings.
	 * 
	 * @param expr the new expression for the text type
	 * @throws SemanticException if the expression contains errors, or the property
	 *                           is locked.
	 * 
	 * @deprecated by the method {@link #setContentType(String)}
	 */

	public void setContentTypeExpr(String expr) throws SemanticException {
		setProperty(ITextDataItemModel.CONTENT_TYPE_PROP, expr);
	}

	/**
	 * Returns the expression that that defines the type of text the multi-line data
	 * item holds. The content type can be one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_AUTO</code> (default)
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_PLAIN</code>: Plain
	 * text;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML</code>: HTML
	 * format;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF</code>: Rich Text
	 * format;
	 * </ul>
	 * 
	 * @return the text type
	 */

	public String getContentType() {
		return getStringProperty(ITextDataItemModel.CONTENT_TYPE_PROP);
	}

	/**
	 * Sets the expression that defines the text type this multi-line data item
	 * holds. The content type can be one of
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_AUTO</code> (default)
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_PLAIN</code>: Plain
	 * text;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML</code>: HTML
	 * format;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF</code>: Rich Text
	 * format;
	 * </ul>
	 * 
	 * @param contentType the new text type
	 * @throws SemanticException if the property is locked or the
	 *                           <code>contentType</code> is not one of the above.
	 */

	public void setContentType(String contentType) throws SemanticException {
		setProperty(ITextDataItemModel.CONTENT_TYPE_PROP, contentType);
	}

	/**
	 * Determines whether there is expression need to be evaluated in the text
	 * content of this text data handle. By default, the return value is
	 * <code>FALSE</code>.
	 * 
	 * @return true if there is expression in the text content, otherwise false
	 */
	public boolean hasExpression() {
		return getBooleanProperty(HAS_EXPRESSION_PROP);
	}

	/**
	 * Sets the status whether there is expression need to be evaluated in the text
	 * content of this text data handle.
	 * 
	 * @param hasExpression true if there is expression in the text content,
	 *                      otherwise false
	 * @throws SemanticException
	 */
	public void setHasExpression(boolean hasExpression) throws SemanticException {
		setBooleanProperty(HAS_EXPRESSION_PROP, hasExpression);
	}

	/**
	 * set if jTidy need to be used to validate the HTML content.
	 * 
	 * If jTidy is set to false, the HTML content is used directly without any
	 * validation. The user needs ensure the content is well formed.
	 * 
	 * @param useJTidy true, use jTidy to validate the content.
	 * @throws SemanticException
	 */
	public void setJTidy(boolean useJTidy) throws SemanticException {
		setBooleanProperty(ITextItemModel.JTIDY_PROP, useJTidy);
	}

	/**
	 * return if jTIdy is used to validate the HTML content.
	 * 
	 * @return true, jTidy is used to validate the content.
	 */
	public boolean isJTidy() {
		return getBooleanProperty(ITextItemModel.JTIDY_PROP);
	}
}
