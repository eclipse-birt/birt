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

package org.eclipse.birt.report.model.core.namespace;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.metadata.ElementDefn;

/**
 * 
 */
public class NameExecutor
{

	protected DesignElement focus;

	/**
	 * 
	 */
	public static final String NAME_SEPARATOR = "/"; //$NON-NLS-1$

	/**
	 * 
	 * @param element
	 */
	public NameExecutor( DesignElement element )
	{
		if ( element == null )
			throw new IllegalArgumentException( "The element can not be null" ); //$NON-NLS-1$
		this.focus = element;
	}

	/**
	 * 
	 * @param module
	 * @return
	 */
	public INameHelper getNameHelper( Module module )
	{
		ElementDefn elementDefn = (ElementDefn) focus.getDefn( );
		IElementDefn holderDefn = elementDefn.getNameConfig( )
				.getNameContainer( );

		if ( holderDefn == null )
			return null;

		// if hold is not module, then search the name container
		if ( !ReportDesignConstants.MODULE_ELEMENT.equalsIgnoreCase( holderDefn
				.getName( ) ) )
		{
			DesignElement e = focus;
			while ( e != null )
			{
				if ( e.getDefn( ).isKindOf( holderDefn ) )
				{
					if ( e instanceof INameContainer )
						return ( (INameContainer) e ).getNameHelper( );
				}
				e = e.getContainer( );
			}

			// if not found, then return null
			return null;
		}

		return module == null ? null : module.getNameHelper( );
	}

	/**
	 * Gets the name space where the name of this element resides.
	 * 
	 * @param module
	 * @return
	 */
	public NameSpace getNameSpace( Module module )
	{
		int id = ( (ElementDefn) focus.getDefn( ) ).getNameSpaceID( );
		INameHelper container = getNameHelper( module );
		return container == null ? null : container.getNameSpace( id );

	}
}
