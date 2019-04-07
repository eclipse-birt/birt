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
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Add the dimension handle to the cross tab through the virtual cell
 * editpart.So the insert position is 0.
 */
// TODO binding the data
public class CreateDimensionViewCommand extends AbstractCrosstabCommand
{

	private CrosstabHandleAdapter handleAdpter;
	/**
	 * cross tab area type.row column
	 */
	private int type = -1;
	private DimensionHandle[] dimensionHandles;
	private LevelHandle[] levelHandles;

	/**
	 * trans name
	 */
	// private static final String NAME = "Create DiminsionViewHandle";
	private static final String NAME = Messages.getString( "CreateDimensionViewCommand.TransName" );//$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param handleAdpter
	 * @param type
	 * @param dimensionHandle
	 */
	public CreateDimensionViewCommand( CrosstabHandleAdapter handleAdpter,
			int type, DimensionHandle dimensionHandle )
	{
		this( handleAdpter, type, new DimensionHandle[]{
			dimensionHandle
		} );
	}

	public CreateDimensionViewCommand( CrosstabHandleAdapter handleAdpter,
			int type, DimensionHandle[] dimensionHandles )
	{
		super( dimensionHandles[0] );
		setHandleAdpter( handleAdpter );
		setType( type );
		setDimensionHandles( dimensionHandles );
		
		setLabel( NAME );
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
		CrosstabReportItemHandle crosstabHandle = (CrosstabReportItemHandle) handleAdpter.getCrosstabItemHandle( );

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
						i );

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

					ActionHandle actionHandle = levelHandle.getActionHandle( );
					if ( actionHandle != null )
					{
						List source = new ArrayList( );
						source.add( actionHandle.getStructure( ) );
						List newAction = ModelUtil.cloneStructList( source );
						dataHandle.setAction( (Action) newAction.get( 0 ) );
					}
					
					// Set content key for label
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

	public LevelHandle[] getLevelHandles( DimensionHandle dimensionHandle )
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

	public void setLevelHandles( LevelHandle[] levelHandles )
	{
		this.levelHandles = levelHandles;
	}
}
