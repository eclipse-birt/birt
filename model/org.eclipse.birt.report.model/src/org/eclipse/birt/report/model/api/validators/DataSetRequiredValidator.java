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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the data set of a listing element should be provided.
 * 
 * <h3>Rule</h3>
 * The rule is that the <code>ListingElement.DATA_SET_PROP</code> should be
 * set on the element itself or its container which is also a listing element.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to <code>ListingElement</code>.
 */

public class DataSetRequiredValidator extends AbstractElementValidator
{

	private final static DataSetRequiredValidator instance = new DataSetRequiredValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static DataSetRequiredValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether the data set of the given listing element is provided.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the listing element to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( ReportDesign design, DesignElement element )
	{
		if ( !( element instanceof ListingElement ) )
			return Collections.EMPTY_LIST;

		return doValidate( design, (ListingElement) element );
	}

	private List doValidate( ReportDesign design, ListingElement toValidate )
	{
		List list = new ArrayList( );

		DesignElement container = toValidate;
		int slot = toValidate.getContainerSlot( );

		boolean dataSetFound = false;
		while ( container.getContainer( ) != null && !dataSetFound )
		{
			if ( container instanceof ListingElement )
			{
				if ( ( (ListingElement) container ).getDataSetElement( design ) != null )
				{
					dataSetFound = true;
				}
			}

			slot = container.getContainerSlot( );
			container = container.getContainer( );
		}

		// Since element in components slot is considered as incompletely
		// defined, the data set is not required on table in components.

		if ( !dataSetFound && ReportDesign.COMPONENT_SLOT != slot )
		{
			list.add( new SemanticError( toValidate,
					SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET ) );
		}
		return list;
	}

}