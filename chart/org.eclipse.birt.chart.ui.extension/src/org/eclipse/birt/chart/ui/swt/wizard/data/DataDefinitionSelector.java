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

package org.eclipse.birt.chart.ui.swt.wizard.data;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ColorPalette;
import org.eclipse.birt.chart.ui.swt.wizard.internal.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.wizard.internal.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 */

public class DataDefinitionSelector
		implements
			ISelectDataComponent,
			SelectionListener
{

	private transient EList seriesDefns = null;

	private transient IUIServiceProvider serviceprovider = null;

	private transient String sTitle = null;

	private transient Object oContext = null;

	private transient Composite cmpTop = null;

	private transient Composite cmpData = null;

	private transient ISelectDataComponent dateComponent = null;

	private transient Button btnSeriesAdd;

	private transient Combo cmbSeriesSelect;

	private transient int axisIndex;

	private transient int numberRows = 2;

	private transient String selectionName = Messages.getString( "DataDefinitionSelector.Label.Series" ); //$NON-NLS-1$

	private transient String description = ""; //$NON-NLS-1$

	private transient int areaType = ISelectDataCustomizeUI.ORTHOGONAL_SERIES;

	private transient ISelectDataCustomizeUI selectDataUI = null;

	private transient Chart chart;

	/**
	 * 
	 * @param chart
	 * @param axisIndex
	 *            0 means single axis; 1 means the first of multiple axes, 2
	 *            means the second axis
	 * @param seriesDefns
	 * @param builder
	 * @param oContext
	 * @param sTitle
	 * @param selectDataUI
	 */
	public DataDefinitionSelector( Chart chart, int axisIndex,
			EList seriesDefns, IUIServiceProvider builder, Object oContext,
			String sTitle, ISelectDataCustomizeUI selectDataUI )
	{
		this.chart = chart;
		this.seriesDefns = seriesDefns;
		this.serviceprovider = builder;
		this.oContext = oContext;
		this.sTitle = sTitle;
		this.axisIndex = axisIndex;
		this.selectDataUI = selectDataUI;
	}

	public Composite createArea( Composite parent )
	{
		cmpTop = null;
		{
			if ( axisIndex > 0 )
			{
				cmpTop = new Group( parent, SWT.NONE );
				( (Group) cmpTop ).setText( Messages.getString( "DataDefinitionSelector.Label.YAxis" ) + axisIndex ); //$NON-NLS-1$
			}
			else
			{
				cmpTop = new Composite( parent, SWT.NONE );
			}

			GridLayout layout = new GridLayout( );
			layout.numColumns = 4 / numberRows;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			cmpTop.setLayout( layout );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpTop.setLayoutData( gd );
		}

		cmbSeriesSelect = new Combo( cmpTop, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			cmbSeriesSelect.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			cmbSeriesSelect.addSelectionListener( this );
			refreshCombo( );
		}

		btnSeriesAdd = new Button( cmpTop, SWT.NONE );
		{
			refreshButton( );
			btnSeriesAdd.addSelectionListener( this );
		}

		createDataDefinition( );

		return cmpTop;
	}

	private void createDataDefinition( )
	{
		ISelectDataComponent newComponent = getDataDefinitionComponent( getCurrentSeriesDefinition( ) );
		if ( dateComponent != null
				&& dateComponent.getClass( ) == newComponent.getClass( ) )
		{
			// No change if new UI is same with the old
			return;
		}

		if ( cmpData != null && !cmpData.isDisposed( ) )
		{
			cmpData.dispose( );
		}

		dateComponent = newComponent;
		cmpData = dateComponent.createArea( cmpTop );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmpData.setLayoutData( gd );
		}
	}

	private SeriesDefinition getCurrentSeriesDefinition( )
	{
		int selectedIndex = cmbSeriesSelect.getSelectionIndex( );
		return (SeriesDefinition) seriesDefns.get( selectedIndex < 0 ? 0
				: selectedIndex );
	}

	private int getFirstIndexOfSameAxis( )
	{
		if ( axisIndex >= 2 )
		{
			return ChartUIUtil.getOrthogonalSeriesDefinitions( chart, 0 )
					.size( );
		}
		return 0;
	}

	protected void addNewSeriesDefinition( )
	{
		// Create a series definition without data definition
		SeriesDefinition sdTmp = SeriesDefinitionImpl.create( );
		sdTmp.getSeriesPalette( ).update( -1 );
		sdTmp.getSeries( )
				.add( EcoreUtil.copy( ( (SeriesDefinition) seriesDefns.get( 0 ) ).getDesignTimeSeries( ) ) );
		cleanDataDefinition( sdTmp );
		sdTmp.eAdapters( ).addAll( getCurrentSeriesDefinition( ).eAdapters( ) );

		int firstIndex = getFirstIndexOfSameAxis( );
		EList list = chart.getSampleData( ).getOrthogonalSampleData( );

		// Create a new OrthogonalSampleData instance from the existing one
		OrthogonalSampleData sdOrthogonal = (OrthogonalSampleData) EcoreUtil.copy( (EObject) list.get( firstIndex ) );
		sdOrthogonal.setSeriesDefinitionIndex( firstIndex + 1 );
		sdOrthogonal.eAdapters( ).addAll( chart.getSampleData( ).eAdapters( ) );

		seriesDefns.add( sdTmp );
		// Update the SampleData. Must do this after add series definition.
		list.add( firstIndex + 1, sdOrthogonal );

		// Reset series index of sample data
		if ( axisIndex == 1 && ChartUIUtil.getOrthogonalAxisNumber( chart ) > 1 )
		{
			for ( int i = 0; i < list.size( ); i++ )
			{
				( (OrthogonalSampleData) list.get( i ) ).setSeriesDefinitionIndex( i );
			}
		}
	}

	private void cleanDataDefinition( SeriesDefinition sd )
	{
		EList dds = sd.getDesignTimeSeries( ).getDataDefinition( );
		for ( int i = 0; i < dds.size( ); i++ )
		{
			( (Query) dds.get( i ) ).setDefinition( "" ); //$NON-NLS-1$
		}
	}

	protected void removeSeriesDefinition( )
	{
		int firstIndex = getFirstIndexOfSameAxis( );
		EList list = chart.getSampleData( ).getOrthogonalSampleData( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			// Check each entry if it is associated with the series
			// definition to be removed
			if ( ( (OrthogonalSampleData) list.get( i ) ).getSeriesDefinitionIndex( ) == ( firstIndex + cmbSeriesSelect.getSelectionIndex( ) ) )
			{
				list.remove( i );
				break;
			}
		}
		// Reset index. If index is wrong, sample data can't display.
		for ( int i = 0; i < list.size( ); i++ )
		{
			( (OrthogonalSampleData) list.get( i ) ).setSeriesDefinitionIndex( i );
		}
		seriesDefns.remove( cmbSeriesSelect.getSelectionIndex( ) );
	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.widget.equals( btnSeriesAdd ) )
		{
			if ( seriesDefns.size( ) <= 1 )
			{
				addNewSeriesDefinition( );
			}
			else
			{
				// Remove color registry
				EList removeDefns = ( (SeriesDefinition) seriesDefns.get( cmbSeriesSelect.getSelectionIndex( ) ) ).getDesignTimeSeries( )
						.getDataDefinition( );
				for ( int i = 0; i < removeDefns.size( ); i++ )
				{
					String expression = ( (Query) removeDefns.get( i ) ).getDefinition( );
					// If it's last element, remove color binding
					List otherDfs = getOtherQueryDefinitions( );
					if ( DataDefinitionTextManager.getInstance( )
							.getNumberOfSameDataDefinition( expression ) == 1
							&& !otherDfs.contains( expression ) )
					{
						ColorPalette.getInstance( )
								.retrieveColor( ChartUIUtil.getColumnName( expression ) );
					}
				}
				// Refresh table color
				for ( int i = 0; i < getCustomTable( ).getColumnNumber( ); i++ )
				{
					getCustomTable( ).setColumnColor( i,
							ColorPalette.getInstance( )
									.getColor( getCustomTable( ).getColumnHeading( i ) ) );
				}

				// Remove sample data and series
				removeSeriesDefinition( );
			}
			refreshButton( );
			refreshCombo( );
			refreshQuery( );

			createDataDefinition( );
			refreshQuery( );
			selectDataUI.refreshRightBindingArea( );
			selectDataUI.layoutAll( );
		}
		else if ( e.widget.equals( cmbSeriesSelect ) )
		{
			createDataDefinition( );
			refreshQuery( );
			selectDataUI.layoutAll( );
		}
	}

	private CustomPreviewTable getCustomTable( )
	{
		return (CustomPreviewTable) selectDataUI.getCustomPreviewTable( );
	}

	private List getOtherQueryDefinitions( )
	{
		int current = cmbSeriesSelect.getSelectionIndex( );
		List list = new ArrayList( );
		for ( int i = 0; i < seriesDefns.size( ); i++ )
		{
			if ( i != current )
			{
				EList dfs = ( (SeriesDefinition) seriesDefns.get( i ) ).getDesignTimeSeries( )
						.getDataDefinition( );
				for ( int j = 0; j < dfs.size( ); j++ )
				{
					list.add( ( (Query) dfs.get( j ) ).getDefinition( ) );
				}
			}
		}
		return list;
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}

	private void refreshQuery( )
	{
		Object[] data = new Object[2];
		data[0] = getCurrentSeriesDefinition( );
		data[1] = ChartUIUtil.getDataQuery( getCurrentSeriesDefinition( ), 0 );
		dateComponent.selectArea( true, data );
	}

	private void refreshButton( )
	{
		if ( seriesDefns.size( ) <= 1 )
		{
			btnSeriesAdd.setText( "+" ); //$NON-NLS-1$
		}
		else
		{
			// btnSeriesAdd.setImage( UIHelper.getImage(
			// "icons/obj16/operator.gif" ) );
			btnSeriesAdd.setText( "-" ); //$NON-NLS-1$
		}
	}

	private void refreshCombo( )
	{
		ArrayList itemList = new ArrayList( );
		int seriesSize = seriesDefns.size( );
		for ( int i = 1; i <= seriesSize; i++ )
		{
			itemList.add( selectionName + " " + i ); //$NON-NLS-1$
		}
		cmbSeriesSelect.removeAll( );
		cmbSeriesSelect.setItems( (String[]) itemList.toArray( new String[seriesSize] ) );
		cmbSeriesSelect.select( seriesSize - 1 );
	}

	private ISelectDataComponent getDataDefinitionComponent(
			SeriesDefinition seriesDefn )
	{
		ISelectDataComponent sdc = selectDataUI.getAreaComponent( areaType,
				seriesDefn,
				serviceprovider,
				oContext,
				sTitle );
		if ( sdc instanceof BaseDataDefinitionComponent )
		{
			( (BaseDataDefinitionComponent) sdc ).setDescription( description );
		}
		return sdc;
	}

	public void selectArea( boolean selected, Object data )
	{
		dateComponent.selectArea( selected, data );
	}

	public void dispose( )
	{
		dateComponent.dispose( );

	}

	/**
	 * Sets the selector the row number
	 * 
	 * @param numberRows
	 *            only 1 and 2 accepted
	 */
	public void setNumberRows( int numberRows )
	{
		if ( numberRows == 1 || numberRows == 1 )
		{
			this.numberRows = numberRows;
		}
	}

	/**
	 * Sets the name prefix in the combo
	 * 
	 * @param selectionNamePrefix
	 */
	public void setSelectionPrefix( String selectionNamePrefix )
	{
		this.selectionName = selectionNamePrefix;
	}

	/**
	 * Sets the description in the left of data text box.
	 * 
	 * @param description
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	public void setAreaType( int areaType )
	{
		this.areaType = areaType;
	}

}
