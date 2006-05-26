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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.IChangeWithoutNotification;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ColorPalette;
import org.eclipse.birt.chart.ui.swt.wizard.internal.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.wizard.internal.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.util.ChartUIConstancts;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
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
import org.eclipse.swt.widgets.Label;

/**
 * This UI component is made up of a combo selector for series selection, a
 * button for series deletion and a dynamic data components which is decided by
 * series type. An axis or a <code>ChartWithoutAxis</code> uses a selector
 * instance. Series adding is embedded in Combo selector.
 */

public class DataDefinitionSelector extends DefaultSelectDataComponent
		implements
			SelectionListener
{

	private transient EList seriesDefns = null;

	private transient ChartWizardContext wizardContext = null;

	private transient String sTitle = null;

	private transient Composite cmpTop = null;

	private transient Composite cmpData = null;

	private transient ISelectDataComponent dateComponent = null;

	private transient Button btnAxisDelete;

	private transient Combo cmbAxisSelect;

	private transient Button btnSeriesDelete;

	private transient Combo cmbSeriesSelect;

	private transient int axisIndex;

	private transient String selectionName = Messages.getString( "DataDefinitionSelector.Label.Series" ); //$NON-NLS-1$

	private transient String description = ""; //$NON-NLS-1$

	private transient int areaType = ISelectDataCustomizeUI.ORTHOGONAL_SERIES;

	private transient ISelectDataCustomizeUI selectDataUI = null;

	/**
	 * 
	 * @param axisIndex
	 *            -1 means single axis; nonnegative number means the axis index
	 * @param seriesDefns
	 * @param wizardContext
	 * @param sTitle
	 * @param selectDataUI
	 */
	public DataDefinitionSelector( int axisIndex, EList seriesDefns,
			ChartWizardContext wizardContext, String sTitle,
			ISelectDataCustomizeUI selectDataUI )
	{
		this.seriesDefns = seriesDefns;
		this.wizardContext = wizardContext;
		this.sTitle = sTitle;
		this.axisIndex = axisIndex;
		this.selectDataUI = selectDataUI;
	}

	public DataDefinitionSelector( ChartWizardContext wizardContext,
			String sTitle, ISelectDataCustomizeUI selectDataUI )
	{
		this.wizardContext = wizardContext;
		this.sTitle = sTitle;
		this.axisIndex = -1;
		this.selectDataUI = selectDataUI;
	}

	public Composite createArea( Composite parent )
	{
		{
			if ( axisIndex >= 0 )
			{
				cmpTop = new Group( parent, SWT.NONE );
				( (Group) cmpTop ).setText( Messages.getString( "DataDefinitionSelector.Label.YAxis" ) + ( axisIndex + 1 ) ); //$NON-NLS-1$
			}
			else
			{
				cmpTop = new Composite( parent, SWT.NONE );
			}

			GridLayout layout = new GridLayout( );
			layout.numColumns = 2;
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.horizontalSpacing = 0;
			layout.verticalSpacing = 2;
			cmpTop.setLayout( layout );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpTop.setLayoutData( gd );
		}

		if ( wizardContext.isMoreAxesSupported( ) )
		{
			cmbAxisSelect = new Combo( cmpTop, SWT.DROP_DOWN | SWT.READ_ONLY );
			{
				cmbAxisSelect.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				cmbAxisSelect.addSelectionListener( this );
				refreshAxisCombo( );
				cmbAxisSelect.select( 0 );
			}

			btnAxisDelete = new Button( cmpTop, SWT.NONE );
			{
				GridData gridData = new GridData( );
				gridData.heightHint = 20;
				gridData.widthHint = 20;
				btnAxisDelete.setLayoutData( gridData );
				btnAxisDelete.setImage( UIHelper.getImage( ChartUIConstancts.IMAGE_DELETE ) );
				btnAxisDelete.setToolTipText( Messages.getString( "DataDefinitionSelector.Tooltip.RemoveAxis" ) ); //$NON-NLS-1$
				btnAxisDelete.addSelectionListener( this );
				setAxisDeleteEnabled( );
			}

			Label lblSeparator = new Label( cmpTop, SWT.SEPARATOR
					| SWT.HORIZONTAL );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalSpan = 2;
				lblSeparator.setLayoutData( gd );
			}

			axisIndex = cmbAxisSelect.getSelectionIndex( );

			// Update series definition
			seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions( getChart( ),
					axisIndex );
		}

		cmbSeriesSelect = new Combo( cmpTop, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			cmbSeriesSelect.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			cmbSeriesSelect.addSelectionListener( this );
			refreshSeriesCombo( );
			cmbSeriesSelect.select( 0 );
		}

		btnSeriesDelete = new Button( cmpTop, SWT.NONE );
		{
			GridData gridData = new GridData( );
			gridData.heightHint = 20;
			gridData.widthHint = 20;
			btnSeriesDelete.setLayoutData( gridData );
			btnSeriesDelete.setImage( UIHelper.getImage( ChartUIConstancts.IMAGE_DELETE ) );
			btnSeriesDelete.setToolTipText( Messages.getString( "DataDefinitionSelector.Tooltip.RemoveSeries" ) ); //$NON-NLS-1$
			btnSeriesDelete.addSelectionListener( this );
			setSeriesDeleteEnabled( );
		}

		updateDataDefinition( );

		return cmpTop;
	}

	private void updateDataDefinition( )
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
		return (SeriesDefinition) seriesDefns.get( cmbSeriesSelect.getSelectionIndex( ) );
	}

	private int getFirstIndexOfSameAxis( )
	{
		if ( axisIndex > 0 )
		{
			return ChartUIUtil.getLastSeriesIndexWithinAxis( getChart( ),
					axisIndex - 1 ) + 1;
		}
		return 0;
	}

	protected void addNewSeriesDefinition( )
	{
		// Create a series definition without data definition
		SeriesDefinition sdTmp = SeriesDefinitionImpl.create( );
		sdTmp.getSeriesPalette( ).update( -seriesDefns.size( ) );
		sdTmp.getSeries( )
				.add( EcoreUtil.copy( ( (SeriesDefinition) seriesDefns.get( 0 ) ).getDesignTimeSeries( ) ) );
		// Add grouping query of the first series definition
		sdTmp.setQuery( (Query) EcoreUtil.copy( ( (SeriesDefinition) seriesDefns.get( 0 ) ).getQuery( ) ) );
		cleanDataDefinition( sdTmp );
		sdTmp.eAdapters( )
				.addAll( ( (SeriesDefinition) seriesDefns.get( 0 ) ).eAdapters( ) );

		int firstIndex = getFirstIndexOfSameAxis( );
		EList list = getChart( ).getSampleData( ).getOrthogonalSampleData( );

		// Create a new OrthogonalSampleData instance from the existing one
		OrthogonalSampleData sdOrthogonal = (OrthogonalSampleData) EcoreUtil.copy( (EObject) list.get( firstIndex ) );
		sdOrthogonal.setSeriesDefinitionIndex( list.size( ) );
		sdOrthogonal.eAdapters( ).addAll( getChart( ).getSampleData( )
				.eAdapters( ) );

		// Update the Sample Data without event fired.
		boolean isNotificaionIgnored = ChartAdapter.isNotificationIgnored( );
		ChartAdapter.ignoreNotifications( true );
		list.add( sdOrthogonal );
		ChartAdapter.ignoreNotifications( isNotificaionIgnored );

		seriesDefns.add( sdTmp );
	}

	private void cleanDataDefinition( SeriesDefinition sd )
	{
		EList dds = sd.getDesignTimeSeries( ).getDataDefinition( );
		for ( int i = 0; i < dds.size( ); i++ )
		{
			( (Query) dds.get( i ) ).setDefinition( "" ); //$NON-NLS-1$
		}
	}

	/**
	 * Updates series palette of series definition list without the series to be
	 * moved
	 * 
	 * @param removedIndex
	 *            the index of the series to be removed
	 */
	private void updateSeriesPalette( int removedIndex )
	{
		for ( int i = 0, j = 0; i < seriesDefns.size( ); i++ )
		{
			if ( i != removedIndex )
			{
				( (SeriesDefinition) seriesDefns.get( i ) ).getSeriesPalette( )
						.update( -j++ );
			}
		}
	}

	protected void removeSeriesDefinition( )
	{
		boolean isNotificaionIgnored = ChartAdapter.isNotificationIgnored( );
		ChartAdapter.ignoreNotifications( true );
		int firstIndex = getFirstIndexOfSameAxis( );
		EList list = getChart( ).getSampleData( ).getOrthogonalSampleData( );
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
		ChartUIUtil.reorderOrthogonalSampleDataIndex( getChart( ) );
		updateSeriesPalette( cmbSeriesSelect.getSelectionIndex( ) );
		ChartAdapter.ignoreNotifications( isNotificaionIgnored );

		seriesDefns.remove( cmbSeriesSelect.getSelectionIndex( ) );
	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.widget.equals( btnSeriesDelete ) )
		{
			// Update color registry
			updateColorRegistry( cmbSeriesSelect.getSelectionIndex( ) );

			// Remove sample data and series
			removeSeriesDefinition( );

			setSeriesDeleteEnabled( );

			int oldSelectedIndex = cmbSeriesSelect.getSelectionIndex( );
			refreshSeriesCombo( );
			// Selects the new item or last item
			if ( oldSelectedIndex > cmbSeriesSelect.getItemCount( ) - 2 )
			{
				oldSelectedIndex = cmbSeriesSelect.getItemCount( ) - 2;
			}
			cmbSeriesSelect.select( oldSelectedIndex );

			// Update data definition component and refresh query in it
			updateDataDefinition( );
			refreshQuery( );

			// Sets current series index and update bottom component if needed
			setSelectedSeriesIndex( );

			// CHART ENGINE NOT SUPPORT MULTI-GROUPING, NO NEED TO REFRESH UI
			// selectDataUI.refreshRightBindingArea( );
			selectDataUI.layoutAll( );

		}
		else if ( e.widget.equals( cmbSeriesSelect ) )
		{
			// Check if needing to add a new series
			if ( cmbSeriesSelect.getSelectionIndex( ) == cmbSeriesSelect.getItemCount( ) - 1 )
			{
				addNewSeriesDefinition( );

				setSeriesDeleteEnabled( );

				refreshSeriesCombo( );
				// Selects the new item
				cmbSeriesSelect.select( cmbSeriesSelect.getItemCount( ) - 2 );
			}

			// Update data definition component and refresh query in it
			updateDataDefinition( );
			refreshQuery( );

			// Sets current series index and update bottom component if needed
			setSelectedSeriesIndex( );

			// CHART ENGINE NOT SUPPORT MULTI-GROUPING, NO NEED TO REFRESH UI
			// selectDataUI.refreshRightBindingArea( );
			selectDataUI.layoutAll( );
		}
		else if ( e.widget.equals( cmbAxisSelect ) )
		{
			// Check if needing to add a new series
			if ( cmbAxisSelect.getSelectionIndex( ) == cmbAxisSelect.getItemCount( ) - 1 )
			{
				// Update dimension if it doesn't support multiple axes
				ChartAdapter.changeChartWithoutNotification( new IChangeWithoutNotification( ) {

					public Object run( )
					{
						String currentDimension = ChartUIUtil.getDimensionString( getChart( ).getDimension( ) );
						boolean isDimensionSupported = wizardContext.getChartType( )
								.isDimensionSupported( currentDimension,
										cmbAxisSelect.getItemCount( ),
										0 );
						if ( !isDimensionSupported )
						{
							getChart( ).setDimension( ChartUIUtil.getDimensionType( wizardContext.getChartType( )
									.getDefaultDimension( ) ) );
						}
						return null;
					}
				} );

				// Update model
				ChartUIUtil.addAxis( (ChartWithAxes) getChart( ) );

				// Update UI
				setAxisDeleteEnabled( );
				refreshAxisCombo( );
				cmbAxisSelect.select( cmbAxisSelect.getItemCount( ) - 2 );
			}

			axisIndex = cmbAxisSelect.getSelectionIndex( );

			updateAllSeriesUnderAxis( );
		}
		else if ( e.widget.equals( btnAxisDelete ) )
		{
			// Update color registry
			updateColorRegistry( -1 );

			// Update model
			ChartUIUtil.removeAxis( getChart( ), axisIndex );

			// Update UI
			setAxisDeleteEnabled( );
			refreshAxisCombo( );
			// Selects the new item or last item
			if ( axisIndex > cmbAxisSelect.getItemCount( ) - 2 )
			{
				axisIndex = cmbAxisSelect.getItemCount( ) - 2;
			}
			cmbAxisSelect.select( axisIndex );

			updateAllSeriesUnderAxis( );
		}
	}

	private void updateAllSeriesUnderAxis( )
	{
		// Update series definition
		seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions( getChart( ),
				axisIndex );
		setSeriesDeleteEnabled( );
		refreshSeriesCombo( );
		// Selects the new item or last item
		cmbSeriesSelect.select( 0 );

		// Update data definition component and refresh query in it
		updateDataDefinition( );
		refreshQuery( );

		// Sets current series index and update bottom component if needed
		setSelectedSeriesIndex( );

		// CHART ENGINE NOT SUPPORT MULTI-GROUPING, NO NEED TO REFRESH UI
		// selectDataUI.refreshRightBindingArea( );
		selectDataUI.layoutAll( );
	}

	/**
	 * Updates the color registry and refresh all background color of the text
	 * field
	 * 
	 * @param seriesIndex
	 *            -1 means all series under selected axis
	 */
	private void updateColorRegistry( int seriesIndex )
	{
		List dataDefinitions = null;
		if ( seriesIndex > -1 )
		{
			dataDefinitions = ( (SeriesDefinition) seriesDefns.get( seriesIndex ) ).getDesignTimeSeries( )
					.getDataDefinition( );
		}
		else
		{
			List allSeriesDefns = ChartUIUtil.getAllOrthogonalSeriesDefinitions( getChart( ) );
			dataDefinitions = new ArrayList( );
			for ( int i = 0; i < allSeriesDefns.size( ); i++ )
			{
				dataDefinitions.addAll( ( (SeriesDefinition) allSeriesDefns.get( i ) ).getDesignTimeSeries( )
						.getDataDefinition( ) );
			}
		}

		// Count each expression
		Map queryMap = new HashMap( );
		for ( int i = 0; i < dataDefinitions.size( ); i++ )
		{
			String expression = ( (Query) dataDefinitions.get( i ) ).getDefinition( );
			if ( queryMap.containsKey( expression ) )
			{
				int expCount = ( (Integer) queryMap.get( expression ) ).intValue( );
				queryMap.put( expression, new Integer( expCount++ ) );
			}
			else
			{
				queryMap.put( expression, new Integer( 1 ) );
			}
		}
		// If the expression count is the same to the count of all, delete this
		// color registry of the expression
		for ( Iterator iterator = queryMap.keySet( ).iterator( ); iterator.hasNext( ); )
		{
			String expression = (String) iterator.next( );
			if ( DataDefinitionTextManager.getInstance( )
					.getNumberOfSameDataDefinition( expression ) == ( (Integer) queryMap.get( expression ) ).intValue( ) )
			{
				ColorPalette.getInstance( ).retrieveColor( expression );
			}
		}

		// Refresh table color
		for ( int i = 0; i < getCustomTable( ).getColumnNumber( ); i++ )
		{
			getCustomTable( ).setColumnColor( i,
					ColorPalette.getInstance( )
							.getColor( ChartUIUtil.getExpressionString( getCustomTable( ).getColumnHeading( i ) ) ) );
		}
	}

	private void setSelectedSeriesIndex( )
	{
		// Only standard type shows multiple series at the same time
		if ( !wizardContext.isMoreAxesSupported( ) )
		{
			int axisNum = axisIndex < 0 ? 0 : axisIndex;
			int[] indexArray = selectDataUI.getSeriesIndex( );
			indexArray[axisNum] = cmbSeriesSelect.getSelectionIndex( );
			selectDataUI.setSeriesIndex( indexArray );
		}
	}

	private void setSeriesDeleteEnabled( )
	{
		btnSeriesDelete.setEnabled( seriesDefns.size( ) > 1 );
	}

	private void setAxisDeleteEnabled( )
	{
		btnAxisDelete.setEnabled( ChartUIUtil.getOrthogonalAxisNumber( getChart( ) ) > 1 );
	}

	private CustomPreviewTable getCustomTable( )
	{
		return (CustomPreviewTable) selectDataUI.getCustomPreviewTable( );
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

	private void refreshSeriesCombo( )
	{
		ArrayList itemList = new ArrayList( );
		int seriesSize = seriesDefns.size( );
		for ( int i = 1; i <= seriesSize; i++ )
		{
			itemList.add( selectionName + " " + i ); //$NON-NLS-1$
		}
		itemList.add( Messages.getString( "DataDefinitionSelector.Text.NewSeries" ) ); //$NON-NLS-1$
		cmbSeriesSelect.removeAll( );
		cmbSeriesSelect.setItems( (String[]) itemList.toArray( new String[seriesSize] ) );
	}

	private void refreshAxisCombo( )
	{
		ArrayList itemList = new ArrayList( );
		int axisNum = ChartUIUtil.getOrthogonalAxisNumber( getChart( ) );
		for ( int i = 1; i <= axisNum; i++ )
		{
			itemList.add( Messages.getString( "DataDefinitionSelector.Label.Axis" ) + i ); //$NON-NLS-1$
		}
		itemList.add( Messages.getString( "DataDefinitionSelector.Text.NewAxis" ) ); //$NON-NLS-1$
		cmbAxisSelect.removeAll( );
		cmbAxisSelect.setItems( (String[]) itemList.toArray( new String[axisNum] ) );
	}

	private ISelectDataComponent getDataDefinitionComponent(
			SeriesDefinition seriesDefn )
	{
		ISelectDataComponent sdc = selectDataUI.getAreaComponent( areaType,
				seriesDefn,
				wizardContext,
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
		super.dispose( );
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

	private Chart getChart( )
	{
		return wizardContext.getModel( );
	}

}
