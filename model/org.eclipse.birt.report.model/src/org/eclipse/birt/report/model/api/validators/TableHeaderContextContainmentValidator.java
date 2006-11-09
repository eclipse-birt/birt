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

import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the element is not allowed to appear in the specific slot of the
 * given container type in any level.
 * 
 * <h3>Rule</h3>
 * The rule is that whether the given element can recursively resides in the
 * specific slot of specific container type.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to <code>TableItem</code> and
 * <code>ListItem</code> currently.
 * 
 */

public class TableHeaderContextContainmentValidator
		extends
			AbstractElementValidator
{

	private final static TableHeaderContextContainmentValidator instance = new TableHeaderContextContainmentValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static TableHeaderContextContainmentValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether the given element can recursively resides in the
	 * specific slot of specific container type.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the element to validate
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( Module module, DesignElement element )
	{
		if ( !( element instanceof ListingElement ) )
			return Collections.EMPTY_LIST;

		return doValidate( module, element, IDesignElementModel.NO_SLOT );
	}

	/**
	 * Checks whether the <code>toValidate</code> is or is in the table
	 * element and its slotId is <code>TableItem.HEADER_SLOT</code>.
	 * 
	 * @param module
	 *            the module
	 * @param toValidate
	 *            the element to validate
	 * @param slotId
	 *            the slot id
	 * @return <code>true</code> if <code>toValidate</code> is a table item
	 *         or nested in the table item and the table slot is
	 *         <code>TableItem.HEADER_SLOT</code>.
	 */

	private List doValidate( Module module, DesignElement toValidate, int slotId )
	{
		List list = new ArrayList( );

		DesignElement curContainer = toValidate;
		int curSlotID = slotId;

		if ( slotId == IDesignElementModel.NO_SLOT )
		{
			curContainer = toValidate.getContainer( );
			curSlotID = toValidate.getContainerSlot( );
		}

		while ( curContainer != null )
		{
			IElementDefn containerDefn = curContainer.getDefn( );

			if ( ReportDesignConstants.TABLE_ITEM
					.equalsIgnoreCase( containerDefn.getName( ) )
					&& curSlotID == IListingElementModel.HEADER_SLOT )
			{
				list
						.add( new ContentException(
								curContainer,
								curSlotID,
								toValidate,
								ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT ) );
			}

			curSlotID = curContainer.getContainerSlot( );
			curContainer = curContainer.getContainer( );
		}
		return list;
	}

	/**
	 * Validates whether the given element can recursively resides in the
	 * specific slot of specific container type when trying to add an element.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the container element
	 * @param slotId
	 *            the slot where the new element to insert
	 * @param toAdd
	 *            the element to add
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validateForAdding( Module module, DesignElement element,
			int slotId, DesignElement toAdd )
	{
		if ( !( toAdd instanceof ListingElement )
				&& !( ModelUtil.containElement( toAdd,
						ReportDesignConstants.LISTING_ITEM ) ) )
			return Collections.EMPTY_LIST;

		return doValidate( module, element, slotId );
	}

	/**
	 * Validates whether the given element can recursively resides in the
	 * specific slot of specific container type when trying to add an element.
	 * 
	 * @param module
	 *            the root module of the element to validate
	 * @param element
	 *            the container element
	 * @param toAdd
	 *            the element definition to add
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validateForAdding( Module module, DesignElement element,
			IElementDefn toAdd )
	{
		ElementDefn listingDefn = (ElementDefn) MetaDataDictionary
				.getInstance( ).getElement( ReportDesignConstants.LISTING_ITEM );
		if ( !toAdd.isKindOf( listingDefn ) )
			return Collections.EMPTY_LIST;

		return doValidate( module, element, IDesignElementModel.NO_SLOT );
	}
}