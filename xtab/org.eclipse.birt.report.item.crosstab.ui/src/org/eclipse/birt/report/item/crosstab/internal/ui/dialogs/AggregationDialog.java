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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class AggregationDialog extends BaseDialog
{

	private CheckboxTableViewer subTableViewer;

	public AggregationDialog( Shell shell )
	{
		super( shell, "Aggregation" );
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite dialogArea = (Composite) super.createDialogArea( parent );

		Composite content = new Composite( dialogArea, SWT.NONE );
		content.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		content.setLayout( layout );
		createSubTotalArea( content );
		createGrandTotalArea( content );

		init( );

		return dialogArea;
	}

	private void init( )
	{
		if ( subList != null )
		{
			subTableViewer.setInput( subList );
			for ( int i = 0; i < subTableViewer.getTable( ).getItemCount( ); i++ )
			{
				TableItem item = subTableViewer.getTable( ).getItem( i );
				if ( item.getData( ) != null
						&& item.getData( ) instanceof SubTotalInfo )
				{
					item.setChecked( ( (SubTotalInfo) item.getData( ) ).isAggregationOn( ) );
				}
			}
		}

		if ( grandList != null )
		{
			grandTableViewer.setInput( grandList );
			for ( int i = 0; i < grandTableViewer.getTable( ).getItemCount( ); i++ )
			{
				TableItem item = grandTableViewer.getTable( ).getItem( i );
				if ( item.getData( ) != null
						&& item.getData( ) instanceof GrandTotalInfo )
				{
					item.setChecked( ( (GrandTotalInfo) item.getData( ) ).isAggregationOn( ) );
				}
			}
		}

	}

	private void createGrandTotalArea( Composite content )
	{
		Label grandTotalLabel = new Label( content, SWT.NONE );
		grandTotalLabel.setText( "Grand Totals:" );

		Table table = new Table( content, SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION
				| SWT.CHECK );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		table.setLayoutData( gd );

		grandTableViewer = new CheckboxTableViewer( table );
		GrandTotalProvider provider = new GrandTotalProvider( grandTableViewer );

		String[] columnNames = provider.getColumnNames( );
		int[] columnWidths = provider.columnWidths( );
		for ( int i = 0; i < columnNames.length; i++ )
		{
			TableColumn column = new TableColumn( table, SWT.LEFT );
			column.setText( columnNames[i] );
			column.setWidth( columnWidths[i] );
		}

		grandTableViewer.setUseHashlookup( true );
		grandTableViewer.setColumnProperties( provider.getColumnNames( ) );
		grandTableViewer.setCellEditors( provider.getEditors( table ) );
		grandTableViewer.setContentProvider( provider );
		grandTableViewer.setLabelProvider( provider );
		grandTableViewer.setCellModifier( provider );

		grandTableViewer.addCheckStateListener( new ICheckStateListener( ) {

			public void checkStateChanged( CheckStateChangedEvent event )
			{
				GrandTotalInfo info = (GrandTotalInfo) event.getElement( );
				if ( event.getChecked( ) )
				{
					info.setAggregationOn( true );
				}
				else
					info.setAggregationOn( false );
			}

		} );

	}

	private List subList = new LinkedList( );
	private List grandList = new LinkedList( );
	private CheckboxTableViewer grandTableViewer;

	public void setInput( List subList, List grandList )
	{
		this.subList.addAll( subList );
		this.grandList.addAll( grandList );
	}

	private void createSubTotalArea( Composite content )
	{
		Label subTotalLabel = new Label( content, SWT.NONE );
		subTotalLabel.setText( "Sub Totals:" );

		Table table = new Table( content, SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION
				| SWT.CHECK );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		table.setLayoutData( gd );

		subTableViewer = new CheckboxTableViewer( table );
		SubTotalProvider provider = new SubTotalProvider( subTableViewer );

		String[] columnNames = provider.getColumnNames( );
		int[] columnWidths = provider.columnWidths( );
		for ( int i = 0; i < columnNames.length; i++ )
		{
			TableColumn column = new TableColumn( table, SWT.LEFT );
			column.setText( columnNames[i] );
			column.setWidth( columnWidths[i] );
		}

		subTableViewer.setUseHashlookup( true );
		subTableViewer.setColumnProperties( provider.getColumnNames( ) );
		subTableViewer.setCellEditors( provider.getEditors( table ) );
		subTableViewer.setContentProvider( provider );
		subTableViewer.setLabelProvider( provider );
		subTableViewer.setCellModifier( provider );

		subTableViewer.addCheckStateListener( new ICheckStateListener( ) {

			public void checkStateChanged( CheckStateChangedEvent event )
			{
				SubTotalInfo info = (SubTotalInfo) event.getElement( );
				if ( event.getChecked( ) )
				{
					info.setAggregationOn( true );
				}
				else
					info.setAggregationOn( false );
			}

		} );

	}

	public Object getResult( )
	{
		return new Object[]{
				subList, grandList
		};
	}
	
	/**
	 * SubTotalInfo
	 */
	public static  class SubTotalInfo
	{

		private LevelHandle level;
		private MeasureHandle measure;

		private boolean aggregationOn = false;

		private String function = "";

		public SubTotalInfo copy()
		{
			SubTotalInfo retValue = new SubTotalInfo();
			retValue.setAggregateOnMeasure( getAggregateOnMeasure( ) );
			retValue.setAggregationOn( isAggregationOn( ) );
			retValue.setFunction( getFunction( ) );
			retValue.setLevel( getLevel( ) );
			return retValue;
		}
		
		public MeasureHandle getAggregateOnMeasure( )
		{
			return measure;
		}

		public String getFunction( )
		{
			return function;
		}

		public LevelHandle getLevel( )
		{
			return level;
		}

		public boolean isAggregationOn( )
		{
			return aggregationOn;
		}

		public void setAggregateOnMeasure( MeasureHandle measure )
		{
			this.measure = measure;
		}

		public void setAggregationOn( boolean aggregationOn )
		{
			this.aggregationOn = aggregationOn;
		}

		

		public void setFunction( String function )
		{
			this.function = function;
		}

		public void setLevel( LevelHandle level )
		{
			this.level = level;
		}
		
		public boolean isSameInfo(Object obj)
		{
			if (!(obj instanceof SubTotalInfo))
			{
				return false;
			}
			SubTotalInfo temp = (SubTotalInfo)obj;
			return temp.getLevel( ) == level && temp.getAggregateOnMeasure( ) == measure;
		}
		
		public boolean equals( Object obj )
		{
			if (!(obj instanceof SubTotalInfo))
			{
				return false;
			}
			SubTotalInfo temp = (SubTotalInfo)obj;
			return temp.getLevel( ) == level && temp.getAggregateOnMeasure( ) == measure
				&& temp.getFunction( ) == function && temp.isAggregationOn( ) == aggregationOn; 
		}
	}
	
	/**
	 * GrandTotalInfo
	 */
	public static class GrandTotalInfo
	{

		private MeasureHandle measure;

		private boolean aggregationOn = false;

		private String function = "";

		public GrandTotalInfo copy()
		{
			GrandTotalInfo retValue = new GrandTotalInfo();
			retValue.setAggregationOn( isAggregationOn( ) );
			retValue.setFunction( getFunction( ) );
			retValue.setMeasure( getMeasure( ) );
			return retValue;
		}
		
		public String getFunction( )
		{
			return function;
		}

		public MeasureHandle getMeasure( )
		{
			return measure;
		}

		public boolean isAggregationOn( )
		{
			return aggregationOn;
		}

		public void setAggregationOn( boolean aggregationOn )
		{
			this.aggregationOn = aggregationOn;
		}

		public void setFunction( String function )
		{
			this.function = function;
		}

		public void setMeasure( MeasureHandle measure )
		{
			this.measure = measure;
		}
		
		public boolean isSameInfo(Object obj)
		{
			if (!(obj instanceof GrandTotalInfo))
			{
				return false;
			}
			GrandTotalInfo temp = (GrandTotalInfo)obj;
			return temp.getMeasure( ) == measure;
		}

	}
}
