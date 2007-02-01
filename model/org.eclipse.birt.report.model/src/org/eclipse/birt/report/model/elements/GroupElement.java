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

import org.eclipse.birt.report.model.api.validators.ValueRequiredValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class represents a grouping level within either a List or Table item.
 * Groups are defined by a grouping expression on the data set.
 * 
 */

public abstract class GroupElement extends DesignElement
		implements
			IGroupElementModel
{

	/**
	 * The constants value representing no group level.
	 */

	private static final int LEVEL_NOT_SET = -1;

	/**
	 * The group level.
	 */

	protected int groupLevel = LEVEL_NOT_SET;

	/**
	 * Default constructor. Note that groups do not have names.
	 */

	public GroupElement( )
	{
		initSlots( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert slot >= 0 && slot < SLOT_COUNT;
		return slots[slot];
	}

	/**
	 * Returns the level of this group within the list. The grouping level is
	 * cached for performance.
	 * 
	 * @return the 1-based grouping level of this group
	 */

	public int getGroupLevel( )
	{
		DesignElement container = getContainer( );
		if ( container == null )
			groupLevel = LEVEL_NOT_SET;
		else
		{
			groupLevel = getContainerInfo( ).indexOf( this ) + 1;
		}
		return groupLevel;
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

		list.addAll( ValueRequiredValidator.getInstance( ).validate( module,
				this, KEY_EXPR_PROP ) );

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getNameForDisplayLabel()
	 */

	protected String getNameForDisplayLabel( )
	{
		// This getting is not relative to design.

		return (String) getLocalProperty( null, GROUP_NAME_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getFactoryProperty(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getFactoryProperty( Module module, ElementPropertyDefn prop )
	{
		if ( !prop.isStyleProperty( ) )
			return super.getFactoryProperty( module, prop );

		return getStrategy( ).getPropertyFromElement( module, this, prop );
	}
}