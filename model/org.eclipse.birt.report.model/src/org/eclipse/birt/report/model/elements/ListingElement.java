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

import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.DataSetRequiredValidator;
import org.eclipse.birt.report.model.api.validators.GroupNameValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * This class represents the properties and slots common to the List and Table
 * elements.
 * 
 */

public abstract class ListingElement extends ReportItem
		implements
			IListingElementModel
{

	/**
	 * Default constructor.
	 */

	public ListingElement( )
	{
		super( );
		initSlots( );
	}

	/**
	 * Constructs the listing element with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public ListingElement( String theName )
	{
		super( theName );
		initSlots( );
	}

	/**
	 * Gets the list of groups. Groups are in order from the outer-most (most
	 * general) group to the inner-most (most specific) group.
	 * <p>
	 * The application MUST NOT modify this list. Use the handle class to make
	 * modifications.
	 * 
	 * @return the list of groups. The list contains <code>ListingGroup</code>
	 *         elements.
	 */

	public List getGroups( )
	{
		return slots[GROUP_SLOT].getContents( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert slot >= 0 && slot < getDefn( ).getSlotCount( );
		return slots[slot];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getDisplayLabel(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      int)
	 */

	public String getDisplayLabel( Module module, int level )
	{
		String displayLabel = super.getDisplayLabel( module, level );
		if ( level == IDesignElementModel.FULL_LABEL )
		{
			String name = getStringProperty( module,
					IReportItemModel.DATA_SET_PROP );
			name = limitStringLength( name );
			if ( !StringUtil.isBlank( name ) )
			{
				displayLabel += "(" + name + ")"; //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		return displayLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( Module module )
	{
		List list = super.validate( module );

		list.addAll( validateStructureList( module, SORT_PROP ) );
		list.addAll( validateStructureList( module, FILTER_PROP ) );

		// Check whether this table/list has data set or its List/Table
		// container has data set.

		if ( getDataSetElement( module ) == null )
		{
			list.addAll( DataSetRequiredValidator.getInstance( ).validate(
					module, this ) );
		}
		else
		{
			// do the check of the group name

			list.addAll( GroupNameValidator.getInstance( ).validate( module,
					this ) );
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.core.DesignElement, int,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected List checkContent( Module module, DesignElement container,
			int slotId, DesignElement content )
	{
		List errors = super.checkContent( module, container, slotId, content );
		if ( !errors.isEmpty( ) )
			return errors;

		// do the check of the group name

		if ( content instanceof GroupElement )
		{
			String checkedName = (String) content.getLocalProperty( module,
					IGroupElementModel.GROUP_NAME_PROP );
			if ( StringUtil.isBlank( checkedName ) )
				return errors;

			errors.addAll( GroupNameValidator.getInstance( )
					.validateForAddingGroup(
							(ListingHandle) getHandle( module ), checkedName ) );
		}

		return errors;
	}
}