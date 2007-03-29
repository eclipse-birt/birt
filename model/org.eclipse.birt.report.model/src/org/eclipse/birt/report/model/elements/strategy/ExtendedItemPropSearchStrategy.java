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

package org.eclipse.birt.report.model.elements.strategy;

import java.util.List;

import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Provides the specific property searching route for <code>ExtendedItem</code>.
 */

public class ExtendedItemPropSearchStrategy extends PropertySearchStrategy
{

	private final static ExtendedItemPropSearchStrategy instance = new ExtendedItemPropSearchStrategy( );

	/**
	 * Protected constructor.
	 */

	protected ExtendedItemPropSearchStrategy( )
	{
	}

	/**
	 * Returns the instance of <code>ExtendedItemPropSearchStrategy</code>
	 * which provide the specific property searching route for
	 * <code>ExtendedItem</code>.
	 * 
	 * @return the instance of <code>ExtendedItemPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance( )
	{
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.PropertySearchStrategy#getPropertyFromSelfSelector(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.core.DesignElement,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getPropertyFromSelfSelector( Module module,
			DesignElement element, ElementPropertyDefn prop )
	{
		ExtendedItem extendedItem = (ExtendedItem) element;
		Object value = null;

		// find the selector defined in extension definition
		IElementDefn elementDefn = extendedItem.getExtDefn( );
		if ( elementDefn != null )
		{
			String selector = extendedItem.getExtDefn( ).getSelector( );
			value = getPropertyFromSelector( module, prop, selector );
			if ( value != null )
				return value;
		}

		// find other pre-defined styles, such as selector : x-tab header, x-tab
		// detail, it has the highest priority than other selector
		value = getPropertyFromPredefinedStyles( module, extendedItem, prop );
		if ( value != null )
			return value;

		// find the "extended-item" selector
		String selector = ( (ElementDefn) extendedItem.getDefaultDefn( ) )
				.getSelector( );

		return getPropertyFromSelector( module, prop, selector );
	}

	/**
	 * Gets the property value from some predefined-styles in this extended
	 * item. Such as x-tab header, x-tab footer.
	 * 
	 * @param module
	 * @param extendedItem
	 * @param prop
	 * @return
	 */
	private Object getPropertyFromPredefinedStyles( Module module,
			ExtendedItem extendedItem, ElementPropertyDefn prop )
	{

		IReportItem reportItem = extendedItem.getExtendedElement( );
		try
		{
			if ( reportItem == null )
			{
				extendedItem.initializeReportItem( module );
				reportItem = extendedItem.getExtendedElement( );
			}
		}
		catch ( ExtendedElementException e )
		{
			// TODO: do some log
		}
		if ( reportItem != null )
		{
			List predefinedStyles = reportItem.getPredefinedStyles( );
			if ( predefinedStyles == null || predefinedStyles.isEmpty( ) )
				return null;
			for ( int i = 0; i < predefinedStyles.size( ); i++ )
			{
				Object predefinedStyle = predefinedStyles.get( i );

				// if the item is String, then search the named style in the
				// module and then find property value in it
				if ( predefinedStyle instanceof String )
				{
					String styleName = (String) predefinedStyle;
					Object value = getPropertyFromSelector( module, prop,
							styleName );
					if ( value != null )
						return value;
				}
				else if ( predefinedStyle instanceof IStyleDeclaration )
				{
					// if the item is a StyleHandle, then read local property
					// value set in this style directly
					IStyleDeclaration style = (IStyleDeclaration) predefinedStyle;
					Object value = style.getProperty( prop.getName( ) );
					if ( value != null )
					{
						// do some validation for the value
						try
						{
							value = prop.validateValue( module, value );
							if ( value != null )
								return value;
						}
						catch ( PropertyValueException e )
						{
							// do nothing
						}
					}
				}
			}
		}

		return null;
	}
}
