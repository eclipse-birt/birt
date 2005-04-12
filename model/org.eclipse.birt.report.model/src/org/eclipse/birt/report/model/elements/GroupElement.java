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
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.validators.ValueRequiredValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MultiElementSlot;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.SlotDefn;

/**
 * This class represents a grouping level within either a List or Table item.
 * Groups are defined by a grouping expression on the data set.
 *  
 */

public abstract class GroupElement extends DesignElement
{

	/**
	 * Identifier for the group header slot.
	 */

	public static final int HEADER_SLOT = 0;

	/**
	 * Identifier for the group footer slot.
	 */

	public static final int FOOTER_SLOT = 1;

	/**
	 * Number of slots defined for a group.
	 */

	public static final int SLOT_COUNT = 2;

	/**
	 * Name of the group name property. The group name property is provided
	 * because group element is not in element name space and can not use the
	 * name property of <code>DesignElement</code>.
	 */

	public static final String GROUP_NAME_PROP = "groupName"; //$NON-NLS-1$

	/**
	 * Name of the key expression property. This determines the data value used
	 * to define each group.
	 */

	public static final String KEY_EXPR_PROP = "keyExpr"; //$NON-NLS-1$

	/**
	 * Name of the grouping interval property. This is a choice with values such
	 * as "year", "month" and "day."
	 */

	public static final String INTERVAL_PROP = "interval"; //$NON-NLS-1$

	/**
	 * Name of the grouping interval range property. The range says how many
	 * intervals to group together. For example, 3 months or 6 hours.
	 */

	public static final String INTERVAL_RANGE_PROP = "intervalRange"; //$NON-NLS-1$

	/**
	 * Name of the sort direction property. Defines the direction of sorting for
	 * the groups themselves.
	 */

	public static final String SORT_DIRECTION_PROP = "sortDirection"; //$NON-NLS-1$

	/**
	 * Name of the Sort property, sort is a list of <code>SortKey</code>.
	 */

	public static final String SORT_PROP = "sort"; //$NON-NLS-1$

	/**
	 * Name of the TOC expression property. This determines the TOC entry to
	 * appear for this group.
	 */

	public static final String TOC_PROP = "toc"; //$NON-NLS-1$

	/**
	 * Name of the filter property. This defines the filter criteria to match
	 * the rows to appear.
	 */

	public static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * Name of the property that provides the script called before the first row
	 * is retrieved from the data set for this element. Called after the data
	 * set is open but before the header band is created.
	 */

	public static final String ON_START_METHOD = "onStart"; //$NON-NLS-1$

	/**
	 * Name of the property that provides the script called for each row
	 * retrieved from the data set for this element, but before creating any
	 * content for that row.
	 */

	public static final String ON_ROW_METHOD = "onRow"; //$NON-NLS-1$

	/**
	 * Name of the property that provides the script called after the last row
	 * is read from the data set for this element, but before the footer band is
	 * created.
	 */

	public static final String ON_FINISH_METHOD = "onFinish"; //$NON-NLS-1$

	/**
	 * The set of slots for the group.
	 */

	protected ContainerSlot slots[] = null;

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
		if ( groupLevel == LEVEL_NOT_SET )
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

	public String[] getSelectors( int slotID )
	{
		String[] selectors = {null, null};

		IElementDefn defn = getDefn( );
		SlotDefn slotDefn = (SlotDefn) defn.getSlot( slotID );
		String selector = ( (ElementDefn) defn ).getSelector( );

		if ( slotDefn == null )
		{
			selectors[0] = selector;
			return selectors;
		}

		int depth = getGroupLevel( );
		if ( depth > 9 )
			depth = 9;

		String slotSelector = slotDefn.getSelector( );
		if ( StringUtil.isBlank( slotSelector ) )
		{
			selectors[0] = selector;
			return selectors;
		}

		selectors[0] = selector
				+ "-" + slotSelector + "-" + Integer.toString( depth ); //$NON-NLS-1$ //$NON-NLS-2$
		selectors[1] = slotSelector + "-" + Integer.toString( depth ); //$NON-NLS-1$

		return selectors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		list.addAll( validateStructureList( design, SORT_PROP ) );
		list.addAll( validateStructureList( design, FILTER_PROP ) );

		list.addAll( ValueRequiredValidator.getInstance( ).validate( design,
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
}