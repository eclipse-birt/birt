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

import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.NamePropertyType;

/**
 * 
 */
public class DimensionNameHelper extends AbstractNameHelper
{

	protected Dimension dimension = null;

	/**
	 * 
	 * @param dimension
	 */
	public DimensionNameHelper( Dimension dimension )
	{
		super( );
		this.dimension = dimension;
		initialize( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.core.namespace.AbstractNameHelper#
	 * getNameSpaceCount()
	 */
	protected int getNameSpaceCount( )
	{
		return Dimension.NAME_SPACE_COUNT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.AbstractNameHelper#initialize
	 * ()
	 */
	protected void initialize( )
	{
		int count = getNameSpaceCount( );
		nameContexts = new INameContext[count];
		for ( int i = 0; i < count; i++ )
		{
			nameContexts[i] = NameContextFactory.createDimensionNameContext(
					dimension, i );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#getUniqueName
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */
	public String getUniqueName( DesignElement element )
	{
		if ( element == null )
			return null;

		ElementDefn eDefn = (ElementDefn) element.getDefn( );

		// if the element does not reside in the dimension, then get namehelper
		// for the element and get unique name from there
		if ( !dimension.getDefn( ).isKindOf(
				eDefn.getNameConfig( ).getNameContainer( ) ) )
		{
			INameHelper nameHelper = new NameExecutor( element )
					.getNameHelper( dimension.getRoot( ) );
			return nameHelper == null ? null : nameHelper
					.getUniqueName( element );
		}

		String name = StringUtil.trimString( element.getName( ) );

		// replace all the illegal chars with '_'
		name = NamePropertyType.validateName( name );

		// Some elements can have a blank name.
		if ( eDefn.getNameOption( ) == MetaDataConstants.NO_NAME )
			return null;

		if ( eDefn.getNameOption( ) == MetaDataConstants.OPTIONAL_NAME
				&& name == null && dimension.getRoot( ) instanceof ReportDesign )
			return null;

		// If the element already has a unique name, return it.
		int id = eDefn.getNameSpaceID( );
		NameSpace nameSpace = getCachedNameSpace( id );
		NameSpace moduleNameSpace = nameContexts[id].getNameSpace( );
		if ( name != null && isValidInNameSpace( nameSpace, element, name )
				&& isValidInNameSpace( moduleNameSpace, element, name ) )
			return name;

		// If the element has no name, create it as "New<new name>" where
		// "<new name>" is the new element display name for the element. Both
		// "New" and the new element display name are localized to the user's
		// locale.

		if ( name == null )
		{
			name = ModelMessages.getMessage( "New." //$NON-NLS-1$
					+ element.getDefn( ).getName( ) );
			name = name.trim( );
		}

		// Add a numeric suffix that makes the name unique.
		int index = 0;
		String baseName = name;
		while ( nameSpace.contains( name ) || moduleNameSpace.contains( name ) )
		{
			name = baseName + ++index;
		}

		return name;
	}

	/**
	 * Adds a element to the cached name space.
	 * 
	 * @param element
	 */
	public void addElement( DesignElement element )
	{
		if ( element == null || element.getName( ) == null )
			return;
		ElementDefn defn = (ElementDefn) element.getDefn( );
		if ( !dimension.getDefn( ).isKindOf(
				defn.getNameConfig( ).getNameContainer( ) ) )
			return;
		int id = defn.getNameSpaceID( );
		NameSpace ns = getCachedNameSpace( id );
		if ( !ns.contains( element.getName( ) ) )
			ns.insert( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#addContentName
	 * (int, java.lang.String)
	 */
	public void addContentName( int id, String name )
	{
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameHelper#getElement()
	 */
	public DesignElement getElement( )
	{
		return dimension;
	}
}
