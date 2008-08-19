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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
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
				modelCmptdColumn.getAggregateFunction( ),
				modelCmptdColumn.getFilterExpression( ) == null
						? null
						: new ScriptExpression( modelCmptdColumn.getFilterExpression( ) ),
				populateArgument( modelCmptdColumn ) );
	}
	
	/**
	 * Populate the arguments to a List by the order of the IAggrFunction saved
	 * 
	 * @param modelCmptdColumn
	 * @return
	 */
	private static List populateArgument( ComputedColumnHandle modelCmptdColumn )
	{
		Map argumentList = new HashMap( );
		Iterator argumentIter = modelCmptdColumn.argumentsIterator( );
		while ( argumentIter.hasNext( ) )
		{
			AggregationArgumentHandle handle = (AggregationArgumentHandle) argumentIter.next( );
			argumentList.put( handle.getName( ),
					new ScriptExpression( handle.getValue( ) ) );
		}

		List orderedArgument = new ArrayList( );
		if ( modelCmptdColumn.getAggregateFunction( ) != null )
		{
			IAggrFunction info = null;
			try
			{
				info = AggregationManager.getInstance( )
						.getAggregation( modelCmptdColumn.getAggregateFunction( ) );
			}
			catch ( DataException e )
			{
				e.printStackTrace();
			}
			if ( info != null )
			{
				IParameterDefn[] parameters = info.getParameterDefn( );

				if ( parameters != null )
				{
					for ( int i = 0; i < parameters.length; i++ )
					{
						IParameterDefn pInfo = parameters[i];
						if ( argumentList.get( pInfo.getName( ) ) != null )
						{
							orderedArgument.add( argumentList.get( pInfo.getName( ) ) );
						}
					}
				}
			}
		}
		return orderedArgument;

	}
	

}
