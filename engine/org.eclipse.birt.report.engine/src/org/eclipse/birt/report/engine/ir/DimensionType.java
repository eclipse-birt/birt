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

package org.eclipse.birt.report.engine.ir;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.DimensionUtil;

/**
 * 
 * @version $Revision: 1.5 $ $Date: 2005/04/12 05:26:21 $
 */
public class DimensionType
{
	private static Logger log = Logger.getLogger(DimensionType.class.getName());

	public final static int TYPE_DIMENSION = 1;
	public final static int TYPE_CHOICE = 0;
	public final static String UNITS_CM = EngineIRConstants.UNITS_CM;
	public final static String UNITS_EM = EngineIRConstants.UNITS_EM;
	public final static String UNITS_EX = EngineIRConstants.UNITS_EX;
	public final static String UNITS_IN = EngineIRConstants.UNITS_IN;
	public final static String UNITS_MM = EngineIRConstants.UNITS_MM;
	public final static String UNITS_PC = EngineIRConstants.UNITS_PC;
	public final static String UNITS_PERCENTAGE = EngineIRConstants.UNITS_PERCENTAGE;
	public final static String UNITS_PT = EngineIRConstants.UNITS_PT;
	public final static String UNITS_PX = EngineIRConstants.UNITS_PX;
	final protected int type;
	final protected String unitType;
	final protected double measure;
	final protected String choice;

	public DimensionType( String choice )
	{
		this.type = TYPE_CHOICE;
		this.choice = choice;
		this.measure = 0;
		this.unitType = null;
	}
	
	public DimensionType( double value, String units )
	{
		this.type = TYPE_DIMENSION;
		this.unitType = units;
		this.measure = value;
		this.choice = null;
	}
	
	public int getValueType( )
	{
		return type;
	}

	public double getMeasure( )
	{
		assert this.type == TYPE_DIMENSION;
		return this.measure;
	}
	
	public String getUnits()
	{
		assert this.type == TYPE_DIMENSION;
		return this.unitType;
	}

	public String getChoice( )
	{
		return this.choice;
	}

	public String toString( )
	{
		if ( type == TYPE_DIMENSION )
		{
			//Copy from DimensionValue
	   		String value = Double.toString( measure );
	   	   
	   	   	// Eliminate the ".0" that the default implementation tacks onto
	   	   	// the end of integers.
	   	   	if ( value.substring( value.length( ) - 2 ).equals( ".0" ) ) //$NON-NLS-1$
	   	   			value = value.substring( 0, value.length( ) - 2 );
			return value + this.unitType;
		}
		return choice;
	}

	public double convertTo( String targetUnit )
	{
		assert type == TYPE_DIMENSION;
		DimensionValue value = DimensionUtil.convertTo(this.measure, this.unitType, targetUnit);
		if (value != null)
		{
			return value.getMeasure();
		}
		return 0;
	}
	/**
	 * Implement the subtract operation of type <Code>DimensionType</Code>.
	 *	
	 * @param subtrahend
	 *            the subtrahend
	 * @return the result whose unit is <Code>CM_UNIT</Code>
	 */
	public DimensionType subtract(  DimensionType subtrahend )
	{
		assert ( getValueType( ) == DimensionType.TYPE_DIMENSION );
		assert ( subtrahend != null && subtrahend.getValueType( ) == DimensionType.TYPE_DIMENSION );

		double measure = convertTo( DimensionType.UNITS_CM );
		measure -= subtrahend.convertTo( DimensionType.UNITS_CM );
		DimensionType ret = new DimensionType( measure, DimensionType.UNITS_CM );
		return ret;
	}
	/**
	 * Implement the compare operation of type <Code>DimensionType</Code>.
	 * 	
	 * @param subtrahend
	 *            the subtrahend operand
	 * @return a negative double, zero, or a positive double as the first
	 *         operand is less than, equal to, or greater than the second.
	 */
	public double compare( DimensionType subtrahend )
	{
		assert ( getValueType( ) == DimensionType.TYPE_DIMENSION );
		assert ( subtrahend != null && subtrahend.getValueType( ) == DimensionType.TYPE_DIMENSION );

		double measure = convertTo( DimensionType.UNITS_CM );
		measure -= subtrahend.convertTo( DimensionType.UNITS_CM );

		return measure;
	}
	
	/**
	 * Parses a dimension string. The string must match the following:
	 * <ul>
	 * <li>null</li>
	 * <li>[1-9][0-9]*[.[0-9]*[ ]*[in|cm|mm|pt|pc|em|ex|px|%]]</li>
	 * </ul>
	 * 
	 * If the error exists, return the result whose measure is 0. 
	 */
	public static DimensionType parserUnit(String value)
	{
		if (value != null)
		{
			try
			{
				DimensionValue val=DimensionValue.parse(value);
				return new DimensionType(val.getMeasure(),val.getUnits());
			}
			catch ( PropertyValueException e )
			{
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		return new DimensionType(0,DimensionType.UNITS_CM);
	}
}
