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
import java.util.List;

import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Add the dimension handle to the cross tab through the virtual cell editpart.So
 * the insert position is 0.
 */
// TODO binding the data
public class CreateDimensionViewCommand extends AbstractCrosstabCommand
{

	private CrosstabHandleAdapter handleAdpter;
	/**
	 * cross tab area type.row column
	 */
	private int type = -1;
	private DimensionHandle dimensionHandle;

	/**
	 * trans name
	 */
	private static final String NAME = "Create DiminsionView";

	/**Constructor
	 * @param handleAdpter
	 * @param type
	 * @param dimensionHandle
	 */
	public CreateDimensionViewCommand( CrosstabHandleAdapter handleAdpter,
			int type, DimensionHandle dimensionHandle )
	{
		super( dimensionHandle );
		setHandleAdpter( handleAdpter );
		setType( type );
		setDimensionHandle( dimensionHandle );
	}

	/**
	 * Sets the handle adapter
	 * 
	 * @param handleAdpter
	 */
	public void setHandleAdpter( CrosstabHandleAdapter handleAdpter )
	{
		this.handleAdpter = handleAdpter;
	}

	/**
	 * Gets the type
	 * 
	 * @return
	 */
	public int getType( )
	{
		return type;
	}

	/**
	 * Sets the type
	 * 
	 * @param type
	 */
	public void setType( int type )
	{
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute( )
	{
		return getType( ) != VirtualCrosstabCellAdapter.IMMACULATE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute( )
	{
		transStart( NAME );
		CrosstabReportItemHandle reportHandle = (CrosstabReportItemHandle) handleAdpter.getCrosstabItemHandle( );

		try
		{
			DimensionViewHandle viewHandle = reportHandle.insertDimension( getDimensionHandle( ),
					getType( ),
					0 );
			HierarchyHandle hierarchyHandle = getDimensionHandle( ).getDefaultHierarchy( );
			int count = hierarchyHandle.getLevelCount( );
			if ( count == 0 )
			{
				rollBack( );
				return;
			}
			LevelHandle levelHandle = hierarchyHandle.getLevel( 0 );
			//new a bing
			ComputedColumn bindingColumn = CrosstabAdaptUtil.createComputedColumn( (ExtendedItemHandle)reportHandle.getModelHandle( ), levelHandle );
			
//			ComputedColumn bindingColumn = StructureFactory.newComputedColumn( reportHandle.getModelHandle( ),
//					levelHandle.getName( ) );
//			
//			bindingColumn.setDataType( DesignChoiceConstants. COLUMN_DATA_TYPE_ANY);
			
	
			((ExtendedItemHandle)reportHandle.getModelHandle( )).addColumnBinding( bindingColumn, false );
			
//			List list = new ArrayList();
//			int measureCount = reportHandle.getMeasureCount( );
//			for (int i=0; i<measureCount; i++)
//			{
//				MeasureViewHandle measureHandle = reportHandle.getMeasure( i );
//				list.add( measureHandle );
//			}
			
			LevelViewHandle levelViewHandle = CrosstabUtil.insertLevel( viewHandle, levelHandle, 0 );
		
			CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

			DataItemHandle dataHandle = DesignElementFactory.getInstance( )
					.newDataItem( levelHandle.getName( ) );
			dataHandle.setResultSetColumn( bindingColumn.getName( ) );
			
			cellHandle.addContent( dataHandle );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			e.printStackTrace( );
		}
		transEnd( );

	}

	public DimensionHandle getDimensionHandle( )
	{
		return dimensionHandle;
	}

	public void setDimensionHandle( DimensionHandle dimensionHandle )
	{
		this.dimensionHandle = dimensionHandle;
	}
}
