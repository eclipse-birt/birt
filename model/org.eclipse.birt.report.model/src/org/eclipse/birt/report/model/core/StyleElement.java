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

package org.eclipse.birt.report.model.core;

import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Represents an element that defines a style. An element that uses this style
 * is called a <em>client</em> element. This class manages the inverse
 * style-to-client relationship. It also handles sending notifications to the
 * client elements.
 * 
 */

public abstract class StyleElement extends ReferencableElement
{
	/**
	 * Default constructor.
	 */

	public StyleElement ( )
	{
	}

	/**
	 * Constructor with the element name.
	 * 
	 * @param theName
	 *            the element name
	 */

	public StyleElement ( String theName )
	{
		super ( theName );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.model.core.ReferencableElement#setDeliveryPath(org.eclipse.birt.report.model.activity.NotificationEvent)
	 */
	
	protected void adjustDeliveryPath ( NotificationEvent ev )
	{
		ev.setDeliveryPath ( NotificationEvent.STYLE_CLIENT ); 
	}
	
	/**
	 * Returns true if the element is style.
	 * 
	 * @return true if the element is style, otherwise return false.
	 *  
	 */

	public boolean isStyle( )
	{
		return true;
	}

	/**
	 * Gets the value of property.
	 * 
	 * @param design the report design
	 * @param prop definition of the property to get
	 * 
	 * @return the value of the property.
	 */

	public Object getFactoryProperty( ReportDesign design,
			ElementPropertyDefn prop )
	{
		return getProperty ( design, prop );
	}

	/**
	 * Gets the extended element of this element. Always return null cause style
	 * element is not allowed to extend.
	 * 
	 * @return null
	 *  
	 */
	public DesignElement getExtendsElement( )
	{
		return null;
	}

	/**
	 * Gets the name if the extended element. Always return null cause style
	 * element is not allowed to extend.
	 * 
	 * @return null
	 */

	public String getExtendsName( )
	{
		return null;
	}

	/**
	 * Sets the extended element. This operation is not allowed to do for style
	 * element.
	 * 
	 * @param base the base element to set 
	 */

	public void setExtendsElement( DesignElement base )
	{
		assert false;
	}

	/**
	 * Sets the extended element name. This operation is not allowed to do for
	 * style element.
	 * 
	 * @param name name of the base element to set 
	 */

	public void setExtendsName( String name )
	{
		assert false;
	}
}
