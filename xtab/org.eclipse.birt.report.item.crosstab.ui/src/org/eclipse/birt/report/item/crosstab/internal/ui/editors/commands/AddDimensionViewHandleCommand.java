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

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Add the Dimension handle to the cross tab.When drag the Dimension handle to
 * the column or row area of the cross tab, execute the command.
 */
public class AddDimensionViewHandleCommand extends AbstractCrosstabCommand
{

	private CrosstabCellAdapter handleAdpter;
	/**
	 * Column or the row type.See the ICrosstabConstants row and column axis
	 * type.
	 */
	private int type = -1;
	private DimensionHandle dimensionHandle;
	private Object after = null;
	private LevelHandle levelHandle;

	/**
	 * Trans name
	 */
	private static final String NAME = "Add DiminsionView";

	/**
	 * Constructor
	 * 
	 * @param handleAdpter
	 * @param type
	 * @param dimensionHandle
	 */
	public AddDimensionViewHandleCommand( CrosstabCellAdapter handleAdpter,
			int type, DimensionHandle dimensionHandle, Object after)
	{
		super( dimensionHandle );
		setHandleAdpter( handleAdpter );
		setType( type );
		setDimensionHandle( dimensionHandle );
		this.after = after;
	}

	/**
	 * Gets the dimension handle
	 * 
	 * @return
	 */
	public DimensionHandle getDimensionHandle( )
	{
		return dimensionHandle;
	}

	/**
	 * Sets the dimension handle
	 * 
	 * @param dimensionHandle
	 */
	public void setDimensionHandle( DimensionHandle dimensionHandle )
	{
		this.dimensionHandle = dimensionHandle;
	}

	/**
	 * Sets the handle adapter
	 * 
	 * @param handleAdpter
	 */
	public void setHandleAdpter( CrosstabCellAdapter handleAdpter )
	{
		this.handleAdpter = handleAdpter;
	}

	/**
	 * Gets the tyoe
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
	 *            ICrosstabConstants.COLUMN_AXIS_TYPE or
	 *            ICrosstabConstants.ROW_AXIS_TYPE
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
		CrosstabReportItemHandle reportHandle = (CrosstabReportItemHandle) handleAdpter.getCrosstabCellHandle( ).getCrosstab( );

		try
		{
			//int position = reportHandle.getDimensionCount( getType( ) );
			int position = findPosition(  );
			DimensionViewHandle viewHandle = reportHandle.insertDimension( getDimensionHandle( ),
					getType( ),
					position );
//			HierarchyHandle hierarchyHandle = getDimensionHandle( ).getDefaultHierarchy( );
//			int count = hierarchyHandle.getLevelCount( );
//			if ( count == 0 )
//			{
//				rollBack( );
//				return;
//			}
//			LevelHandle levelHandle = hierarchyHandle.getLevel( 0 );
			
			LevelHandle levelHandle = getLevelHandle( );
			if ( levelHandle == null )
			{
				rollBack( );
				return;
			}

			ComputedColumn bindingColumn = CrosstabAdaptUtil.createComputedColumn( (ExtendedItemHandle)reportHandle.getModelHandle( ), levelHandle );
			ComputedColumnHandle bindingHandle = ((ExtendedItemHandle)reportHandle.getModelHandle( )).addColumnBinding( bindingColumn, false );
						
			LevelViewHandle levelViewHandle = CrosstabUtil.insertLevel( viewHandle, levelHandle, 0 );

			CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

			DataItemHandle dataHandle = DesignElementFactory.getInstance( )
					.newDataItem( levelHandle.getName( ) );
			dataHandle.setResultSetColumn( bindingHandle.getName( ) );
			
			cellHandle.addContent( dataHandle );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			ExceptionHandler.handle( e );
		}
		transEnd( );
	}

	private int findPosition()
	{
		int base = CrosstabAdaptUtil.getDimensionViewHandle((ExtendedItemHandle) handleAdpter.getCrosstabCellHandle( ).getModelHandle( )).getModelHandle( ).getIndex( );
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

	
	public LevelHandle getLevelHandle( )
	{
		if (levelHandle == null)
		{
			return getDimensionHandle( ).getDefaultHierarchy( ).getLevel( 0 );
		}
		return levelHandle;
	}

	
	public void setLevelHandle( LevelHandle levelHandle )
	{
		this.levelHandle = levelHandle;
	}
}
