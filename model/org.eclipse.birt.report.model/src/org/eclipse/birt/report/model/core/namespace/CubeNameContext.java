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
import org.eclipse.birt.report.model.elements.olap.Dimension;
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
	 * @param nameSpaceID
	 *            the name space ID
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
			PropertyDefn propDefn, ElementDefn elementDefn )
	{
		if ( element == null )
			return null;

		String elementName = element.getName( );

		ElementDefn targetDefn = getTargetDefn( propDefn, elementDefn );
		if ( targetDefn == null || isCubeReferred( targetDefn ) )
			return super.resolve( focus, element, propDefn, elementDefn );

		// dimension to shared dimension case
		int nameSpaceID = targetDefn.getNameSpaceID( );

		if ( nameSpaceID == Module.DIMENSION_NAME_SPACE )
		{
			if ( focus instanceof Dimension )
				return super.resolve( focus, element, propDefn, elementDefn );
		}

		// the focus is data object cube.
		if ( focus != null && focus.canDynamicExtends( ) )
		{
			Cube referredCube = (Cube) focus.getDynamicExtendsElement( focus
					.getRoot( ) );
			if ( referredCube == null )
				return new ElementRefValue( null, elementName );
		}

		Cube cube = findTarget( focus );
		if ( cube == null )
			return super.resolve( focus, element, propDefn, elementDefn );

		//TODO cache the element definitions in two resolve methods.
		if ( targetDefn.isKindOf( MetaDataDictionary.getInstance( ).getElement(
				ReportDesignConstants.HIERARCHY_ELEMENT ) )
				|| targetDefn.isKindOf( MetaDataDictionary.getInstance( )
						.getElement( ReportDesignConstants.DIMENSION_ELEMENT ) ) )
		{
			DesignElement retElement = cube.findLocalElement( elementName,
					targetDefn );
			if ( retElement != null )
				return new ElementRefValue( null, retElement );
		}
		
		return new ElementRefValue( null, elementName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.IModuleNameSpace#resolveNative
	 * (java.lang.String, org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public ElementRefValue resolve( DesignElement focus, String elementName,
			PropertyDefn propDefn, ElementDefn elementDefn )
	{
		if ( StringUtil.isBlank( elementName ) )
			return null;

		ElementDefn targetDefn = getTargetDefn( propDefn, elementDefn );
		if ( targetDefn == null || isCubeReferred( targetDefn ) )
			return super.resolve( focus, elementName, propDefn, elementDefn );

		// dimension to shared dimension case
		int nameSpaceID = targetDefn.getNameSpaceID( );

		if ( nameSpaceID == Module.DIMENSION_NAME_SPACE )
		{
			if ( focus instanceof Dimension )
				return super
						.resolve( focus, elementName, propDefn, elementDefn );
		}

		// data object cube case.
		if ( focus != null && focus.canDynamicExtends( ) )
		{
			Cube referredCube = (Cube) focus.getDynamicExtendsElement( focus
					.getRoot( ) );
			if ( referredCube == null )
				return new ElementRefValue( null, elementName );
		}

		Cube cube = findTarget( focus );
		if ( cube == null )
			return super.resolve( focus, elementName, propDefn, elementDefn );

		if ( targetDefn.isKindOf( MetaDataDictionary.getInstance( ).getElement(
				ReportDesignConstants.HIERARCHY_ELEMENT ) )
				|| targetDefn.isKindOf( MetaDataDictionary.getInstance( )
						.getElement( ReportDesignConstants.DIMENSION_ELEMENT ) ) )
		{
			DesignElement retElement = cube.findLocalElement( elementName,
					targetDefn );
			if ( retElement != null )
				return new ElementRefValue( null, retElement );

			// not resolved
			return new ElementRefValue( null, elementName );
		}

		return super.resolve( focus, elementName, propDefn, elementDefn );
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

	private ElementDefn getTargetDefn( PropertyDefn propDefn,
			ElementDefn elementDefn )
	{
		if ( elementDefn != null )
			return elementDefn;
		return (ElementDefn) ( propDefn == null ? null : propDefn
				.getTargetElementType( ) );
	}
}
