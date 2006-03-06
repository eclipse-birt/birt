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

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.ValueRequiredValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.strategy.GroupElementPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.SlotDefn;

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
	 * The set of slots for the group.
	 */

	protected ContainerSlot slots[] = null;

	/**
	 * Default constructor. Note that groups do not have names.
	 */

	public GroupElement( )
	{
		initSlots( );
	}

	/**
	 * Privates method to create the slots.
	 */

	private void initSlots( )
	{
		slots = new ContainerSlot[SLOT_COUNT];
		for ( int i = 0; i < slots.length; i++ )
			slots[i] = new MultiElementSlot( );
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
		if ( container == null )
			groupLevel = LEVEL_NOT_SET;
		else
		{
			ContainerSlot slot = container.getSlot( containerSlotID );
			groupLevel = slot.findPosn( this ) + 1;
		}
		return groupLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSelectors(int)
	 */

	public String getSelector( int slotID )
	{
		IElementDefn defn = getDefn( );
		SlotDefn slotDefn = (SlotDefn) defn.getSlot( slotID );
		if ( slotDefn == null )
		{
			return null;
		}

		int depth = getGroupLevel( );
		if ( depth > 9 )
			depth = 9;

		String slotSelector = slotDefn.getSelector( );
		if ( StringUtil.isBlank( slotSelector ) )
		{
			return null;
		}

		return slotSelector + "-" + Integer.toString( depth ); //$NON-NLS-1$
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

	/**
	 * Makes a clone of this group element. The cloned element contains all of
	 * the copied slot defined in the original one.
	 * 
	 * @return the cloned group element.
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		GroupElement element = (GroupElement) super.clone( );
		element.initSlots( );

		for ( int i = 0; i < slots.length; i++ )
		{
			element.slots[i] = slots[i].copy( element, i );
		}
		return element;
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
	 * @see org.eclipse.birt.report.model.core.DesignElement#getStrategy()
	 */

	public PropertySearchStrategy getStrategy( )
	{
		return GroupElementPropSearchStrategy.getInstance( );
	}
}