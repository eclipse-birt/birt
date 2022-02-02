/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents a the design of a TextItem in the scripting environment
 */
public interface ITextItem extends IReportItem {

	/**
	 * Gets the text of this text element.
	 * 
	 * @return the text to display with the element, if this property value is not
	 *         set, return <code>null</code>.
	 */

	String getContent();

	/**
	 * Returns the localized content for the text. If the localized text for the
	 * text resource key is found, it will be returned. Otherwise, the static text
	 * will be returned.
	 * 
	 * @return the localized content for the text.
	 */

	String getDisplayContent();

	/**
	 * Sets the text for the text element.
	 * 
	 * @param value the new content of the text item
	 * @throws SemanticException if the property is locked.
	 */

	void setContent(String value) throws SemanticException;

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

	String getContentType();

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

	void setContentType(String contentType) throws SemanticException;

	/**
	 * Gets the resource key of the text for the item.
	 * 
	 * @return the resource key of the text
	 */

	String getContentKey();

	/**
	 * Sets the resource key of the text for the item.
	 * 
	 * @param resourceKey the resource key of the text
	 * @throws SemanticException if the property is locked.
	 */

	void setContentKey(String resourceKey) throws SemanticException;

}
