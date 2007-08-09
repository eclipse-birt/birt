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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Provides the specific property searching route for <code>GroupElement</code>.
 */

public class GroupPropSearchStrategy extends PropertySearchStrategy
{

	private final static GroupPropSearchStrategy instance = new GroupPropSearchStrategy( );

	private final static Set dataBindingProps;

	static
	{
		dataBindingProps = new HashSet( );
		dataBindingProps.add( IGroupElementModel.GROUP_NAME_PROP );
		dataBindingProps.add( IGroupElementModel.KEY_EXPR_PROP );
		dataBindingProps.add( IGroupElementModel.FILTER_PROP );
		dataBindingProps.add( IGroupElementModel.SORT_PROP );
		dataBindingProps.add( IGroupElementModel.INTERVAL_BASE_PROP );
		dataBindingProps.add( IGroupElementModel.INTERVAL_PROP );
		dataBindingProps.add( IGroupElementModel.INTERVAL_RANGE_PROP );
		dataBindingProps.add( IGroupElementModel.SORT_DIRECTION_PROP );
		dataBindingProps.add( IGroupElementModel.SORT_TYPE_PROP );
	}

	/**
	 * Protected constructor.
	 */
	protected GroupPropSearchStrategy( )
	{
	}

	/**
	 * Returns the instance of <code>GroupPropSearchStrategy</code> which
	 * provide the specific property searching route for
	 * <code>GroupElement</code>.
	 * 
	 * @return the instance of <code>GroupPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance( )
	{
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.PropertySearchStrategy#getPropertyFromSelf(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.core.DesignElement,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getPropertyFromSelf( Module module, DesignElement element,
			ElementPropertyDefn prop )
	{
		if ( !dataBindingProps.contains( prop.getName( ) ) )
			return super.getPropertyFromSelf( module, element, prop );

		GroupElement tmpGroup = findCorrespondingGroupElement( module, element );
		if ( tmpGroup == null )
			return super.getPropertyFromSelf( module, element, prop );

		return tmpGroup.getProperty( module, prop );
	}

	/**
	 * Returns the group element that contains the data group value and matches
	 * the given group element.
	 * 
	 * @param module
	 *            the root
	 * @param element
	 *            the group element
	 * @return the group element contains the data group value.
	 */

	private GroupElement findCorrespondingGroupElement( Module module,
			DesignElement element )
	{
		DesignElement tmpContainer = element.getContainer( );

		if ( tmpContainer == null )
			return null;

		// the data binding reference property has high priority than local
		// properties.

		ElementRefValue refValue = (ElementRefValue) tmpContainer
				.getLocalProperty( module,
						IReportItemModel.DATA_BINDING_REF_PROP );

		DesignElement target = null;
		while ( refValue != null && refValue.isResolved( ) )
		{
			target = refValue.getElement( );
			if ( target.getDefn( ) != tmpContainer.getDefn( ) )
				break;

			tmpContainer = target;
			refValue = (ElementRefValue) tmpContainer.getLocalProperty( module,
					IReportItemModel.DATA_BINDING_REF_PROP );
		}

		if ( !( target instanceof ListingElement ) )
			return null;

		int index = element.getContainerInfo( ).indexOf( element );

		ListingElement listing = (ListingElement) target;
		List groups = listing.getGroups( );

		if ( groups.isEmpty( ) || groups.size( ) <= index )
			return null;

		return (GroupElement) groups.get( index );
	}

	/**
	 * @param propName
	 * @return
	 */

	public static boolean isDataBindingProperty( String propName )
	{
		return dataBindingProps.contains( propName );
	}
}
