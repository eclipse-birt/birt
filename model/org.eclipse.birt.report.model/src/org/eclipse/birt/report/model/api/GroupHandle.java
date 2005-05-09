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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;

/**
 * Represents both list and table groups in the design. Groups provide a way of
 * showing common headings for a group of related rows.
 * <p>
 * A group is defined by a group key. The key is a column from the query. If the
 * group key is a time field then user often want to group on an interval such
 * as month or quarter.
 * 
 * @see org.eclipse.birt.report.model.elements.GroupElement
 * @see SlotHandle
 */

public abstract class GroupHandle extends ReportElementHandle
		implements
			IGroupElementModel
{

	/**
	 * Constructs a group handle with the given design and the design element.
	 * The application generally does not create handles directly. Instead, it
	 * uses one of the navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public GroupHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the header slot in the group. The header slot represents
	 * subsections that print at the start of the group.
	 * 
	 * @return a slot handle to the header
	 */

	public SlotHandle getHeader( )
	{
		return getSlot( GroupElement.HEADER_SLOT );
	}

	/**
	 * Returns the footer slot. The footer slot represents subsections that
	 * print at the end of the group.
	 * 
	 * @return a slot handle to the footer
	 */

	public SlotHandle getFooter( )
	{
		return getSlot( GroupElement.FOOTER_SLOT );
	}

	/**
	 * Gets the expression that defines the group. This is normally simply a
	 * reference to a data set column.
	 * 
	 * @return the expression as a string
	 * 
	 * @see #setKeyExpr(String)
	 */

	public String getKeyExpr( )
	{
		return getStringProperty( GroupElement.KEY_EXPR_PROP );
	}

	/**
	 * Gets the name of the group.
	 * 
	 * @return the name of the group
	 */

	public String getName( )
	{
		return getStringProperty( GroupElement.GROUP_NAME_PROP );
	}

	/**
	 * Gets the on-start script of the group.
	 * 
	 * @return the on-start script of the group
	 */

	public String getOnStart( )
	{
		return getStringProperty( GroupElement.ON_START_METHOD );
	}

	/**
	 * Gets the on-row script of the group.
	 * 
	 * @return the on-row script of the group
	 */

	public String getOnRow( )
	{
		return getStringProperty( GroupElement.ON_ROW_METHOD );
	}

	/**
	 * Gets the on-finish script of the group.
	 * 
	 * @return the on-finish script of the group
	 */

	public String getOnFinish( )
	{
		return getStringProperty( GroupElement.ON_FINISH_METHOD );
	}

	/**
	 * Sets the group name.
	 * 
	 * @param theName
	 *            the group name to set
	 */

	public void setName( String theName )
	{
		try
		{
			// trim the name, have the same behavior as Name property.

			setProperty( GroupElement.GROUP_NAME_PROP, StringUtil
					.trimString( theName ) );
		}
		catch ( SemanticException e )
		{
			assert false;
		}
	}

	/**
	 * Sets the on-start script of the group element.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnStart()
	 */

	public void setOnStart( String script ) throws SemanticException
	{
		setProperty( GroupElement.ON_START_METHOD, script );
	}

	/**
	 * Sets the on-row script of the group element.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnRow()
	 */

	public void setOnRow( String script ) throws SemanticException
	{
		setProperty( GroupElement.ON_ROW_METHOD, script );
	}

	/**
	 * Sets the on-finish script of the group element.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if the method is locked.
	 * 
	 * @see #getOnFinish()
	 */

	public void setOnFinish( String script ) throws SemanticException
	{
		setProperty( GroupElement.ON_FINISH_METHOD, script );
	}

	/**
	 * Sets the group expression.
	 * 
	 * @param expr
	 *            the expression to set
	 * @throws SemanticException
	 *             If the expression is invalid.
	 * 
	 * @see #getKeyExpr()
	 */

	public void setKeyExpr( String expr ) throws SemanticException
	{
		setProperty( GroupElement.KEY_EXPR_PROP, expr );
	}

	/**
	 * Returns the iterator for Sort list defined on the group. The element in
	 * the iterator is the corresponding <code>StructureHandle</code>.
	 * 
	 * @return the iterator for <code>SortKey</code> structure list defined on
	 *         the group.
	 */

	public Iterator sortsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( GroupElement.SORT_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns an iterator for the filter list defined on the group. Each object
	 * returned is of type <code>StructureHandle</code>.
	 * 
	 * @return the iterator for <code>FilterCond</code> structure list defined
	 *         on the group.
	 */

	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( GroupElement.FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the interval of this group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 * 
	 * </ul>
	 * 
	 * @return the interval value as a string
	 */

	public String getInterval( )
	{
		return getStringProperty( GroupElement.INTERVAL_PROP );
	}

	/**
	 * Returns the interval of this group. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>INTERVAL_NONE</code>
	 * <li><code>INTERVAL_PREFIX</code>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_INTERVAL</code>
	 * 
	 * </ul>
	 * 
	 * @param interval
	 *            the interval value as a string
	 * @throws SemanticException
	 *             if the property is locked or the input value is not one of
	 *             the above.
	 */

	public void setInterval( String interval ) throws SemanticException
	{
		setStringProperty( GroupElement.INTERVAL_PROP, interval );
	}

	/**
	 * Returns the interval range of this group.
	 * 
	 * @return the interval range value as a double
	 */

	public double getIntervalRange( )
	{
		return this.getFloatProperty( GroupElement.INTERVAL_RANGE_PROP );
	}

	/**
	 * Returns the interval range of this group.
	 * 
	 * @param intervalRange
	 *            the interval range value as a double
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setIntervalRange( double intervalRange )
			throws SemanticException
	{
		setFloatProperty( GroupElement.INTERVAL_RANGE_PROP, intervalRange );
	}

	/**
	 * Returns the sort direction of this group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 * 
	 * </ul>
	 * 
	 * @return the sort direction of this group
	 */

	public String getSortDirection( )
	{
		return getStringProperty( GroupElement.SORT_DIRECTION_PROP );
	}

	/**
	 * Sets the sort direction of this group. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 * 
	 * </ul>
	 * 
	 * @param direction
	 *            the sort direction of this group
	 * @throws SemanticException
	 *             if the property is locked or the input value is not one of
	 *             the above.
	 * 
	 */

	public void setSortDirection( String direction ) throws SemanticException
	{
		setStringProperty( GroupElement.SORT_DIRECTION_PROP, direction );
	}

	/**
	 * Checks whether the group header slot is empty.
	 * 
	 * @return true is the header slot is not empty, otherwise, return false.
	 * 
	 */

	public boolean hasHeader( )
	{
		return ( getHeader( ).getCount( ) != 0 );
	}

	/**
	 * Checks whether the group footer slot is empty.
	 * 
	 * @return true is the footer slot is not empty, otherwise, return false.
	 * 
	 */

	public boolean hasFooter( )
	{
		return ( getFooter( ).getCount( ) != 0 );
	}

}