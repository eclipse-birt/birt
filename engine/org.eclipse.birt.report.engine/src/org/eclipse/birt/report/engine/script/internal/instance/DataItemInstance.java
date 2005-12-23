/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of a data item
 */
public class DataItemInstance extends ReportItemInstance implements
		IDataItemInstance
{

	public DataItemInstance( DataContent data, ExecutionContext context )
	{
		super( data, context );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance#getValue()
	 */
	public Object getValue( )
	{
		return ( ( DataContent ) content ).getValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance#setValue(java.lang.Object)
	 */
	public void setValue( Object value )
	{
		( ( DataContent ) content ).setValue( value );
	}

}
