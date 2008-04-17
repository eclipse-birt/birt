/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.LibRootModel;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 */

public class CubeDropAdapter implements IDropAdapter
{

	public int canDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( target != null && transfer instanceof TabularCubeHandle )
		{
			SlotHandle targetSlot = getTargetSlotHandle( target,
					ICrosstabConstants.CROSSTAB_EXTENSION_NAME ); //$NON-NLS-1$
			if ( targetSlot != null )
				if ( DNDUtil.handleValidateTargetCanContainType( targetSlot,
						"Crosstab" )
						&& DNDUtil.handleValidateTargetCanContainMore( targetSlot,
								0 ) )
					return DNDService.LOGIC_TRUE;
		}
		return DNDService.LOGIC_UNKNOW;
	}

	private SlotHandle getTargetSlotHandle( Object target, String insertType )
	{
		IStructuredSelection models = InsertInLayoutUtil.editPart2Model( new StructuredSelection( target ) );
		if ( models.isEmpty( ) )
		{
			return null;
		}
		// model = models.getFirstElement( );
		Object model = DNDUtil.unwrapToModel( models.getFirstElement( ) );
		if ( model instanceof LibRootModel )
		{
			model = ( (LibRootModel) model ).getModel( );
		}
		if ( model instanceof SlotHandle )
		{
			return (SlotHandle) model;
		}
		else if ( model instanceof DesignElementHandle )
		{
			DesignElementHandle handle = (DesignElementHandle) model;

			if ( handle.getDefn( ).isContainer( ) )
			{
				int slotId = DEUtil.getDefaultSlotID( handle );
				if ( handle.canContain( slotId, insertType ) )
				{
					return handle.getSlot( slotId );
				}
			}
			return handle.getContainerSlotHandle( );
		}
		return null;
	}

	public boolean performDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		TabularCubeHandle cube = (TabularCubeHandle) transfer;

		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getCommandStack( );
		stack.startTrans( "Create crosstab from Cube" ); //$NON-NLS-1$
		ExtendedItemHandle handle = null;
		String name = ReportPlugin.getDefault( )
				.getCustomName( ICrosstabConstants.CROSSTAB_EXTENSION_NAME );

		try
		{
			handle = CrosstabExtendedItemFactory.createCrosstabReportItem( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ),
					null,
					name );
		}
		catch ( Exception e )
		{
			stack.rollback( );
			return false;
		}

		Map map = new HashMap( );
		map.put( DesignerConstants.KEY_NEWOBJECT, handle );
		CreateCommand command = new CreateCommand( map );

		try
		{
			SlotHandle parentModel = getTargetSlotHandle( target,
					ICrosstabConstants.CROSSTAB_EXTENSION_NAME );

			if ( parentModel != null )
			{
				command.setParent( parentModel );
			}
			else
			{
				command.setParent( SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( ) );
			}
			command.execute( );

			handle.setProperty( IReportItemModel.CUBE_PROP, cube );

			List dimensions = cube.getContents( CubeHandle.DIMENSIONS_PROP );
			for ( Iterator iterator = dimensions.iterator( ); iterator.hasNext( ); )
			{
				TabularDimensionHandle dimension = (TabularDimensionHandle) iterator.next( );
				if ( dimension.isTimeType( ) )
				{
					createDimensionViewHandle( handle,
							dimension,
							ICrosstabConstants.COLUMN_AXIS_TYPE );
				}
				else
				{
					createDimensionViewHandle( handle,
							dimension,
							ICrosstabConstants.ROW_AXIS_TYPE );
				}
			}

			List measureGroups = cube.getContents( CubeHandle.MEASURE_GROUPS_PROP );
			int index = 0;
			for ( Iterator iterator = measureGroups.iterator( ); iterator.hasNext( ); )
			{
				MeasureGroupHandle measureGroup = (MeasureGroupHandle) iterator.next( );
				List measures = measureGroup.getContents( MeasureGroupHandle.MEASURES_PROP );
				for ( int j = 0; j < measures.size( ); j++ )
				{
					Object temp = measures.get( j );
					if ( temp instanceof MeasureHandle )
					{
						addMeasureHandle( handle, (MeasureHandle) temp, index++ );
					}
				}
			}
			stack.commit( );

			ReportRequest request = new ReportRequest( );
			List selectionObjects = new ArrayList( );
			selectionObjects.add( handle );
			request.setSelectionObject( selectionObjects );
			request.setType( ReportRequest.SELECTION );
			SessionHandleAdapter.getInstance( )
					.getMediator( )
					.notifyRequest( request );

		}
		catch ( Exception e )
		{
			stack.rollback( );
			return false;
		}

		return true;
	}

	private void addMeasureHandle( ExtendedItemHandle handle,
			MeasureHandle measureHandle, int index ) throws SemanticException
	{
		IReportItem reportItem = handle.getReportItem( );
		CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) reportItem;
		CrosstabAdaptUtil.addMeasureHandle( xtabHandle, measureHandle, index );
	}

	private void createDimensionViewHandle( ExtendedItemHandle handle,
			DimensionHandle dimensionHandle, int type )
			throws SemanticException
	{
		if ( dimensionHandle.getDefaultHierarchy( ).getLevelCount( ) > 0 )
		{
			IReportItem reportItem = handle.getReportItem( );
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) reportItem;

			DimensionViewHandle viewHandle = xtabHandle.insertDimension( dimensionHandle,
					type,
					xtabHandle.getDimensionCount( type ) );

			LevelHandle[] levels = getLevelHandles( dimensionHandle );
			for ( int j = 0; j < levels.length; j++ )
			{

				LevelHandle levelHandle = levels[j];
				DataItemHandle dataHandle = CrosstabAdaptUtil.createColumnBindingAndDataItem( (ExtendedItemHandle) xtabHandle.getModelHandle( ),
						levelHandle );
				LevelViewHandle levelViewHandle = viewHandle.insertLevel( levelHandle,
						j );
				CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

				cellHandle.addContent( dataHandle );
			}
		}
	}

	private LevelHandle[] getLevelHandles( DimensionHandle dimensionHandle )
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
}
