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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.validators.ColumnBindingNameValidator;
import org.eclipse.birt.report.model.api.validators.ElementReferenceValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Base class for all report items. Represents anything that can be placed in a
 * layout container. Items have a size and position that are used in some of the
 * containers.
 * 
 */

public abstract class ReportItem extends StyledElement
		implements
			IReportItemModel
{

	/**
	 * Default constructor.
	 */

	public ReportItem( )
	{
	}

	/**
	 * Constructs the report item with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public ReportItem( String theName )
	{
		super( theName );
	}

	/**
	 * Returns the data set element, if any, for this element.
	 * 
	 * @param module
	 *            the report design of the report item
	 * 
	 * @return the data set element defined on this specific element
	 */

	public DesignElement getDataSetElement( Module module )
	{
		ElementRefValue dataSetRef = (ElementRefValue) getProperty( module,
				DATA_SET_PROP );
		if ( dataSetRef == null )
			return null;
		return dataSetRef.getElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( Module module )
	{
		List list = super.validate( module );

		// Check the element reference of dataSet property

		list.addAll( ElementReferenceValidator.getInstance( ).validate( module,
				this, DATA_SET_PROP ) );

		list.addAll( validateStructureList( module, PARAM_BINDINGS_PROP ) );

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getStrategy()
	 */

	public PropertySearchStrategy getStrategy( )
	{
		return ReportItemPropSearchStrategy.getInstance( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkStructureList(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.metadata.PropertyDefn, java.util.List,
	 *      org.eclipse.birt.report.model.api.core.IStructure)
	 */

	public void checkStructureList( Module module, PropertyDefn propDefn,
			List list, IStructure toAdd ) throws PropertyValueException
	{

		String propName = propDefn.getName( );
		if ( BOUND_DATA_COLUMNS_PROP.equals( propName ) )
		{
			List errorList = ColumnBindingNameValidator.getInstance( )
					.validateForAdding( getHandle( module ), propDefn, toAdd );

			if ( errorList.size( ) > 0 )
			{
				throw (PropertyValueException) errorList.get( 0 );
			}
		}

		super.checkStructureList( module, propDefn, list, toAdd );
	}
}
