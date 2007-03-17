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

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.GrandTotalInfo;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.SubTotalInfo;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.window.Window;

/**
 * Add the sub total to the level handle.
 */
//NOTE maybe this is a temp class because the SPEC
//TODO i18n the string
//TODO binding the data 
public class AddSubTotalAction extends AbstractCrosstabAction
{

	private static final String LABEL_NAME = "Grand Total";
	LevelViewHandle levelHandle = null;
	private static final String NAME = "add subtotal";
	private static final String ID = "add_subtotal";
	private static final String TEXT = "add subtotal";
	
	/**
	 * The name of the label into the sub total cell.
	 */
	private static final String DISPALY_NAME = "TOTAL";

	/**Constructor
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
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		transStar( NAME );
		try
		{
			AggregationDialog dialog = new AggregationDialog(UIUtil.getDefaultShell( ));
			List subTotals = getSubTotalInfo( );
			List grandTotoals = getGrandTotalInfo( );
			dialog.setInput( copySubTotal( subTotals ), copyGrandTotal( grandTotoals ));
			if ( dialog.open( ) == Window.OK )
			{
				Object[] result = (Object[])dialog.getResult( );
				processSubTotal( subTotals, (List)result[0] );
				processGrandTotal( grandTotoals, (List)result[1]  );
			}
			
//			String funString = DesignChoiceConstants.MEASURE_FUNCTION_SUM;
//			CrosstabReportItemHandle reportHandle = levelHandle.getCrosstab( );
//			List list = new ArrayList();
//			int measureCount = reportHandle.getMeasureCount( );
//			List functionList = new ArrayList();
//			for (int i=0; i<measureCount; i++)
//			{
//				MeasureViewHandle measureHandle = reportHandle.getMeasure( i );
//				list.add( measureHandle );
//				functionList.add(funString  );
//			}
//			
//			
//			CrosstabCellHandle cellHandle = CrosstabUtil.addAggregationHeader( levelHandle, functionList, list );
//			if (cellHandle == null)
//			{
//				return;
//			}
//			LabelHandle dataHandle = DesignElementFactory.getInstance( )
//			.newLabel(null );
//			//Label name is a compand name.
//			dataHandle.setText( "[" + levelHandle.getCubeLevelName( )+ "]" + DISPALY_NAME);
//			
//			cellHandle.addContent( dataHandle );
			
			//System.out.println(CrosstabUtil.getAggregationFunction(levelHandle,reportHandle.getMeasure( 0 )  ));
			
			
//			levelHandle.removeAggregationHeader( );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			return;
		}
		transEnd( );
	}
	
	private List copySubTotal(List list)
	{
		List retValue = new ArrayList();
		for (int i=0; i<list.size( ); i++)
		{
			retValue.add( ((SubTotalInfo)(list.get( i ))).copy( ) );
		}
		return retValue;
	}
	private List copyGrandTotal(List list)
	{
		List retValue = new ArrayList();
		for (int i=0; i<list.size( ); i++)
		{
			retValue.add( ((GrandTotalInfo)(list.get( i ))).copy( ) );
		}
		return retValue;
	}
	
	private void processGrandTotal(List ori, List newList)throws SemanticException
	{
		GrandOpration oriOperation = new GrandOpration();
		GrandOpration newOperation = new GrandOpration();
		for (int i=0; i<ori.size( ); i++)
		{
			GrandTotalInfo oriInfo = (GrandTotalInfo)ori.get( i );
			GrandTotalInfo newInfo = (GrandTotalInfo)newList.get( i );
			oriOperation.addInfo( oriInfo );
			newOperation.addInfo( newInfo );
			if (i== ori.size( ) -1)
			{
				processOperation( oriOperation, newOperation );
			}
		}
	}
	
	private void processOperation(GrandOpration oriOperation, GrandOpration newOperation)throws SemanticException
	{
		if (oriOperation.getMeasures( ).size( ) == 0 && newOperation.getMeasures( ).size( ) == 0)
		{
			return;
		}
		if (oriOperation.getMeasures( ).size( ) == 0 && newOperation.getMeasures( ).size( ) != 0)
		{
			addGrandTotal(levelHandle.getCrosstab( ), getDimensionViewHandle( ).getAxisType( ), 
					newOperation.getFunctions( ), 
					findMeasureViewHandleList(newOperation.getMeasures( )));
		}
		else if (oriOperation.getMeasures( ).size( ) != 0 && newOperation.getMeasures( ).size( ) == 0)
		{
			levelHandle.getCrosstab( ).removeGrandTotal( getDimensionViewHandle( ).getAxisType( ) );
		}
		else 
		{
			int oriSize = oriOperation.getMeasures( ).size( );
			int newSize = newOperation.getMeasures( ).size( );
			if (oriSize != newSize)
			{
				levelHandle.getCrosstab( ).removeGrandTotal( getDimensionViewHandle( ).getAxisType( ) );
				addGrandTotal(levelHandle.getCrosstab( ), getDimensionViewHandle( ).getAxisType( ), 
						newOperation.getFunctions( ), 
						findMeasureViewHandleList(newOperation.getMeasures( )));
				
				return;
			}
			for (int i=0; i<oriSize; i++)
			{
				if (oriOperation.getMeasures( ).get( i ) != newOperation.getMeasures( ).get( i ))
				{
					levelHandle.getCrosstab( ).removeGrandTotal( getDimensionViewHandle( ).getAxisType( ) );
					addGrandTotal(levelHandle.getCrosstab( ), getDimensionViewHandle( ).getAxisType( ), 
							newOperation.getFunctions( ), 
							findMeasureViewHandleList(newOperation.getMeasures( )));
					return;
				}
			}
			for (int i=0; i<oriSize; i++)
			{
				if (!oriOperation.getFunctions( ).get( i ) .equals(newOperation.getFunctions( ).get( i )))
				{
//					CrosstabUtil.setAggregationFunction( findLevelViewHandle( newOperation.getLevelHandle( ) ), 
//							findMeasureViewHandle( (MeasureHandle)newOperation.getMeasures( ).get( i )), 
//							(String)newOperation.getFunctions( ).get( i ) );
					CrosstabUtil.setAggregationFunction( levelHandle.getCrosstab( ), getDimensionViewHandle( ).getAxisType( ),
							findMeasureViewHandle( (MeasureHandle)newOperation.getMeasures( ).get( i )), 
							(String)newOperation.getFunctions( ).get( i ) );
				}
			}
		}
	}
	
	private void processSubTotal(List ori, List newList)throws SemanticException
	{
		SubOpration oriOperation = new SubOpration();
		SubOpration newOperation = new SubOpration();
		for (int i=0; i<ori.size( ); i++)
		{
			SubTotalInfo oriInfo = (SubTotalInfo)ori.get( i );
			SubTotalInfo newInfo = (SubTotalInfo)newList.get( i );
			if (i==0)
			{
				oriOperation.setLevelHandle( oriInfo.getLevel( ) );
				newOperation.setLevelHandle( newInfo.getLevel( ) );
			}
			else if (!oriOperation.isSameOperation( oriInfo ))
			{
				processOperation( oriOperation, newOperation );
				oriOperation = new SubOpration();
				oriOperation.setLevelHandle( oriInfo.getLevel( ) );
				newOperation = new SubOpration();
				newOperation.setLevelHandle( newInfo.getLevel( ) );
			}
			oriOperation.addInfo( oriInfo );
			newOperation.addInfo( newInfo );
			if (i== ori.size( ) -1)
			{
				processOperation( oriOperation, newOperation );
			}
		}
	}
	
	private void processOperation(SubOpration oriOperation, SubOpration newOperation)throws SemanticException
	{
		if (oriOperation.getMeasures( ).size( ) == 0 && newOperation.getMeasures( ).size( ) == 0)
		{
			return;
		}
		if (oriOperation.getMeasures( ).size( ) == 0 && newOperation.getMeasures( ).size( ) != 0)
		{
			addAggregationHeader( findLevelViewHandle( newOperation.getLevelHandle( ) ), newOperation.getFunctions( ), 
					findMeasureViewHandleList(newOperation.getMeasures( )));
		}
		else if (oriOperation.getMeasures( ).size( ) != 0 && newOperation.getMeasures( ).size( ) == 0)
		{
			findLevelViewHandle( oriOperation.getLevelHandle( )).removeAggregationHeader( );
		}
		else 
		{
			int oriSize = oriOperation.getMeasures( ).size( );
			int newSize = newOperation.getMeasures( ).size( );
			if (oriSize != newSize)
			{
				findLevelViewHandle( oriOperation.getLevelHandle( )).removeAggregationHeader( );
				addAggregationHeader( findLevelViewHandle( newOperation.getLevelHandle( ) ), newOperation.getFunctions( ), 
						findMeasureViewHandleList(newOperation.getMeasures( )));
				return;
			}
			for (int i=0; i<oriSize; i++)
			{
				if (oriOperation.getMeasures( ).get( i ) != newOperation.getMeasures( ).get( i ))
				{
					findLevelViewHandle( oriOperation.getLevelHandle( )).removeAggregationHeader( );
					addAggregationHeader( findLevelViewHandle( newOperation.getLevelHandle( ) ), newOperation.getFunctions( ), 
							findMeasureViewHandleList(newOperation.getMeasures( )));
					return;
				}
			}
			for (int i=0; i<oriSize; i++)
			{
				if (!oriOperation.getFunctions( ).get( i ) .equals(newOperation.getFunctions( ).get( i )))
				{
					CrosstabUtil.setAggregationFunction( findLevelViewHandle( newOperation.getLevelHandle( ) ), 
							findMeasureViewHandle( (MeasureHandle)newOperation.getMeasures( ).get( i )), 
							(String)newOperation.getFunctions( ).get( i ) );
				}
			}
		}
	}
	
	private  void addGrandTotal(
			CrosstabReportItemHandle crosstab, int axisType, List functions,
			List measures ) throws SemanticException
	{
		CrosstabCellHandle cellHandle = CrosstabUtil.addGrandTotal( crosstab, axisType , functions, measures );
		if (cellHandle == null)
		{
			return;
		}
		LabelHandle dataHandle = DesignElementFactory.getInstance( )
				.newLabel( null );
		
		// dataHandle.setDisplayName( NAME );
		dataHandle.setText( LABEL_NAME );
		cellHandle.addContent( dataHandle );
	}
	
	private void addAggregationHeader(LevelViewHandle levelView, List functions, List measures)throws SemanticException
	{
		long current = System.currentTimeMillis( );
		CrosstabCellHandle cellHandle = CrosstabUtil.addAggregationHeader( levelView, functions, measures );
		if (cellHandle == null)
		{
			return;
		}
		LabelHandle dataHandle = DesignElementFactory.getInstance( )
		.newLabel(null );
		//Label name is a compand name.
		dataHandle.setText( "[" + levelHandle.getCubeLevelName( )+ "]" + DISPALY_NAME);
		
		cellHandle.addContent( dataHandle );
		System.out.println("model operator" + (System.currentTimeMillis( ) - current));
	}
	
	
	
	private List findMeasureViewHandleList(List list)
	{
		List retValue = new ArrayList();
		for (int i=0; i<list.size( ); i++)
		{
			retValue.add( findMeasureViewHandle( (MeasureHandle)list.get( i ) ) );
		}
		return retValue;
	}
	
	private MeasureViewHandle findMeasureViewHandle(MeasureHandle handle)
	{
		return levelHandle.getCrosstab( ).getMeasure( handle.getQualifiedName( ) );
	}
	private LevelViewHandle findLevelViewHandle(LevelHandle handle)
	{
		DimensionViewHandle viewHandle = getDimensionViewHandle( );
		return viewHandle.getLevel( handle.getQualifiedName( ) );
		//int count = viewHandle.getLevelCount( );
	}
	
	static class SubOpration 
	{
		private LevelHandle levelHandle;
		private List functions = new ArrayList();
		private List measures = new ArrayList();
		
		
		public boolean isSameOperation(SubTotalInfo info)
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
		
		public void addInfo(SubTotalInfo info)
		{
			if (info.isAggregationOn( ))
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

		private List functions = new ArrayList();
		private List measures = new ArrayList();
		public void addInfo(GrandTotalInfo info)
		{
			if (info.isAggregationOn( ))
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
	

	
	private List getSubTotalInfo()
	{
		List retValue = new ArrayList();
		DimensionViewHandle viewHandle = getDimensionViewHandle( );
		CrosstabReportItemHandle reportHandle = viewHandle.getCrosstab( );

		int count = viewHandle.getLevelCount( );
		int measureCount = reportHandle.getMeasureCount( );
		for (int i=0; i<count; i++)
		{
			LevelViewHandle tempViewHandle = viewHandle.getLevel( i );
			LevelHandle tempHandle = tempViewHandle.getCubeLevel( );
			for (int j=0; j<measureCount; j++)
			{
				AggregationDialog.SubTotalInfo info = new AggregationDialog.SubTotalInfo();
				info.setLevel( tempHandle );
				info.setAggregateOnMeasure( reportHandle.getMeasure( j ).getCubeMeasure( ) );
				retValue.add( info );
			}
		}
		
		count = viewHandle.getLevelCount( );
		for (int i=0; i<count; i++)
		{
			LevelViewHandle tempViewHandle = viewHandle.getLevel( i );
			LevelHandle tempHandle = tempViewHandle.getCubeLevel( );
			List measures = CrosstabUtil.getAggregationMeasures( tempViewHandle );
			
			for (int j=0; j<measures.size( ); j++)
			{
				MeasureHandle tempMeasureHandle = ((MeasureViewHandle)measures.get( j )).getCubeMeasure( );
				AggregationDialog.SubTotalInfo info = new AggregationDialog.SubTotalInfo();
				info.setLevel( tempHandle );
				info.setAggregateOnMeasure( tempMeasureHandle);
				info.setFunction( CrosstabUtil.getAggregationFunction(tempViewHandle,(MeasureViewHandle)measures.get( j )  ) );
				//info.setFunction( DesignChoiceConstants.MEASURE_FUNCTION_SUM);
				replaceInfo( info, retValue );
			}
		}
		
		return retValue;
	}
	
	private List getGrandTotalInfo()
	{
		List retValue = new ArrayList();
		DimensionViewHandle viewHandle = getDimensionViewHandle( );
		CrosstabReportItemHandle reportHandle = levelHandle.getCrosstab( );
		int measureCount = reportHandle.getMeasureCount( );
		for (int i=0; i<measureCount; i++)
		{
			AggregationDialog.GrandTotalInfo info = new AggregationDialog.GrandTotalInfo();
			info.setMeasure( reportHandle.getMeasure( i ).getCubeMeasure( ) );
			retValue.add( info );
		}
		
		List measures = CrosstabUtil.getAggregationMeasures( reportHandle, viewHandle.getAxisType( ) );
		for (int i=0; i<measures.size( ); i++)
		{
			AggregationDialog.GrandTotalInfo info = new AggregationDialog.GrandTotalInfo();
			MeasureViewHandle measureViewHandle = (MeasureViewHandle)measures.get( i );
			info.setMeasure( measureViewHandle.getCubeMeasure( ) );
			info.setFunction( CrosstabUtil.getAggregationFunction( reportHandle, viewHandle.getAxisType( ), measureViewHandle ) );
			replaceInfo( info, retValue );
		}
		
		return retValue;
		
	}
	
	//private List 
	
	private void replaceInfo(AggregationDialog.SubTotalInfo info, List list)
	{
		for (int i=0; i<list.size( ); i++)
		{
			if (info.isSameInfo( list.get( i ) ))
			{
				AggregationDialog.SubTotalInfo tempInfo = (AggregationDialog.SubTotalInfo)list.get( i );
				tempInfo.setAggregationOn( true );
				tempInfo.setFunction( info.getFunction( ) );
			}
		}
	}
	
	private void replaceInfo(AggregationDialog.GrandTotalInfo info, List list)
	{
		for (int i=0; i<list.size( ); i++)
		{
			if (info.isSameInfo( list.get( i ) ))
			{
				AggregationDialog.GrandTotalInfo tempInfo = (AggregationDialog.GrandTotalInfo)list.get( i );
				tempInfo.setAggregationOn( true );
				tempInfo.setFunction( info.getFunction( ) );
			}
		}
	}
	
	private DimensionViewHandle getDimensionViewHandle()
	{
		return CrosstabAdaptUtil.getDimensionViewHandle( (ExtendedItemHandle)(levelHandle.getModelHandle()) );
	}
	
}
