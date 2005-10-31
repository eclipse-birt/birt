/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class SeriesSheetImpl extends SubtaskSheetImpl

{

	private transient Composite cmpContent = null;

	private static Hashtable htSeriesNames = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		final int COLUMN_NUMBER = 3;
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( COLUMN_NUMBER, true );
			glContent.horizontalSpacing = 40;
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Label lblBlank = new Label( cmpContent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblBlank.setLayoutData( gd );
		}

		Label lblVisible = new Label( cmpContent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblVisible.setLayoutData( gd );
			lblVisible.setFont( JFaceResources.getBannerFont( ) );
			lblVisible.setText( Messages.getString( "SeriesSheetImpl.Label.Title" ) ); //$NON-NLS-1$
		}

		Label lblType = new Label( cmpContent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblType.setLayoutData( gd );
			lblType.setFont( JFaceResources.getBannerFont( ) );
			lblType.setText( Messages.getString( "SeriesSheetImpl.Label.Type" ) ); //$NON-NLS-1$
		}

		EList seriesDefns = ChartUIUtil.getBaseSeriesDefinitions( getChart( ) );
		for ( int i = 0; i < seriesDefns.size( ); i++ )
		{
			Composite cmpY = new SeriesOptionComposite( cmpContent,
					( (SeriesDefinition) seriesDefns.get( i ) ),
					getChart( ) instanceof ChartWithAxes
							? Messages.getString( "SeriesSheetImpl.Label.CategoryXSeries" ) : Messages.getString( "SeriesSheetImpl.Label.CategoryBaseSeries" ), //$NON-NLS-1$ //$NON-NLS-2$
					i );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalSpan = COLUMN_NUMBER;
				cmpY.setLayoutData( gd );
			}
		}

		seriesDefns = ChartUIUtil.getOrthogonalSeriesDefinitions( getChart( ),
				-1 );
		for ( int i = 0; i < seriesDefns.size( ); i++ )
		{
			String text = getChart( ) instanceof ChartWithAxes
					? Messages.getString( "SeriesSheetImpl.Label.ValueYSeries" ) : Messages.getString( "SeriesSheetImpl.Label.ValueOrthogonalSeries" ); //$NON-NLS-1$ //$NON-NLS-2$
			Composite cmpY = new SeriesOptionComposite( cmpContent,
					( (SeriesDefinition) seriesDefns.get( i ) ),
					( seriesDefns.size( ) == 1 ? text
							: ( text + " - " + ( i + 1 ) ) ) + ":", i ); //$NON-NLS-1$ //$NON-NLS-2$
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalSpan = COLUMN_NUMBER;
				cmpY.setLayoutData( gd );
			}
		}
	}

	public Object onHide( )
	{
		detachPopup( );
		cmpContent.dispose( );
		return getContext( );
	}

	class SeriesOptionComposite extends Composite
			implements
				SelectionListener,
				Listener
	{

		private transient SeriesDefinition seriesDefn;
		private transient String seriesName;

		private transient ExternalizedTextEditorComposite txtTitle;
		private transient Combo cmbTypes;
		private transient int iSeriesDefinitionIndex = 0;

		public SeriesOptionComposite( Composite parent,
				SeriesDefinition seriesDefn, String seriesName,
				int iSeriesDefinitionIndex )
		{
			super( parent, SWT.NONE );
			this.seriesDefn = seriesDefn;
			this.seriesName = seriesName;
			this.iSeriesDefinitionIndex = iSeriesDefinitionIndex;
			placeComponents( );
		}

		private void placeComponents( )
		{
			GridLayout layout = new GridLayout( 3, true );
			layout.horizontalSpacing = 40;
			this.setLayout( layout );

			Label lblAxis = new Label( this, SWT.NONE );
			{
				GridData gd = new GridData( );
				lblAxis.setLayoutData( gd );
				lblAxis.setText( appendBlank( seriesName, 20 ) );
			}

			List keys = null;
			if ( serviceprovider != null )
			{
				keys = serviceprovider.getRegisteredKeys( );
			}

			txtTitle = new ExternalizedTextEditorComposite( this,
					SWT.BORDER | SWT.SINGLE,
					-1,
					-1,
					keys,
					serviceprovider,
					seriesDefn.getDesignTimeSeries( )
							.getSeriesIdentifier( )
							.toString( ) );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				txtTitle.setLayoutData( gd );
				txtTitle.addListener( this );
			}

			cmbTypes = new Combo( this, SWT.DROP_DOWN | SWT.READ_ONLY );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				cmbTypes.setLayoutData( gd );
				cmbTypes.addSelectionListener( this );
			}

			populateLists( seriesDefn.getDesignTimeSeries( ) );
		}

		public void widgetSelected( SelectionEvent e )
		{
			if ( e.getSource( ).equals( cmbTypes ) )
			{
				if ( seriesDefn.getDesignTimeSeries( )
						.canParticipateInCombination( ) )
				{
					// Get a new series of the selected type by using as much
					// information as possible from the existing series
					Series newSeries = getNewSeries( cmbTypes.getText( ),
							seriesDefn.getDesignTimeSeries( ) );
					newSeries.eAdapters( ).addAll( seriesDefn.eAdapters( ) );
					seriesDefn.getSeries( ).set( 0, newSeries );
				}
			}
		}

		private Series getNewSeries( String sSeriesName, Series oldSeries )
		{
			try
			{
				Class seriesClass = Class.forName( (String) htSeriesNames.get( sSeriesName ) );
				Method createMethod = seriesClass.getDeclaredMethod( "create", new Class[]{} ); //$NON-NLS-1$
				Series series = (Series) createMethod.invoke( seriesClass,
						new Object[]{} );
				setIgnoreNotifications( true );
				series.translateFrom( oldSeries,
						iSeriesDefinitionIndex,
						getChart( ) );
				setIgnoreNotifications( false );
				return series;
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
			return null;
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void handleEvent( Event event )
		{
			if ( event.widget.equals( txtTitle ) )
			{
				seriesDefn.getDesignTimeSeries( )
						.setSeriesIdentifier( txtTitle.getText( ) );
			}
		}

		private void populateLists( Series series )
		{
			// Populate Series Types List
			if ( series.canParticipateInCombination( ) )
			{
				try
				{
					populateSeriesTypes( PluginSettings.instance( )
							.getRegisteredSeries( ), series );
				}
				catch ( ChartException e )
				{
					e.printStackTrace( );
				}
			}
			else
			{
				String seriesName = PluginSettings.instance( )
						.getSeriesDisplayName( series.getClass( ).getName( ) );
				cmbTypes.add( seriesName );
				cmbTypes.select( 0 );
			}
		}

		private void populateSeriesTypes( String[] allSeriesTypes, Series series )
		{
			for ( int i = 0; i < allSeriesTypes.length; i++ )
			{
				try
				{
					Class seriesClass = Class.forName( allSeriesTypes[i] );
					Method createMethod = seriesClass.getDeclaredMethod( "create", new Class[]{} ); //$NON-NLS-1$
					Series newSeries = (Series) createMethod.invoke( seriesClass,
							new Object[]{} );
					if ( htSeriesNames == null )
					{
						htSeriesNames = new Hashtable( 20 );
					}
					String sDisplayName = PluginSettings.instance( )
							.getSeriesDisplayName( allSeriesTypes[i] );
					htSeriesNames.put( sDisplayName, allSeriesTypes[i] );
					if ( newSeries.canParticipateInCombination( ) )
					{
						cmbTypes.add( sDisplayName );
						if ( allSeriesTypes[i].equals( series.getClass( )
								.getName( ) ) )
						{
							cmbTypes.select( cmbTypes.getItemCount( ) - 1 );
						}
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace( );
				}
			}
		}

		private String appendBlank( String str, int lengthMax )
		{
			if ( str.length( ) <= lengthMax )
			{
				StringBuffer sb = new StringBuffer( str );
				for ( int i = str.length( ); i < lengthMax; i++ )
				{
					sb.append( " " ); //$NON-NLS-1$
				}
				return sb.toString( );
			}
			return str;
		}

	}

}