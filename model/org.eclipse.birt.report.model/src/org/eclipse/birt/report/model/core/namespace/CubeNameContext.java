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
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * The special case for the elements stored in cube name space, such as cube,
 * hierarchy, measure group and measure.
 */
public class CubeNameContext extends GeneralModuleNameContext
{

	/**
	 * Constructs one cube element name space.
	 * 
	 * @param module
	 *            the attached module
	 */

	CubeNameContext( Module module, int nameSpaceID )
	{
		super( module, nameSpaceID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.IModuleNameSpace#resolve
	 * (org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public ElementRefValue resolve( DesignElement focus, DesignElement element,
			PropertyDefn propDefn, ElementDefn elementDefn  )
	{
		if ( element == null )
			return null;

		String name = element.getName( );

		IElementDefn targetDefn = getTargetDefn( propDefn, elementDefn );
		if ( targetDefn != null )
		{
			if ( isCubeReferred( targetDefn ) )
			{
				return super.resolve( focus, element, propDefn, elementDefn );
			}
			else
			{
				Cube cube = findTarget( focus );
				if ( cube == null || !cube.canDynamicExtends( ) )
					return super.resolve( focus, element, propDefn, elementDefn );

				Cube referredCube = (Cube) cube.getDynamicExtends( cube
						.getRoot( ) );
				if ( referredCube == null )
					return new ElementRefValue( null, name );

				DesignElement retElement = cube.findLocalElement( name,
						targetDefn );
				if ( retElement != null )
					return new ElementRefValue( null, retElement );

				else
					return new ElementRefValue( null, name );
			}
		}
		else
		{
			return super.resolve( focus, element, propDefn, elementDefn );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.IModuleNameSpace#resolveNative
	 * (java.lang.String, org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public ElementRefValue resolve( DesignElement focus, String elementName,
			PropertyDefn propDefn, ElementDefn elementDefn  )
	{
		if ( StringUtil.isBlank( elementName ) )
			return null;

		IElementDefn targetDefn = getTargetDefn( propDefn, elementDefn );
		if ( targetDefn != null )
		{
			if ( isCubeReferred( targetDefn ) )
			{
				return super.resolve( focus, elementName, propDefn, elementDefn );
			}
			else
			{
				Cube cube = findTarget( focus );
				if ( cube == null || !cube.canDynamicExtends( ) )
					return super.resolve( focus, elementName, propDefn, elementDefn );

				Cube referredCube = (Cube) cube.getDynamicExtends( cube
						.getRoot( ) );
				if ( referredCube == null )
					return new ElementRefValue( null, elementName );

				DesignElement retElement = cube.findLocalElement( elementName,
						targetDefn );
				if ( retElement != null )
					return new ElementRefValue( null, retElement );

				else
					return new ElementRefValue( null, elementName );
			}
		}
		else
		{
			return super.resolve( focus, elementName, propDefn, elementDefn );
		}
	}

	private boolean isCubeReferred( IElementDefn targetDefn )
	{
		assert targetDefn != null;

		if ( targetDefn.isKindOf( MetaDataDictionary.getInstance( ).getElement(
				ReportDesignConstants.CUBE_ELEMENT ) ) )
			return true;

		return false;
	}

	private Cube findTarget( DesignElement focus )
	{
		if ( focus == null )
			return null;

		// if the focus referred a cube or it is a cube and the cube has dynamic
		// extends, then do some special resolve
		DesignElement element = focus;
		while ( element != null )
		{
			if ( element instanceof Cube )
				return (Cube) element;
			if ( element instanceof ReportItem )
			{
				ReportItem item = (ReportItem) element;
				Cube cube = (Cube) item.getCubeElement( item.getRoot( ) );
				if ( cube != null )
					return cube;
			}

			element = element.getContainer( );
		}

		return null;
	}
	
	private ElementDefn getTargetDefn( PropertyDefn propDefn, ElementDefn elementDefn )
	{
		if ( elementDefn != null )
			return elementDefn;
		return (ElementDefn) ( propDefn == null ? null : propDefn
				.getTargetElementType( ) );
	}
}
