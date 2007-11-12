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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.GrandTotalInfo;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.SubTotalInfo;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

/**
 * Add the sub total to the level handle.
 */
public class AddSubTotalAction extends AbstractCrosstabAction
{

	LevelViewHandle levelHandle = null;
	private static final String NAME = Messages.getString( "AddSubTotalAction.TransName" );//$NON-NLS-1$
	private static final String ID = "add_subtotal";//$NON-NLS-1$
	private static final String TEXT = Messages.getString( "AddSubTotalAction.DisplayName" );//$NON-NLS-1$

	/**
	 * The name of the label into the sub total cell.
	 */
	// private static final String DISPALY_NAME = "TOTAL";
	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public AddSubTotalAction( DesignElementHandle handle )
	{
		super( handle );
		setId( ID );
		setText( TEXT );
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle( handle );
		setHandle( extendedHandle );
		levelHandle = CrosstabAdaptUtil.getLevelViewHandle( extendedHandle );

		Image image = CrosstabUIHelper.getImage( CrosstabUIHelper.LEVEL_AGGREGATION );
		setImageDescriptor( ImageDescriptor.createFromImage( image ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		transStar( NAME );
		try
		{
			AggregationDialog dialog = new AggregationDialog( UIUtil.getDefaultShell( ) );
			dialog.setAxis( levelHandle.getAxisType( ) );
			List rowSubTotals = getSubTotalInfo( ICrosstabConstants.ROW_AXIS_TYPE );
			List rowGrandTotals = getGrandTotalInfo(ICrosstabConstants.ROW_AXIS_TYPE );
			List colSubTotals = getSubTotalInfo( ICrosstabConstants.COLUMN_AXIS_TYPE );
			List colGrandTotals = getGrandTotalInfo(ICrosstabConstants.COLUMN_AXIS_TYPE );
			dialog.setInput( copySubTotal( rowSubTotals ),
					copyGrandTotal( rowGrandTotals ), 
					copySubTotal( colSubTotals ),
					copyGrandTotal( colGrandTotals ));
			if ( dialog.open( ) == Window.OK )
			{
				Object[] result = (Object[]) dialog.getResult( );
				processSubTotal( rowSubTotals, (List) result[0] );
				processGrandTotal( rowGrandTotals, (List) result[1], ICrosstabConstants.ROW_AXIS_TYPE);
				
				processSubTotal( colSubTotals, (List) result[2] );
				processGrandTotal( colGrandTotals, (List) result[3],ICrosstabConstants.COLUMN_AXIS_TYPE );
			}
		}
		catch ( SemanticException e )
		{
			rollBack( );
			ExceptionHandler.handle( e );
			return;
		}
		transEnd( );
	}

	private List copySubTotal( List list )
	{
		List retValue = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			retValue.add( ( (SubTotalInfo) ( list.get( i ) ) ).copy( ) );
		}
		return retValue;
	}

	private List copyGrandTotal( List list )
	{
		List retValue = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			retValue.add( ( (GrandTotalInfo) ( list.get( i ) ) ).copy( ) );
		}
		return retValue;
	}

	private void processGrandTotal( List ori, List newList, int axisType )
			throws SemanticException
	{
		GrandOpration oriOperation = new GrandOpration( );
		GrandOpration newOperation = new GrandOpration( );
		for ( int i = 0; i < ori.size( ); i++ )
		{
			GrandTotalInfo oriInfo = (GrandTotalInfo) ori.get( i );
			GrandTotalInfo newInfo = (GrandTotalInfo) newList.get( i );
			oriOperation.addInfo( oriInfo );
			newOperation.addInfo( newInfo );
			if ( i == ori.size( ) - 1 )
			{
				processOperation( oriOperation, newOperation, axisType );
			}
		}
	}

	private void processOperation( GrandOpration oriOperation,
			GrandOpration newOperation, int axisType ) throws SemanticException
	{
		if ( oriOperation.getMeasures( ).size( ) == 0
				&& newOperation.getMeasures( ).size( ) == 0 )
		{
			return;
		}
		if ( oriOperation.getMeasures( ).size( ) == 0
				&& newOperation.getMeasures( ).size( ) != 0 )
		{
			addGrandTotal( levelHandle.getCrosstab( ),
					axisType,
					newOperation.getFunctions( ),
					findMeasureViewHandleList( newOperation.getMeasures( ) ) );
		}
		else if ( oriOperation.getMeasures( ).size( ) != 0
				&& newOperation.getMeasures( ).size( ) == 0 )
		{
			levelHandle.getCrosstab( )
					.removeGrandTotal( axisType );
		}
		else
		{
			int oriSize = oriOperation.getMeasures( ).size( );
			int newSize = newOperation.getMeasures( ).size( );
			if ( oriSize != newSize )
			{
				levelHandle.getCrosstab( )
						.removeGrandTotal( axisType );
				addGrandTotal( levelHandle.getCrosstab( ),
						axisType,
						newOperation.getFunctions( ),
						findMeasureViewHandleList( newOperation.getMeasures( ) ) );

				return;
			}
			for ( int i = 0; i < oriSize; i++ )
			{
				if ( oriOperation.getMeasures( ).get( i ) != newOperation.getMeasures( )
						.get( i ) )
				{
					levelHandle.getCrosstab( )
							.removeGrandTotal( axisType );
					addGrandTotal( levelHandle.getCrosstab( ),
							axisType,
							newOperation.getFunctions( ),
							findMeasureViewHandleList( newOperation.getMeasures( ) ) );
					return;
				}
			}
			for ( int i = 0; i < oriSize; i++ )
			{
				if ( !oriOperation.getFunctions( )
						.get( i )
						.equals( newOperation.getFunctions( ).get( i ) ) )
				{
					// CrosstabUtil.setAggregationFunction( findLevelViewHandle(
					// newOperation.getLevelHandle( ) ),
					// findMeasureViewHandle(
					// (MeasureHandle)newOperation.getMeasures( ).get( i )),
					// (String)newOperation.getFunctions( ).get( i ) );
					levelHandle.getCrosstab( )
							.setAggregationFunction( axisType,
									findMeasureViewHandle( (MeasureHandle) newOperation.getMeasures( )
											.get( i ) ),
									(String) newOperation.getFunctions( )
											.get( i ) );
				}
			}
		}
	}

	private void processSubTotal( List ori, List newList )
			throws SemanticException
	{
		SubOpration oriOperation = new SubOpration( );
		SubOpration newOperation = new SubOpration( );
		for ( int i = 0; i < ori.size( ); i++ )
		{
			SubTotalInfo oriInfo = (SubTotalInfo) ori.get( i );
			SubTotalInfo newInfo = (SubTotalInfo) newList.get( i );
			if ( i == 0 )
			{
				oriOperation.setLevelHandle( oriInfo.getLevel( ) );
				newOperation.setLevelHandle( newInfo.getLevel( ) );
			}
			else if ( !oriOperation.isSameOperation( oriInfo ) )
			{
				processOperation( oriOperation, newOperation );
				oriOperation = new SubOpration( );
				oriOperation.setLevelHandle( oriInfo.getLevel( ) );
				newOperation = new SubOpration( );
				newOperation.setLevelHandle( newInfo.getLevel( ) );
			}
			oriOperation.addInfo( oriInfo );
			newOperation.addInfo( newInfo );
			if ( i == ori.size( ) - 1 )
			{
				processOperation( oriOperation, newOperation );
			}
		}
	}

	private void processOperation( SubOpration oriOperation,
			SubOpration newOperation ) throws SemanticException
	{
		if ( oriOperation.getMeasures( ).size( ) == 0
				&& newOperation.getMeasures( ).size( ) == 0 )
		{
			return;
		}
		if ( oriOperation.getMeasures( ).size( ) == 0
				&& newOperation.getMeasures( ).size( ) != 0 )
		{
			addAggregationHeader( findLevelViewHandle( newOperation.getLevelHandle( ) ),
					newOperation.getFunctions( ),
					findMeasureViewHandleList( newOperation.getMeasures( ) ) );
		}
		else if ( oriOperation.getMeasures( ).size( ) != 0
				&& newOperation.getMeasures( ).size( ) == 0 )
		{
			findLevelViewHandle( oriOperation.getLevelHandle( ) ).removeSubTotal( );
		}
		else
		{
			int oriSize = oriOperation.getMeasures( ).size( );
			int newSize = newOperation.getMeasures( ).size( );
			if ( oriSize != newSize )
			{
				findLevelViewHandle( oriOperation.getLevelHandle( ) ).removeSubTotal( );
				addAggregationHeader( findLevelViewHandle( newOperation.getLevelHandle( ) ),
						newOperation.getFunctions( ),
						findMeasureViewHandleList( newOperation.getMeasures( ) ) );
				return;
			}
			for ( int i = 0; i < oriSize; i++ )
			{
				if ( oriOperation.getMeasures( ).get( i ) != newOperation.getMeasures( )
						.get( i ) )
				{
					findLevelViewHandle( oriOperation.getLevelHandle( ) ).removeSubTotal( );
					addAggregationHeader( findLevelViewHandle( newOperation.getLevelHandle( ) ),
							newOperation.getFunctions( ),
							findMeasureViewHandleList( newOperation.getMeasures( ) ) );
					return;
				}
			}
			for ( int i = 0; i < oriSize; i++ )
			{
				if ( !oriOperation.getFunctions( )
						.get( i )
						.equals( newOperation.getFunctions( ).get( i ) ) )
				{
					findLevelViewHandle( newOperation.getLevelHandle( ) ).setAggregationFunction( findMeasureViewHandle( (MeasureHandle) newOperation.getMeasures( )
							.get( i ) ),
							(String) newOperation.getFunctions( ).get( i ) );
				}
			}
		}
	}

	private void addGrandTotal( CrosstabReportItemHandle crosstab,
			int axisType, List functions, List measures )
			throws SemanticException
	{
		CrosstabCellHandle cellHandle = crosstab.addGrandTotal( axisType,
				measures,
				functions );
		if ( cellHandle == null )
		{
			return;
		}
		CrosstabUIHelper.CreateGrandTotalLabel( cellHandle );
	}

	private void addAggregationHeader( LevelViewHandle levelView,
			List functions, List measures ) throws SemanticException
	{
		CrosstabCellHandle cellHandle = levelView.addSubTotal( measures,
				functions );
		if ( cellHandle == null )
		{
			return;
		}
		CrosstabUIHelper.CreateSubTotalLabel( levelView, cellHandle );
	}

	private List findMeasureViewHandleList( List list )
	{
		List retValue = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			retValue.add( findMeasureViewHandle( (MeasureHandle) list.get( i ) ) );
		}
		return retValue;
	}

	private MeasureViewHandle findMeasureViewHandle( MeasureHandle handle )
	{
		return levelHandle.getCrosstab( )
				.getMeasure( handle.getQualifiedName( ) );
	}

	private LevelViewHandle findLevelViewHandle( LevelHandle handle )
	{
		DimensionViewHandle viewHandle = getDimensionViewHandle( );
		CrosstabReportItemHandle crosstab = viewHandle.getCrosstab( );

		int dimCount = crosstab.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE );
		for(int i = 0; i < dimCount; i ++)
		{
			DimensionViewHandle tmpDimView = crosstab.getDimension( ICrosstabConstants.ROW_AXIS_TYPE, i );
			LevelViewHandle levelView = tmpDimView.getLevel( handle.getQualifiedName( ) );
			if(levelView != null)
			{
				return levelView;
			}
		}
		
		dimCount = crosstab.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE );
		for(int i = 0; i < dimCount; i ++)
		{
			DimensionViewHandle tmpDimView = crosstab.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE, i );
			LevelViewHandle levelView = tmpDimView.getLevel( handle.getQualifiedName( ) );
			if(levelView != null)
			{
				return levelView;
			}
		}
		
		return null;
	}

	static class SubOpration
	{

		private LevelHandle levelHandle;
		private List functions = new ArrayList( );
		private List measures = new ArrayList( );

		public boolean isSameOperation( SubTotalInfo info )
		{
			return info.getLevel( ) == levelHandle;
		}

		public LevelHandle getLevelHandle( )
		{
			return levelHandle;
		}

		public void setLevelHandle( LevelHandle levelHandle )
		{
			this.levelHandle = levelHandle;
		}

		public void addInfo( SubTotalInfo info )
		{
			if ( info.isAggregationOn( ) )
			{
				functions.add( info.getFunction( ) );
				measures.add( info.getAggregateOnMeasure( ) );
			}
		}

		public List getFunctions( )
		{
			return functions;
		}

		public List getMeasures( )
		{
			return measures;
		}
	}

	static class GrandOpration
	{

		private List functions = new ArrayList( );
		private List measures = new ArrayList( );

		public void addInfo( GrandTotalInfo info )
		{
			if ( info.isAggregationOn( ) )
			{
				functions.add( info.getFunction( ) );
				measures.add( info.getMeasure( ) );
			}
		}

		public List getFunctions( )
		{
			return functions;
		}

		public List getMeasures( )
		{
			return measures;
		}
	}

	private boolean isVertical( CrosstabReportItemHandle reportHandle )
	{
		return ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( reportHandle.getMeasureDirection( ) );
	}

	private boolean getAssociation( int axis )
	{
		DimensionViewHandle viewHandle = getDimensionViewHandle( );
		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab( );
		if ( ICrosstabConstants.COLUMN_AXIS_TYPE == axis )
		{
			if ( isVertical( reportHandle ) )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		if ( ICrosstabConstants.ROW_AXIS_TYPE == axis )
		{
			if ( isVertical( reportHandle ) )
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		return false;
	}

	private List getSubTotalInfo( int axis )
	{
		List retValue = new ArrayList( );
		DimensionViewHandle viewHandle = getDimensionViewHandle( );
		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab( );
		int dimCount = reportHandle.getDimensionCount( axis );
		int measureCount = reportHandle.getMeasureCount( );
		LevelViewHandle lastLevelHandle = getLastLevelViewHandle( axis );

		for ( int k = 0; k < dimCount; k++ )
		{
			DimensionViewHandle dimension = reportHandle.getDimension( axis, k );
			int count = dimension.getLevelCount( );

			for ( int i = 0; i < count; i++ )
			{
				LevelViewHandle tempViewHandle = dimension.getLevel( i );
				if ( tempViewHandle == lastLevelHandle )
				{
					continue;
				}
				LevelHandle tempHandle = tempViewHandle.getCubeLevel( );
				for ( int j = 0; j < measureCount; j++ )
				{
					AggregationDialog.SubTotalInfo info = new AggregationDialog.SubTotalInfo( );
					info.setLevel( tempHandle );
					info.setAggregateOnMeasure( reportHandle.getMeasure( j )
							.getCubeMeasure( ) );

					info.setFunction( reportHandle.getMeasure( j )
							.getCubeMeasure( )
							.getFunction( ) );
					retValue.add( info );
					// fix bug
					info.setAssociation( getAssociation( axis ) );
				}
			}
		}

		for ( int k = 0; k < dimCount; k++ )
		{
			DimensionViewHandle dimension = reportHandle.getDimension( axis, k );
			int count = dimension.getLevelCount( );
			for ( int i = 0; i < count; i++ )
			{
				LevelViewHandle tempViewHandle = dimension.getLevel( i );
				LevelHandle tempHandle = tempViewHandle.getCubeLevel( );
				List measures = tempViewHandle.getAggregationMeasures( );

				for ( int j = 0; j < measures.size( ); j++ )
				{
					MeasureHandle tempMeasureHandle = ( (MeasureViewHandle) measures.get( j ) ).getCubeMeasure( );
					AggregationDialog.SubTotalInfo info = new AggregationDialog.SubTotalInfo( );
					info.setLevel( tempHandle );
					info.setAggregateOnMeasure( tempMeasureHandle );
					// info.setFunction( tempViewHandle.getAggregationFunction(
					// (MeasureViewHandle) measures.get( j ) ) );
					info.setFunction( tempMeasureHandle.getFunction( ) );
					// tempMeasureHandle.getFunction( );
					// info.setFunction(
					// DesignChoiceConstants.MEASURE_FUNCTION_SUM);
					replaceInfo( info, retValue );
				}
			}
		}
		return retValue;
	}

	private LevelViewHandle getLastLevelViewHandle( int axis )
	{
		DimensionViewHandle viewHandle = getDimensionViewHandle( );
		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab( );
		int count = reportHandle.getDimensionCount( axis );
		if ( count == 0 )
		{
			return null;
		}
		DimensionViewHandle lastDimension = reportHandle.getDimension( axis,
				count - 1 );

		return lastDimension.getLevel( lastDimension.getLevelCount( ) - 1 );
	}

	private List getGrandTotalInfo( int axis )
	{
		List retValue = new ArrayList( );
		CrosstabReportItemHandle reportHandle = levelHandle.getCrosstab( );
		CrosstabViewHandle crosstabView = reportHandle.getCrosstabView( axis );
		if(crosstabView == null || crosstabView.getDimensionCount( ) == 0)
		{
			return retValue;
		}
		
		int measureCount = reportHandle.getMeasureCount( );
		for ( int i = 0; i < measureCount; i++ )
		{
			AggregationDialog.GrandTotalInfo info = new AggregationDialog.GrandTotalInfo( );
			info.setMeasure( reportHandle.getMeasure( i ).getCubeMeasure( ) );

			info.setFunction( reportHandle.getMeasure( i )
					.getCubeMeasure( )
					.getFunction( ) );
			retValue.add( info );
			info.setAssociation( getAssociation( axis ) );
		}

		List measures = reportHandle.getAggregationMeasures( axis );
		for ( int i = 0; i < measures.size( ); i++ )
		{
			AggregationDialog.GrandTotalInfo info = new AggregationDialog.GrandTotalInfo( );
			MeasureViewHandle measureViewHandle = (MeasureViewHandle) measures.get( i );
			info.setMeasure( measureViewHandle.getCubeMeasure( ) );
			// info.setFunction( reportHandle.getAggregationFunction(
			// viewHandle.getAxisType( ),
			// measureViewHandle ) );

			info.setFunction( measureViewHandle.getCubeMeasure( ).getFunction( ) );
			replaceInfo( info, retValue );
		}

		return retValue;

	}

	// private List

	private void replaceInfo( AggregationDialog.SubTotalInfo info, List list )
	{
		for ( int i = 0; i < list.size( ); i++ )
		{
			if ( info.isSameInfo( list.get( i ) ) )
			{
				AggregationDialog.SubTotalInfo tempInfo = (AggregationDialog.SubTotalInfo) list.get( i );
				tempInfo.setAggregationOn( true );
				tempInfo.setFunction( info.getFunction( ) );
				break;
			}
		}
	}

	private void replaceInfo( AggregationDialog.GrandTotalInfo info, List list )
	{
		for ( int i = 0; i < list.size( ); i++ )
		{
			if ( info.isSameInfo( list.get( i ) ) )
			{
				AggregationDialog.GrandTotalInfo tempInfo = (AggregationDialog.GrandTotalInfo) list.get( i );
				tempInfo.setAggregationOn( true );
				tempInfo.setFunction( info.getFunction( ) );
			}
		}
	}

	private DimensionViewHandle getDimensionViewHandle( )
	{
		return CrosstabAdaptUtil.getDimensionViewHandle( (ExtendedItemHandle) ( levelHandle.getModelHandle( ) ) );
	}

}
