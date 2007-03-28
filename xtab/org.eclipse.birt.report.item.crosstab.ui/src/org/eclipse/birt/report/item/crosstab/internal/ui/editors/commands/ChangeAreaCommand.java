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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 *Drop the demension view handle from the row to column 
 */

public class ChangeAreaCommand extends AbstractCrosstabCommand
{
	//private int type = -1;
	//private DimensionHandle dimensionHandle;
	private Object after = null;

	DimensionViewHandle parentVewHandle;
	DimensionViewHandle childViewHandle;
	Map measureMap = new HashMap();
	Map funcMap = new HashMap();
	/**
	 * Trans name
	 */
	private static final String NAME = "Add DiminsionView";
	
	public ChangeAreaCommand(DesignElementHandle parent, DesignElementHandle child, Object after)
	{
		super( parent );
		//this.parent = parent;
		//this.child = child;
		this.after = after;
		
		parentVewHandle = CrosstabAdaptUtil.getDimensionViewHandle( CrosstabAdaptUtil.getExtendedItemHandle( parent ) );
		childViewHandle = CrosstabAdaptUtil.getDimensionViewHandle( CrosstabAdaptUtil.getExtendedItemHandle( child ) );
		
		int levelCount = childViewHandle.getLevelCount( );
		for (int i=0; i<levelCount; i++)
		{
			LevelViewHandle levelViewHandle = childViewHandle.getLevel( i );
			String name = levelViewHandle.getCubeLevel( ).getQualifiedName( );
			
			List measures = CrosstabUtil.getAggregationMeasures( levelViewHandle );
			
			List funcs = new ArrayList();
			
			for (int j=0; j<measures.size( ); j++)
			{
				String funcName = CrosstabUtil.getAggregationFunction( levelViewHandle, (MeasureViewHandle)measures.get( j ) );
				funcs.add( funcName );
			}
			
			measureMap.put( name, measures);
			measureMap.put( name,  funcs);
		}
	}
	
	public boolean canExecute( )
	{
		return parentVewHandle.getAxisType( ) != childViewHandle.getAxisType( );
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute( )
	{
		transStart( NAME );
		CrosstabReportItemHandle reportHandle = parentVewHandle.getCrosstab( );

		try
		{
			//reportHandle.removeDimension(childViewHandle.getAxisType( ), childViewHandle.getIndex( ) );
			//CrosstabUtil.insertDimension( reportHandle, childViewHandle, parentVewHandle.getAxisType( ), findPosition( ), measureMap, funcMap );
			reportHandle.pivotDimension( childViewHandle.getAxisType( ), childViewHandle.getIndex( ), parentVewHandle.getAxisType( ), findPosition( ) );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			e.printStackTrace( );
		}
		transEnd( );
	}
	
	private int findPosition()
	{
		//int base = handleAdpter.getCrosstabCellHandle( ).getCrosstabHandle( ).getIndex( );
		//System.out.println(after);
		int base = parentVewHandle.getIndex( );
		if (after instanceof  DesignElementHandle)
		{
			int index = ((DesignElementHandle)after).getIndex( );
			if (index == 0)
			{
				return base;
			}
		}
		return base + 1;
		//return ((CrosstabReportItemHandle) handleAdpter.getCrosstabItemHandle( )).getDimensionCount( getType( ) );
	}
}
