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

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * Adaptor class of <code>IParameter</code>
 * 
 */

public abstract class ScalarParameter implements IParameter
{

	/**
	 * Parameter Group
	 */
	protected IParameterGroup group = null;

	private String selectionValue = null;

	/**
	 * Scalar parameter handle.
	 */
	protected ScalarParameterHandle handle;

	/**
	 * engine task.
	 */
	protected IEngineTask engineTask;

	/**
	 * Constructor
	 * 
	 * @param handle
	 *            scalar parameter handle.
	 * @param engineTask
	 *            engine task.
	 */

	public ScalarParameter( ScalarParameterHandle handle, IEngineTask engineTask )
	{
		this.handle = handle;
		this.engineTask = engineTask;
	}

	/**
	 * Gets values of parameter.
	 * 
	 * @return value list.
	 */
	public abstract List getValueList( );

	/**
	 * Gets selection value.
	 * 
	 * @return selection value.
	 */
	public String getSelectionValue( )
	{
		return selectionValue;
	}

	/**
	 * Sets selection value.
	 * 
	 * @param value
	 */
	public void setSelectionValue( String value )
	{
		selectionValue = value;
	}

	/**
	 * Sets parameter group
	 */
	public void setParentGroup( IParameterGroup group )
	{
		this.group = group;
	}

	/**
	 * Gets parameter group
	 */
	public IParameterGroup getParentGroup( )
	{
		return group;
	}

	/**
	 * Gets default value.
	 * 
	 * @return default value
	 */

	public String getDefaultValue( )
	{
		return handle.getDefaultValue( );
	}

	/**
	 * Get parameter handle.
	 * 
	 * @return parameter handle.
	 */

	public ScalarParameterHandle getHandle( )
	{
		return handle;
	}

	public String format( String input ) throws BirtException
	{
		return ParameterUtil.format( handle, input );
	}

	public Object converToDataType( Object value ) throws BirtException
	{
		return ParameterUtil.convert( value, handle.getDataType( ) );
	}

	/**
	 * Gets isRequired property.
	 * 
	 * @return
	 */
	public boolean isRequired( )
	{
		return handle.isRequired( );
	}

}
