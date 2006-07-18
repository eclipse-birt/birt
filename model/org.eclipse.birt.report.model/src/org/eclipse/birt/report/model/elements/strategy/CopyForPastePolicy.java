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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * This policy is a copy policy for pasting, which means, after copying, the
 * original object is deeply cloned, and the target object can be pasted to
 * every where.
 */

public class CopyForPastePolicy extends CopyPolicy
{

	/**
	 * Private constructor.
	 */

	private CopyForPastePolicy( )
	{
	}

	private final static CopyForPastePolicy instance = new CopyForPastePolicy( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.strategy.CopyStrategy#execute(org.eclipse.birt.report.model.core.DesignElement,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void execute( DesignElement source, DesignElement destination )
	{
		if ( destination.getExtendsName( ) == null
				&& !destination.isVirtualElement( ) )
		{
			clearDisplayName( destination );
			return;
		}

		destination.setExtendsName( null );
		destination.setBaseId( DesignElement.NO_BASE_ID );

		// copy user property definitions first, otherwise definition will
		// not be found when copying property values

		Iterator iter = null;
		DesignElement current = null;
		if ( !source.isVirtualElement( ) )
		{
			current = source.getExtendsElement( );
			while ( current != null )
			{
				if ( current.hasUserProperties( ) )
				{
					iter = current.getLocalUserProperties( ).iterator( );
					while ( iter.hasNext( ) )
					{
						UserPropertyDefn uDefn = (UserPropertyDefn) iter.next( );
						if ( destination.getLocalUserPropertyDefn( uDefn
								.getName( ) ) != null )
							continue;
						destination
								.addUserPropertyDefn( (UserPropertyDefn) uDefn
										.copy( ) );
					}
				}

				current = current.getExtendsElement( );
			}
		}

		// the element id is the same as the matrix, then if we add the copy
		// to the design tree, we will check the id and re-allocate a unique
		// name for it. This is the same issue as the name does.

		iter = source.getPropertyDefns( ).iterator( );
		Module module = source.getRoot( );
		while ( iter.hasNext( ) )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) iter.next( );
			if ( !propDefn.canInherit( ) )
				continue;

			String propName = propDefn.getName( );

			// Style property and extends property will be removed.
			// The properties inherited from style or parent will be
			// flatten to new element.

			if ( StyledElement.STYLE_PROP.equals( propName )
					|| DesignElement.EXTENDS_PROP.equals( propName )
					|| DesignElement.USER_PROPERTIES_PROP.equals( propName ) )
				continue;

			if ( destination.getLocalProperty( null, propDefn ) != null )
				continue;

			current = source.isVirtualElement( )
					? source.getVirtualParent( )
					: source.getExtendsElement( );
			while ( current != null )
			{
				Object value = current.getLocalProperty( module, propDefn );
				if ( value != null )
				{
					destination.setProperty( propDefn, ModelUtil.copyValue(
							propDefn, value ) );
					break;
				}

				current = current.isVirtualElement( ) ? current
						.getVirtualParent( ) : current.getExtendsElement( );
			}
		}

		clearDisplayName( destination );
	}

	/**
	 * Auxiliary function helps to clear display name and diaplay name id.
	 * 
	 * @param e
	 *            the design element need to clear display name infos.
	 */

	private void clearDisplayName( DesignElement e )
	{
		// clear text-property of displayName
		if ( e.getLocalProperty( null, DesignElement.DISPLAY_NAME_PROP ) != null )
			e.setProperty( DesignElement.DISPLAY_NAME_PROP, null );

		// clear text-property of displayNameID

		if ( e.getLocalProperty( null, DesignElement.DISPLAY_NAME_ID_PROP ) != null )
			e.setProperty( DesignElement.DISPLAY_NAME_ID_PROP, null );
	}

	/**
	 * Returns the instance of this class.
	 * 
	 * @return the instance of this class
	 */

	public static CopyForPastePolicy getInstance( )
	{
		return instance;
	}

}
