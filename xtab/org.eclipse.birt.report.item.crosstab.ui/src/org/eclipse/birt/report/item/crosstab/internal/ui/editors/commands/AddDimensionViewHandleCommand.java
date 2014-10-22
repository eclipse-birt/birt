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

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.util.ModelUtil;

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
	//private DimensionHandle dimensionHandle;
	private Object after = null;
	private LevelHandle[] levelHandles;
	private DimensionHandle[] dimensionHandles;

	/**
	 * Trans name
	 */
	//private static final String NAME = "Add DiminsionViewHandle";
	private static final String NAME = Messages.getString( "AddDimensionViewHandleCommand.TransName" );//$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param handleAdpter
	 * @param type
	 * @param dimensionHandle
	 */
	public AddDimensionViewHandleCommand( CrosstabCellAdapter handleAdpter,
			int type, DimensionHandle dimensionHandle, Object after )
	{
		super( dimensionHandle );
		setHandleAdpter( handleAdpter );
		setType( type );
		setDimensionHandles( new DimensionHandle[]{
			dimensionHandle
		} );
		this.after = after;
		
		setLabel( NAME );
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
		CrosstabReportItemHandle crosstabHandle = handleAdpter.getCrosstabCellHandle( ).getCrosstab( );

		int position = findPosition( );

		for ( int i = 0; i < this.dimensionHandles.length; i++ )
		{
			DimensionHandle dimensionHandle = dimensionHandles[i];

			try
			{
				if ( crosstabHandle.getCube( ) == null )
				{
					crosstabHandle.setCube( CrosstabAdaptUtil.getCubeHandle( dimensionHandle ) );
				}

				DimensionViewHandle viewHandle = crosstabHandle.insertDimension( dimensionHandle,
						getType( ),
						position );

				// add dataitem to cell
				//			DataItemHandle dataHandle = CrosstabAdaptUtil.createColumnBindingAndDataItem( (ExtendedItemHandle) reportHandle.getModelHandle( ),
				//					levelHandle );

				LevelHandle[] levels = getLevelHandles( dimensionHandle );
				for ( int j = 0; j < levels.length; j++ )
				{

					LevelHandle levelHandle = levels[j];
					if ( levelHandle == null )
					{
						rollBack( );
						return;
					}
					DataItemHandle dataHandle = CrosstabAdaptUtil.createColumnBindingAndDataItem( (ExtendedItemHandle) crosstabHandle.getModelHandle( ),
							levelHandle );
					LevelViewHandle levelViewHandle = viewHandle.insertLevel( levelHandle,
							j );
					CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

					cellHandle.addContent( dataHandle );
					
					//copy action to dataHandle
					ActionHandle actionHandle = levelHandle.getActionHandle( );
					if ( actionHandle != null )
					{
						List source = new ArrayList( );
						source.add( actionHandle.getStructure( ) );
						List newAction = ModelUtil.cloneStructList( source );
						dataHandle.setAction( (Action) newAction.get( 0 ) );
					}
					
					String displayNameKey = dimensionHandle.getDisplayNameKey( );
					if ( displayNameKey == null
							&& levelHandles != null
							&& levelHandles.length != 0 )
					{
						displayNameKey = levelHandles[0].getDisplayNameKey( );
					}
					CrosstabUtil.setLabelDisplayNameKey( displayNameKey );
					CrosstabUtil.addLabelToHeader( levelViewHandle );
					CrosstabUtil.clearLabelDisplayNameKey( );
				}

			}
			catch ( Exception e )
			{
				rollBack( );
				ExceptionUtil.handle( e );
				return;
			}
		}
		transEnd( );
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
		int base = CrosstabAdaptUtil.getDimensionViewHandle( (ExtendedItemHandle) handleAdpter.getCrosstabCellHandle( )
				.getModelHandle( ) )
				.getModelHandle( )
				.getIndex( );
		if ( after instanceof DesignElementHandle )
		{
			int index = ( (DesignElementHandle) after ).getIndex( );
			if ( index == 0 )
			{
				return base;
			}
		}
		return base + 1;
		//return ((CrosstabReportItemHandle) handleAdpter.getCrosstabItemHandle( )).getDimensionCount( getType( ) );
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
