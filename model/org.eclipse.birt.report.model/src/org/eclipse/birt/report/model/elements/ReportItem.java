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

import org.eclipse.birt.report.model.api.validators.ElementReferenceValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

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
	 * @param design
	 *            the report design of the report item
	 * 
	 * @return the data set element defined on this specific element
	 */

	public DesignElement getDataSetElement( ReportDesign design )
	{
		ElementRefValue dataSetRef = (ElementRefValue) getProperty( design,
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

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		// Check the element reference of dataSet property

		list.addAll( ElementReferenceValidator.getInstance( ).validate( design,
				this, DATA_SET_PROP ) );

		list.addAll( validateStructureList( design, PARAM_BINDINGS_PROP ) );

		return list;
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

	protected boolean isInheritableProperty( ElementPropertyDefn prop )
	{
		assert prop != null;

		if ( Style.VERTICAL_ALIGN_PROP.equalsIgnoreCase( prop.getName( ) )
				&& getContainer( ) instanceof Cell )
			return true;

		return super.isInheritableProperty( prop );
	}
}
