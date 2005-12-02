/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.SeriesGroupingComposite;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.MoreOptionsSeriesXSheet;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class SeriesXSheetImpl extends SubtaskSheetImpl
		implements
			Listener,
			SelectionListener
{

	private transient Combo cmbSorting = null;

	private static final String UNSORTED_OPTION = Messages.getString( "BaseSeriesDataSheetImpl.Choice.Unsorted" ); //$NON-NLS-1$

	private transient Button btnSeriesPal;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, false );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 2, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpBasic.setLayoutData( gd );
		}

		Label lblSorting = new Label( cmpBasic, SWT.NONE );
		lblSorting.setText( Messages.getString( "BaseSeriesDataSheetImpl.Lbl.DataSorting" ) ); //$NON-NLS-1$

		cmbSorting = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBSorting = new GridData( GridData.FILL_HORIZONTAL );
		cmbSorting.setLayoutData( gdCMBSorting );
		cmbSorting.addSelectionListener( this );

		Composite cmpGrouping = new Composite( cmpBasic, SWT.NONE );
		GridData gdCMPGrouping = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPGrouping.horizontalSpan = 2;
		cmpGrouping.setLayoutData( gdCMPGrouping );
		cmpGrouping.setLayout( new FillLayout( ) );

		new SeriesGroupingComposite( cmpGrouping,
				SWT.NONE,
				getSeriesDefinitionForProcessing( ),
				( getChart( ) instanceof ChartWithoutAxes ) );

		createButtonGroup( cmpContent );

		populateLists( );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		btnSeriesPal = createToggleButton( cmp,
				Messages.getString( "SeriesXSheetImpl.Label.SeriesPalette" ) ); //$NON-NLS-1$
		btnSeriesPal.addSelectionListener( this );
	}

	private void populateLists( )
	{
		// populate sorting combo
		cmbSorting.add( UNSORTED_OPTION );

		String[] nss = LiteralHelper.sortOptionSet.getDisplayNames( );
		for ( int i = 0; i < nss.length; i++ )
		{
			cmbSorting.add( nss[i] );
		}

		// Select value
		if ( !getSeriesDefinitionForProcessing( ).eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
		{
			cmbSorting.select( 0 );
		}
		else
		{
			// plus one for the first is unsorted option.
			cmbSorting.select( LiteralHelper.sortOptionSet.getNameIndex( getSeriesDefinitionForProcessing( ).getSorting( )
					.getName( ) ) + 1 );
		}

	}

	private SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		SeriesDefinition sd = null;
		if ( getChart( ) instanceof ChartWithAxes )
		{
			sd = ( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).get( getIndex( ) ) );
		}
		else if ( getChart( ) instanceof ChartWithoutAxes )
		{
			sd = ( (SeriesDefinition) ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
					.get( getIndex( ) ) );
		}
		return sd;
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

		if ( e.widget.equals( btnSeriesPal ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new MoreOptionsSeriesXSheet( popupShell,
					getChart( ),
					getSeriesDefinitionForProcessing( ) );
			getWizard( ).attachPopup( btnSeriesPal.getText( ), -1, -1 );
		}

		if ( e.getSource( ).equals( cmbSorting ) )
		{
			if ( cmbSorting.getText( ).equals( UNSORTED_OPTION ) )
			{
				getSeriesDefinitionForProcessing( ).eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			}
			else
			{
				getSeriesDefinitionForProcessing( ).setSorting( SortOption.get( LiteralHelper.sortOptionSet.getNameByDisplayName( cmbSorting.getText( ) ) ) );
			}
		}

	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

}