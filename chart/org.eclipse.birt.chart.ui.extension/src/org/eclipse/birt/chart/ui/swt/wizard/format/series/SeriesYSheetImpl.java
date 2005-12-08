/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartUIExtensionsImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DialLabelSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.PieTitleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesLabelSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesPaletteSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesRegionSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesTrendlineSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class SeriesYSheetImpl extends SubtaskSheetImpl
		implements
			Listener,
			SelectionListener
{

	private transient Button btnShowLine;

	// Toggle button group
	private transient Button btnLabel;

	private transient Button btnDialRegion;

	private transient Button btnDialLabel;

	private transient Button btnPieTitle;

	private transient Button btnInteractivity;

	private transient Button btnTrendline;

	private transient Button btnPalette;

	private transient Hashtable htSeriesAttributeUIProviders = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		// Series details composite
		Group grpDetails = new Group( cmpContent, SWT.NONE );
		GridData gdCMPDetails = new GridData( GridData.FILL_BOTH );
		gdCMPDetails.horizontalSpan = 2;
		grpDetails.setLayoutData( gdCMPDetails );
		grpDetails.setLayout( new FillLayout( ) );
		grpDetails.setText( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.SeriesDetails" ) ); //$NON-NLS-1$

		// Series composite
		Series series = getSeriesDefinitionForProcessing( ).getDesignTimeSeries( );
		getSeriesAttributeUI( series, grpDetails );

		if ( isTrendlineAvailable( ) )
		{
			btnShowLine = new Button( cmpContent, SWT.CHECK );
			{
				btnShowLine.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowTrendline" ) ); //$NON-NLS-1$
				btnShowLine.addSelectionListener( this );
				btnShowLine.setSelection( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
						.getCurveFitting( ) != null );
			}
		}

		createButtonGroup( cmpContent );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 5, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		// Label or Region
		if ( !( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof StockSeries ) )
		{
			if ( isMeterSeries( ) )
			{
				btnDialLabel = createToggleButton( cmp,
						Messages.getString( "SeriesYSheetImpl.Label.Labels" ) ); //$NON-NLS-1$ 
				btnDialLabel.addSelectionListener( this );

				btnDialRegion = createToggleButton( cmp,
						Messages.getString( "SeriesYSheetImpl.Label.Region" ) ); //$NON-NLS-1$ 
				btnDialRegion.addSelectionListener( this );
			}
			else
			{
				btnLabel = createToggleButton( cmp,
						Messages.getString( "SeriesYSheetImpl.Label.Labels" ) ); //$NON-NLS-1$ 
				btnLabel.addSelectionListener( this );
			}
		}
		else
		{
			// Disable Label properties for Stock series
			btnLabel = new Button( cmp, SWT.NONE );
			btnLabel.setVisible( false );
		}

		if ( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof PieSeries )
		{
			btnPieTitle = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Titles" ) ); //$NON-NLS-1$
			btnPieTitle.addSelectionListener( this );
		}

		// Interactivity
		btnInteractivity = createToggleButton( cmp,
				Messages.getString( "SeriesYSheetImpl.Label.Interactivity" ) ); //$NON-NLS-1$
		btnInteractivity.addSelectionListener( this );

		// Trendline
		if ( isTrendlineAvailable( ) )
		{
			btnTrendline = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Trendline" ) ); //$NON-NLS-1$
			btnTrendline.addSelectionListener( this );
			btnTrendline.setEnabled( btnShowLine.getSelection( ) );
		}

		// DataPoint
		btnPalette = createToggleButton( cmp,
				Messages.getString( "SeriesXSheetImpl.Label.SeriesPalette" ) ); //$NON-NLS-1$
		btnPalette.addSelectionListener( this );
	}

	private void getSeriesAttributeUI( Series series, Composite parent )
	{
		if ( this.htSeriesAttributeUIProviders == null )
		{
			htSeriesAttributeUIProviders = new Hashtable( );
			getSeriesAttributeUIProviders( );
		}
		( (ISeriesUIProvider) htSeriesAttributeUIProviders.get( series.getClass( )
				.getName( ) ) ).getSeriesAttributeSheet( parent,
				series,
				getContext( ).getUIServiceProvider( ),
				getContext( ).getExtendedItem( ) );
	}

	private void getSeriesAttributeUIProviders( )
	{
		// Get collection of registered UI Providers
		Collection cRegisteredEntries = ChartUIExtensionsImpl.instance( )
				.getSeriesUIComponents( );
		Iterator iterEntries = cRegisteredEntries.iterator( );
		while ( iterEntries.hasNext( ) )
		{
			ISeriesUIProvider provider = (ISeriesUIProvider) iterEntries.next( );
			String sSeries = provider.getSeriesClass( );
			htSeriesAttributeUIProviders.put( sSeries, provider );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{

	}

	public void widgetSelected( SelectionEvent e )
	{
		// detach popup dialogue
		if ( detachPopup( e.widget ) )
		{
			return;
		}
		if ( e.widget instanceof Button
				&& ( ( (Button) e.widget ).getStyle( ) & SWT.TOGGLE ) == SWT.TOGGLE
				&& ( (Button) e.widget ).getSelection( ) )
		{
			selectAllButtons( false );
			( (Button) e.widget ).setSelection( true );
		}

		if ( e.widget.equals( btnLabel ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new SeriesLabelSheet( popupShell,
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			getWizard( ).attachPopup( btnLabel.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnDialRegion ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new SeriesRegionSheet( popupShell,
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			getWizard( ).attachPopup( btnDialRegion.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnDialLabel ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new DialLabelSheet( popupShell,
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			getWizard( ).attachPopup( btnDialLabel.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnPieTitle ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new PieTitleSheet( popupShell,
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			getWizard( ).attachPopup( btnPieTitle.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnInteractivity ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new InteractivitySheet( popupShell,
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			getWizard( ).attachPopup( btnInteractivity.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnTrendline ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new SeriesTrendlineSheet( popupShell,
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			getWizard( ).attachPopup( btnTrendline.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnPalette ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new SeriesPaletteSheet( popupShell,
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			getWizard( ).attachPopup( btnPalette.getText( ), -1, -1 );
		}

		if ( e.widget.equals( btnShowLine ) )
		{
			btnTrendline.setEnabled( btnShowLine.getSelection( ) );
			if ( btnShowLine.getSelection( ) )
			{
				CurveFitting cf = CurveFittingImpl.create( );
				cf.eAdapters( )
						.addAll( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
								.eAdapters( ) );
				getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
						.setCurveFitting( cf );
			}
			else
			{
				getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
						.setCurveFitting( null );
				// Close trendline popup
				if ( btnTrendline.getSelection( ) )
				{
					btnTrendline.setSelection( false );
					detachPopup( btnTrendline );
				}
			}
		}

	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

	private SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		SeriesDefinition sd = null;
		if ( getChart( ) instanceof ChartWithAxes )
		{
			int iAxis = getParentAxisIndex( getIndex( ) );
			int iAxisSeries = getSeriesIndexWithinAxis( getIndex( ) );
			sd = ( (SeriesDefinition) ( (Axis) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( iAxis ) ).getSeriesDefinitions( )
					.get( iAxisSeries ) );
		}
		else if ( getChart( ) instanceof ChartWithoutAxes )
		{
			sd = (SeriesDefinition) ( (SeriesDefinition) ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ).get( getIndex( ) );
		}
		return sd;
	}

	private int getParentAxisIndex( int iSeriesDefinitionIndex )
	{
		int iTmp = 0;
		int iAxisCount = ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
				.get( 0 ) ).getAssociatedAxes( ).size( );
		for ( int i = 0; i < iAxisCount; i++ )
		{
			iTmp += ( (Axis) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( i ) ).getSeriesDefinitions( )
					.size( );
			if ( iTmp - 1 >= iSeriesDefinitionIndex )
			{
				return i;
			}
		}
		return 0;
	}

	private int getSeriesIndexWithinAxis( int iSeriesDefinitionIndex )
	{
		int iTotalDefinitions = 0;
		int iAxisCount = ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
				.get( 0 ) ).getAssociatedAxes( ).size( );
		for ( int i = 0; i < iAxisCount; i++ )
		{
			int iOldTotal = iTotalDefinitions;
			iTotalDefinitions += ( (Axis) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( i ) ).getSeriesDefinitions( )
					.size( );
			if ( iTotalDefinitions - 1 >= iSeriesDefinitionIndex )
			{
				return iSeriesDefinitionIndex - iOldTotal;
			}
		}
		return iSeriesDefinitionIndex;
	}

	private boolean isTrendlineAvailable( )
	{
		return ( getChart( ) instanceof ChartWithAxes )
				&& ( getChart( ).getDimension( ) != ChartDimension.THREE_DIMENSIONAL_LITERAL );
	}

	private boolean isMeterSeries( )
	{
		return getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof DialSeries;
	}

}