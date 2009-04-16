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
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.BaseCrosstabAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Add the Dimension handle to the cross tab.When drag the Dimension handle to
 * the column or row area of the cross tab, execute the command.
 */
public class AddLevelAttributeHandleCommand extends AbstractCrosstabCommand
{

	private BaseCrosstabAdapter handleAdpter;
	/**
	 * Column or the row type.See the ICrosstabConstants row and column axis
	 * type.
	 */
	private int type = -1;
	private LevelAttributeHandle levelAttributeHandle;
	private LevelHandle[] levelHandles;
	private DimensionHandle[] dimensionHandles;
	private DimensionHandle dimensionHandle;
	private Object after;

	/**
	 * Trans name
	 */
	// private static final String NAME = "Add DiminsionViewHandle";
	private static final String NAME = Messages.getString( "AddDimensionViewHandleCommand.TransName" );//$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param handleAdpter
	 * @param type
	 * @param levelHandle
	 */
	public AddLevelAttributeHandleCommand( CrosstabCellAdapter handleAdpter,
			int type, DimensionHandle dimensionHandle,
			LevelAttributeHandle levelAttrHandle, Object after )
	{
		super( dimensionHandle );
		this.dimensionHandle = dimensionHandle;
		this.levelAttributeHandle = levelAttrHandle;
		setHandleAdpter( handleAdpter );
		setType( type );
		setDimensionHandles( new DimensionHandle[]{
			dimensionHandle
		} );
		this.after = after;
		setLabel( NAME );
	}

	public AddLevelAttributeHandleCommand( CrosstabHandleAdapter handleAdpter,
			int type, DimensionHandle dimensionHandle,
			LevelAttributeHandle levelAttrHandle )
	{
		super( dimensionHandle );
		this.dimensionHandle = dimensionHandle;
		this.levelAttributeHandle = levelAttrHandle;
		setHandleAdpter( handleAdpter );
		setType( type );
		setDimensionHandles( new DimensionHandle[]{
			dimensionHandle
		} );
		setLabel( NAME );
	}

	/**
	 * Sets the handle adapter
	 * 
	 * @param handleAdpter
	 */
	public void setHandleAdpter( BaseCrosstabAdapter handleAdpter )
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

	public CrosstabReportItemHandle getCrosstabHandle( )
	{
		if ( this.handleAdpter instanceof CrosstabHandleAdapter )
		{
			return (CrosstabReportItemHandle) ( (CrosstabHandleAdapter) this.handleAdpter ).getCrosstabItemHandle( );
		}
		if ( this.handleAdpter instanceof CrosstabCellAdapter )
		{
			return ( (CrosstabCellAdapter) this.handleAdpter ).getCrosstabCellHandle( )
					.getCrosstab( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute( )
	{
		transStart( NAME );
		CrosstabReportItemHandle crosstabHandle = getCrosstabHandle( );

		try
		{
			// if dimension is not in the crosstab, then add it
			DimensionViewHandle viewHandle = null;
			int position = findPosition( );
			if ( CrosstabUtil.canContain( crosstabHandle, this.dimensionHandle ) )
			{
				if ( crosstabHandle.getCube( ) == null )
				{
					crosstabHandle.setCube( CrosstabAdaptUtil.getCubeHandle( dimensionHandle ) );
				}
				viewHandle = crosstabHandle.insertDimension( dimensionHandle,
						getType( ),
						position );
			}
			else
			{
				viewHandle = crosstabHandle.getDimension( getType( ), position-1 );
			}

			// if level attribute's level is not in the crosstab, then add it
			LevelHandle levelHandle = (LevelHandle) this.levelAttributeHandle.getElementHandle( );
			if ( levelHandle == null )
			{
				rollBack( );
				return;
			}
			LevelViewHandle levelViewHandle = null;
			if ( viewHandle.getLevel( levelHandle.getQualifiedName( ) ) == null )
			{
				DataItemHandle dataHandle = CrosstabAdaptUtil.createColumnBindingAndDataItem( (ExtendedItemHandle) crosstabHandle.getModelHandle( ),
						levelHandle );
				levelViewHandle = viewHandle.insertLevel( levelHandle,
						viewHandle.getLevelCount( ) );
				CrosstabCellHandle cellHandle = levelViewHandle.getCell( );
				cellHandle.addContent( dataHandle );
			}
			else
			{
				levelViewHandle = viewHandle.getLevel( levelHandle.getQualifiedName( ) );
			}

			// add level attribute to crosstab
			DataItemHandle dataHandle = CrosstabAdaptUtil.createColumnBindingAndDataItem( (ExtendedItemHandle) crosstabHandle.getModelHandle( ),
					this.levelAttributeHandle );
			CrosstabCellHandle cellHandle = levelViewHandle.getCell( );
			cellHandle.addContent( dataHandle );
			transEnd( );
		}
		catch ( Exception e )
		{
			rollBack( );
			ExceptionHandler.handle( e );
		}
	}

	private LevelHandle[] getLevelHandles( DimensionHandle dimensionHandle )
	{
		if ( levelHandles == null )
		{
			LevelHandle[] dimensionLevelHandles = new LevelHandle[dimensionHandle.getDefaultHierarchy( )
					.getLevelCount( )];
			for ( int i = 0; i < dimensionLevelHandles.length; i++ )
			{
				dimensionLevelHandles[i] = dimensionHandle.getDefaultHierarchy( )
						.getLevel( i );
			}
			return dimensionLevelHandles;
		}
		return levelHandles;
	}

	private int findPosition( )
	{
		if ( this.handleAdpter instanceof CrosstabCellAdapter )
		{
			int base =  CrosstabAdaptUtil.getDimensionViewHandle( (ExtendedItemHandle) ( (CrosstabCellAdapter) handleAdpter ).getCrosstabCellHandle( )
					.getModelHandle( ) )
					.getModelHandle( )
					.getIndex( );
			return base + 1;
		}
		return 0;
	}

	public void setLevelHandles( LevelHandle[] levelHandles )
	{
		this.levelHandles = levelHandles;
	}

	/**
	 * @return
	 */
	public DimensionHandle[] getDimensionHandles( )
	{
		return dimensionHandles;
	}

	/**
	 * @param dimensionHandle
	 */
	public void setDimensionHandles( DimensionHandle[] dimensionHandles )
	{
		this.dimensionHandles = dimensionHandles;
	}
}
