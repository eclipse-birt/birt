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
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.BaseCrosstabAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.util.ModelUtil;

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
	private LevelAttributeHandle[] levelAttributeHandles;
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
			LevelAttributeHandle[] levelAttrHandles, Object after )
	{
		super( dimensionHandle );
		this.dimensionHandle = dimensionHandle;
		this.levelAttributeHandles = levelAttrHandles;
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
			LevelAttributeHandle[] levelAttrHandles )
	{
		super( dimensionHandle );
		this.dimensionHandle = dimensionHandle;
		this.levelAttributeHandles = levelAttrHandles;
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
		if ( this.levelAttributeHandles != null
				&& this.levelAttributeHandles.length > 0 )
		{
			transStart( NAME );
			CrosstabReportItemHandle crosstabHandle = getCrosstabHandle( );

			try
			{
				// if dimension is not in the crosstab, then add it
				DimensionViewHandle viewHandle = null;
				int position = findCellPosition( );
				if ( CrosstabUtil.canContain( crosstabHandle,
						this.dimensionHandle ) )
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
					viewHandle = crosstabHandle.getDimension( getType( ),
							position - 1 );
				}

				// if level attribute's level is not in the crosstab, then add
				// it
				LevelHandle levelHandle = (LevelHandle) this.levelAttributeHandles[0].getElementHandle( );
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
					
					ActionHandle actionHandle = levelHandle.getActionHandle( );
					if ( actionHandle != null )
					{
						List source = new ArrayList( );
						source.add( actionHandle.getStructure( ) );
						List newAction = ModelUtil.cloneStructList( source );
						dataHandle.setAction( (Action) newAction.get( 0 ) );
					}
					
					CrosstabUtil.addLabelToHeader( levelViewHandle );
				}
				else
				{
					levelViewHandle = viewHandle.getLevel( levelHandle.getQualifiedName( ) );
				}

				position = findPosition( );

				// add level attribute to crosstab
				for ( LevelAttributeHandle lah : this.levelAttributeHandles )
				{
					DataItemHandle dataHandle = CrosstabAdaptUtil.createColumnBindingAndDataItem( (ExtendedItemHandle) crosstabHandle.getModelHandle( ),
							lah );
					CrosstabCellHandle cellHandle = levelViewHandle.getCell( );
					if ( position > 0 )
						cellHandle.addContent( dataHandle, position );
					else
						cellHandle.addContent( dataHandle );
				}
				transEnd( );
			}
			catch ( Exception e )
			{
				rollBack( );
				ExceptionUtil.handle( e );
			}
		}
	}

	private int findCellPosition( )
	{
		if ( this.handleAdpter instanceof CrosstabCellAdapter )
		{
			int base = CrosstabAdaptUtil.getDimensionViewHandle( (ExtendedItemHandle) ( (CrosstabCellAdapter) handleAdpter ).getCrosstabCellHandle( )
					.getModelHandle( ) )
					.getModelHandle( )
					.getIndex( );
			return base + 1;
		}
		return 0;
	}

	private int findPosition( )
	{
		if ( after instanceof DesignElementHandle )
		{
			return ( (DesignElementHandle) after ).getIndex( );
		}
		return 0;
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
