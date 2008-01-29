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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Provides the specific property searching route for <code>ReportItem</code>,
 * especially about how to apply CSS rules on <code>ReportItem</code>.
 */

public class ReportItemPropSearchStrategy extends PropertySearchStrategy
{

	private final static ReportItemPropSearchStrategy instance = new ReportItemPropSearchStrategy( );

	/**
	 * Data binding properties for the listing elements.
	 */

	protected final static Set listingElementDataBindingProps;

	/**
	 * Data binding properties for the extended elements.
	 */

	protected final static Set extendedItemDataBindingProps;

	/**
	 * Data binding properties for the report items.
	 */

	protected final static Set reportItemDataBindingProps;

	static
	{
		Set tmpSet = new HashSet( );
		tmpSet.add( IReportItemModel.PARAM_BINDINGS_PROP );
		tmpSet.add( IReportItemModel.BOUND_DATA_COLUMNS_PROP );
		tmpSet.add( IReportItemModel.DATA_SET_PROP );
		tmpSet.add( IReportItemModel.CUBE_PROP );
		tmpSet.add( IListingElementModel.FILTER_PROP );
		tmpSet.add( IListingElementModel.SORT_PROP );
		listingElementDataBindingProps = Collections.unmodifiableSet( tmpSet );

		tmpSet = new HashSet( );
		tmpSet.add( IReportItemModel.PARAM_BINDINGS_PROP );
		tmpSet.add( IReportItemModel.BOUND_DATA_COLUMNS_PROP );
		tmpSet.add( IReportItemModel.DATA_SET_PROP );
		tmpSet.add( IReportItemModel.CUBE_PROP );
		tmpSet.add( IExtendedItemModel.FILTER_PROP );
		extendedItemDataBindingProps = Collections.unmodifiableSet( tmpSet );

		tmpSet = new HashSet( );
		tmpSet = new HashSet( );
		tmpSet.add( IReportItemModel.PARAM_BINDINGS_PROP );
		tmpSet.add( IReportItemModel.BOUND_DATA_COLUMNS_PROP );
		tmpSet.add( IReportItemModel.DATA_SET_PROP );
		tmpSet.add( IReportItemModel.CUBE_PROP );
		reportItemDataBindingProps = Collections.unmodifiableSet( tmpSet );
	}

	/**
	 * Protected constructor.
	 */

	protected ReportItemPropSearchStrategy( )
	{
	}

	/**
	 * Returns the instance of <code>ReportItemPropSearchStrategy</code> which
	 * provide the specific property searching route for <code>ReportItem</code>.
	 * 
	 * @return the instance of <code>ReportItemPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance( )
	{
		return instance;
	}

	/**
	 * Tests if the property of a cell is inheritable in the context.
	 * <p>
	 * If the cell resides in the row and the property is "vertical-align",
	 * return <code>true</code>. Otherwise, return the value from its super
	 * class.
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#isInheritableProperty(org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected boolean isInheritableProperty( DesignElement element,
			ElementPropertyDefn prop )
	{
		assert prop != null;

		if ( IStyleModel.VERTICAL_ALIGN_PROP.equalsIgnoreCase( prop.getName( ) )
				&& element.getContainer( ) instanceof Cell )
			return true;

		return super.isInheritableProperty( element, prop );
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
		if ( !getDataBindingProperties( element ).contains( prop.getName( ) ) )
			return super.getPropertyFromSelf( module, element, prop );

		// the data binding reference property has high priority than local
		// properties.

		ElementRefValue refValue = (ElementRefValue) element.getLocalProperty(
				module, IReportItemModel.DATA_BINDING_REF_PROP );
		if ( refValue == null || !refValue.isResolved( ) )
			return super.getPropertyFromSelf( module, element, prop );

		return refValue.getElement( ).getProperty( module, prop );
	}

	/**
	 * Returns properties that are bound to data related values.
	 * 
	 * @param tmpElement
	 *            the design element
	 * @return a set containing property names in string
	 */

	public static Set getDataBindingProperties( DesignElement tmpElement )
	{

		if ( tmpElement instanceof ListingElement )
		{
			return listingElementDataBindingProps;
		}
		else if ( tmpElement instanceof ExtendedItem )
		{
			return extendedItemDataBindingProps;
		}
		else if ( tmpElement instanceof ReportItem )
			return reportItemDataBindingProps;
		else
			return Collections.EMPTY_SET;

	}
}
