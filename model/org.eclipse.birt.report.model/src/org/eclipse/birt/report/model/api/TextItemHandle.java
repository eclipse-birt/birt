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

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TextItem;

/**
 * Represents a text data item. The text item allows the developer to provide
 * the text as part of the report design. The text can be localized. Text can be
 * in HTML or plain text format.
 *  
 */

public class TextItemHandle extends ReportItemHandle
{

	/**
	 * Constructs a handle for a text item. The application generally does not
	 * create handles directly. Instead, it uses one of the navigation methods
	 * available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public TextItemHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Gets the text of this text element.
	 * 
	 * @return the text to display with the element, if this property value is
	 *         not set, return <code>null</code>.
	 */

	public String getContent( )
	{
		return getStringProperty( TextItem.CONTENT_PROP );
	}

	/**
	 * Sets the text for the text element.
	 * 
	 * @param value
	 *            the new content of the text item
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setContent( String value ) throws SemanticException
	{
		setStringProperty( TextItem.CONTENT_PROP, value );
	}

	/**
	 * Returns the content type of this text item. The content type will one of
	 * the following constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>CONTENT_TYPE_AUTO</code>
	 * <li><code>CONTENT_TYPE_PLAIN</code>
	 * <li><code>CONTENT_TYPE_HTML</code>
	 * <li><code>CONTENT_TYPE_RTF</code>
	 * </ul>
	 * 
	 * @return the content type. if this property value is not set, return
	 *         <code>null</code>.
	 * 
	 * @see org.eclipse.birt.report.model.elements.DesignChoiceConstants
	 */

	public String getContentType( )
	{
		return getStringProperty( TextItem.CONTENT_TYPE_PROP );
	}

	/**
	 * Sets the content type of this text item. The content type will one of the
	 * following constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>CONTENT_TYPE_AUTO</code>
	 * <li><code>CONTENT_TYPE_PLAIN</code>
	 * <li><code>CONTENT_TYPE_HTML</code>
	 * <li><code>CONTENT_TYPE_RTF</code>
	 * </ul>
	 * 
	 * @param contentType
	 *            the content type of this text item.
	 * 
	 * @throws SemanticException
	 *             if the value is not a valid choice item.
	 * @see org.eclipse.birt.report.model.elements.DesignChoiceConstants
	 *  
	 */

	public void setContentType( String contentType ) throws SemanticException
	{
		setStringProperty( TextItem.CONTENT_TYPE_PROP, contentType );
	}

	/**
	 * Gets the resource key of the text for the item.
	 * 
	 * @return the resource key of the text
	 */

	public String getContentKey( )
	{
		return getStringProperty( TextItem.CONTENT_RESOURCE_KEY_PROP );
	}

	/**
	 * Sets the resource key of the text for the item.
	 * 
	 * @param resourceKey
	 *            the resource key of the text
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setContentKey( String resourceKey ) throws SemanticException
	{
		setStringProperty( TextItem.CONTENT_RESOURCE_KEY_PROP, resourceKey );
	}
}