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

package org.eclipse.birt.report.model.api.olap;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;

/**
 * Represents a level element.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Level
 */

public class LevelHandle extends ReportElementHandle implements ILevelModel
{

	/**
	 * Constructs a handle for the given design and design element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public LevelHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Gets the column name of this level.
	 * 
	 * @return column name of this level
	 */
	public String getColumnName( )
	{
		return getStringProperty( COLUMN_NAME_PROP );
	}

	/**
	 * Sets the column name for this level.
	 * 
	 * @param columnName
	 *            the column name to set
	 * @throws SemanticException
	 *             property is locked
	 */
	public void setColumnName( String columnName ) throws SemanticException
	{
		setStringProperty( COLUMN_NAME_PROP, columnName );
	}

	/**
	 * Returns the iterator of attributes. The element in the iterator is a
	 * <code>StructureHandle</code>.
	 * 
	 * @return the iterator of attribute string list
	 */

	public Iterator attributesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ATTRIBUTES_PROP );

		assert propHandle != null;

		return propHandle.iterator( );
	}

	/**
	 * Returns the iterator of static values. The element in the iterator is
	 * instanceof <code>RuleHandle</code>.
	 * 
	 * @return iterator of static values
	 */
	public Iterator staticValuesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( STATIC_VALUES_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Sets the base of the interval property of this level.IntervalBase, in
	 * conjunction with Interval and IntervalRange, determines how data is
	 * divided into levels.
	 * 
	 * @param intervalBase
	 *            interval base property value.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setIntervalBase( String intervalBase ) throws SemanticException
	{
		setStringProperty( INTERVAL_BASE_PROP, intervalBase );
	}

	/**
	 * Return the interval base property value of this level.
	 * 
	 * @return interval baseF property value of this level.
	 */

	public String getIntervalBase( )
	{
		return getStringProperty( INTERVAL_BASE_PROP );
	}

	/**
	 * Returns the interval of this level. The return value is defined in
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
		return getStringProperty( INTERVAL_PROP );
	}

	/**
	 * Returns the interval of this level. The input value is defined in
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
		setStringProperty( INTERVAL_PROP, interval );
	}

	/**
	 * Returns the interval range of this level.
	 * 
	 * @return the interval range value as a double
	 */

	public double getIntervalRange( )
	{
		return this.getFloatProperty( INTERVAL_RANGE_PROP );
	}

	/**
	 * Returns the interval range of this level.
	 * 
	 * @param intervalRange
	 *            the interval range value as a double
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setIntervalRange( double intervalRange )
			throws SemanticException
	{
		setFloatProperty( INTERVAL_RANGE_PROP, intervalRange );
	}

	/**
	 * Sets the interval range of this level.
	 * 
	 * @param intervalRange
	 *            the interval range value as a string.value is locale
	 *            dependent.
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setIntervalRange( String intervalRange )
			throws SemanticException
	{
		setStringProperty( INTERVAL_RANGE_PROP, intervalRange );
	}

	/**
	 * Returns the level type of this level. The returned value is one of:
	 * 
	 * <ul>
	 * <li><code>LEVEL_TYPE_DYNAMIC</code>
	 * <li><code>LEVEL_TYPE_MIRRORED</code>
	 * </ul>
	 * 
	 * @return the level type
	 */

	public String getLevelType( )
	{
		return getStringProperty( LEVEL_TYPE_PROP );
	}

	/**
	 * Sets the level type. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>LEVEL_TYPE_DYNAMIC</code>
	 * <li><code>LEVEL_TYPE_MIRRORED</code>
	 * </ul>
	 * 
	 * @param levelType
	 * @throws SemanticException
	 */
	public void setLevelType( String levelType ) throws SemanticException
	{
		setStringProperty( LEVEL_TYPE_PROP, levelType );
	}

	/**
	 * Returns the data type of this level. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 * 
	 * @return the data type of this level.
	 */

	public String getDataType( )
	{
		return getStringProperty( DATA_TYPE_PROP );
	}

	/**
	 * Sets the data type of this level. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_ANY
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 * 
	 * @param dataType
	 *            the data type to set
	 * @throws SemanticException
	 *             if the dataType is not in the choice list.
	 */

	public void setDataType( String dataType ) throws SemanticException
	{
		setStringProperty( DATA_TYPE_PROP, dataType );
	}

	/**
	 * Returns an iterator for the value access controls. Each object returned
	 * is of type <code>ValueAccessControlHandle</code>.
	 * 
	 * @return the iterator for user accesses defined on this cube.
	 */

	public Iterator valueAccessControlsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( VALUE_ACCESS_CONTROLS_PROP );
		return propHandle.getContents( ).iterator( );
	}

}
