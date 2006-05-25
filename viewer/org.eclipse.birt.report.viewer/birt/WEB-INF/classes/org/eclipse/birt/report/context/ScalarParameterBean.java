/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.context;

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Parameter bean object used by parameter related jsp pages. It carries the data shared between
 * front-end jsp page and back-end fragment class. 
 * In current implementation, ScalarParameterBean uses request scope.
 * <p>
 */
public class ScalarParameterBean extends ParameterAttributeBean
{
	/**
	 * Parameter definition reference.
	 */
	private ParameterDefinition parameter = null;
	
	/**
	 * Is parameter value required. 
	 */
	private boolean isRequired = false;
	
	/**
	 * Current parameter value.
	 */
	private String value = null;
	
	/**
	 * Selection lable list. Label is HTML encoded.
	 */
	private Vector selectionList = new Vector( );
	
	/**
	 * selection lable to value mapping. Label is HTML encoded.
	 */
	private Hashtable selectionTable = new Hashtable( );
	
	/**
	 * Whether current value is in the selection list.
	 */
	private boolean valueInList = true;

	/**
	 * Current parameter default value.
	 */
	private String defaultValue = null;	
	
	/**
	 * Constructor.
	 * 
	 * @param parameter
	 */
	public ScalarParameterBean( ParameterDefinition  parameter )
	{
		this.parameter = parameter;
	}
	
	/**
	 * Adapt to IScalarParameterDefn's allowNull( ).
	 * 
	 * @return whether parameter value allows null.
	 */
	public boolean allowNull( )
	{
		if ( parameter == null )
		{
			return false;
		}
		
		return parameter.allowNull( );
	}
	
	/**
	 * Adapt to IScalarParameterDefn's allowBlank( ).
	 * 
	 * @return whether parameter value allows blank.
	 */
	public boolean allowBlank( )
	{
		if ( parameter == null )
		{
			return true;
		}
		
		return parameter.allowBlank( );
	}
	
	/**
	 * Adapt to IScalarParameterDefn's allowNewValues( ).
	 * 
	 * @return whether parameter selection list allows new value.
	 */
	public boolean allowNewValues( )
	{
		if ( parameter == null )
		{
			return false;
		}
		
		return !parameter.mustMatch( );
	}

	/**
	 * Adapt to IScalarParameterDefn's isValueConcealed( ).
	 * 
	 * @return whether parameter value is concealed.
	 */
	public boolean isValueConcealed( )
	{
		if ( parameter == null )
		{
			return false;
		}
		
		return parameter.concealValue( );
	}

	/**
	 * Adapt to IScalarParameterDefn's getName( ).
	 * 
	 * @return parameter name.
	 */
	public String getName( )
	{
		if ( parameter == null )
		{
			return null;
		}

		return parameter.getName( ); 
	}

	/**
	 * Adapt to IScalarParameterDefn's getHelpText( ).
	 * 
	 * @return parameter help text.
	 */
	public String getToolTip( )
	{
		String toolTip = ""; //$NON-NLS-1$
		
		if ( parameter != null && parameter.getHelpText( ) != null )
		{
			toolTip =  parameter.getHelpText( ); 
		}

		return ParameterAccessor.htmlEncode( toolTip );
	}

	/**
	 * @return Returns the isValueInList.
	 */
	public boolean isValueInList( )
	{
		return valueInList;
	}
	
	/**
	 * @param valueInList The isValueInList to set.
	 */
	public void setValueInList( boolean valueInList )
	{
		this.valueInList = valueInList;
	}
	
	/**
	 * @return Returns the parameter.
	 */
	public ParameterDefinition  getParameter( )
	{
		return parameter;
	}
	
	/**
	 * @param parameter The parameter to set.
	 */
	public void setParameter( ParameterDefinition  parameter )
	{
		this.parameter = parameter;
	}
	
	/**
	 * @return Returns the parameterValue.
	 */
	public String getValue( )
	{
		return value;
	}
	
	/**
	 * @param value The parameterValue to set.
	 */
	public void setValue( String value )
	{
		this.value = value;
	}
	
	/**
	 * @return Returns the selectionList.
	 */
	public Vector getSelectionList( )
	{
		return selectionList;
	}
	
	/**
	 * @param selectionList The selectionList to set.
	 */
	public void setSelectionList( Vector selectionList )
	{
		this.selectionList = selectionList;
	}
	
	/**
	 * @return Returns the selectionTable.
	 */
	public Hashtable getSelectionTable( )
	{
		return selectionTable;
	}
	
	/**
	 * @param selectionTable The selectionTable to set.
	 */
	public void setSelectionTable( Hashtable selectionTable )
	{
		this.selectionTable = selectionTable;
	}
	
	/**
	 * @return Returns the isRequired.
	 */
	public boolean isRequired( )
	{
		return isRequired;
	}
	
	/**
	 * @param isRequired The isRequired to set.
	 */
	public void setRequired( boolean isRequired )
	{
		this.isRequired = isRequired;
	}
	
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue( )
	{
		return defaultValue;
	}
	
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue( String defaultValue )
	{
		this.defaultValue = defaultValue;
	}		
}