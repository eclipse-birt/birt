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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.AggregationDialog.SubTotalInfo;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;

public class SubTotalProvider extends TotalProvider implements
		ITableLabelProvider,
		IStructuredContentProvider,
		ICellModifier
{
	private int axis;
	private CellEditor[] cellEditor;
	TableViewer viewer;
	
	private String[] comboItems = null;
//	private IAggregationCellViewProvider[] providers;
	private String[] viewNames;

	private CrosstabReportItemHandle crosstab;
	private AggregationCellProviderWrapper cellProviderWrapper;
	
//	private void initialization( )
//	{
//
//		String firstItem = Messages.getString( "SubTotalProvider.Column.ViewStatus" ); //$NON-NLS-1$
//		List viewNameList = new ArrayList( );
//		List itemList = new ArrayList( );
//
//		itemList.add( firstItem );
//		viewNameList.add( "" ); //$NON-NLS-1$
//
//		Object obj = ElementAdapterManager.getAdapters( crosstab.getModelHandle( ),
//				IAggregationCellViewProvider.class );
//		if ( obj instanceof Object[] )
//		{
//			Object arrays[] = (Object[]) obj;
//			providers = new IAggregationCellViewProvider[arrays.length + 1];
//			providers[0] = null;
//			for ( int i = 0; i < arrays.length; i++ )
//			{
//				IAggregationCellViewProvider tmp = (IAggregationCellViewProvider) arrays[i];
//				String viewName = tmp.getViewName( );
//				viewNameList.add( viewName );
//				providers[i + 1] = tmp;
//				itemList.add( Messages.getString( "SubTotalProvider.ShowAs", viewName ) ); //$NON-NLS-1$
//			}
//		}
//
//		comboItems = (String[]) itemList.toArray( new String[itemList.size( )] );
//		viewNames = (String[]) viewNameList.toArray( new String[viewNameList.size( )] );
//
//	}
	
	private void initializeItems(SubTotalInfo subTotalInfo)
	{
		String firstItem = Messages.getString( "GrandTotalProvider.ViewStatus" ); //$NON-NLS-1$
		List viewNameList = new ArrayList( );
		List itemList = new ArrayList( );

		itemList.add( firstItem );
		viewNameList.add( "" ); //$NON-NLS-1$
		
		AggregationCellHandle cell = getAggregationCell( subTotalInfo );
		IAggregationCellViewProvider providers[] = cellProviderWrapper.getAllProviders( );
		for(int i = 0; i < providers.length; i ++)
		{
			IAggregationCellViewProvider tmp = (IAggregationCellViewProvider) providers[i];
			if(tmp == null)
			{
				continue;
			}
			if((cell != null) && (!providers[i].canSwitch( cell )))
			{
				continue;
			}
			String viewName = tmp.getViewName( );			
			viewNameList.add( viewName );
			itemList.add( Messages.getString( "GrandTotalProvider.ShowAs", //$NON-NLS-1$
					new String[]{
						viewName
					} ) );
		}
		comboItems = (String[]) itemList.toArray( new String[itemList.size( )] );
		viewNames = (String[]) viewNameList.toArray( new String[viewNameList.size( )] );
	}
	
	public SubTotalProvider( TableViewer viewer,CrosstabReportItemHandle crosstab,int axis )
	{
		this.viewer = viewer;
		this.crosstab = crosstab;
		this.axis = axis;
//		initialization();
		cellProviderWrapper = new AggregationCellProviderWrapper(crosstab);
	}

	
	public String[] getColumnNames( )
	{
		return columnNames;
	}

	//private CellEditor[] editors;
	private String[] columnNames = new String[]{
			"", Messages.getString("SubTotalProvider.Column.AggregateOn"),//Messages.getString("SubTotalProvider.Column.DataField"), Messages.getString("SubTotalProvider.Column.Function") //$NON-NLS-1$ //$NON-NLS-2$ 
			Messages.getString( "SubTotalProvider.Column.View" ) //$NON-NLS-1$
	};

	/*
	public CellEditor[] getEditors( Table table )
	{
		if ( editors == null )
		{
			editors = new CellEditor[columnNames.length];

			editors[3] = new ComboBoxCellEditor( table,
					new String[0],
					SWT.READ_ONLY );
			String[] items = getFunctionDisplayNames( );
			( (ComboBoxCellEditor) editors[3] ).setItems( items );

		}
		return editors;
	}
*/


	public Image getColumnImage( Object element, int columnIndex )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText( Object element, int columnIndex )
	{
		SubTotalInfo info = (SubTotalInfo) element;
		switch ( columnIndex )
		{
			case 0 :
				return ""; //$NON-NLS-1$
			case 1 :
				return info.getLevel( ).getName( )+"- "+(info.getAggregateOnMeasure( ) == null ? "" //$NON-NLS-1$ //$NON-NLS-2$
						: info.getAggregateOnMeasure( ).getName( ));
			case 2:
				initializeItems(info );
				((ComboBoxCellEditor)cellEditor[2]).setItems( comboItems );
				
				String expectedView = info.getExpectedView( );
				if(expectedView == null || expectedView.length( ) == 0)
				{
					return comboItems[0];
				}
				int index = Arrays.asList( viewNames ).indexOf( expectedView );
				if(index <= 0)
				{
					index = 0;
				}
				return comboItems[index];
			default :
				break;
		}
		return ""; //$NON-NLS-1$
	}

	public Object[] getElements( Object inputElement )
	{
		if ( inputElement instanceof List )
			return ( (List) inputElement ).toArray( );
		return new Object[]{};

	}

	public CellEditor[] getCellEditors()
	{
		if(cellEditor != null)
		{
			return cellEditor;			
		}
		
		ComboBoxCellEditor comboCell = new ComboBoxCellEditor(viewer.getTable( ),new String[0],SWT.READ_ONLY);
		cellEditor = new CellEditor[]{null, null, comboCell};
		return cellEditor;
	}

	
	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{
	}

	public int[] columnWidths( )
	{
		return new int[]{
				20,210,120
		};
	}

	public boolean canModify( Object element, String property )
	{
		// TODO Auto-generated method stub
		if ( Arrays.asList( columnNames ).indexOf( property ) == 2 )
		{
			if(viewer instanceof CheckboxTableViewer)
			{
				return ((CheckboxTableViewer)viewer).getChecked( element );
			}else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}

	public Object getValue( Object element, String property )
	{
		// TODO Auto-generated method stub
		if ( element instanceof Item )
		{
			element = ( (Item) element ).getData( );
		}
		Object value = null;
		int index =  Arrays.asList( columnNames ).indexOf( property );
		switch(index)
		{
			case 1:
				break;
			case 2:
				initializeItems((SubTotalInfo) element );
				((ComboBoxCellEditor)cellEditor[2]).setItems( comboItems );
				String expectedView = ( (SubTotalInfo) (element )).getExpectedView( );
				if(expectedView == null || expectedView.length( ) == 0)
				{
					return new Integer(0);
				}					
				int sel = Arrays.asList( viewNames ).indexOf( expectedView );
				value = sel <= 0 ? new Integer(0) : new Integer(sel);
				break;
			default:
		}
		return value;
	}

	public void modify( Object element, String property, Object value )
	{
		// TODO Auto-generated method stub
		if ( element instanceof Item )
		{
			element = ( (Item) element ).getData( );
		}
		
		int index =  Arrays.asList( columnNames ).indexOf( property );
		switch(index)
		{
			case 0:
				break;
			case 1:
				break;
			case 2:
				int sel = ((Integer)value).intValue( );
				if(sel == 0)
				{
					( (SubTotalInfo) (element )).setExpectedView( "" ); //$NON-NLS-1$
				}else
				{
					( (SubTotalInfo) element ).setExpectedView( viewNames[sel] );
				}
				break;
			default:
		}
		viewer.refresh( );
	}
	
	private LevelViewHandle findLevelViewHandle( LevelHandle handle )
	{

		int dimCount = crosstab.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE );
		for ( int i = 0; i < dimCount; i++ )
		{
			DimensionViewHandle tmpDimView = crosstab.getDimension( ICrosstabConstants.ROW_AXIS_TYPE,
					i );
			LevelViewHandle levelView = tmpDimView.getLevel( handle.getQualifiedName( ) );
			if ( levelView != null )
			{
				return levelView;
			}
		}

		dimCount = crosstab.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE );
		for ( int i = 0; i < dimCount; i++ )
		{
			DimensionViewHandle tmpDimView = crosstab.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE,
					i );
			LevelViewHandle levelView = tmpDimView.getLevel( handle.getQualifiedName( ) );
			if ( levelView != null )
			{
				return levelView;
			}
		}

		return null;
	}
	
	private AggregationCellHandle getAggregationCell(SubTotalInfo subTotalInfo)
	{
		AggregationCellHandle cell = null;
		MeasureHandle measure = subTotalInfo.getAggregateOnMeasure();
		LevelHandle level = subTotalInfo.getLevel( );
		if(measure == null || level == null)
		{
			return cell;
		}
		MeasureViewHandle measureView = crosstab.getMeasure( measure.getQualifiedName( ));
		LevelViewHandle levelView = findLevelViewHandle(level);
		if(measureView == null || levelView == null)
		{
			return cell;
		}		
		
		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;
		
		int axisType = levelView.getAxisType( );

		int counterAxisType = CrosstabUtil.getOppositeAxisType( levelView.getAxisType( ) );
		DimensionViewHandle counterDimension = crosstab.getDimension( counterAxisType,
				crosstab.getDimensionCount( counterAxisType ) - 1 );
		
		String counterDimensionName = null;
		String counterLevelName = null;
		if(counterDimension != null)
		{
			counterDimensionName = counterDimension.getCubeDimensionName( );
			counterLevelName = counterDimension.getLevel( counterDimension.getLevelCount( ) - 1 )
					.getCubeLevelName( );
		}
		

		String dimensionName = ( (DimensionViewHandle) levelView.getContainer( ) ).getCubeDimensionName( );
		String levelName = levelView.getCubeLevelName( );
		if ( levelName == null || dimensionName == null )
			return cell;

		if ( axisType == ICrosstabConstants.ROW_AXIS_TYPE )
		{
			rowDimension = dimensionName;
			rowLevel = levelName;
			colDimension = counterDimensionName;
			colLevel = counterLevelName;
		}
		else if ( axisType == ICrosstabConstants.COLUMN_AXIS_TYPE )
		{
			colDimension = dimensionName;
			colLevel = levelName;
			rowDimension = counterDimensionName;
			rowLevel = counterLevelName;
		}

		cell = measureView.getAggregationCell( rowDimension,
				rowLevel,
				colDimension,
				colLevel );

		
		return cell;
	}
}
