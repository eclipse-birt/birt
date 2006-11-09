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

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ListingElement;
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
	 * @param module
	 *            the module
	 * @param element
	 *            the listing element to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( Module module, DesignElement element )
	{
		if ( !( element instanceof ListingElement || element instanceof ExtendedItem ) )
			return Collections.EMPTY_LIST;

		return doValidate( module, element );
	}

	private List doValidate( Module module, DesignElement toValidate )
	{
		List list = new ArrayList( );

		DesignElement container = toValidate;
		int slot = toValidate.getContainerSlot( );

		boolean dataSetFound = false;
		if ( toValidate instanceof ExtendedItem )
		{
			if ( ( (ExtendedItem) toValidate ).getDataSetElement( module ) != null )
			{
				dataSetFound = true;
			}
			else
			{
				while ( container.getContainer( ) != null )
				{
					slot = container.getContainerSlot( );
					container = container.getContainer( );
				}
			}
		}
		else if ( toValidate instanceof ListingElement )
		{
			while ( container.getContainer( ) != null && !dataSetFound )
			{
				if ( container instanceof ListingElement )
				{
					if ( ( (ListingElement) container )
							.getDataSetElement( module ) != null )
					{
						dataSetFound = true;
						break;
					}
				}

				slot = container.getContainerSlot( );
				container = container.getContainer( );
			}
		}
		else
		{
			// now the check is only employed to listing elements, extended
			// items.

			assert false;
		}

		// Since element in components slot is considered as incompletely
		// defined, the data set is not required on table in components.

		if ( !dataSetFound && IModuleModel.COMPONENT_SLOT != slot )
		{
			list.add( new SemanticError( toValidate,
					SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET ) );
		}
		return list;
	}

}