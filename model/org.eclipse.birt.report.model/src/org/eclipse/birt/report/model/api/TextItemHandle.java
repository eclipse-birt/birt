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
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;

/**
 * Represents a text data item. The text item allows the developer to provide
 * the text as part of the report design. The text can be localized. Text can be
 * in HTML or plain text format.
 * 
 */

public class TextItemHandle extends ReportItemHandle implements ITextItemModel {

	/**
	 * Constructs a handle for a text item. The application generally does not
	 * create handles directly. Instead, it uses one of the navigation methods
	 * available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TextItemHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Gets the text of this text element.
	 * 
	 * @return the text to display with the element, if this property value is not
	 *         set, return <code>null</code>.
	 */

	public String getContent() {
		return getStringProperty(ITextItemModel.CONTENT_PROP);
	}

	/**
	 * Returns the localized content for the text. If the localized text for the
	 * text resource key is found, it will be returned. Otherwise, the static text
	 * will be returned.
	 * 
	 * @return the localized content for the text.
	 */

	public String getDisplayContent() {
		return super.getExternalizedValue(ITextItemModel.CONTENT_RESOURCE_KEY_PROP, ITextItemModel.CONTENT_PROP);
	}

	/**
	 * Sets the text for the text element.
	 * 
	 * @param value the new content of the text item
	 * @throws SemanticException if the property is locked.
	 */

	public void setContent(String value) throws SemanticException {
		setStringProperty(ITextItemModel.CONTENT_PROP, value);
	}

	/**
	 * Returns the content type of this text item. The content type will one of the
	 * following constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>TEXT_CONTENT_TYPE_AUTO</code>
	 * <li><code>TEXT_CONTENT_TYPE_PLAIN</code>
	 * <li><code>TEXT_CONTENT_TYPE_HTML</code>
	 * <li><code>TEXT_CONTENT_TYPE_RTF</code>
	 * </ul>
	 * 
	 * @return the content type. if this property value is not set, return
	 *         <code>null</code>.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getContentType() {
		return getStringProperty(ITextItemModel.CONTENT_TYPE_PROP);
	}

	/**
	 * Sets the content type of this text item. The content type will one of the
	 * following constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>TEXT_CONTENT_TYPE_AUTO</code>
	 * <li><code>TEXT_CONTENT_TYPE_PLAIN</code>
	 * <li><code>TEXT_CONTENT_TYPE_HTML</code>
	 * <li><code>TEXT_CONTENT_TYPE_RTF</code>
	 * </ul>
	 * 
	 * @param contentType the content type of this text item.
	 * 
	 * @throws SemanticException if the value is not a valid choice item.
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 * 
	 */

	public void setContentType(String contentType) throws SemanticException {
		setStringProperty(ITextItemModel.CONTENT_TYPE_PROP, contentType);
	}

	/**
	 * Gets the resource key of the text for the item.
	 * 
	 * @return the resource key of the text
	 */

	public String getContentKey() {
		return getStringProperty(ITextItemModel.CONTENT_RESOURCE_KEY_PROP);
	}

	/**
	 * Sets the resource key of the text for the item.
	 * 
	 * @param resourceKey the resource key of the text
	 * @throws SemanticException if the property is locked.
	 */

	public void setContentKey(String resourceKey) throws SemanticException {
		setStringProperty(ITextItemModel.CONTENT_RESOURCE_KEY_PROP, resourceKey);
	}

	/**
	 * Determines whether there is expression need to be evaluated in the text
	 * content of this text item handle. By default, the return value is
	 * <code>FALSE</code>.
	 * 
	 * @return true if there is expression in the text content, otherwise false
	 */
	public boolean hasExpression() {
		return getBooleanProperty(HAS_EXPRESSION_PROP);
	}

	/**
	 * Sets the status whether there is expression need to be evaluated in the text
	 * content of this text item handle.
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