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

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
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
		// find the selector defined in extension
		ExtendedItem extendedItem = (ExtendedItem) element;

		IElementDefn elementDefn = extendedItem.getExtDefn( );
		if ( elementDefn != null )
		{
			String selector = extendedItem.getExtDefn( ).getSelector( );
			Object value = getPropertyFromSelector( module, prop, selector );
			if ( value != null )
				return value;
		}

		// find the "extended-item" selector
		String selector = ( (ElementDefn) extendedItem.getDefaultDefn( ) )
				.getSelector( );

		return super.getPropertyFromSelector( module, prop, selector );
	}
}
