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

import java.util.Iterator;

import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.ReferenceableElement.BackRef;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PredefinedStyle;

/**
 * Represents an element that defines a style. An element that uses this style
 * is called a <em>client</em> element. This class manages the inverse
 * style-to-client relationship. It also handles sending notifications to the
 * client elements.
 *  
 */

public abstract class StyleElement extends ReferenceableElement
{

	/**
	 * Default constructor.
	 */

	public StyleElement( )
	{
	}

	/**
	 * Constructor with the element name.
	 * 
	 * @param theName
	 *            the element name
	 */

	public StyleElement( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferenceableElement#setDeliveryPath(org.eclipse.birt.report.model.activity.NotificationEvent)
	 */

	protected void adjustDeliveryPath( NotificationEvent ev )
	{
		ev.setDeliveryPath( NotificationEvent.STYLE_CLIENT );
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
	 * @param design
	 *            the report design
	 * @param prop
	 *            definition of the property to get
	 * 
	 * @return the value of the property.
	 */

	public Object getFactoryProperty( ReportDesign design,
			ElementPropertyDefn prop )
	{
		return getProperty( design, prop );
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
	 * @param base
	 *            the base element to set
	 */

	public void setExtendsElement( DesignElement base )
	{
		assert false;
	}

	/**
	 * Sets the extended element name. This operation is not allowed to do for
	 * style element.
	 * 
	 * @param name
	 *            name of the base element to set
	 */

	public void setExtendsName( String name )
	{
		assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferenceableElement#broadcastToClients(org.eclipse.birt.report.model.activity.NotificationEvent,
	 *      org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	protected void broadcastToClients( NotificationEvent ev, ReportDesign design )
	{
		super.broadcastToClients( ev, design );

		// Broad the event to the elements selected by selector style.

		PredefinedStyle predefinedStyle = MetaDataDictionary.getInstance( )
				.getPredefinedStyle( getName( ) );
		if ( predefinedStyle != null )
		{
			broadcastToSelectedElementsInSlot( ev, design, design
					.getSlot( ReportDesign.COMPONENT_SLOT ) );
			broadcastToSelectedElementsInSlot( ev, design, design
					.getSlot( ReportDesign.PAGE_SLOT ) );
			broadcastToSelectedElementsInSlot( ev, design, design
					.getSlot( ReportDesign.BODY_SLOT ) );
			broadcastToSelectedElementsInSlot( ev, design, design
					.getSlot( ReportDesign.SCRATCH_PAD_SLOT ) );
		}
	}

	/**
	 * Broadcasts the event to all elements in the given slot if the elements
	 * are selected by selector style.
	 * 
	 * @param ev
	 *            the event to send
	 * @param design
	 *            the report design
	 * @param slot
	 *            the slot to send
	 */
	
	private void broadcastToSelectedElementsInSlot( NotificationEvent ev,
			ReportDesign design, ContainerSlot slot )
	{
		Iterator iter = slot.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElement element = (DesignElement) iter.next( );
			assert element != null;

			// Broadcast the element which is selected by this style
			
			String selector = element.getDefn( ).getSelector( );
			if ( selector != null && selector.equalsIgnoreCase( getName( ) ) )
				element.broadcast( ev, design );

			String[] selectors = element.getContainer( ).getSelectors(
					element.getContainerSlot( ) );
			for ( int i = 0; i < selectors.length; i++ )
			{
				if ( selectors[i] != null && selectors[i].equalsIgnoreCase( getName( ) ) )
					element.broadcast( ev, design );
			}

			int count = element.getDefn( ).getSlotCount( );
			for ( int i = 0; i < count; i++ )
			{
				broadcastToSelectedElementsInSlot( ev, design, element
						.getSlot( i ) );
			}
		}
	}
}