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
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

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
	private DimensionHandle dimensionHandle;
	private LevelHandle levelHandle;

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
			if ( reportHandle.getCube( ) == null )
			{
				reportHandle.setCube( CrosstabAdaptUtil.getCubeHandle( getDimensionHandle( ) ) );
			}
			DimensionViewHandle viewHandle = reportHandle.insertDimension( getDimensionHandle( ),
					getType( ),
					0 );

			LevelHandle levelHandle = getLevelHandle( );
			if ( levelHandle == null )
			{
				rollBack( );
				return;
			}

			// add dataitem to cell
			DataItemHandle dataHandle = CrosstabAdaptUtil.createColumnBindingAndDataItem( (ExtendedItemHandle) reportHandle.getModelHandle( ),
					levelHandle );

			LevelViewHandle levelViewHandle = viewHandle.insertLevel( levelHandle,
					0 );

			CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

			cellHandle.addContent( dataHandle );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			ExceptionHandler.handle( e );
		}
		transEnd( );

	}

	/**
	 * @return
	 */
	public DimensionHandle getDimensionHandle( )
	{
		return dimensionHandle;
	}

	/**
	 * @param dimensionHandle
	 */
	public void setDimensionHandle( DimensionHandle dimensionHandle )
	{
		this.dimensionHandle = dimensionHandle;
	}

	public LevelHandle getLevelHandle( )
	{
		if ( levelHandle == null )
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
