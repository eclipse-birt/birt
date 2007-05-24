/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.report.data.adapter.internal.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;

/**
 * Adapts a Model computed column
 */
public class ComputedColumnAdapter extends ComputedColumn
{
	public ComputedColumnAdapter ( ComputedColumnHandle modelCmptdColumn ) throws AdapterException
	{
		
		
		super( modelCmptdColumn.getName( ),
				modelCmptdColumn.getExpression( ),
				org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType( modelCmptdColumn.getDataType( ) ),
				org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelAggregationType( modelCmptdColumn.getAggregateFunction( )),
				modelCmptdColumn.getFilterExpression( ) == null ? null:new ScriptExpression( modelCmptdColumn.getFilterExpression( )),
				populateArgument(modelCmptdColumn));
	}
	
	private static List populateArgument( ComputedColumnHandle modelCmptdColumn )
	{
		List argument = new ArrayList();
		Iterator it = modelCmptdColumn.argumentsIterator( );
		while( it!= null && it.hasNext( ))
		{
			AggregationArgumentHandle arg = (AggregationArgumentHandle)it.next( );
			argument.add( arg.getValue( ) );
		}
		return argument;
	}

}
