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

import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;

/**
 * This class represents a measure element.
 */

public abstract class MeasureHandle extends ReportElementHandle
		implements
			IMeasureModel
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

	public MeasureHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Gets the function defined in this measure.
	 * 
	 * @return function for this measure
	 */

	public String getFunction( )
	{
		return getStringProperty( FUNCTION_PROP );
	}

	/**
	 * Sets the function for this measure.
	 * 
	 * @param function
	 *            the function to set
	 * @throws SemanticException
	 *             property is locked or value is invalid
	 */
	public void setFunction( String function ) throws SemanticException
	{
		setStringProperty( FUNCTION_PROP, function );
	}

	/**
	 * Gets the measure expression of this measure element.
	 * 
	 * @return measure expression of this measure element
	 */

	public String getMeasureExpression( )
	{
		return getStringProperty( MEASURE_EXPRESSION_PROP );
	}

	/**
	 * Sets the measure expression for this measure.
	 * 
	 * @param expression
	 *            the measure expression to set
	 * @throws SemanticException
	 *             property is locked
	 */
	public void setMeasureExpression( String expression )
			throws SemanticException
	{
		setStringProperty( MEASURE_EXPRESSION_PROP, expression );
	}

	/**
	 * Indicates whether this measure is computed by other measures or not.
	 * 
	 * @return true if this measure is computed by other measures, otherwise
	 *         false
	 */

	public boolean isCalculated( )
	{
		return getBooleanProperty( IS_CALCULATED_PROP );
	}

	/**
	 * Sets whether this measure is computed by other measures or not.
	 * 
	 * @param isCalculated
	 *            true if this measure is computed by other measures, otherwise
	 *            false
	 * @throws SemanticException
	 *             property is locked
	 */

	public void setCalculated( boolean isCalculated ) throws SemanticException
	{
		setProperty( IS_CALCULATED_PROP, Boolean.valueOf( isCalculated ) );
	}

	/**
	 * Returns the data type information of this measure. The possible values
	 * are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
	 * <li>COLUMN_DATA_TYPE_INTEGER
	 * <li>COLUMN_DATA_TYPE_STRING
	 * <li>COLUMN_DATA_TYPE_DATETIME
	 * <li>COLUMN_DATA_TYPE_DECIMAL
	 * <li>COLUMN_DATA_TYPE_FLOAT
	 * <li>COLUMN_DATA_TYPE_STRUCTURE
	 * <li>COLUMN_DATA_TYPE_TABLE
	 * </ul>
	 * 
	 * @return the data type of this measure.
	 */

	public String getDataType( )
	{
		return getStringProperty( DATA_TYPE_PROP );
	}

	/**
	 * Sets the data type of this measure. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants},
	 * and they are:
	 * <ul>
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
		setProperty( DATA_TYPE_PROP, dataType );
	}
}
